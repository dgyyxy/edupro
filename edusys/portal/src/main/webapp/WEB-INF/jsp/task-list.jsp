<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="航空CBT在线学习系统">
    <meta name="author" content="Gary Duan，gary_duan@yeah.net">
    <title>航空CBT在线学习系统</title>

    <jsp:include page="common/css.jsp" flush="true"/>
    <link rel="stylesheet" href="${ctx}/resources/css/card.css"/>
</head>
<body class="off">
<!-- /.wrapbox start-->
<div class="wrapbox">
    <jsp:include page="common/top.jsp" flush="true">
        <jsp:param name="menu" value="task"/>
    </jsp:include>

    <!-- PAGE TITLE
================================================== -->
    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig"></h1>
            <p class="animated fadeInRightBig container page-description">

            </p>
        </div>
    </section>
    <div class="wrapsemibox">
        <div class="semiboxshadow text-center">
            <img src="${ctx}/resources/img/shp.png" class="img-responsive" alt="">
        </div>
        <!-- PORTFOLIO THREE COLUMNS
================================================== -->
        <section class="container animated fadeInDown">

            <div class="row" style="margin-left: 45px;">
                <div id="filter">
                    <ul>
                        <li><a href="${ctx}/task/list?tag=1" <c:if test="${tag == 1}">class="selected"</c:if>><i class="icon icon-reorder"></i> 进行中学习任务 </a></li>
                        <li><a href="${ctx}/task/list?tag=2" <c:if test="${tag == 2}">class="selected"</c:if>><i class="icon icon-th-large"></i> 已结束学习任务 </a></li>
                    </ul>
                </div>
            </div>

            <div id="content" class="cart-list">
                <c:forEach var="item" items="${list}" varStatus="vstatus">
                    <c:set var="colorstr" value="green"/>
                    <c:if test="${vstatus.index%2 == 0}">
                        <c:set var="colorstr" value="red"/>
                    </c:if>
                    <c:if test="${vstatus.index%3 == 0}">
                        <c:set var="colorstr" value="purple"/>
                    </c:if>
                    <c:if test="${vstatus.index%5 == 0}">
                        <c:set var="colorstr" value="blue"/>
                    </c:if>
                    <div class="index-card-container course-card-container container">
                        <div class="course-card">
                            <c:if test="${not empty map[item.id]}"><div class="course-card-learning">${map[item.id]}</div></c:if>
                            <div class="course-card-top cart-color ${colorstr}">
                                <i class="icon icon-flag-alt"></i>
                                <span>${item.name}</span>
                            </div>
                            <div class="course-card-content">
                                <jsp:useBean id="startDate" class="java.util.Date"/>
                                <jsp:useBean id="endDate" class="java.util.Date"/>
                                <jsp:setProperty name="startDate" property="time" value="${item.startTime}"/>
                                <jsp:setProperty name="endDate" property="time" value="${item.endTime}"/>
                                <label class="course-label">开始时间：<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd HH:mm:ss" /></label>
                                <label class="course-label">截止时间：<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd HH:mm:ss" /></label>
                                <label class="course-label">学时：<i style="color:green;">${item.time}</i>&nbsp;分钟</label>
                                <label class="course-label">开课老师：${item.teacher}</label>
                                <div class="clearfix course-card-bottom">
                                    <div class="course-card-info">
                                        已学时间：<i style="color:red;" class="time">${timeMap[item.id]}</i>
                                    </div>
                                    <a class="study-btn" data-id="${item.id}" data-start="${item.startTime}" data-end="${item.endTime}" href="javascript:void(0);">开始学习</a>
                                </div>
                            </div>
                        </div>
                        <div class="course-card-bk" style="background-color:#f3f5f7;">

                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="row pagediv">
                ${pageHtml}
            </div>
        </section>
    </div>
    <!-- /.wrapsemibox end-->
    <jsp:include page="common/js.jsp" flush="true"/>

    <jsp:include page="common/footer.jsp" flush="true"/>

</div>


<script>
    //CALL TESTIMONIAL ROTATOR
    $(function() {
        $('.time').each(function(){
            var timestr = $(this).text();
            $(this).text(millisecondToDate(timestr));
        });

        $('.study-btn').click(function(){
            var id = $(this).data('id');
            var startTime = $(this).data('start');
            var endTime = $(this).data('end');

            var nowTime = new Date().getTime();

            if(nowTime > endTime){
                layer.alert('该学习任务已经结束！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                    layer.closeAll();
                });
            }else{
                location.href = '${ctx}/course/list/'+id;
            }
        });
    });
</script>
</body>
</html>
