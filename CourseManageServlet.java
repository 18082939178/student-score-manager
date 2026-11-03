package com.score.servlet;

import com.score.dao.CourseDao;
import com.score.entity.Course;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 教师端：课程管理 Servlet
 * 动作：list / add / delete
 */
@WebServlet("/teacher/course")
public class CourseManageServlet extends HttpServlet {

    private final CourseDao courseDao = new CourseDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                List<Course> courses = courseDao.findAll();
                req.setAttribute("courses", courses);
                req.getRequestDispatcher("/teacher/courseList.jsp").forward(req, resp);
                break;
            case "add":
                add(req, resp);
                break;
            case "delete":
                delete(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/teacher/course?action=list");
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String courseName = req.getParameter("courseName");
        String creditStr = req.getParameter("credit");
        String teacher = req.getParameter("teacher");

        if (courseName == null || courseName.trim().isEmpty()
                || teacher == null || teacher.trim().isEmpty()) {
            req.setAttribute("error", "课程名称和任课教师不能为空");
            req.getRequestDispatcher("/teacher/courseList.jsp").forward(req, resp);
            return;
        }
        double credit = 1.0;
        if (creditStr != null && !creditStr.isEmpty()) {
            try {
                credit = Double.parseDouble(creditStr.trim());
            } catch (NumberFormatException ignored) {
                credit = 1.0;
            }
        }

        Course course = new Course();
        course.setCourseName(courseName.trim());
        course.setCredit(credit);
        course.setTeacher(teacher.trim());
        courseDao.insert(course);
        resp.sendRedirect(req.getContextPath() + "/teacher/course?action=list");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            courseDao.delete(Long.valueOf(idStr));
        }
        resp.sendRedirect(req.getContextPath() + "/teacher/course?action=list");
    }
}
