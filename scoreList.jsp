<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>成绩管理 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card">
        <h2>成绩管理</h2>

        <div class="toolbar">
            <form action="${pageContext.request.contextPath}/teacher/score" method="get">
                <input type="hidden" name="action" value="list">
                <select name="courseId">
                    <option value="">全部课程</option>
                    <c:forEach items="${courses}" var="c">
                        <option value="${c.id}"
                                <c:if test="${currentCourseId == c.id}">selected</c:if>>
                            <c:out value="${c.courseName}"/>
                        </option>
                    </c:forEach>
                </select>
                <select name="className">
                    <option value="">全部班级</option>
                    <c:forEach items="${classes}" var="cl">
                        <option value="${cl}"
                                <c:if test="${currentClassName == cl}">selected</c:if>>
                            <c:out value="${cl}"/>
                        </option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-primary">查询</button>
            </form>
            <a class="btn btn-success" href="${pageContext.request.contextPath}/teacher/score?action=toAdd">+ 录入成绩</a>
            <a class="btn btn-gray" title="导出当前筛选条件下的成绩为 Excel 可打开的 CSV 文件"
               href="${pageContext.request.contextPath}/teacher/export?courseId=${currentCourseId}&className=${currentClassName}">导出 CSV</a>
        </div>

        <%-- 不及格预警摘要 --%>
        <c:set var="failCount" value="0"/>
        <c:forEach items="${scores}" var="s">
            <c:if test="${s.score < 60}"><c:set var="failCount" value="${failCount + 1}"/></c:if>
        </c:forEach>
        <c:if test="${failCount > 0}">
            <div class="alert-box warn">
                <strong>⚠ 成绩预警：</strong>当前列表共有 <b><c:out value="${failCount}"/></b> 条不及格成绩（&lt;60 分），
                已在下表红色标出，请及时关注。
            </div>
        </c:if>
        <c:if test="${failCount == 0 && not empty scores}">
            <div class="alert-box ok">当前列表全部成绩均在及格线（60 分）及以上。</div>
        </c:if>

        <c:choose>
            <c:when test="${empty scores}">
                <div class="empty-tip">暂无成绩记录，请先录入成绩</div>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>班级</th>
                        <th>课程</th>
                        <th>成绩</th>
                        <th>学期</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${scores}" var="s">
                        <tr>
                            <td><c:out value="${s.studentNo}"/></td>
                            <td><c:out value="${s.studentName}"/></td>
                            <td><c:out value="${s.className}"/></td>
                            <td><c:out value="${s.courseName}"/></td>
                            <td>
                                <span class="${s.score >= 60 ? 'score-high' : 'score-low'}">
                                    <c:out value="${s.score}"/>
                                </span>
                            </td>
                            <td><c:out value="${s.term}"/></td>
                            <td class="actions">
                                <a class="btn btn-warning"
                                   href="${pageContext.request.contextPath}/teacher/score?action=toEdit&id=${s.id}">修改</a>
                                <a class="btn btn-danger"
                                   href="${pageContext.request.contextPath}/teacher/score?action=delete&id=${s.id}"
                                   onclick="return confirm('确定删除该条成绩记录吗？');">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
