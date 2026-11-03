<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>出错了</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-card" style="text-align:center;">
        <h1 style="font-size:48px;color:#dc2626;">⚠</h1>
        <h1>页面出错了</h1>
        <p class="sub">请求的页面不存在或服务器发生异常，请返回重试。</p>
        <%
            Object msg = request.getAttribute("javax.servlet.error.message");
            if (msg != null) {
        %>
        <p class="sub" style="color:#dc2626;">错误信息：<%= msg %></p>
        <%
            }
        %>
        <br>
        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">返回登录页</a>
    </div>
</div>
</body>
</html>
