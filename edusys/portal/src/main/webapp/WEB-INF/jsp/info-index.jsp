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
    <link href="${ctx}/resources/css/info.css" rel="stylesheet">
    <link href="${ctx}/resources/css/fieldset.css" rel="stylesheet">
    <link href="${ctx}/resources/css/modal.css" rel="stylesheet">
</head>
<body class="off">
<!-- /.wrapbox start-->
<div class="wrapbox">
    <jsp:include page="common/top.jsp" flush="true">
        <jsp:param name="menu" value="info"/>
    </jsp:include>
</div>

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
    <!-- BLOG
================================================== -->
    <section class="container animated fadeInDown notransition">
        <div class="row blogindex">
            <!-- MAIN -->
            <div class="col-md-12">
                <div class="info-content">
                    <div class="info-header">
                        <h1>
                            <span class="active-title" data-target="${ctx}/info/info-detail">个人信息</span>
                            <span data-target="${ctx}/info/study-history">学习记录</span>
                            <span data-target="${ctx}/info/favorite">课件收藏</span>
                            <span data-target="${ctx}/info/exam-history">考试记录</span>
                        </h1>
                    </div>
                    <!-- 个人信息 -->
                    <div class="info-body animated fadeInLeft" id="iframe-show">

                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<!-- issues 安全问题设置 -->
<div id="issuesSet" class="rl-modal-issues in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span class="active-title">安全问题</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="issuesForm" autocomplete="off">
                    <p class="rlf-tip-globle color-red"></p>
                    <div class="rlf-group clearfix">
                        <input type="button" value="保存" onclick="issuesSetSave()" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- editStudent -->
<div id="editStudent" class="rl-modal-info in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span class="active-title">完善信息</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="info-form" autocomplete="off">
                    <p class="rlf-tip-globle color-red"></p>
                    <div class="rlf-group pr">
                        <input type="text" id="stuName" maxlength="37" name="stuName" value="${student.stuName}"
                               data-validate="require-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="请输入您的姓名">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的姓名"></p>
                    </div>

                    <div class="rlf-group pr">
                        <input type="text" id="stuNo" maxlength="37" name="stuNo" value="${student.stuNo}" data-validate="require-stuNo"
                               autocomplete="off" class="xa-emailOrPhone ipt ipt-email js-own-name"
                               placeholder="请输入您的学号">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的学号"></p>
                    </div>

                    <div class="rlf-group pr">
                        <input type="text" id="phone" maxlength="37" name="phone" value="${student.phone}"
                               data-validate="require-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="请输入您的手机号">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的手机号"></p>
                    </div>



                    <%--<div class="rlf-group pr">
                        <input type="text" id="company" maxlength="37" name="company" value="${student.company}"
                               data-validate="require-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="请输入您的公司名称">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的公司名称"></p>
                    </div>--%>

                    <div class="rlf-group clearfix">
                        <input type="button" value="保存" onclick="updateInfo()" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- updatePassword -->
<div id="updatePassword" class="rl-modal-password in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span class="active-title">修改密码</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="pwd-form" autocomplete="off">
                    <p class="rlf-tip-globle color-red"></p>
                    <div class="rlf-group pr">
                        <input type="password" id="oldpwd" maxlength="37" name="oldpwd" data-validate="require-mobile-phone"
                               autocomplete="off" class="xa-emailOrPhone ipt ipt-email js-own-name"
                               placeholder="请输入旧密码">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入旧密码"></p>
                    </div>
                    <div class="rlf-group pr">
                        <input type="password" id="newpwd" maxlength="37" name="newpwd"
                               data-validate="require-mobile-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="请输入新密码">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入新密码"></p>
                    </div>

                    <div class="rlf-group pr">
                        <input type="password" id="repwd" maxlength="37" name="repwd"
                               data-validate="require-mobile-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="再次输入密码">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="再次输入密码"></p>
                    </div>

                    <div class="rlf-group clearfix">
                        <input type="button" value="保存" onclick="updatePwd()" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="common/js.jsp" flush="true"/>

<jsp:include page="common/footer.jsp" flush="true"/>

</div>

