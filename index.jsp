<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>教师首页 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="welcome-banner">
        <h2>欢迎，<c:out value="${loginUser.name}"/> 老师</h2>
        <p>班级学生成绩管理系统 —— 教师工作台</p>
    </div>

    <div class="func-grid">
        <a class="func-card" href="${pageContext.request.contextPath}/teacher/score?action=toAdd">
            <div class="icon">📝</div>
            <h3>录入成绩</h3>
            <p>选择班级学生与课程，录入考试成绩</p>
        </a>
        <a class="func-card" href="${pageContext.request.contextPath}/teacher/score?action=list">
            <div class="icon">📋</div>
            <h3>成绩管理</h3>
            <p>按课程 / 班级筛选查看、修改、删除成绩</p>
        </a>
        <a class="func-card" href="${pageContext.request.contextPath}/teacher/stat">
            <div class="icon">📊</div>
            <h3>成绩统计</h3>
            <p>课程 / 班级平均分、及格率、优秀率分析</p>
        </a>
        <a class="func-card" href="${pageContext.request.contextPath}/teacher/student?action=list">
            <div class="icon">👥</div>
            <h3>学生管理</h3>
            <p>维护学生档案：学号、姓名、班级</p>
        </a>
        <a class="func-card" href="${pageContext.request.contextPath}/teacher/course?action=list">
            <div class="icon">📚</div>
            <h3>课程管理</h3>
            <p>维护课程信息：课程名、学分、教师</p>
        </a>
    </div>
</div>
</body>
</html>
