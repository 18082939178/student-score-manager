<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的成绩 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <%-- 个人统计卡片：stat = [课程门数, 平均分, 最高分, 最低分, 及格门数] --%>
    <div class="stat-overview">
        <div class="stat-box">
            <div class="num"><c:out value="${stat[0]}"/></div>
            <div class="label">已修课程（门）</div>
        </div>
        <div class="stat-box">
            <div class="num"><c:out value="${stat[1]}"/></div>
            <div class="label">平均分</div>
        </div>
        <div class="stat-box">
            <div class="num"><c:out value="${stat[2]}"/></div>
            <div class="label">最高分</div>
        </div>
        <div class="stat-box">
            <div class="num"><c:out value="${stat[3]}"/></div>
            <div class="label">最低分</div>
        </div>
        <div class="stat-box">
            <div class="num"><c:out value="${stat[4]}"/></div>
            <div class="label">及格门数</div>
        </div>
        <div class="stat-box">
            <div class="num"><c:out value="${passRate}"/>%</div>
            <div class="label">个人及格率</div>
        </div>
    </div>

    <div class="card">
        <h2><c:out value="${student.name}"/>（<c:out value="${student.studentNo}"/> · <c:out value="${student.className}"/>）的成绩单</h2>

        <%-- 不及格预警 --%>
        <c:if test="${stat[4] < stat[0]}">
            <div class="alert-box warn">
                <strong>⚠ 学习预警：</strong>你当前有 <b><c:out value="${stat[0] - stat[4]}"/></b> 门课程不及格（&lt;60 分），
                已在下表红色标出，请重点关注并针对性复习。
            </div>
        </c:if>
        <c:if test="${stat[4] == stat[0] && stat[0] > 0}">
            <div class="alert-box ok">当前全部课程均达及格线（60 分），继续保持。</div>
        </c:if>

        <c:choose>
            <c:when test="${empty scores}">
                <div class="empty-tip">暂无成绩记录</div>
            </c:when>
            <c:otherwise>
                <%-- 个人成绩可视化：成绩条 --%>
                <div class="score-bars">
                    <c:forEach items="${scores}" var="s">
                        <div class="score-bar-row">
                            <div class="bar-label"><c:out value="${s.courseName}"/></div>
                            <div class="bar-track">
                                <div class="bar-fill ${s.score >= 60 ? '' : 'warn'}"
                                     style="width:${s.score}%"></div>
                            </div>
                            <div class="bar-num">
                                <span class="${s.score >= 60 ? 'score-high' : 'score-low'}"><c:out value="${s.score}"/></span>
                                <c:if test="${s.rank == 1}"> · 班级第1名</c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <table class="data-table">
                    <thead>
                    <tr>
                        <th>课程名称</th>
                        <th>成绩</th>
                        <th>是否及格</th>
                        <th>班级排名</th>
                        <th>学期</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${scores}" var="s">
                        <tr>
                            <td><c:out value="${s.courseName}"/></td>
                            <td>
                                <span class="${s.score >= 60 ? 'score-high' : 'score-low'}">
                                    <c:out value="${s.score}"/>
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${s.score >= 60}">
                                        <span class="pass-badge ok">及格</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="pass-badge fail">不及格</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <span class="rank-badge ${s.rank == 1 ? 'top' : 'mid'}"><c:out value="${s.rank}"/></span>
                                / <c:out value="${scores.size()}"/> 人
                            </td>
                            <td><c:out value="${s.term}"/></td>
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
