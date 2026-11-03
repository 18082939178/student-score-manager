package com.score.servlet;

import com.score.dao.ScoreDao;
import com.score.entity.Score;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 教师端：成绩导出 CSV Servlet
 * 将当前筛选条件下的成绩列表导出为 CSV 文件（UTF-8 BOM，Excel 可直接打开）。
 */
@WebServlet("/teacher/export")
public class ExportServlet extends HttpServlet {

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

        String courseIdStr = req.getParameter("courseId");
        String className = req.getParameter("className");
        Long courseId = null;
        if (courseIdStr != null && !courseIdStr.isEmpty()) {
            courseId = Long.valueOf(courseIdStr);
        }

        List<Score> scores = scoreDao.findList(courseId, className);

        resp.setContentType("text/csv;charset=UTF-8");
        String fileName = "成绩导出_" + System.currentTimeMillis() + ".csv";
        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())
                .replace("+", "%20");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + encoded + "\"; filename*=UTF-8''" + encoded);

        // UTF-8 BOM，保证 Excel 正确识别中文
        OutputStreamWriter writer = new OutputStreamWriter(
                resp.getOutputStream(), StandardCharsets.UTF_8);
        writer.write("\uFEFF");
        writer.write("学号,姓名,班级,课程,成绩,学期\r\n");
        for (Score s : scores) {
            writer.write(csv(s.getStudentNo()) + "," + csv(s.getStudentName()) + ","
                    + csv(s.getClassName()) + "," + csv(s.getCourseName()) + ","
                    + s.getScore() + "," + csv(s.getTerm()) + "\r\n");
        }
        writer.flush();
        writer.close();
    }

    /** CSV 字段转义：含逗号/引号/换行时加引号并转义内部引号 */
    private String csv(String value) {
        if (value == null) {
            return "";
        }
        if (value.indexOf(',') >= 0 || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
