package com.score.servlet;

import com.score.dao.ScoreDao;
import com.score.dao.StudentDao;
import com.score.entity.Score;
import com.score.entity.Student;
import com.score.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 学生端：查询本人成绩与个人统计 Servlet
 */
@WebServlet("/student/myScore")
public class StudentScoreServlet extends HttpServlet {

    private final ScoreDao scoreDao = new ScoreDao();
    private final StudentDao studentDao = new StudentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loginUser");
        Student student = studentDao.findByUserId(user.getId());
        if (student == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "当前账号未关联学生信息");
            return;
        }

        List<Score> scores = scoreDao.findByStudentId(student.getId());
        double[] stat = scoreDao.statForStudent(student.getId());

        // 为每条成绩填充班级内排名
        for (Score s : scores) {
            s.setRank(scoreDao.rankInCourse(s.getCourseId(), student.getId(), s.getScore()));
        }

        // 计算个人及格率（及格门数 / 已修课程门数）
        double passRate = 0;
        if (stat[0] > 0) {
            passRate = Math.round(stat[4] / stat[0] * 10000) / 100.0;
        }

        req.setAttribute("student", student);
        req.setAttribute("scores", scores);
        req.setAttribute("stat", stat);
        req.setAttribute("passRate", passRate);
        req.getRequestDispatcher("/student/myScore.jsp").forward(req, resp);
    }
}
