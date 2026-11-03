<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>录入成绩 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card" style="max-width:560px;margin:0 auto;">
        <h2>录入成绩</h2>

        <%
            Object error = request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error-msg"><%= error %></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/teacher/score?action=add" method="post">
            <div class="form-group">
                <label for="studentId">学生</label>
                <select id="studentId" name="studentId" required>
                    <option value="">-- 请选择学生 --</option>
                    <c:forEach items="${students}" var="st">
                        <option value="${st.id}">
                            <c:out value="${st.studentNo}"/> - <c:out value="${st.name}"/>（<c:out value="${st.className}"/>）
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="courseId">课程</label>
                <select id="courseId" name="courseId" required>
                    <option value="">-- 请选择课程 --</option>
                    <c:forEach items="${courses}" var="c">
                        <option value="${c.id}">
                            <c:out value="${c.courseName}"/>（<c:out value="${c.teacher}"/>）
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="score">成绩（0~100）</label>
                <input type="number" id="score" name="score" min="0" max="100" step="0.5" required>
            </div>
            <div class="form-group">
                <label for="term">学期</label>
                <input type="text" id="term" name="term" value="<c:out value="${defaultTerm}"/>" required>
            </div>
            <button type="submit" class="btn btn-success">保存成绩</button>
            <a class="btn btn-gray" href="${pageContext.request.contextPath}/teacher/score?action=list">返回列表</a>
        </form>
    </div>
</div>
</body>
</html>
