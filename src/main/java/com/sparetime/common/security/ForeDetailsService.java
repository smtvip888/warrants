package com.sparetime.common.security;

import com.sparetime.domian.NetUser;
import com.sparetime.domian.Product;
import com.sparetime.service.NetUserService;
import com.sparetime.service.ProductService;
import com.sparetime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 17/8/12.
 */
@Component
public class ForeDetailsService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Autowired
    private NetUserService netUserService;

    @Autowired
    private ProductService productService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        com.sparetime.domian.User user = null;
        
        try {
            user = userService.queryBySubId(new BigDecimal(s));
        }catch (Exception e){
            user = userService.queryByName(s);
        }
        user = userService.queryByName(s);
        
        if (user == null)
            throw new UsernameNotFoundException("用户名或密码不正确");

        if ("-1".equals(user.getStatus()))
            throw new RuntimeException("账号锁定中,请通过领导人反馈联系");

        NetUser netUser = netUserService.queryByUserId(user.getUserId());

        Product product = productService.queryByKey(netUser.getProductId());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "FORE");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("userId", user.getUserId());
        request.getSession().setAttribute("subId", user.getSubId());
        request.getSession().setAttribute("isOut", netUser.getIsOut());
        request.getSession().setAttribute("userName", user.getUserName());
        request.getSession().setAttribute("isService", user.getIsService());
        request.getSession().setAttribute("productName", product.getName());
        request.getSession().setAttribute("level", product.getLevel());

        User details = new User(user.getUserName(), user.getPassword(), authorities);

        return details;
    }
}
