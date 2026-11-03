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
 * 修改密码 Servlet（教师 / 学生通用）
 * 校验原密码（MD5 比对）后更新新密码，登录态不变。
 */
@WebServlet("/changePwd")
public class ChangePasswordServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 未登录由 AuthFilter 拦截；已登录则展示修改页
        req.getRequestDispatcher("/changePwd.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User loginUser = (User) (session == null ? null : session.getAttribute("loginUser"));
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String oldPwd = req.getParameter("oldPwd");
        String newPwd = req.getParameter("newPwd");
        String confirmPwd = req.getParameter("confirmPwd");

        String error = null;
        if (oldPwd == null || oldPwd.isEmpty()
                || newPwd == null || newPwd.isEmpty()
                || confirmPwd == null || confirmPwd.isEmpty()) {
            error = "请完整填写原密码与新密码";
        } else if (newPwd.length() < 6 || newPwd.length() > 20) {
            error = "新密码长度须为 6~20 位";
        } else if (!newPwd.equals(confirmPwd)) {
            error = "两次输入的新密码不一致";
        } else if (newPwd.equals(oldPwd)) {
            error = "新密码不能与原密码相同";
        } else {
            User fresh = userDao.findByUsername(loginUser.getUsername());
            if (fresh == null || !fresh.getPassword().equals(MD5Util.md5(oldPwd))) {
                error = "原密码错误";
            }
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/changePwd.jsp").forward(req, resp);
            return;
        }

        userDao.updatePassword(loginUser.getId(), MD5Util.md5(newPwd));
        // 会话内缓存用户对象同步刷新
        loginUser.setPassword(MD5Util.md5(newPwd));
        session.setAttribute("loginUser", loginUser);

        req.setAttribute("success", "密码修改成功");
        req.getRequestDispatcher("/changePwd.jsp").forward(req, resp);
    }
}
