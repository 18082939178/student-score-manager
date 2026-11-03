<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改成绩 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card" style="max-width:560px;margin:0 auto;">
        <h2>修改成绩</h2>

        <form action="${pageContext.request.contextPath}/teacher/score?action=update" method="post">
            <input type="hidden" name="id" value="${score.id}">
            <div class="form-group">
                <label>学生</label>
                <input type="text" value="<c:out value="${score.studentNo}"/> - <c:out value="${score.studentName}"/>（<c:out value="${score.className}"/>）" readonly>
            </div>
            <div class="form-group">
                <label>课程</label>
                <input type="text" value="<c:out value="${score.courseName}"/>（学期：<c:out value="${score.term}"/>）" readonly>
            </div>
            <div class="form-group">
                <label for="score">成绩（0~100）</label>
                <input type="number" id="score" name="score" min="0" max="100" step="0.5"
                       value="${score.score}" required>
            </div>
            <button type="submit" class="btn btn-warning">保存修改</button>
            <a class="btn btn-gray" href="${pageContext.request.contextPath}/teacher/score?action=list">返回列表</a>
        </form>
    </div>
</div>
</body>
</html>
