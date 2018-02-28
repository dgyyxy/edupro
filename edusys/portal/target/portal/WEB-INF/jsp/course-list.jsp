<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
    <link href="${ctx}/resources/css/course.css" rel="stylesheet">
    <link href="${ctx}/resources/css/modal.css" rel="stylesheet">
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
            <h3 style="color:white;">${job.name}</h3>
        </div>
    </section>
    <div class="wrapsemibox">
        <div class="semiboxshadow text-center">
            <img src="${ctx}/resources/img/shp.png" class="img-responsive" alt="">
        </div>
        <!-- PORTFOLIO THREE COLUMNS
================================================== -->
        <section class="container animated fadeInDown">
            <div class="moco-course-list">

                <div class="row" style="margin-left: 10px;">
                    <div id="filter">
                        <ul>
                            <li><a href="${ctx}/course/list/${job.id}"  class="selected"><i class="icon icon-reorder"></i> 全部课件 <span class="badge badge-warning">${total}</span></a></li>
                            <li><a href="javascript:void(0);" ><i class="icon icon-th-large"></i> 未学课件 <span class="badge badge-warning">${unstudy}</span></a></li>
                            <li><a href="${ctx}/task/list"><i class="icon icon-mail-reply"></i> 返回学习任务</a></li>
                        </ul>
                        <div style="float: right;">
                            <input type="text" placeholder="Search…" class="input-medium" id="search">
                            <button class="btn btn-success" type="button" id="searchBtn"><i class="icon icon-search"></i>搜索</button>
                        </div>
                    </div>
                </div>
                <c:if test="${total == 0}">
                    没有搜索到您需要的课程！
                </c:if>
                <c:if test="${total > 0}">
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

                        <div class="moco-course-wrap">
                            <div class="moco-course-box">
                                <div class="sort-num">${vstatus.index+1}</div>
                                <div class="course-card-learning">
                                    <c:if test="${empty mapStatus[item.courseware.id]}">
                                        未学
                                    </c:if>
                                    <c:if test="${mapStatus[item.courseware.id]==1}">学习中</c:if>
                                    <c:if test="${mapStatus[item.courseware.id]==2}">已完成</c:if>
                                </div>
                                <img src="http://${ipstr}/upload/courseware/${item.courseware.picture}" height="124" width="100%"/>
                                <div class="moco-course-intro" id="detail1">
                                    <h3>
                                            ${item.courseware.name}
                                    </h3>
                                    <p>${item.courseware.content}</p>
                                </div>
                                <div class="moco-course-bottom">
                                    <span class="l color-red favorite" data-id="${item.courseware.id}"><c:if test="${empty mapFavorite[item.courseware.id] || mapFavorite[item.courseware.id] == 0}"><i class="icon icon-heart-empty"></i></c:if><c:if test="${mapFavorite[item.courseware.id] == 1}"><i class="icon icon-heart"></i></c:if></span>
                                    <span class="m playshow" data-name="${item.courseware.name}" data-id="${item.courseware.id}" data-uri="${item.courseware.uriStr}" data-time="${item.courseware.time}">进入学习</span>
                                    <span class="r">${item.courseware.time}分钟</span>
                                </div>
                            </div>
                        </div>

                    </c:forEach>
                </c:if>

            </div>
        </section>
        <div class="row pagediv">
            ${pageHtml}
        </div>
    </div>
    <!-- /.wrapsemibox end-->

    <jsp:include page="common/js.jsp" flush="true"/>
    <jsp:include page="common/footer.jsp" flush="true"/>

