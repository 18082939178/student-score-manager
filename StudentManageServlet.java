package com.score.servlet;

import com.score.dao.StudentDao;
import com.score.entity.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 教师端：学生管理 Servlet
 * 动作：list / toAdd / add / toEdit / update / delete
 */
@WebServlet("/teacher/student")
public class StudentManageServlet extends HttpServlet {

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
        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                list(req, resp);
                break;
            case "toAdd":
                req.getRequestDispatcher("/teacher/studentAdd.jsp").forward(req, resp);
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

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Student> students = studentDao.findAll();
        req.setAttribute("students", students);
        req.getRequestDispatcher("/teacher/studentList.jsp").forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String studentNo = req.getParameter("studentNo");
        String name = req.getParameter("name");
        String className = req.getParameter("className");

        if (studentNo == null || name == null || className == null
                || studentNo.trim().isEmpty() || name.trim().isEmpty()
                || className.trim().isEmpty()) {
            req.setAttribute("error", "学号、姓名、班级均不能为空");
            req.getRequestDispatcher("/teacher/studentAdd.jsp").forward(req, resp);
            return;
        }

        if (studentDao.existsByStudentNo(studentNo.trim(), null)) {
            req.setAttribute("error", "学号 " + studentNo.trim() + " 已存在");
            req.setAttribute("studentNo", studentNo.trim());
            req.setAttribute("name", name.trim());
            req.setAttribute("className", className.trim());
            req.getRequestDispatcher("/teacher/studentAdd.jsp").forward(req, resp);
            return;
        }

        Student student = new Student();
        student.setStudentNo(studentNo.trim());
        student.setName(name.trim());
        student.setClassName(className.trim());
        studentDao.insert(student);
        resp.sendRedirect(req.getContextPath() + "/teacher/student?action=list");
    }

    private void toEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/teacher/student?action=list");
            return;
        }
        Student student = studentDao.findById(Long.valueOf(idStr));
        if (student == null) {
            resp.sendRedirect(req.getContextPath() + "/teacher/student?action=list");
            return;
        }
        req.setAttribute("student", student);
        req.getRequestDispatcher("/teacher/studentEdit.jsp").forward(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        String studentNo = req.getParameter("studentNo");
        String name = req.getParameter("name");
        String className = req.getParameter("className");

        if (idStr == null || idStr.isEmpty() || studentNo == null || name == null || className == null) {
            resp.sendRedirect(req.getContextPath() + "/teacher/student?action=list");
            return;
        }
        Long id = Long.valueOf(idStr);
        if (studentDao.existsByStudentNo(studentNo.trim(), id)) {
            resp.sendRedirect(req.getContextPath() + "/teacher/student?action=toEdit&id=" + idStr);
            return;
        }
        Student student = new Student();
        student.setId(id);
        student.setStudentNo(studentNo.trim());
        student.setName(name.trim());
        student.setClassName(className.trim());
        studentDao.update(student);
        resp.sendRedirect(req.getContextPath() + "/teacher/student?action=list");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            studentDao.delete(Long.valueOf(idStr));
        }
        resp.sendRedirect(req.getContextPath() + "/teacher/student?action=list");
    }
}