<script>
    var jobName = '';//记录学习任务名称
    //CALL TESTIMONIAL ROTATOR
    $(function(){
        //初始个人信息
        var obj = $('.info-header>h1').find('span').eq(0);
        var url = $(obj).data('target')+'?refresh='+new Date().getTime();
        loadFrame(url);

        $('.info-header>h1').find('span').each(function(){
            $(this).click(function(){
                hideItem();
                $(this).addClass('active-title');
                loadFrame($(this).data('target'));
            });
        });

        // 监听完善资料窗口显示
        $('#editStudent').on('show.bs.modal', function (event) {
            $('#stuNo').val($('#stuNoSpan').text());
            $('#phone').val($('#phoneSpan').text());
        });

        // 监听安全问题窗口显示
        $('#issuesSet').on('show.bs.modal', function (event) {
            initIssuesList();
        });

        // 监听安全问题窗口关闭
        $('#issuesSet').on('hidden.bs.modal', function (){
            $('#issuesForm').find('div.question').each(function(){
                $(this).remove();
            });
        });

    });

    var hideItem = function() {
        var item = $('.info-header>h1').find('span.active-title');
        $(item).removeClass('active-title')
    };

    var loadFrame = function(url){
        $.get(url, function(data){
            $('#iframe-show').html(data);
            if(url.indexOf('study')!=-1){
                regjob();
            }
        });
    }

    // 当加载已学习任务列表时，注册该事件
    var regjob = function(){
        $('#jobtables').find('span').each(function(){
            $(this).click(function(){
                var userId = $(this).data('user-id');
                var jobId = $(this).data('job-id');
                jobName = $(this).data('job-name');
                loadFrame('${ctx}/info/course-history/'+userId+'/'+jobId);
            });
        });
    }

    //计算两个整数的百分比值
    var getPercent = function (num, total) {
        num = parseFloat(num);
        total = parseFloat(total);
        if (isNaN(num) || isNaN(total)) {
            return "-";
        }
        return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%");
    }

    //完善个人信息
    var updateInfo = function(){

        var stuNo = $('#stuNo').val();
        var stuNoMsg = $('#stuNo').next('p');
        var phone = $('#phone').val();
        var phoneMsg = $('#phone').next('p');

        /*if(stuNo == ""){
            $(stuNoMsg).text($(stuNoMsg).attr('data-error-hint'));
            return false;
        }
        $(stuNoMsg).empty();
        if(phone == ""){
            $(phoneMsg).text($(phoneMsg).attr('data-error-hint'));
            return false;
        }
        $(phoneMsg).empty();*/
        var entity = $('#info-form').serializeObject();

        $.ajax({
            type: 'post',
            url: '${basePath}/info/edit-info',
            data: entity,
            beforeSend: function() {
            },
            success: function(result) {
                $('#editStudent').modal('hide');
                $('#stuNo').val("");
                $('#phone').val("");
                $(stuNoMsg).empty();
                $(phoneMsg).empty();
                $('#stuNoSpan').text(stuNo);
                $('#phoneSpan').text(phone);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg(textStatus);
            }
        });
    }

    //修改密码
    var updatePwd = function(){

        var oldpwd = $('#oldpwd').val();
        var oldpwdMsg = $('#oldpwd').next('p');
        var newpwd = $('#newpwd').val();
        var newpwdMsg = $('#newpwd').next('p');
        var repwd = $('#repwd').val();
        var repwdMsg = $('#repwd').next('p');

        if(oldpwd == ""){
            $(oldpwdMsg).text($(oldpwdMsg).attr('data-error-hint'));
            return false;
        }
        $(oldpwdMsg).empty();
        if(newpwd == ""){
            $(newpwdMsg).text($(newpwdMsg).attr('data-error-hint'));
            return false;
        }
        $(newpwdMsg).empty();
        if(repwd == ""){
            $(repwdMsg).text($(repwdMsg).attr('data-error-hint'));
            return false;
        }
        $(repwdMsg).empty();
        if(newpwd!=repwd){
            $(repwdMsg).text("新密码与重复输入的密码不一致！");
            return false;
        }

        var entity = $('#pwd-form').serializeObject();
        $.ajax({
            type: 'post',
            url: '${basePath}/info/edit-info',
            data: entity,
            beforeSend: function() {
            },
            success: function(result) {
                if(result.data == 'fail'){
                    $(oldpwdMsg).text("旧密码输入有误，请重新输入！");
                }else {
                    $('#updatePassword').modal('hide');
                    $('#oldpwd').text("");
                    $('#newpwd').text("");
                    $('#repwd').text("");
                    $(oldpwdMsg).empty();
                    $(newpwdMsg).empty();
                    $(repwdMsg).empty();
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg(textStatus);
            }
        });
    }

    var initIssuesList = function (){
        $.getJSON('${ctx}/issues',{}, function(json){
            var htmlstr = '';
            var answers = json.answer.split(',');
            for(var i=0; i<json.list.length; i++){
                var obj = json.list[i];
                var _answer = '';
                if(answers[i]!=null){
                    _answer = answers[i];
                }
                htmlstr += '<div class="rlf-group pr question">'
                        +'<span>'+obj.question+'</span>'
                        +'</div>'
                        +'<div class="rlf-group pr question">'
                        +'<input type="hidden" name="questionId" value="'+obj.id+'"/>'
                        +'<input type="text" id="answer'+obj.id+'" name="answer" value="'+_answer+'" class="xa-emailOrPhone ipt ipt-email js-own-name" data-validate="require-password" maxlength="25" autocomplete="off">'
                        +'</div>'
                        +'<div style="line-height: 10px;" class="question">&nbsp;</div>';
            }
            $('#issuesForm').find('p').after(htmlstr);
        });
    }

    var issuesSetSave = function(){
        var entity = $('#issuesForm').serialize();
        $.ajax({
            type: 'post',
            url: '${basePath}/info/issues-setting',
            data: entity,
            beforeSend: function() {
            },
            success: function(result) {
                $('#issuesSet').modal('hide');
                $('#issuesForm').find('div.question').each(function(){
                    $(this).remove();
                });
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg(textStatus);
            }
        });
    }
</script>
</body>
</html>
