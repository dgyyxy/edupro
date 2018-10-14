<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>航空CBT系统管理</title>
    <link href="${basePath}/resources/plugins/fullPage/jquery.fullPage.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/bootstrap-3.3.0/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/material-design-iconic-font-2.2.0/css/material-design-iconic-font.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/waves-0.7.5/waves.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/plugins/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.min.css" rel="stylesheet"/>
    <link href="${basePath}/resources/css/admin.css" rel="stylesheet"/>

    <style>
        /** skins **/
        #zheng-upms #header {background: #29A176;}
        #zheng-upms .content_tab{background:#29A176;}
        #zheng-upms .s-profile>a{background:url(${basePath}/resources/images/zheng-upms.png) left top no-repeat;}
    </style>
</head>
<body>
<header id="header">
    <ul id="menu">
        <li id="guide" class="line-trigger">
            <div class="line-wrap">
                <div class="line top"></div>
                <div class="line center"></div>
                <div class="line bottom"></div>
            </div>
        </li>
        <li id="logo" class="hidden-xs">
            <a href="index.html">
                <img src="${basePath}/resources/images/logo_image.png"/>
            </a>
        </li>
        <li class="pull-right">
            <ul class="hi-menu">
               <%-- <!-- 搜索 -->
                <li class="dropdown">
                    <a class="waves-effect waves-light" data-toggle="dropdown" href="javascript:;">
                        <i class="him-icon zmdi zmdi-search"></i>
                    </a>
                    <ul class="dropdown-menu dm-icon pull-right">
                        <form id="search-form" class="form-inline">
                            <div class="input-group">
                                <input id="keywords" type="text" name="keywords" class="form-control" placeholder="搜索"/>
                                <div class="input-group-btn">
                                    <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                                </div>
                            </div>
                        </form>
                    </ul>
                </li>--%>
                <li>
                    <a class="waves-effect aves-light" data-ma-action="fullscreen" href="javascript:fullPage();" title="全屏模式"><i class="him-icon zmdi zmdi-fullscreen"></i></a>
                </li>
                <li>
                    <a class="waves-effect aves-light" href="${basePath}/sso/logout" title="退出"><i class="him-icon zmdi zmdi-run"></i> </a>
                </li>
            </ul>
        </li>
    </ul>
</header>
<section id="main">
    <!-- 左侧导航区 -->
    <aside id="sidebar">
        <!-- 个人资料区 -->
        <div class="s-profile">
            <a class="waves-effect waves-light" href="javascript:;">
                <div class="sp-pic">
                    <img src="${basePath}/resources/images/avatar.jpg"/>
                </div>
                <div class="sp-info">
                    ${user.realname}，您好！
                </div>
            </a>
        </div>
        <!-- /个人资料区 -->
        <!-- 菜单区 -->
        <ul class="main-menu">
            <li>
                <a class="waves-effect" href="javascript:Tab.addTab('首页', 'home');"><i class="zmdi zmdi-home"></i> 首页</a>
            </li>
            <c:forEach var="permission" items="${permissions}" varStatus="status">
                <c:if test="${permission.pid == 0}">
                    <li class="sub-menu system_menus system_1 ${status.index}">
                        <a class="waves-effect" href="javascript:;"><i class="${permission.icon}"></i> ${permission.name}</a>
                        <ul>
                            <c:forEach var="subPermission" items="${permissions}">
                                <c:if test="${subPermission.pid == permission.permissionId}">
                                    <li><a class="waves-effect" href="javascript:Tab.addTab('${subPermission.name}', '${subPermission.uri}');">${subPermission.name}</a></li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
            </c:forEach>
            <div class="upms-version">
                &copy; AIKS-Tech V1.0.1
            </div>
            </li>
        </ul>
        <!-- /菜单区 -->
    </aside>
    <!-- /左侧导航区 -->
    <section id="content">
        <div class="content_tab">
            <div class="tab_left">
                <a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-left"></i></a>
            </div>
            <div class="tab_right">
                <a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-right"></i></a>
            </div>
            <ul id="tabs" class="tabs">
                <li id="tab_home" data-index="home" data-closeable="false" class="cur">
                    <a class="waves-effect waves-light" href="javascript:;">首页</a>
                </li>
            </ul>
        </div>
        <div class="content_main">
            <div id="iframe_home" class="iframe cur">
                <div style="width: 60px;float:left;">&nbsp;</div><h3 style="font-weight: bold;font-family: '宋体'">AIKS在线学习/考试系统</h3>
                <div style="width: 100px;float:left;">&nbsp;</div><h3 style="font-weight: bold;font-family: '宋体'">（系统简介）</h3>
                <p>本系统是面向学员、教师和管理人员的一套BS结构软件系统，用户端无需插件，<br/>
                    通过网络访问方式即可实现eLearning在线学习、考试的整个应用流程。其核心功能简介如下：</p>

                <b>1、	学员登录：</b>学员通过专用的入口，以证件号和密码形式登录；学习中心可以学习后台布置的学习任务，<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;考试中心可以参加后台安排的考试，个人中心可以完善个人资料和修改密码，以及查询学习记录和考试成绩；<br/>
                <b>2、	后台管理：</b>教师、考务人员和管理员通过系统后台入口登录；不同的登录人员可拥有不同的管理权限；<br/>
                <b>3、	系统机构管理：</b>可设定各级人员的后台帐号及相应角色、管理权限；设置学员所属的机构和层级；<br/>
                <b>4、	学员管理：</b>按机构所属新建、导入和管理学员的帐号等信息；<br/>
                <b>5、	学习管理：</b>分类建立课件资源库，并给学员设定学习任务，管理学习记录；<br/>
                <b>6、	题库管理：</b>分类建立各级试题库，支持判断题、单选题、多选题、填空题等；<br/>
                <b>7、	试卷管理：</b>分类建立各级试卷库，支持手动和随机生成试卷；<br/>
                <b>8、	考试管理：</b>以“选择试卷、考题范围、题库选题”三种方式开设考试，可随时监控考试进行状况，<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;记录管理考试成绩；<br/>
                <b>9、	系统设置：</b>管理系统公告和轮播图等图文资讯。<br/>

            </div>
        </div>
    </section>
</section>
<footer id="footer"></footer>
<script src="${basePath}/resources/plugins/jquery.1.12.4.min.js"></script>
<script src="${basePath}/resources/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${basePath}/resources/plugins/waves-0.7.5/waves.min.js"></script>
<script src="${basePath}/resources/plugins/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="${basePath}/resources/plugins/BootstrapMenu.min.js"></script>
<script src="${basePath}/resources/plugins/device.min.js"></script>
<script src="${basePath}/resources/plugins/fullPage/jquery.fullPage.min.js"></script>
<script src="${basePath}/resources/plugins/fullPage/jquery.jdirk.min.js"></script>
<script src="${basePath}/resources/plugins/jquery.cookie.js"></script>

<script src="${basePath}/resources/js/admin.js"></script>
</body>
</html>
