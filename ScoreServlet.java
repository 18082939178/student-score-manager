package com.score.servlet;

import com.score.dao.CourseDao;
import com.score.dao.ScoreDao;
import com.score.dao.StudentDao;
import com.score.entity.Course;
import com.score.entity.Score;
import com.score.entity.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 教师端：成绩管理 Servlet
 * 动作：list / toAdd / add / toEdit / update / delete
 */
@WebServlet("/teacher/score")
public class ScoreServlet extends HttpServlet {

    private final ScoreDao scoreDao = new ScoreDao();
    private final StudentDao studentDao = new StudentDao();
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
                list(req, resp);
                break;
            case "toAdd":
                toAdd(req, resp);
                break;
            case "add":
                add(req, resp);
                break;
            case "toEdit":
                toEdit(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            case "delete":
                delete(req, resp);
                break;
            default:
                list(req, resp);
        }
    }

    /** 成绩列表（可按课程/班级筛选） */
    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String courseIdStr = req.getParameter("courseId");
        String className = req.getParameter("className");
        Long courseId = null;
        if (courseIdStr != null && !courseIdStr.isEmpty()) {
            courseId = Long.valueOf(courseIdStr);
        }
        List<Score> scores = scoreDao.findList(courseId, className);
        List<Course> courses = courseDao.findAll();
        List<String> classes = studentDao.findAllClasses();

        req.setAttribute("scores", scores);
        req.setAttribute("courses", courses);
        req.setAttribute("classes", classes);
        req.setAttribute("currentCourseId", courseId);
        req.setAttribute("currentClassName", className);
        req.getRequestDispatcher("/teacher/scoreList.jsp").forward(req, resp);
    }

    /** 跳转录入页 */
    private void toAdd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Student> students = studentDao.findAll();
        List<Course> courses = courseDao.findAll();
        req.setAttribute("students", students);
        req.setAttribute("courses", courses);
        req.setAttribute("defaultTerm", "2025-2026-1");
        req.getRequestDispatcher("/teacher/scoreAdd.jsp").forward(req, resp);
    }

    /** 录入成绩 */
    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String studentIdStr = req.getParameter("studentId");
        String courseIdStr = req.getParameter("courseId");
        String scoreStr = req.getParameter("score");
        String term = req.getParameter("term");

        // 参数校验
        if (studentIdStr == null || courseIdStr == null || scoreStr == null || term == null
                || studentIdStr.isEmpty() || courseIdStr.isEmpty() || scoreStr.isEmpty()
                || term.trim().isEmpty()) {
            req.setAttribute("error", "请完整填写成绩信息");
            toAdd(req, resp);
            return;
        }
        double scoreValue;
        try {
            scoreValue = Double.parseDouble(scoreStr.trim());
        } catch (NumberFormatException e) {
            req.setAttribute("error", "成绩必须是数字");
            toAdd(req, resp);
            return;
        }
        if (scoreValue < 0 || scoreValue > 100) {
            req.setAttribute("error", "成绩必须在 0~100 之间");
            toAdd(req, resp);
            return;
        }

        Score score = new Score();
        score.setStudentId(Long.valueOf(studentIdStr));
        score.setCourseId(Long.valueOf(courseIdStr));
        score.setScore(scoreValue);
        score.setTerm(term.trim());

        if (scoreDao.exists(score)) {
            req.setAttribute("error", "该学生此课程此学期已有成绩，请勿重复录入");
            toAdd(req, resp);
            return;
        }

        scoreDao.insert(score);
        resp.sendRedirect(req.getContextPath() + "/teacher/score?action=list");
    }

    /** 跳转修改页 */
    private void toEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/teacher/score?action=list");
            return;
        }
        // 通过列表查询获取该记录（简单实现：按课程查全部再定位）
        List<Score> all = scoreDao.findList(null, null);
        Score target = null;
        for (Score s : all) {
            if (s.getId().equals(Long.valueOf(idStr))) {
                target = s;
                break;
            }
        }
        if (target == null) {
            resp.sendRedirect(req.getContextPath() + "/teacher/score?action=list");
            return;
        }
        req.setAttribute("score", target);
        req.getRequestDispatcher("/teacher/scoreEdit.jsp").forward(req, resp);
    }

    /** 修改成绩 */
    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String scoreStr = req.getParameter("score");
        if (idStr == null || idStr.isEmpty() || scoreStr == null || scoreStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/teacher/score?action=list");
            return;
        }
        double scoreValue;
        try {
            scoreValue = Double.parseDouble(scoreStr.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/teacher/score?action=toEdit&id=" + idStr);
            return;
        }
        if (scoreValue < 0 || scoreValue > 100) {
            resp.sendRedirect(req.getContextPath() + "/teacher/score?action=toEdit&id=" + idStr);
            return;
        }
        Score score = new Score();
        score.setId(Long.valueOf(idStr));
        score.setScore(scoreValue);
        scoreDao.update(score);
        resp.sendRedirect(req.getContextPath() + "/teacher/score?action=list");
    }

    /** 删除成绩 */
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            scoreDao.delete(Long.valueOf(idStr));
        }
        resp.sendRedirect(req.getContextPath() + "/teacher/score?action=list");
    }
}
