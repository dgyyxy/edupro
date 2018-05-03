<%@ page contentType="text/html; charset=utf-8"%>
<jsp:include page="../common/taglib.jsp"/>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>航空CBT系统管理</title>

    <link href="${basePath}/resources/plugins/bootstrap-3.3.0/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/material-design-iconic-font-2.2.0/css/material-design-iconic-font.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/waves-0.7.5/waves.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/checkbix/css/checkbix.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/css/login.css" rel="stylesheet"/>
</head>
<body>
<div style="position: relative;top: 20%;left: 40%;">
    <%--<img src="${basePath}/resources/images/login.png" width="282" height="114"/>--%>
    <img src="${basePath}/resources/images/login.bak.png" width="275" height="62"/>
</div>
<div id="login-window">
    <div class="input-group m-b-20">
        <span class="input-group-addon"><i class="zmdi zmdi-account"></i></span>
        <div class="fg-line">
            <input id="username" type="text" class="form-control" name="username" placeholder="帐号" required autofocus value="">
        </div>
    </div>
    <div class="input-group m-b-20">
        <span class="input-group-addon"><i class="zmdi zmdi-lock-outline"></i></span>
        <div class="fg-line">
            <input id="password" type="password" class="form-control" name="password" placeholder="密码" required value="">
        </div>
    </div>
    <div class="clearfix">
    </div>
    <div class="checkbox">
        <input id="rememberMe" type="checkbox" class="checkbix" data-text="自动登录" name="rememberMe">
    </div>
    <a id="login-bt" href="javascript:;" class="waves-effect waves-button waves-float"><i class="zmdi zmdi-arrow-forward"></i></a>
</div>
<script src="${basePath}/resources/plugins/jquery.1.12.4.min.js"></script>
<script src="${basePath}/resources/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${basePath}/resources/plugins/waves-0.7.5/waves.min.js"></script>
<script src="${basePath}/resources/plugins/checkbix/js/checkbix.min.js"></script>

<script src="${basePath}/resources/js/login.js"></script>
<script type="text/javascript">
    Checkbix.init();
    var BASE_PATH = '${basePath}';
    var BACK_URL = '${param.backurl}';

    //解决iframe下系统超时无法跳出iframe框架的问题
    if (window != top){
        top.location.href = location.href;
    }
</script>
</body>
</html>
