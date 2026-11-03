package com.score.servlet;

import com.score.dao.ScoreDao;
import com.score.entity.StatItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 教师端：成绩统计 Servlet
 * 按课程 / 按班级统计平均分、及格率、最高分、最低分、优秀率
 */
@WebServlet("/teacher/stat")
public class StatServlet extends HttpServlet {

    private final ScoreDao scoreDao = new ScoreDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String term = req.getParameter("term");
        if (term == null || term.isEmpty()) {
            term = "2025-2026-1";
        }
        String type = req.getParameter("type");
        if (type == null || type.isEmpty()) {
            type = "course";
        }

        List<StatItem> items;
        if ("class".equals(type)) {
            items = scoreDao.statByClass(term);
        } else {
            items = scoreDao.statByCourse(term);
        }

        req.setAttribute("items", items);
        req.setAttribute("term", term);
        req.setAttribute("type", type);
        req.getRequestDispatcher("/teacher/stat.jsp").forward(req, resp);
    }
}
