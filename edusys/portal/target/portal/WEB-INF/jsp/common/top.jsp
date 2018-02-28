<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="menu" value="<%=request.getParameter(\"menu\")%>"/>
<!-- TOP AREA
================================================== -->
<section class="toparea">
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-right animated fadeInRight">
                <div class="social-icons">
                    <c:choose>
                        <c:when test="${empty user}">
                            <span style="cursor:pointer;" data-toggle="modal" data-target="#signin"><a class="icon icon-user" href="javascript:void(0);"></a>登录</span>
                            <span style="cursor:pointer;" data-toggle="modal" data-target="#signup"><a class="icon icon-signin" href="javascript:void(0);"></a>注册</span>
                        </c:when>
                        <c:otherwise>
                            <span><a class="icon icon-user" href="javascript:void(0);"></a>${user.stuName}</span>
                            <span style="cursor:pointer;" onclick="javascript:location.href='${ctx}/login/out';"><a class="icon icon-signout" href="javascript:void(0);"></a>安全退出</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- /.toparea end-->
<!-- NAV
================================================== -->
<nav class="navbar navbar-fixed-top wowmenu" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand logo-nav" href="${ctx}/index"><img src="${ctx}/resources/img/logo.png" alt="logo"></a>
        </div>
        <ul id="nav" class="nav navbar-nav pull-right">
            <li <c:if test="${menu=='index'}">class="active"</c:if>><a href="${ctx}/index">首页</a></li>

            <c:choose>
                <c:when test="${empty user}">
                    <li data-toggle="modal" data-target="#signin" data-menu="task"><a href="javascript:void(0);">学习中心</a></li>
                    <li data-toggle="modal" data-target="#signin" data-menu="exam"><a href="javascript:void(0);">考试中心</a></li>
                    <li data-toggle="modal" data-target="#signin" data-menu="info"><a href="javascript:void(0);">个人中心</a></li>
                </c:when>
                <c:otherwise>
                    <li <c:if test="${menu=='task'}">class="active"</c:if>><a href="${ctx}/task/list">学习中心</a></li>
                    <li <c:if test="${menu=='exam'}">class="active"</c:if>><a href="${ctx}/exam/list">考试中心</a></li>
                    <li <c:if test="${menu=='info'}">class="active"</c:if>><a href="${ctx}/info/index">个人中心</a></li>
                </c:otherwise>
            </c:choose>

        </ul>
    </div>
</nav>
<!-- /nav end-->
<!-- HEADER IMAGE AREA
================================================== -->
