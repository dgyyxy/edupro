<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link href="${ctx}/resources/css/card.css" rel="stylesheet">
    <link href="${ctx}/resources/css/exam_modal.css" rel="stylesheet">
</head>
<body class="off">
<!-- /.wrapbox start-->
<div class="wrapbox">
    <jsp:include page="common/top.jsp" flush="true">
        <jsp:param name="menu" value="exam"/>
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
                        <li><a href="javascript:void(0);" data-filter="*" class="selected"><i class="icon icon-reorder"></i> 您目前共有<span class="badge badge-warning">${total}</span>&nbsp;场考试</a></li>
                    </ul>
                </div>
            </div>

            <!-- MASONRY ITEMS START
        ================================================== -->
            <!-- END filtering menu -->
            <div id="content" class="cart-list">
                <c:forEach var="item" items="${list}" varStatus="vstatus">
                    <c:set var="colorstr" value="green"/>
                    <c:if test="${vstatus.index%2 == 0}">
                        <c:set var="colorstr" value="orange"/>
                    </c:if>
                    <c:if test="${vstatus.index%3 == 0}">
                        <c:set var="colorstr" value="purple"/>
                    </c:if>
                    <c:if test="${vstatus.index%5 == 0}">
                        <c:set var="colorstr" value="blue"/>
                    </c:if>
                    <div class="index-card-container course-card-container container">
                        <div class="course-card">
                            <div class="course-card-top cart-color ${colorstr}">
                                <i class="icon icon-trophy"></i>
                                <span>${item.examName}</span>
                                <c:if test="${item.status == 2 || item.status == 3}">
                                    <div class="course-card-learning">
                                            已交卷
                                    </div>
                                </c:if>
                            </div>

                            <div class="course-card-content">
                                <label class="course-label">开始时间：<fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
                                <label class="course-label">截止时间：<fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
                                <label class="course-label">时长：<i style="color:red;">${item.duration}</i>&nbsp;分钟</label>
                                <label class="course-label">题量：<i style="color:red;">${item.amount}</i>&nbsp;道</label>
                                <div class="clearfix course-card-bottom">
                                    <div class="course-card-info">
                                        总分：<i style="color:red;">${item.totalPoint}</i>&nbsp;分
                                    </div>
                                    <c:choose>
                                        <c:when test="${item.status == 2 || item.status == 3}">
                                            <span class="study-btn" onclick="examing(${item.pointGet});">开始答卷</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="study-btn" data-id="${item.id}" data-status="${item.status}" data-toggle="modal" data-target="#exam-notice-panel">开始答卷</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
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


<div id="exam-notice-panel" class="rl-modal-exam in modal" aria-hidden="true">
    <div class="rl-modal-header-exam">
        <h1>
            <span class="active-title">考生信息</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true" aria-hidden="true"></button>
    </div>
    <div class="rl-modal-body-exam">
        <div class="row">
            <span class="col-md-6">考生姓名：${user.stuName}</span>
            <span class="col-md-6">身份证号：${user.cardNo}</span>
        </div>
        <div class="row">
            <span class="col-md-6">一级机构：${user.organizationName1}</span>
            <span class="col-md-6">二级机构：${user.organizationName2}</span>
        </div>
    </div>
    <div class="rl-modal-header-exam">
        <h1>
            <span class="active-title">考试须知</span>
        </h1>
    </div>
    <div class="rl-modal-body-exam">
        <div class="row notice-context">
1、考试全部采用上机操作的方式，任何书籍、资料、计算器、计算机、光盘、纸、笔、平板及手机等通讯工具一律不得带入考场；<br/>
2、考生只能按规定的时间、座位参加规定科目的考试，并严格按照本人身份证或其他证件和相应科目的座位号进入考试系统答题；<br/>
3、由监考老师统一告知本场考试的“开考密码”，请注意前方黑板；<br/>
4、考试中应严格按照考试系统要求操作，若发生“电脑死机”等异常情况（如：无法答题、提交考卷失败、点击按钮无效等）<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;，考生应举手报告监考人员，由监考人员进行处置并换机考试， 重新登录后可继续答题，已做题目保留；<br/>
5、考试答题完毕并在检查无误后，必须点击“提交试卷”完成交卷；<br/>
6、考试时间一律以考场计时器为准。迟到考生不得延时交卷。提前交卷者，须经监考人员确认正常无误方可离场；<br/>
7、遇到特殊情况（如停电、机房严重故障等）需延期考试时，由考点另行通知；<br/>
8、严格遵守考场纪律，严禁网上搜索或抄袭他人答案，以及替考、扰乱考场秩序等违规、舞弊行为。如有违反按有关规定处理；<br/>
9、保持考场安静卫生，爱护机房设备，损坏者照价赔偿；<br/>
10、违反上述考场规定及考试纪律者，将进行上报并做严肃处理。
            <br/>
        </div>
    </div>
    <div class="rl-model-footer-exam">
        <input id="start_exam" type="button" value="开始考试" hidefocus="true" class="btn-green-exam">
    </div>
</div>


<script type="application/javascript" src="${ctx}/resources/js/layer/layer.js"></script>
<script>
    //CALL TESTIMONIAL ROTATOR
    $( function() {
        var stuId = '${user.stuId}';
        var examId = 0;

        // 监听开始答卷窗口显示
        $('#exam-notice-panel').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            examId = button.data('id');
            examPwd = button.data('pwd');

        });

        //开始考试
        $('#start_exam').click(function(){
            layer.prompt({title: '输入本场开考密码',value:'', formType: 0},
                function(pass, index){

                    var data = {};
                    data.examId = examId;
                    data.stuId = stuId;
                    data.exampwd = pass;

                    $.ajax({
                        type: 'post',
                        url: '${ctx}/exam/pwd',
                        data: data,
                        success: function(result){
                            if(result.status == 'fail') {
                                layer.msg('考试密码输入有误，请重新输入！', {time: 1000})
                            }else if (result.status == 'success'){
                                if(result.examStatus > 1){
                                    layer.msg('您已经提交试卷了，得分为'+result.score, {time: 1000})
                                }else{
                                    location.href = '${ctx}/exam/examdo/'+result.stuExamId+'/'+examId;
                                }
                            }


                        }
                    });
                });
        });


    } );

    var examing = function(point){
        layer.alert('您已完成本次考试，得分：'+point,{skin: 'layui-layer-molv',shade: [0.9,'#696969'],icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
            layer.closeAll();
        });
    };
</script>
</body>
</html>
