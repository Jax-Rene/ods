package com.wskj.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//HandlerInterceptorAdapter是单例 所有变量会被共享
//you can implements ServletContextAware to get servletContext, the servletContext is application scope.
public class AbsolutelyPathInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("absoluteFullPath", request.getRequestURL());
        request.setAttribute("absoluteContextPath", getAbsolutelypath(request));
        return super.preHandle(request, response, handler);
    }

    private String getAbsolutelypath(HttpServletRequest request) {
        String absoluteContextPath;
        String portocol = "";
        if (request.isSecure()) {
            portocol = "https://";
        } else {
            portocol = "http://";
        }
        absoluteContextPath = portocol + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return absoluteContextPath;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}