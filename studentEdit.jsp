<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑学生 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card" style="max-width:560px;margin:0 auto;">
        <h2>编辑学生</h2>

        <form action="${pageContext.request.contextPath}/teacher/student?action=update" method="post">
            <input type="hidden" name="id" value="${student.id}">
            <div class="form-group">
                <label for="studentNo">学号</label>
                <input type="text" id="studentNo" name="studentNo" required value="${student.studentNo}">
            </div>
            <div class="form-group">
                <label for="name">姓名</label>
                <input type="text" id="name" name="name" required value="${student.name}">
            </div>
            <div class="form-group">
                <label for="className">班级</label>
                <input type="text" id="className" name="className" required value="${student.className}">
            </div>
            <button type="submit" class="btn btn-warning">保存修改</button>
            <a class="btn btn-gray" href="${pageContext.request.contextPath}/teacher/student?action=list">返回列表</a>
        </form>
    </div>
</div>
</body>
</html>
