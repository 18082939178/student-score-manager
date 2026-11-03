<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增学生 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card" style="max-width:560px;margin:0 auto;">
        <h2>新增学生</h2>

        <%
            Object error = request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error-msg"><%= error %></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/teacher/student?action=add" method="post">
            <div class="form-group">
                <label for="studentNo">学号</label>
                <input type="text" id="studentNo" name="studentNo" required
                       value="<c:out value="${studentNo}"/>"
                       placeholder="如 20230106">
            </div>
            <div class="form-group">
                <label for="name">姓名</label>
                <input type="text" id="name" name="name" required
                       value="<c:out value="${name}"/>">
            </div>
            <div class="form-group">
                <label for="className">班级</label>
                <input type="text" id="className" name="className" required
                       value="<c:out value="${className}"/>"
                       placeholder="如 软件2301班">
            </div>
            <button type="submit" class="btn btn-success">保存</button>
            <a class="btn btn-gray" href="${pageContext.request.contextPath}/teacher/student?action=list">返回列表</a>
        </form>
    </div>
</div>
</body>
</html>
