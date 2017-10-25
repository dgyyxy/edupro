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
    <title>考试考生管理</title>
    <jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
    <div id="toolbar">
        <a class="waves-effect waves-button orange" href="javascript:location.href='${basePath}/manage/exam/index';"><i class="zmdi zmdi-view-comfy"></i> 返回考试列表</a>
        <%--<a class="waves-effect waves-button" href="javascript:;" onclick="examOrganAction()"><i class="zmdi zmdi-cloud-box"></i> 安排参考机构</a>--%>
        <a class="waves-effect waves-button green" href="javascript:;" onclick="examStudentAction()"><i class="zmdi zmdi-accounts"></i> 新增考生</a>
    </div>
    <table id="table"></table>
</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script src="${basePath}/resources/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script>
    var examId = '${examId}';
    var $table = $('#table');
    $(function() {
        // bootstrap table初始化
        $table.bootstrapTable({
            url: '${basePath}/manage/exam/studentList/'+examId,
            height: getHeight(),
            striped: true,
            search: true,
            strictSearch: true,
            queryParams: 'queryParams',
            showRefresh: true,
            showColumns: true,
            minimumCountColumns: 2,
            clickToSelect: true,
            detailView: false,
            pagination: true,
            paginationLoop: false,
            sidePagination: 'server',
            silentSort: false,
            smartDisplay: false,
            escape: true,
            idField: 'id',
            maintainSelected: true,
            toolbar: '#toolbar',
            columns: [
                {field: 'id', title: '编号', sortable: true, align: 'center'},
                {field: 'stuName', title: '姓名'},
                {field: 'stuOrgan', title: '所属机构'},
                {field: 'point', title: '总分'},
                {field: 'pointGet', title: '得分'},
                {field: 'approved', title: '是否通过', formatter: 'statusFormatter'},
                {field: 'duration', title: '考试时间'},
                {field: 'submitTime', title: '交卷时间', formatter: 'dateFormatter'}
            ]
        });
    });

    function queryParams(params){
        return params;
    }

    function dateFormatter(value, row, index){
        if(value == null){
            return '-';
        }
        return new Date(value).Format('yyyy-MM-dd HH:mm:ss')
    }

    function statusFormatter(value, row, index){
        if(value == 0){
            return '<span class="label label-default">未登录</span>';
        }else if(value == 1){
            return '<span class="label label-primary">考试中</span>';
        }else if(value == 2){
            return '<span class="label label-success">及格</span>'
        }else if(value == 3){
            return '<span class="label label-danger">不及格</span>'
        }
    }

    // 格式化操作按钮
    function actionFormatter(value, row, index) {
        return [
            '<a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
            '<a class="delete" href="javascript:;" onclick="deleteAction()" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
        ].join('');
    }

    // 安排参考机构
    var examOrganDialog;
    function examOrganAction() {
        examOrganDialog = $.dialog({
            type: 'green',
            animationSpeed: 300,
            title: '安排参考机构',
            content: 'url:${basePath}/manage/exam/organ/${examId}',
            onContentReady: function () {
                initMaterialInput();
                $('select').select2({
                    width: 200
                });
            },
            onClose: function(){
                $table.bootstrapTable('refresh');
            }
        });
    }

    // 安排参考学员
    var examStudentDialog;
    function examStudentAction() {
        examStudentDialog = $.dialog({
            type: 'blue',
            animationSpeed: 300,
            columnClass: 'col-md-10 col-md-offset-1',
            title: '安排考生',
            content: 'url:${basePath}/manage/exam/student/${examId}',
            onContentReady: function () {
                initMaterialInput();
                $('select').select2({
                    width: 150
                });
            },
            onClose: function(){
                $table.bootstrapTable('refresh');
            }
        });
    }
</script>
</body>
</html>