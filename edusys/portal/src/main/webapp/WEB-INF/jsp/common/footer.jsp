<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<section>
    <div class="footer">
        <div class="container animated fadeInUpNow notransition">
            <div class="row">
            </div>
        </div>
    </div>
    <p id="back-top">
        <a href="#top"><span></span></a>
    </p>
    <div class="copyright">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <p class="pull-left">
                        &copy; Copyright 2017 上海艾克斯网络传播有限公司 版权所有 |  技术支持：cbt@aiks.cn、021-51581848
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- /footer section end-->

<!-- signin -->
<div id="signin" class="rl-modal-signin in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span id="signinSpan" class="active-title">登录</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body" id="signin-show">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="signin-form" autocomplete="off">
                    <p class="rlf-tip-globle color-red" id="signin-globle-error"></p>
                    <div class="rlf-group pr">
                        <input type="text" id="account" maxlength="37" name="account"
                               data-validate="require-mobile-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="请输入身份证号">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入身份证号"></p>
                    </div>
                    <div class="rlf-group pr">
                        <input type="password" id="password" name="password" data-validate="require-password"
                               class="ipt ipt-pwd js-loginPassword js-pass-pwd" placeholder="4-16位密码，区分大小写，不能用空格"
                               maxlength="16" autocomplete="off" readonly onfocus="this.removeAttribute('readonly');">
                        <p class="rlf-tip-wrap errorHint color-red " data-error-hint="请输入4-16位密码，区分大小写，不能使用空格！"></p>
                    </div>
                    <div class="rlf-group rlf-appendix clearfix">
                        <a href="javascript:forgetPass();" class="rlf-forget r" target="_blank" hidefocus="true">忘记密码 </a>
                    </div>

                    <div class="rlf-group clearfix">
                        <input type="button" value="登录" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="rl-modal-footer" style="padding: 10px 30px 4px;">
        <span class="l " style="color:royalblue"></span>
    </div>
</div>

<!-- signup -->
<div id="signup" class="rl-modal-signup in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span id="signupSpan" class="active-title">注册</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body" id="signup-show">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="signup-form" autocomplete="off">
                    <p class="rlf-tip-globle color-red"></p>
                    <div class="rlf-group pr">
                        <select id="organ1" class="ipt" name="organizationId1">
                            <option value="0">请选择一级机构</option>
                        </select>
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请选择一级机构"></p>
                    </div>
                    <div class="rlf-group pr">
                        <select id="organ2" class="ipt" name="organizationId2">
                            <option value="0">请选择二级机构</option>
                        </select>
                        <input type="hidden" name="organizationName1" id="organizationName1"/>
                        <input type="hidden" name="organizationName2" id="organizationName2"/>
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请选择二级机构"></p>
                    </div>
                    <div class="rlf-group pr">
                        <input type="text" id="cardNo" maxlength="37" name="cardNo" data-validate="require-mobile-phone"
                               autocomplete="off" class="xa-emailOrPhone ipt ipt-email js-own-name"
                               placeholder="请输入您的身份证件号">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的身份证件号"></p>
                    </div>
                    <div class="rlf-group pr">
                        <input type="text" id="pwd" maxlength="37" name="password" data-validate="require-mobile-phone"
                               autocomplete="off" class="xa-emailOrPhone ipt ipt-email js-own-name"
                               placeholder="4-16位密码，区分大小写，不能用空格">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入4-16位密码，区分大小写，不能使用空格！"></p>
                    </div>
                    <div class="rlf-group pr">
                        <input type="text" id="repwd" maxlength="37" name="repassword" data-validate="require-mobile-phone"
                               autocomplete="off" class="xa-emailOrPhone ipt ipt-email js-own-name"
                               placeholder="再次输入密码">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请再次输入密码！"></p>
                    </div>
                    <div class="rlf-group pr">
                        <input type="text" id="stuName" maxlength="37" name="stuName"
                               data-validate="require-mobile-phone" autocomplete="off"
                               class="xa-emailOrPhone ipt ipt-email js-own-name" placeholder="请输入您的姓名">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的姓名"></p>
                    </div>

                    <div class="rlf-group clearfix">
                        <input type="button" value="下一步" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- issue setting -->
