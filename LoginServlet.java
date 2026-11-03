package com.score.servlet;

import com.score.dao.UserDao;
import com.score.entity.User;
import com.score.util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录处理 Servlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String verifyCode = req.getParameter("verifyCode");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            req.setAttribute("error", "请输入用户名和密码");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 校验图形验证码（忽略大小写）
        Object sessionCode = req.getSession().getAttribute("verifyCode");
        if (verifyCode == null || verifyCode.trim().isEmpty()
                || sessionCode == null
                || !verifyCode.trim().equalsIgnoreCase(sessionCode.toString())) {
            req.setAttribute("error", "验证码错误，请重新输入");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }
        // 校验通过后立即失效，防止复用
        req.getSession().removeAttribute("verifyCode");

        User user = userDao.findByUsername(username.trim());
        // 校验密码（MD5 比对）
        if (user == null || !user.getPassword().equals(MD5Util.md5(password))) {
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 登录成功，保存会话
        HttpSession session = req.getSession();
        session.setAttribute("loginUser", user);

        if (user.getRole() == 1) {
            resp.sendRedirect(req.getContextPath() + "/teacher/index.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/student/index.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 已登录直接进首页
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            User user = (User) session.getAttribute("loginUser");
            if (user.getRole() == 1) {
                resp.sendRedirect(req.getContextPath() + "/teacher/index.jsp");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/student/index.jsp");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
