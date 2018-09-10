package com.sparetime.aop;

import com.sparetime.domian.Authority;
import com.sparetime.domian.Resource;
import com.sparetime.err.WarrantsException;
import com.sparetime.service.AuthorityService;
import com.sparetime.service.ResourceService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Aspect
@Component
public class AuthAop {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AuthorityService authorityService;

    @Before("execution(* com.sparetime.controller.admin.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void authAop(JoinPoint point){
        List<Authority> authList = (List<Authority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        StringBuffer _url = new StringBuffer();
        Optional<Annotation> classMapping = Arrays.asList(point.getTarget().getClass().getAnnotations()).stream().filter(a -> a.annotationType() == RequestMapping.class).findFirst();
        if (classMapping.isPresent()) _url.append(((RequestMapping)classMapping.get()).value()[0]);
        _url.append(((RequestMapping) Arrays.asList(((MethodSignature) point.getSignature()).getMethod().getAnnotations()).stream().filter(a -> a.annotationType() == RequestMapping.class).findFirst().get()).value()[0]);
        String url = _url.toString();
        boolean isIntercept = !Arrays.asList("/admin/login", "/admin/main", "/admin/panel", "/admin/logout").stream().filter(s -> s.equals(url)).findFirst().isPresent();
        if (isIntercept){
            String parent = url.replace("/" + url.split("/")[url.split("/").length - 1], "");
            if (parent.equals("/admin/message")) parent = "/admin/message/outbox"; else parent += "/";
            Resource resource = resourceService.queryByLikeUrl(parent);
            if (resource == null){
                throw new WarrantsException("not find resource");
            }
            List<Long> parentAuthIds = authorityService.queryIdByResourceId(resource.getId());
            if (authList == null || parent == null || ! authList.stream().filter(auth -> parentAuthIds.contains(auth.getId())).findFirst().isPresent()){
                throw new WarrantsException("not authorized");
            }

            if (Stream.of("toUpdate", "toAdd", "update", "add", "toDelete", "delete", "del", "updateOfBuy", "split", "updateOfGuide", "toReply", "reply").anyMatch(url::endsWith)){
                if (!authList.stream()
                        .filter(auth -> parentAuthIds.contains(auth.getId()))
                        .filter(auth -> authorityService.isWrite(resource.getId(), auth.getId()))
                        .findFirst()
                        .isPresent())
                {
                    throw new WarrantsException("You do not have write permission for the resource.");
                }
            }
        }
    }
}
