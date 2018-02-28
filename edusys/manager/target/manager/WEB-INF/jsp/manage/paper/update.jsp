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
    <title>试卷修改</title>
    <jsp:include page="../../common/css.jsp" flush="true"/>
    <link href="${basePath}/resources/css/exam.css" rel="stylesheet"/>
    <link href="${basePath}/resources/css/question.css" rel="stylesheet"/>
</head>
<body>
<div>
    <!-- Slider (Flex Slider) -->

    <div class="container">

        <div class="row">
            <div class="col-xs-12" id="right-content">
                <div class="page-header">
                    <h4><i class="glyphicon glyphicon-list-alt"></i> 试卷组卷 </h4>
                    <span style="float: right; position: relative; top: -33px;">
                        <button class="btn btn-sm btn-primary" onclick="javascript:location.href='${basePath}/manage/paper/index'"><i class="zmdi zmdi-arrow-back"></i>返回列表
                        </button>
                        <button class="btn btn-sm btn-danger" id="save-paper-btn"><i class="zmdi zmdi-check"></i>完成试卷
                        </button>
                    </span>
                </div>
                <div class="page-content">
                    <div class="def-bk" id="bk-exampaper">

                        <div class="expand-bk-content" id="bk-conent-exampaper">
                            <div id="exampaper-header">
                                <div id="exampaper-title">
                                    <i class="glyphicon glyphicon-send"></i>
                                    <span id="exampaper-title-name">${paper.name}</span>
                                    <span style="margin-left: 20px;">试卷总分：<label id="totalPoint" style="color:red;">${paper.totalPoint}</label></span>
                                    <span style="margin-left: 20px;">及格分：<label id="passPoint" style="color:green;">${paper.passPoint}</label></span>
                                    <span style="display:none;" id="exampaper-id">${paper.id}</span>
                                </div>
                                <div id="exampaper-desc-container">
                                    <div id="exampaper-desc" class="exampaper-filter">

                                    </div>
                                    <div style="margin-top: 10px;">
                                        <span>当前试卷总分：</span>
                                        <span id="exampaper-total-point" style="margin-right:20px;">0</span>
                                        <span>当前总题量：</span>
                                        <span id="exampaper-amount" style="margin-right:20px;">${paper.amount}</span>
                                        <span id="add-more-qt-to-paper">
                                        <i class="glyphicon glyphicon-plus" title="添加选项"></i>
                                        增加题目</span>

                                    </div>

                                </div>
                            </div>
                            <ul id="exampaper-body" style="padding:0px;">
                                ${htmlStr}
                            </ul>
                            <br/><br/>
                            <div id="exampaper-footer">
                                <div id="question-navi">
                                    <div id="question-navi-controller">
                                        <i class="fa fa-arrow-circle-down"></i>
                                        <span>题号</span>
                                    </div>
                                    <div id="question-navi-content">
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script>
    var basePath = '${basePath}';
</script>
<script src="${basePath}/resources/js/exampaper-edit.js"></script>
</body>
</html>

