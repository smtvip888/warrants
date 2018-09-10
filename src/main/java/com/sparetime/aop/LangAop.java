package com.sparetime.aop;

import com.sparetime.config.InternationalConfig;
import com.sparetime.config.TipConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Created by muye on 17/11/3.
 */
@Aspect
@Component
public class LangAop {

    @Autowired
    private TipConfig tipConfig;

    @Autowired
    private InternationalConfig internationalConfig;

    @Around("execution(* com.sparetime.controller.fore.*.*(..))")
    public Object langAop(ProceedingJoinPoint point) throws Throwable{

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String lang = StringUtils.isEmpty(request.getSession().getAttribute("lang")) ? "en_" : (String) request.getSession().getAttribute("lang");

        String stip = (String) request.getSession().getAttribute("tip");
        String sfiled = (String) request.getSession().getAttribute("filed");
        request.getSession().setAttribute("tip", tipConfig.get(lang + stip) == null ? stip : tipConfig.get(lang + stip));
        request.getSession().setAttribute("filed", tipConfig.get(lang + sfiled) == null ? sfiled : tipConfig.get(lang + sfiled));

        Annotation[] annotations = ((MethodSignature) point.getSignature()).getMethod().getAnnotations();
        boolean isMapping = false;
        boolean isRespBody = false;
        for (Annotation annotation : annotations){
            if (annotation.annotationType() == RequestMapping.class){
                isMapping = true;
            }
            if (annotation.annotationType() == ResponseBody.class){
                isRespBody = true;
            }
        }

        if (isMapping && ! isRespBody){

            String path = (String) point.proceed();

            if (point.getArgs() != null){
                Arrays.asList(point.getArgs()).stream().filter(arg -> arg instanceof Model).findFirst().ifPresent(arg -> {
                    String tip = (String) ((Model) arg).asMap().get("tip");
                    String filed = (String) ((Model) arg).asMap().get("filed");
                    ((Model) arg).addAttribute("tip", tipConfig.get(lang + tip) == null ? tip : tipConfig.get(lang + tip));
                    ((Model) arg).addAttribute("filed", tipConfig.get(lang + filed) == null ? filed : tipConfig.get(lang + filed));

                    internationalConfig.keySet().stream()
                            .filter(k -> k.startsWith(lang))
                            .forEach(k -> ((Model) arg).addAttribute(k.substring(3, k.length()),internationalConfig.get(k)));
                });
            }

            return path;
        }

        return point.proceed();
    }
}