</div>
<script>
    var time = 0;
    var courseId = 0;
    var jobId = '${job.id}';
    var userId = '${user.stuId}';
    var status = 0;
    var play_end = 0;

    $( function() {

        //搜索
        $('#searchBtn').click(function(){
            var search = $('#search').val();
            if(search != ''){
                location.href = '${ctx}/course/list/'+jobId+'?page=${page}&search='+search;
            }
        });

        var timer = null;//计时器

        //进入学习
        $('.playshow').each(function(){
            var button = $(this);
            $(this).click(function(){
                var uri = button.data('uri');
                courseId = button.data('id');

                var timesecond = parseInt(button.data('time'))*2;
                time = new Date().getTime();
                timer = window.setTimeout('showTip()', timesecond*60*1000);

                var courseName = button.data('name');
                var filetype = uri.substring(uri.lastIndexOf('.')+1);

                uri = 'http://${ipstr}/upload/courseware/'+uri;

                if(filetype == 'mp4'){
                   uri = '${ctx}/course/player?uri='+encodeURI(uri);
                }else if(filetype == 'pdf'){
                    uri = 'http://${ipstr}/upload/courseware/'+button.data('uri');
                }else{
                    //uri = 'http://localhost/upload/courseware/lvsezhuozhuang/';
                    uri = 'http://${ipstr}/upload/'+button.data('uri');
                    uri = '${ctx}/course/course-player?uri='+encodeURI(uri);
                }

                layer.open({
                    type: 2,
                    title: courseName,
                    resize: true,
                    skin: 'layui-layer-molv',
                    shade: [0.9,'#696969'],
                    area: ['980px', '600px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: uri,
                    success: function(layero, index){
                        //记录学习
                        $.ajax({
                            type: 'get',
                            url: '${ctx}/course/record/'+userId+'/'+jobId+'/'+courseId,
                            success: function(result) {
                                status = result.data;
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                //layer.msg(textStatus);
                            }
                        });

                        //禁用最小化按钮
                        $('a.layui-layer-min').hide();

                    },
                    cancel: function(index, layero){
                        //pdf 学完
                        if(filetype == 'pdf'){
                            if(status != 2){
                                //记录学习
                                $.ajax({
                                    type: 'get',
                                    url: '${ctx}/course/study-end/' + userId + '/' + jobId + '/' + courseId,
                                    success: function (result) {
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    }
                                });
                            }
                        }
                        time = new Date().getTime() - time;
                        window.clearTimeout(timer);
                        //记录学习
                        $.ajax({
                            type: 'get',
                            url: '${ctx}/course/study/'+userId+'/'+jobId+'/'+courseId+'?time='+time+'&status='+status,
                            success: function(result) {
                                layer.close(index)
                                var statusval = result.data;
                                if(statusval == 1){
                                    $(button).parent().parent().find('.course-card-learning').text('学习中');
                                }else if(statusval == 2){
                                    $(button).parent().parent().find('.course-card-learning').text('已完成');
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                //layer.msg(textStatus);
                            }
                        });
                        return false;
                    },
                    full: function(index, layero){
                        //layer.msg('full');
                    },
                    restore: function(index, layero){
                        //禁用最小化按钮
                        $('a.layui-layer-min').hide();
                    }
                });



            });
        });


        //收藏操作
        $('.moco-course-bottom').find('.favorite').each(function(){

            $(this).click(function(){
                var obj = $(this)
                var favorite = 1;
                var classstr = $(obj).find('i').attr('class');
                var courseId = $(obj).data('id');
                if(classstr.indexOf('icon-heart-empty') == -1){
                    favorite = 0
                }
                $.ajax({
                    type: 'get',
                    url: '${ctx}/course/favorite/'+jobId+'/'+courseId+'/'+favorite,
                    success: function(result) {
                        if (result.code != 1) {
                            if (result.data instanceof Array) {
                                $.each(result.data, function(index, value) {
                                    layer.msg(value.errorMsg);
                                });
                            } else {
                                layer.msg(result.data);
                            }
                        } else {
                            if(favorite == 0) {
                                $(obj).find('i').attr('class', 'icon icon-heart-empty');
                            }else if(favorite == 1){
                                $(obj).find('i').attr('class', 'icon icon-heart');
                            }
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        layer.msg(textStatus);
                    }
                });
            });
        });
    });

    var showTip = function(){
        layer.alert('注意，您已超过正常学习用时！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
            layer.closeAll();
        });
    };
</script>
</body>
</html>
