package com.score.filter;

import com.score.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录认证与角色权限过滤器
 * - 未登录用户统一跳转登录页
 * - /teacher/* 仅教师（role=1）可访问
 * - /student/* 仅学生（role=0）可访问
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");

        // 当前请求路径（去掉上下文路径）
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();
        String path = uri.substring(ctx.length());

        // 放行资源：登录页、登录/退出接口、验证码、静态资源
        if (path.equals("/login.jsp") || path.equals("/login")
                || path.equals("/logout") || path.equals("/verifyCode")
                || path.startsWith("/css/")
                || path.startsWith("/js/")) {
            chain.doFilter(request, response);
            return;
        }

        // 检查登录状态
        HttpSession session = req.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("loginUser");
        }
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // 角色权限校验
        if (path.startsWith("/teacher/") && user.getRole() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无权限访问教师页面");
            return;
        }
        if (path.startsWith("/student/") && user.getRole() != 0) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "无权限访问学生页面");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
