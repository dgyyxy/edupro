<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.lang.*" %>
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

    <style type="text/css">
        .message{
            position: relative;
            margin: 0px auto 0 auto;
            max-width: 920px;
            width: 100%;
            min-height: 80px;
        }

        .message blockquote p {
            font-size: 14px;
            color: #000;
            font-weight: 300;
            margin: 0.4em 0 1em;
        }

        .message blockquote span {
            font-size: 14px;
            color: #888;
            font-weight: 300;
            margin: 0.4em 0 1em;
        }

        .pagediv {
            margin-top: 20px;
            text-align: center;
        }

        .pagediv .pagination>li>a {
            padding: 7px 14px;
            margin-left: 10px;
        }
    </style>

    <jsp:include page="common/css.jsp" flush="true"/>
</head>
<body class="off">
<!-- /.wrapbox start-->
<div class="wrapbox">
    <jsp:include page="common/top.jsp" flush="true">
        <jsp:param name="menu" value="index"/>
    </jsp:include>

    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig"></h1>
            <p class="animated fadeInRightBig container page-description">

            </p>
        </div>
    </section>

    <!-- /.carousel end-->
    <div class="wrapsemibox">
        <div class="semiboxshadow text-center">
            <img src="${ctx}/resources/img/shp.png" class="img-responsive" alt="">
        </div>
        <!-- TESTIMONIALS
================================================== -->
        <section class="grayarea topspace0">
            <div class="container">
                <div id="cbp-qtrotator">
                    <c:forEach items="${list}" var="item">
                        <div class="message">
                            <${ctx}/resources/img src="my${ctx}/resources/img/index2.jpg" height="278" width="370" alt="">
                            <blockquote>
                                <p class="bigquote text-left">${item.content}</p>
                                <span><i class="icon icon-user"></i>&nbsp;${item.creator} &nbsp;<i class="icon icon-time"></i>&nbsp;<fmt:formatDate type="date" value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            </blockquote>
                            <hr/>
                        </div>
                        <br/>
                    </c:forEach>
                </div>
            </div>
            <div class="row pagediv">
                ${pageHtml}
            </div>
        </section>
        <!-- /testimonials end-->

    </div>
    <!-- /.wrapsemibox end-->
    <jsp:include page="common/js.jsp" flush="true"/>
    <jsp:include page="common/footer.jsp" flush="true"/>

</div>
<script>
</script>
</body>
</html>
