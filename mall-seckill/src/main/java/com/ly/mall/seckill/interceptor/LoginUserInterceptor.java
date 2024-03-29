package com.ly.mall.seckill.interceptor;


import com.ly.mall.common.vo.MemberResponseVo;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ly.mall.common.constant.AuthServerConstant.LOGIN_USER;


@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Autowired
    BeanFactory beanFactory;
    @Autowired
    ApplicationContext applicationContext;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/kill", uri);
        if (match ) {
            //获取登录的用户信息
            MemberResponseVo attribute = (MemberResponseVo) request.getSession().getAttribute(LOGIN_USER);

            if (attribute != null) {
                //把登录后用户的信息放在ThreadLocal里面进行保存
                loginUser.set(attribute);

                return true;
            } else {
                //未登录，返回登录页面
//            response.setContentType("text/html;charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println("<script>alert('请先进行登录，再进行后续操作！');location.href='http://auth.mall.com/login.html'</script>");
                request.getSession().setAttribute("msg", "请先进行登录");
                response.sendRedirect("http://auth.mall.com/login.html");
                return false;
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
