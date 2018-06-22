package com.edusys.front.interceptor;

import com.edu.common.dao.model.EduStudent;
import com.edusys.front.listener.MemoryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录信息拦截器
 * Created by Gary on 2017/3/28.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static Logger _log = LoggerFactory.getLogger(LoginInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 过滤ajax
        if (null != request.getHeader("X-Requested-With") && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        }

        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        if(obj!=null){
            EduStudent student = (EduStudent) obj;
            String userkey = "student-"+student.getStuId();
            String sessionId = MemoryData.getSessionIDMap().get(userkey);
            if(sessionId!=null){
                if(sessionId.equals(request.getSession().getId())){
                    request.setAttribute("user", student);
                    return true;
                }else{
                    session.removeAttribute("user");
                    response.sendRedirect("/index");
                    return false;
                }
            }else{
                request.setAttribute("user", student);
                return true;
            }


        }else{
            request.setAttribute("user", "");
            response.sendRedirect("/index");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
