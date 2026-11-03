<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-card">
        <h1>班级学生成绩管理系统</h1>

        <%-- 错误提示 --%>
        <%
            Object error = request.getAttribute("error");
            if (error != null) {
        %>
        <div class="error-msg"><%= error %></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" placeholder="请输入用户名" required>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" placeholder="请输入密码" required>
            </div>
            <div class="form-group">
                <label for="verifyCode">验证码</label>
                <div class="verify-row">
                    <input type="text" id="verifyCode" name="verifyCode" placeholder="不区分大小写" maxlength="4" required>
                    <img class="verify-img" id="codeImg" title="看不清？点击刷新"
                         src="${pageContext.request.contextPath}/verifyCode"
                         onclick="this.src='${pageContext.request.contextPath}/verifyCode?t='+new Date().getTime();">
                </div>
            </div>
            <button type="submit" class="btn btn-primary btn-block">登 录</button>
        </form>

        <div class="login-tip">
            测试账号 —— 教师：teacher01 / 123456<br>
            学生：stu001 ~ stu005 / 123456
        </div>
    </div>
</div>
</body>
</html>
