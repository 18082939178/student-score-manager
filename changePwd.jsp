<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改密码 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%
    String _ctx = request.getContextPath();
    Integer _role = ((com.score.entity.User) session.getAttribute("loginUser")).getRole();
    String _home;
    if (_role != null && _role == 1) {
        _home = _ctx + "/teacher/index.jsp";
    } else {
        _home = _ctx + "/student/index.jsp";
    }
%>
<nav class="navbar">
    <div class="brand">🎓 成绩管理系统</div>
    <div class="menu">
        <a href="<%=_home%>">首页</a>
        <a href="<%=_ctx%>/changePwd" class="active">修改密码</a>
    </div>
    <div class="user-info">
        <span>👤 <c:out value="${loginUser.name}"/></span>
        <a href="<%=_ctx%>/logout">退出登录</a>
    </div>
</nav>
<div class="container">
    <div class="card" style="max-width:520px;margin:0 auto;">
        <h2>修改密码</h2>

        <c:if test="${not empty success}">
            <div class="alert-box success"><c:out value="${success}"/></div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert-box warn"><c:out value="${error}"/></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/changePwd" method="post">
            <div class="form-group">
                <label for="oldPwd">原密码</label>
                <input type="password" id="oldPwd" name="oldPwd" placeholder="请输入原密码" required>
            </div>
            <div class="form-group">
                <label for="newPwd">新密码</label>
                <input type="password" id="newPwd" name="newPwd" placeholder="6~20 位" required>
            </div>
            <div class="form-group">
                <label for="confirmPwd">确认新密码</label>
                <input type="password" id="confirmPwd" name="confirmPwd" placeholder="再次输入新密码" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">确认修改</button>
        </form>
    </div>
</div>
</body>
</html>
