package com.sparetime.config;

import com.sparetime.err.WarrantsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private TipConfig tipConfig;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(Exception e, HttpServletRequest req) throws Exception {
        e.printStackTrace();
        String sessionId = Arrays.asList(req.getCookies()).stream().filter(cookie -> cookie.getName().equals("SESSION")).findFirst().get().getValue();
        String lang = "";
        Session session = redisOperationsSessionRepository.getSession(sessionId);
        if (session != null){
            lang = session.getAttribute("lang");
        }
        ModelAndView mav = new ModelAndView();
        if (e instanceof MultipartException){
             e = new RuntimeException("文件大小超出限制");
        }else if(e instanceof WarrantsException){

        }else {
             e = new RuntimeException("Wrong operation, please contact the administrator");
        }

        String message = tipConfig.get(lang + e.getMessage());
        e = new RuntimeException(StringUtils.isEmpty(message) ? e.getMessage() : message);

        mav.addObject("exception", e);
        mav.setViewName("admin/e");
        return mav;
    }
}
