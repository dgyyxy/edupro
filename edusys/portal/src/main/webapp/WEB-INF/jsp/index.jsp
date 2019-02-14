<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.lang.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ipstr" value="${pageContext.request.getServerName()}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="航空CBT在线学习系统">
    <meta name="author" content="Gary Duan，gary_duan@yeah.net">
    <title>航空CBT在线学习系统</title>

    <jsp:include page="common/css.jsp" flush="true"/>
</head>
<body class="off">
<!-- /.wrapbox start-->
<div class="wrapbox">
    <jsp:include page="common/top.jsp" flush="true">
        <jsp:param name="menu" value="index"/>
    </jsp:include>

    <!-- CAROUSEL
================================================== -->
    <section class="carousel carousel-fade slide home-slider" id="c-slide" data-ride="carousel" data-interval="4500"
             data-pause="false">

        <div class="carousel-inner">
            <c:forEach var="item" items="${advertList}" varStatus="vstatus">
                <c:set var="activestr" value=""/>
                <c:if test="${vstatus.index == 0}">
                    <c:set var="activestr" value="active"/>
                </c:if>
            <div class="item ${activestr}" style="background: url(http://${ipstr}/upload${item.imgurl});height:325px;">
                <div class="container">

                </div>
            </div>
            </c:forEach>
        </div>
        <ol class="carousel-indicators">
            <c:forEach begin="0" end="${advertCount-1}" var="i">
                <c:set var="classstr" value=""/>
                <c:if test="${i==0}">
                    <c:set var="classstr" value="class=\"active\""/>
                </c:if>
                <li data-target="#c-slide" data-slide-to="${i}" ${classstr}></li>
            </c:forEach>
        </ol>
        <!-- /.carousel-inner -->
        <a class="left carousel-control animated fadeInLeft" href="#c-slide" data-slide="prev"><i
                class="icon-angle-left"></i></a>
        <a class="right carousel-control animated fadeInRight" href="#c-slide" data-slide="next"><i
                class="icon-angle-right"></i></a>
    </section>
    <!-- /.carousel end-->
    <div class="wrapsemibox">
        <div class="semiboxshadow text-center">
            <img src="${ctx}/resources/img/shp.png" class="img-responsive" alt="">
        </div>
        <!-- TESTIMONIALS
================================================== -->
        <section class="grayarea topspace10">
            <div class="container">
                <h1 class="small text-center notransition">最新公告&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/notice/list" style="font-size:14px;">>>更多</a></h1>
                <div class="br-hr type_short">
				<span class="br-hr-h">
				<i class="icon-pencil"></i>
				</span>
                </div>
                <div id="cbp-qtrotator" class="cbp-qtrotator notransition">
                    <c:forEach items="${noticeList}" var="item">
                        <div class="cbp-qtcontent">
                            <${ctx}/resources/img src="my${ctx}/resources/img/index2.jpg" height="278" width="370" alt="">
                            <blockquote>
                                <p class="bigquote text-left">${item.content}</p>
                                <span><i class="icon icon-user"></i>&nbsp;${item.creator} &nbsp;<i class="icon icon-time"></i>&nbsp;<fmt:formatDate type="date" value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            </blockquote>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>
        <!-- /testimonials end-->
        <!-- SERVICE BOXES
================================================== -->
        <section class="service-box topspace10">
            <div class="container">
                <div class="row">
                    <ul class="ca-menu">
                        <c:choose>
                            <c:when test="${empty user}">
                                <li class="col-md-4 animated notransition" data-toggle="modal" data-target="#signin" data-menu="task">
                                    <a href="javascript:void(0);">
                                        <div class="grey-box-icon">
                                            <div class="icon-box-top grey-box-icon-pos">
                                                <i class="fontawesome-icon medium circle-white center icon-book"></i>
                                            </div>
                                            <div class="ca-content">
                                                <h2 class="ca-main">学习中心</h2>
                                                <h3 class="ca-sub">
                                                    通过这里的各项学习任务，您可以获取到丰富航空知识和职业技能。
                                                </h3>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                                <li class="col-md-4 animated notransition" data-toggle="modal" data-target="#signin" data-menu="exam">
                                    <a href="javascript:void(0);">
                                        <div class="grey-box-icon">
                                            <div class="icon-box-top grey-box-icon-pos">
                                                <i class="fontawesome-icon medium circle-white center icon-trophy"></i>
                                            </div>
                                            <div class="ca-content">
                                                <h2 class="ca-main">考试中心</h2>
                                                <h3 class="ca-sub">
                                                    请参加您所指定的一场考试，在有效的时间内认真作答完成一次知识考验。
                                                </h3>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                                <li class="col-md-4 animated notransition" data-toggle="modal" data-target="#signin" data-menu="info">
                                    <a href="javascript:void(0);">
                                        <div class="grey-box-icon ">
                                            <div class="icon-box-top grey-box-icon-pos">
                                                <i class="fontawesome-icon medium circle-white center icon-user"></i>
                                            </div>
                                            <div class="ca-content">
                                                <h2 class="ca-main">个人中心</h2>
                                                <h3 class="ca-sub">
                                                    您的个人信息、学习记录、考试记录和课件收藏，都可以在此找到和更新。
                                                </h3>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="col-md-4 animated notransition">
                                    <a href="${ctx}/task/list">
                                        <div class="grey-box-icon">
                                            <div class="icon-box-top grey-box-icon-pos">
                                                <i class="fontawesome-icon medium circle-white center icon-book"></i>
                                            </div>
                                            <div class="ca-content">
                                                <h2 class="ca-main">学习中心</h2>
                                                <h3 class="ca-sub">
                                                    通过这里的各项学习任务，您可以获取到丰富航空知识和职业技能。
                                                </h3>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                                <li class="col-md-4 animated notransition">
                                    <a href="${ctx}/exam/list">
                                        <div class="grey-box-icon">
                                            <div class="icon-box-top grey-box-icon-pos">
                                                <i class="fontawesome-icon medium circle-white center icon-trophy"></i>
                                            </div>
                                            <div class="ca-content">
                                                <h2 class="ca-main">考试中心</h2>
                                                <h3 class="ca-sub">
                                                    请参加您所指定的一场考试，在有效的时间内认真作答完成一次知识考验。
                                                </h3>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                                <li class="col-md-4 animated notransition">
                                    <a href="${ctx}/info/index">
                                        <div class="grey-box-icon ">
                                            <div class="icon-box-top grey-box-icon-pos">
                                                <i class="fontawesome-icon medium circle-white center icon-user"></i>
                                            </div>
                                            <div class="ca-content">
                                                <h2 class="ca-main">个人中心</h2>
                                                <h3 class="ca-sub">
                                                    您的个人信息、学习记录、考试记录和课件收藏，都可以在此找到和更新。
                                                </h3>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>

                    </ul>
                </div>
            </div>
        </section>
        <!-- /.service-box end-->

    </div>
    <!-- /.wrapsemibox end-->
    <jsp:include page="common/js.jsp" flush="true"/>
    <jsp:include page="common/footer.jsp" flush="true"/>

</div>
<script>
    //CALL TESTIMONIAL ROTATOR
    $( function() {
        $('#cbp-qtrotator').cbpQTRotator();
    } );

    jQuery(document).ready(function () {
        if (window.history && window.history.pushState) {
            $(window).on('popstate', function () {
                /// 当点击浏览器的 后退和前进按钮 时才会被触发，
                window.history.pushState('forward', null, '');
                window.history.forward(1);
            });
        }
        //
        window.history.pushState('forward', null, '');  //在IE中必须得有这两行
        window.history.forward(1);
    });
</script>
</body>
</html>
