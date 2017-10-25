<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
                            <li><a href="javascript:void(0);"  class="selected"><i class="icon icon-reorder"></i> 全部课件 <span class="badge badge-warning">${total}</span></a></li>
                            <li><a href="javascript:void(0);"  class="finished"><i class="icon icon-th-list"></i> 已学课件 <span class="badge badge-warning">${studyend+study}</span></a></li>
                            <li><a href="javascript:void(0);"  class="unfinished"><i class="icon icon-th-large"></i> 未学课件 <span class="badge badge-warning">${unstudy}</span></a></li>
                            <li><a href="${ctx}/task/list" class="back"><i class="icon icon-mail-reply"></i> 返回学习任务</a></li>
                        </ul>
                        <div style="float: right;">
                            <input type="text" placeholder="Search…" class="input-medium" id="search">
                            <button class="btn btn-success" type="button" id="searchBtn"><i class="icon icon-search"></i>搜索</button>
                        </div>
                    </div>
                </div>

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
                            <div class="sort-num">${item.sortNum}</div>
                            <div class="course-card-learning">
                                <c:if test="${empty mapStatus[item.courseware.id]}">
                                    未学
                                </c:if>
                                <c:if test="${mapStatus[item.courseware.id]==1}">学习中</c:if>
                                <c:if test="${mapStatus[item.courseware.id]==2}">已完成</c:if>
                            </div>
                            <img src="http://106.15.206.195/upload/courseware/${item.courseware.picture}" height="124" width="100%">
                            <div class="moco-course-intro" id="detail1">
                                <h3>
                                    ${item.courseware.name}
                                </h3>
                                <p>${item.courseware.content}</p>
                            </div>
                            <div class="moco-course-bottom">
                                <span class="l color-red favorite" data-id="${item.courseware.id}"><c:if test="${empty mapFavorite[item.courseware.id] || mapFavorite[item.courseware.id] == 0}"><i class="icon icon-heart-empty"></i></c:if><c:if test="${mapFavorite[item.courseware.id] == 1}"><i class="icon icon-heart"></i></c:if></span>
                                <span class="m playshow" data-name="${item.courseware.name}" data-id="${item.courseware.id}" data-uri="${item.courseware.uriStr}" data-time="${item.courseware.time}" data-toggle="modal" data-target="#play_course">进入学习</span>
                                <span class="r">${item.courseware.time}分钟</span>
                            </div>
                        </div>
                    </div>

                </c:forEach>

            </div>
        </section>

        <div class="row pagediv">
            ${pageHtml}
        </div>
    </div>
    <!-- /.wrapsemibox end-->

    <jsp:include page="common/js.jsp" flush="true"/>
    <jsp:include page="common/footer.jsp" flush="true"/>

    <div id="play_course" class="modal_div modal animated fadeInUpNow" aria-hidden="true">
        <div class="my-modal-head">
            <span class="modal-head-title" id="course-name">课程播放</span>
            <button type="button" id="closeBtn" class="close-btn" data-dismiss="modal" hidefocus="true" aria-hidden="true"></button>
        </div>
        <div class="modal-body">
            <!-- <iframe frameborder="0" height="580px" width="940px" name="play_course" src="mp4.html"></iframe> -->
            <iframe id="play_iframe" frameborder="0" height="450px" width="940px" name="play_course" src="javascript:void(0);"></iframe>
        </div>
    </div>

</div>
<script>
    var time = 0;
    var courseId = 0;
    var jobId = '${job.id}';
    var userId = '${user.stuId}';

    $( function() {



        //搜索
        $('#searchBtn').click(function(){
            var search = $('#search').val();
            if(search != ''){
                location.href = '${ctx}/course/list/'+jobId+'?page=${page}&search='+search;
            }
        });

        var timer = null;//计时器

        // 监听课件播放窗口显示
        $('#play_course').on('show.bs.modal', function (event) {

            var button = $(event.relatedTarget);
            var timesecond = parseInt(button.data('time'));
            time = new Date().getTime();
            timer = window.setTimeout('showTip()', timesecond*60*1000);
            var uri = button.data('uri');
            courseId = button.data('id');
            $('#course-name').text(button.data('name'));
            var filetype = uri.substring(uri.lastIndexOf('.')+1);
            uri = 'http://106.15.206.195/upload/courseware/'+uri;
            if(filetype == 'mp4'){
                $('#play_iframe').attr('src', '${ctx}/course/player?uri='+encodeURI(uri));
            }else{
                uri = 'http://106.15.206.195/upload/'+button.data('uri');
                $('#play_iframe').attr('src', uri);
            }

            //记录学习
            $.ajax({
                type: 'get',
                url: '${ctx}/course/record/'+userId+'/'+jobId+'/'+courseId,
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

                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    layer.msg(textStatus);
                }
            });
        });


        // 监听课件播放窗口关闭
        $('#play_course').on('hidden.bs.modal', function (){
            time = new Date().getTime() - time;
            window.clearTimeout(timer);
            $('#play_iframe').attr('src', '');
            //记录学习
            $.ajax({
                type: 'get',
                url: '${ctx}/course/study/'+userId+'/'+jobId+'/'+courseId+'?time='+time,
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

                    }
                    location.reload();
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    layer.msg(textStatus);
                }
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
