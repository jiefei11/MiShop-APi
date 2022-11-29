package com.bhgeek.mishopapi.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.bhgeek.mishopapi.common.RespBean;
import com.bhgeek.mishopapi.common.RespBeanEnum;
import com.bhgeek.mishopapi.common.ServicesException;
import com.bhgeek.mishopapi.common.UserContext;
import com.bhgeek.mishopapi.entity.User;
import com.bhgeek.mishopapi.service.IUserService;
import com.bhgeek.mishopapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(AuthCheck.class)){
            AuthCheck authCheck = method.getAnnotation(AuthCheck.class);
            if (authCheck.required()) {
                User user;
                try {
                    user = JwtUtil.parse(token);
                }catch (JWTDecodeException e){
                    throw new RuntimeException("401");
                }
                User one = userService.getById(user);
                UserContext.setBaseUser(user);
                if (one == null){
                    throw new ServicesException(RespBeanEnum.USER_NOT_LOGIN);
                }
                if (token == null) {
                    throw new ServicesException(RespBeanEnum.TOKEN_VALIDATE_FAILED);
                }

                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
