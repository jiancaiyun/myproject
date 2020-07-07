package com.imooc.seckill.config;

import com.imooc.seckill.domain.MiaoshaUser;
import com.imooc.seckill.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE1_NAME_TOKEN);
        //获取cookie
        String cookieToken = getCookieValue(request,MiaoshaUserService.COOKIE1_NAME_TOKEN);
        System.out.println("@UserArgumentResolver-resolverArgument cookieToken:"+cookieToken);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken))
            return null;
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        MiaoshaUser user = miaoshaUserService.getByToken(token, response);
        System.out.println("@UserArgumentResolver-----user" + user);
        return null;
    }

    public String getCookieValue(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie: cookies) {
                if(cookie.getName().equals(cookieName)){
                    System.out.println("getCookieValue:" +cookie.getValue());

                }
            }
        }
        System.out.println("No getCookieValue");
        return null;
    }
}