<div id="issues" class="rl-modal-issues in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span id="issuesSpan" class="active-title">安全问题</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body" id="issues-show">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="issues-form" autocomplete="off">
                    <p class="rlf-tip-globle color-red" id="issues-globle-error"></p>
                    <div style="line-height: 10px;">&nbsp;</div>
                    <div class="rlf-group clearfix">
                        <input type="button" value="提交" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="rl-modal-footer" style="padding: 10px 30px 4px;">
        <span class="l " style="color:royalblue"></span>
    </div>
</div>

<!-- reset password setting -->
<div id="resetpwd" class="rl-modal-resetpwd in modal" aria-hidden="true">
    <div class="rl-modal-header">
        <h1>
            <span id="resetpwdSpan" class="active-title">找回密码</span>
        </h1>
        <button type="button" class="rl-close closeBtn" data-dismiss="modal" hidefocus="true"
                aria-hidden="true"></button>
    </div>

    <div class="rl-modal-body" id="resetpwd-show">
        <div class="clearfix">
            <div class="l-left-wrap l">
                <form id="resetpwd-form" autocomplete="off">
                    <p class="rlf-tip-globle color-red" id="resetpwd-globle-error"></p>
                    <div class="rlf-group pr">
                        <span>请输入您的证件号</span>
                    </div>
                    <div class="rlf-group pr" id="reset-cardno">
                        <input type="text" id="resetcardNo" maxlength="37" name="cardno"
                               autocomplete="off" class="xa-emailOrPhone ipt ipt-email js-own-name">
                        <p class="rlf-tip-wrap errorHint color-red" data-error-hint="请输入您的证件号"></p>
                    </div>
                    <div style="line-height: 10px;">&nbsp;</div>
                    <div class="rlf-group clearfix">
                        <input type="button" value="找回密码" hidefocus="true" class="btn-red btn-full xa-login">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="rl-modal-footer" style="padding: 10px 30px 4px;">
        <span class="l " style="color:royalblue"></span>
    </div>
</div>

