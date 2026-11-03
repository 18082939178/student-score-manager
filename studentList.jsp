<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生管理 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card">
        <div class="toolbar" style="justify-content:space-between;">
            <h2 style="margin:0;border:none;padding:0;">学生管理</h2>
            <a class="btn btn-success" href="${pageContext.request.contextPath}/teacher/student?action=toAdd">+ 新增学生</a>
        </div>

        <c:choose>
            <c:when test="${empty students}">
                <div class="empty-tip">暂无学生数据</div>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>班级</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${students}" var="st">
                        <tr>
                            <td><c:out value="${st.studentNo}"/></td>
                            <td><c:out value="${st.name}"/></td>
                            <td><c:out value="${st.className}"/></td>
                            <td class="actions">
                                <a class="btn btn-warning"
                                   href="${pageContext.request.contextPath}/teacher/student?action=toEdit&id=${st.id}">编辑</a>
                                <a class="btn btn-danger"
                                   href="${pageContext.request.contextPath}/teacher/student?action=delete&id=${st.id}"
                                   onclick="return confirm('删除学生将同时删除其登录账号和成绩，确定继续吗？');">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <p style="font-size:12px;color:#94a3b8;margin-top:10px;">
                    说明：新增学生时系统自动创建登录账号，初始密码为 123456。
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
