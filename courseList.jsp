<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>课程管理 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card">
        <div class="toolbar" style="justify-content:space-between;">
            <h2 style="margin:0;border:none;padding:0;">课程管理</h2>
        </div>

        <%
            Object error = request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error-msg"><%= error %></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/teacher/course?action=add" method="post"
              style="display:flex;gap:10px;flex-wrap:wrap;margin-bottom:20px;">
            <input type="text" name="courseName" placeholder="课程名称" required
                   style="padding:8px 10px;border:1px solid #d5dee8;border-radius:6px;font-size:13px;">
            <input type="number" name="credit" placeholder="学分" step="0.5" min="0" value="3.0"
                   style="width:90px;padding:8px 10px;border:1px solid #d5dee8;border-radius:6px;font-size:13px;">
            <input type="text" name="teacher" placeholder="任课教师" required
                   style="padding:8px 10px;border:1px solid #d5dee8;border-radius:6px;font-size:13px;">
            <button type="submit" class="btn btn-success">+ 新增课程</button>
        </form>

        <c:choose>
            <c:when test="${empty courses}">
                <div class="empty-tip">暂无课程数据</div>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>课程名称</th>
                        <th>学分</th>
                        <th>任课教师</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${courses}" var="c">
                        <tr>
                            <td><c:out value="${c.id}"/></td>
                            <td><c:out value="${c.courseName}"/></td>
                            <td><c:out value="${c.credit}"/></td>
                            <td><c:out value="${c.teacher}"/></td>
                            <td class="actions">
                                <a class="btn btn-danger"
                                   href="${pageContext.request.contextPath}/teacher/course?action=delete&id=${c.id}"
                                   onclick="return confirm('删除课程将同时删除该课程的全部成绩，确定继续吗？');">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
