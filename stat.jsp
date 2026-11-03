<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>成绩统计 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="card">
        <h2>成绩统计分析</h2>

        <div class="toolbar">
            <form action="${pageContext.request.contextPath}/teacher/stat" method="get">
                <select name="type">
                    <option value="course" <c:if test="${type == 'course'}">selected</c:if>>按课程统计</option>
                    <option value="class" <c:if test="${type == 'class'}">selected</c:if>>按班级统计</option>
                </select>
                <input type="text" name="term" value="<c:out value="${term}"/>" style="width:140px;">
                <button type="submit" class="btn btn-primary">统计</button>
            </form>
            <span style="font-size:13px;color:#7a8aa0;">学期：<c:out value="${term}"/></span>
        </div>

        <c:choose>
            <c:when test="${empty items}">
                <div class="empty-tip">该学期暂无成绩数据</div>
            </c:when>
            <c:otherwise>
                <%-- KPI 概览 --%>
                <c:set var="totalPass" value="0"/>
                <c:set var="warnItems" value="0"/>
                <c:forEach items="${items}" var="it">
                    <c:set var="totalPass" value="${totalPass + it.passRate}"/>
                    <c:if test="${it.failCount > 0}"><c:set var="warnItems" value="${warnItems + 1}"/></c:if>
                </c:forEach>
                <c:set var="avgPass" value="${totalPass / items.size()}"/>
                <div class="kpi-strip">
                    <div class="kpi-box">
                        <div class="num"><c:out value="${items.size()}"/></div>
                        <div class="label"><c:if test="${type == 'course'}">统计课程</c:if><c:if test="${type == 'class'}">统计班级</c:if>（门/个）</div>
                    </div>
                    <div class="kpi-box">
                        <div class="num"><c:out value="${avgPass}"/>%</div>
                        <div class="label">平均及格率</div>
                    </div>
                    <div class="kpi-box">
                        <div class="num"><c:out value="${warnItems}"/></div>
                        <div class="label">存在不及格的项目</div>
                    </div>
                    <c:set var="totalScores" value="0"/>
                    <c:forEach items="${items}" var="it">
                        <c:set var="totalScores" value="${totalScores + it.studentCount}"/>
                    </c:forEach>
                    <div class="kpi-box">
                        <div class="num"><c:out value="${totalScores}"/></div>
                        <div class="label">参与统计成绩（条）</div>
                    </div>
                </div>

                <c:if test="${warnItems > 0}">
                    <div class="alert-box warn">
                        <strong>⚠ 成绩预警：</strong>有 <b><c:out value="${warnItems}"/></b> 个项目存在不及格成绩（&lt;60 分），
                        详见下方表格"不及格人数"列与成绩段分布图。
                    </div>
                </c:if>

                <table class="data-table">
                    <thead>
                    <tr>
                        <th><c:if test="${type == 'course'}">课程</c:if><c:if test="${type == 'class'}">班级</c:if></th>
                        <c:if test="${type == 'course'}"><th>任课教师</th></c:if>
                        <th>参考人数</th>
                        <th>平均分</th>
                        <th>最高分</th>
                        <th>最低分</th>
                        <th>不及格人数</th>
                        <th>及格率</th>
                        <th>优秀率(≥90)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${items}" var="it">
                        <tr>
                            <td><c:out value="${it.name}"/></td>
                            <c:if test="${type == 'course'}"><td><c:out value="${it.extra}"/></td></c:if>
                            <td><c:out value="${it.studentCount}"/></td>
                            <td><b style="color:#1e3c78;"><c:out value="${it.avgScore}"/></b></td>
                            <td class="score-high"><c:out value="${it.maxScore}"/></td>
                            <td class="score-low"><c:out value="${it.minScore}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${it.failCount > 0}">
                                        <span class="pass-badge fail"><c:out value="${it.failCount}"/> 人</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="pass-badge ok">0 人</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:set var="rateClass" value=""/>
                                <c:if test="${it.passRate < 60}"><c:set var="rateClass" value="bad"/></c:if>
                                <c:if test="${it.passRate >= 60 && it.passRate < 85}"><c:set var="rateClass" value="warn"/></c:if>
                                <c:if test="${it.passRate >= 85}"><c:set var="rateClass" value=""/></c:if>
                                <span class="rate-bar"><span class="fill ${rateClass}"
                                      style="width:${it.passRate}%"></span></span>
                                <c:out value="${it.passRate}"/>%
                            </td>
                            <td><c:out value="${it.excellentRate}"/>%</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <%-- 成绩段分布可视化（自绘条形图，无外部依赖） --%>
                <h3 style="font-size:16px;color:#1e3a5f;margin:22px 0 10px;">成绩段分布（<c:out value="${term}"/>）</h3>
                <div class="band-legend">
                    <span><i class="band-seg very-bad" style="width:12px;height:12px;"></i>&lt;60 不及格</span>
                    <span><i class="band-seg bad" style="width:12px;height:12px;"></i>60~69 及格</span>
                    <span><i class="band-seg mid" style="width:12px;height:12px;"></i>70~79 中等</span>
                    <span><i class="band-seg good" style="width:12px;height:12px;"></i>80~89 良好</span>
                    <span><i class="band-seg excellent" style="width:12px;height:12px;"></i>90~100 优秀</span>
                </div>
                <div class="band-chart">
                    <c:forEach items="${items}" var="it">
                        <c:set var="maxBand" value="1"/>
                        <c:forEach begin="0" end="4" var="i">
                            <c:if test="${it.bands[i] > maxBand}"><c:set var="maxBand" value="${it.bands[i]}"/></c:if>
                        </c:forEach>
                        <div class="band-row">
                            <div class="band-name"><c:out value="${it.name}"/></div>
                            <div class="band-track">
                                <c:if test="${it.bands[0] > 0}"><div class="band-seg very-bad" style="width:${it.bands[0] / maxBand * 100}%"></div></c:if>
                                <c:if test="${it.bands[1] > 0}"><div class="band-seg bad" style="width:${it.bands[1] / maxBand * 100}%"></div></c:if>
                                <c:if test="${it.bands[2] > 0}"><div class="band-seg mid" style="width:${it.bands[2] / maxBand * 100}%"></div></c:if>
                                <c:if test="${it.bands[3] > 0}"><div class="band-seg good" style="width:${it.bands[3] / maxBand * 100}%"></div></c:if>
                                <c:if test="${it.bands[4] > 0}"><div class="band-seg excellent" style="width:${it.bands[4] / maxBand * 100}%"></div></c:if>
                            </div>
                            <span style="width:110px;font-size:12px;color:#7a8aa0;white-space:nowrap;">
                                <c:out value="${it.bands[0]}"/>/<c:out value="${it.bands[1]}"/>/<c:out value="${it.bands[2]}"/>/<c:out value="${it.bands[3]}"/>/<c:out value="${it.bands[4]}"/> 人
                            </span>
                        </div>
                    </c:forEach>
                </div>

                <p style="font-size:12px;color:#94a3b8;margin-top:10px;">
                    说明：及格率为成绩 ≥ 60 分人数占比，优秀率为成绩 ≥ 90 分人数占比；分数段依次为
                    &lt;60、60~69、70~79、80~89、90~100，条长按各段人数相对比例绘制。
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
