<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table table-striped table-hover animated fadeInLeft">
    <thead>
    <tr>
        <th>名称</th>
        <th>考试日期</th>
        <th>题量</th>
        <th>总分</th>
        <th>得分</th>
        <th>是否及格</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="item">
    <fmt:parseNumber var="intVar" integerOnly="true" value="${item.pointGet}"/>
    <tr>
        <td>${item.examName}</td>
        <td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td>${item.amount}道</td>
        <td>${item.point}</td>
        <td><c:out value="${intVar}" />分</td>
        <td>
            <c:if test="${item.approved==2}"><span class="label label-success">及格</span></c:if>
            <c:if test="${item.approved==3}"><span class="label label-danger">不及格</span></c:if>
        </td>
    </tr>
    </c:forEach>
    </tbody>
</table>