<script type="application/javascript">
    // 登录操作
    var loginOperate = {
        menu: '',
        init: function () {
            this.submitForm();
        },
        submitForm: function () {
            // 登录操作
            $('#signin-form').find('input[type="button"]').click(function () {
                if (loginOperate.validateForm()) {
                    $.ajax({
                        type: 'post',
                        url: '${ctx}/login/done',
                        data: $('#signin-form').serialize(),
                        success: function (json) {
                            if(json.data == 'fail'){
                                $('.rlf-tip-globle').text(json.message);
                            }else if(json.data == 'success'){

                                var menu = loginOperate.menu;
                                if(menu == undefined){
                                    location.reload();
                                }else if(menu == 'exam'){
                                    location.href = '${ctx}/exam/list';
                                }else if(menu == 'info'){
                                    location.href = '${ctx}/info/index';
                                }else if(menu == 'task'){
                                    location.href = '${ctx}/task/list';
                                }
                            }

                        }
                    });
                }
            });
        },
        validateForm: function () {
            // 请输入学号\身份证号
            var account = $('#account').val();
            var accountMsg = $('#account').next('p');
            var password = $('#password').val();
            var passwordMsg = $('#password').next('p');
            if (account == '') {
                $(accountMsg).text($(accountMsg).attr('data-error-hint'));
                return false;
            }
            $(accountMsg).text('');
            if (password == '' || password.length < 4 || password.length > 20) {
                $(passwordMsg).text($(passwordMsg).attr('data-error-hint'));
                return false;
            }
            $(passwordMsg).text('');
            return true;
        },
        clearForm: function (){
            $('#account').val('');
            $('#password').val('');
            var accountMsg = $('#account').next('p');
            var passwordMsg = $('#password').next('p');
            $(accountMsg).text('');
            $(passwordMsg).text('');
        }
    }

    var submitData = "";

    //注册
    var registerOperate = {
        init: function () {
            this.submitForm();
            this.loadOrganization();
            this.changeParentOrgan();
            this.saveOperate();
            submitData = "";
        },
        submitOperate: function() {
            $.ajax({
                type: 'post',
                url: '${ctx}/register/done',
                data: $('#signup-form').serialize(),
                beforeSend: function() {
                    //获取选中的机构名称
                    $('#organizationName1').val($('#organ1 option:selected').text());
                    $('#organizationName2').val($('#organ2 option:selected').text());
                    this.data = $('#signup-form').serialize();
                },
                success: function (json) {
                    if(json.data == 'success'){
                        registerOperate.clearForm();//清空表单
                        layer.alert('恭喜您注册成功，请准备登录学习！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                            layer.closeAll();
                            $('#signup').modal('hide');
                            $('#signin').modal('show');
                        });
                    }else if(json.data == 'fail'){
                        layer.alert('该证件号已经被注册！请确认',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                            layer.closeAll();
                        });
                    }

                }
            });
        },
        submitForm: function () {
            // 注册操作
            $('#signup-form').find('input[type="button"]').click(function () {
                if(registerOperate.validateForm()){
                    var cardNo = $('#cardNo').val();
                    // 校验身份证号是否存在
                    $.getJSON('${ctx}/register/valid', {cardNo: cardNo}, function(json){
                        if(json.data=='fail'){
                            layer.alert('该证件号已经被注册！请确认',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                                layer.closeAll();
                            });
                            return false;
                        }
                        //获取选中的机构名称
                        $('#organizationName1').val($('#organ1 option:selected').text());
                        $('#organizationName2').val($('#organ2 option:selected').text());
                        submitData = $('#signup-form').serialize();

                        registerOperate.initIssuesList();//获取所有安全问题列表
                        $('#signup').modal('hide');
                        $('#issues').modal('show');
                    });


                }
            });
        },
        saveOperate: function() {
            $('#issues-form').find('input[type="button"]').click(function(){
                var data = submitData+'&'+$('#issues-form').serialize();
                $.ajax({
                    type: 'post',
                    url: '${ctx}/register/done',
                    data: data,
                    beforeSend: function() {
                        //校验安全问题是否为空
                        $('input[name="answer"]').each(function(){
                            var answerval = $(this).val();
                            if(answerval == ''){
                                layer.alert('安全问题不能为空！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']});
                            }
                        });
                    },
                    success: function (json) {
                        if(json.data == 'success'){
                            layer.alert('恭喜您注册成功，请准备登录学习！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                                layer.closeAll();
                                $('#issues').modal('hide');
                                $('#signin').modal('show');
                            });
                        }else if(json.data == 'fail'){
                            layer.alert('该证件号已经被注册！请确认',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                                layer.closeAll();
                            });
                        }

                    }
                });
            });
        },
        validateForm: function () {
            var organ1 = $('#organ1').val();
            var organMsg1 = $('#organ1').next('p');
            var organ2 = $('#organ2').val();
            var organMsg2 = $('#organ2').next('p');
            var cardNo = $('#cardNo').val();
            var cardNoMsg = $('#cardNo').next('p');
            var stuName = $('#stuName').val();
            var stuNameMsg = $('#stuName').next('p');
            var pwd = $('#pwd').val();
            var repwd = $('#repwd').val();
            var pwdMsg = $('#pwd').next('p');
            var repwdMsg = $('#repwd').next('p');
            //一级机构
            if(organ1 == 0){
                $(organMsg1).text($(organMsg1).attr('data-error-hint'));
                return false;
            }
            $(organMsg1).text('');
            //二级机构
            if(organ2 == 0){
                $(organMsg2).text($(organMsg2).attr('data-error-hint'));
                return false;
            }
            $(organMsg2).text('');
            //身份证信息验证
            var re = /(^[A-Za-z0-9]+$)/;
            if(cardNo == '' || !re.test(cardNo) ){
                $(cardNoMsg).text('您输入身份证不合法！');
                return false;
            }
            if(cardNo.length < 6 || cardNo.length > 25){
                $(cardNoMsg).text('您输入身份证不合法！');
                return false;
            }

            $(cardNoMsg).text('');

            if (pwd == '' || pwd.length < 4 || pwd.length > 20) {
                $(pwdMsg).text($(pwdMsg).attr('data-error-hint'));
                return false;
            }
            $(pwdMsg).text('');

            if (repwd == '' || repwd.length < 4 || repwd.length > 20) {
                $(repwdMsg).text($(repwdMsg).attr('data-error-hint'));
                return false;
            }
            if (repwd!=pwd){
                $(repwdMsg).text('两次输入的密码不一致！');
                return false;
            }
            $(repwdMsg).text('');

            //学员姓名验证
            if(stuName == ''){
                $(stuNameMsg).text($(stuNameMsg).attr('data-error-hint'));
                return false;
            }
            $(stuNameMsg).text('');
            return true;
        },
        loadOrganization: function () {
            $.getJSON('${ctx}/organ/list', {pid: 0}, function(json){
                var datas = json.rows;
                var htmlstr = '<option value="0">请选择一级机构</option>';
                for(var i=0; i<datas.length; i++){
                    htmlstr += '<option value="'+datas[i].organizationId+'">'+datas[i].name+'</option>'
                }
                $('#organ1').empty();
                $('#organ1').append(htmlstr);
            });
        },
        changeParentOrgan: function(){
            $('#organ1').change(function(){
                var pid = $(this).val();

                $.getJSON('${ctx}/organ/list', {pid: pid}, function(json){
                    var datas = json.rows;
                    var htmlstr = '<option value="0">请选择二级机构</option>';
                    for(var i=0; i<datas.length; i++){
                        htmlstr += '<option value="'+datas[i].organizationId+'">'+datas[i].name+'</option>'
                    }
                    $('#organ2').empty();
                    $('#organ2').append(htmlstr);
                });
            });
        },
        clearForm: function (){
            $('#organ1').empty();
            $('#organ2').empty();
            $('#pwd').val('');
            $('#repwd').val('');

            $('#cardNo').val('');
            $('#stuName').val('');
            $('#organ1').next('p').empty();
            $('#organ2').next('p').empty();
            $('#cardNo').next('p').empty();
            $('#stuName').next('p').empty();
            $('#pwd').next('p').empty();
            $('#repwd').next('p').empty();
        },
        initIssuesList: function (){

            $.getJSON('${ctx}/issues',{}, function(json){
                registerOperate.clearIssues();
                var htmlstr = '';
                for(var i=0; i<json.list.length; i++){
                    var obj = json.list[i];
                    htmlstr += '<div class="rlf-group pr question">'
                            +'<span>'+obj.question+'</span>'
                            +'</div>'
                            +'<div class="rlf-group pr question">'
                            +'<input type="hidden" name="questionId" value="'+obj.id+'"/>'
                            +'<input type="text" id="answer'+obj.id+'" name="answer" class="xa-emailOrPhone ipt ipt-email js-own-name" data-validate="require-password" maxlength="25" autocomplete="off">'
                            +'</div>'
                            +'<div style="line-height: 10px;" class="question">&nbsp;</div>';
                }
                $('#issues-form').find('p').after(htmlstr);
            });
        },
        clearIssues: function() {
            $('#issues-form').find('div.question').each(function(){
                $(this).remove();
            });
        }
    }

    $(function () {
        // 监听登录窗口显示
        $('#signin').on('show.bs.modal', function (event) {
            $('.rlf-tip-globle').text('');
            var button = $(event.relatedTarget);
            var menu = button.data('menu');
            loginOperate.init();
            loginOperate.menu = menu;
        });

        // 监听登录窗口关闭
        $('#signin').on('hidden.bs.modal', function (){
            loginOperate.clearForm();//清空表单
        });

        // 监听注册窗口显示加载
        $('#signup').on('show.bs.modal', function (event) {
            registerOperate.init();
        });

        // 监听注册关闭
        /*$('#signup').on('hidden.bs.modal', function(){
            registerOperate.clearForm();
        });*/

        // 清空安全问题
        $('#issues').on('hidden.bs.modal', function(){
            registerOperate.clearIssues();
            registerOperate.clearForm();
        });

        // 找回密码
        $('#resetpwd').on('show.bs.modal', function(){
            $.getJSON('${ctx}/issues',{}, function(json){
                var htmlstr = '';
                for(var i=0; i<json.list.length; i++){
                    var obj = json.list[i];
                    htmlstr += '<div class="rlf-group pr question">'
                            +'<span>'+obj.question+'</span>'
                            +'</div>'
                            +'<div class="rlf-group pr question">'
                            +'<input type="hidden" name="questionId" value="'+obj.id+'"/>'
                            +'<input type="text" id="answer'+obj.id+'" name="answer" class="xa-emailOrPhone ipt ipt-email js-own-name" data-validate="require-password" maxlength="25" autocomplete="off">'
                            +'</div>'
                            +'<div style="line-height: 10px;" class="question">&nbsp;</div>';
                }
                $('#reset-cardno').after(htmlstr);
            });
        });

        $('#resetpwd').on('hidden.bs.modal', function(){
            $('#resetcardNo').val('');
            $('#resetcardNo').next('p').text('');
            $('#resetpwd-form').find('div.question').each(function(){
                $(this).remove();
            });
        });
    });

    var forgetPass = function(){
        $('#signin').modal('hide');
        $('#resetpwd').modal('show');
        resetpwdOperate();
    }

    //提交找回密码操作
    var resetpwdOperate = function(){
        $('#resetpwd-form').find('input[type="button"]').click(function(){
            var data = $('#resetpwd-form').serialize();
            if(resetpwdVaild()){
                $.ajax({
                    type: 'post',
                    url: '${ctx}/resetpwd',
                    data: data,
                    beforeSend: function() {

                    },
                    success: function (json) {
                        if(json.data == 'success') {
                            layer.alert('恭喜您密码已重置为证件号后四位，请准备登录学习！', {
                                skin: 'layui-layer-molv',
                                shade: [0.9, '#696969'],
                                closeBtn: 0,
                                icon: 1,
                                shadeClose: false,
                                title: '提示',
                                offset: ['30%', '40%']
                            }, function () {
                                layer.closeAll();
                                $('#resetpwd').modal('hide');
                                $('#signin').modal('show');
                            });
                        }else if(json.data == 'issues_fail'){
                            layer.alert('输入的安全问题有误！请确认',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                                layer.closeAll();
                            });
                        }else if(json.data == 'cardno_fail'){
                            layer.alert('输入证件号未注册！请确认',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                                layer.closeAll();
                            });
                        }
                    }
                });
            }

        });
    }


    var resetpwdVaild = function(){
        var resetcardNo = $('#resetcardNo').val();
        var errorMsg = $('#resetcardNo').next('p');
        if(resetcardNo==""){
            $(errorMsg).text('请输入您的证件号！');
            return false;
        }
        $(errorMsg).text('');

        var tag = 1;
        //校验安全问题是否为空
        $('#resetpwd-form').find('input[name="answer"]').each(function(){
            var answerval = $(this).val();
            if(answerval == ''){
                tag = 0;
                return false;
            }
        });
        if (tag == 0){
            layer.alert('两个安全问题都不允许为空！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']});
            return false;
        }
        return true;
    }

</script>