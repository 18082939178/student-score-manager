<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String _uri = request.getRequestURI();
    String _ctx = request.getContextPath();
    String _home = "", _score = "", _stat = "", _stu = "", _course = "";
    if (_uri.contains("/teacher/index.jsp")) _home = "active";
    else if (_uri.contains("/teacher/score")) _score = "active";
    else if (_uri.contains("/teacher/stat")) _stat = "active";
    else if (_uri.contains("/teacher/student")) _stu = "active";
    else if (_uri.contains("/teacher/course")) _course = "active";
%>
<nav class="navbar">
    <div class="brand">🎓 成绩管理系统</div>
    <div class="menu">
        <a href="<%=_ctx%>/teacher/index.jsp" class="<%=_home%>">首页</a>
        <a href="<%=_ctx%>/teacher/score?action=list" class="<%=_score%>">成绩管理</a>
        <a href="<%=_ctx%>/teacher/stat" class="<%=_stat%>">成绩统计</a>
        <a href="<%=_ctx%>/teacher/student?action=list" class="<%=_stu%>">学生管理</a>
        <a href="<%=_ctx%>/teacher/course?action=list" class="<%=_course%>">课程管理</a>
    </div>
    <div class="user-info">
        <span>👨‍🏫 <c:out value="${loginUser.name}"/>（教师）</span>
        <a href="<%=_ctx%>/changePwd">修改密码</a>
        <a href="<%=_ctx%>/logout">退出登录</a>
    </div>
</nav>
