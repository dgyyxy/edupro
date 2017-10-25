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
	<title>学习记录</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<table id="table"></table>
</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script src="${basePath}/resources/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/record/list',
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
		columns: [
			{field: 'stuId', title: '编号', sortable: true, align: 'center'},
            {field: 'stuName', title: '姓名'},
            {field: 'cardNo', title: '身份证件号'},
            {field: 'stuNo', title: '学号'},
			{field: 'phone', title: '手机号'},
			{field: 'organizationName2', title: '所属机构'},
			{field: 'studyCount', title: '已学任务', formatter: 'studyCountFormatter'},
			{field: 'sumTime', title: '总学时', formatter: 'timeFormatter'}
		]
	});
});

function queryParams(params){
    return params;
}

function timeFormatter(value, row, index){
	return millisecondToDate(value);
}

function studyCountFormatter(value, row, index){
	console.log(row);
	var count = row.studyCount;
	if(count>0)
		return '<span onclick="jobsAction('+row.stuId+');" class="label label-primary" style="cursor:pointer;">'+value+'</span>';
	else
		return '<span class="label label-default">'+value+'</span>';
}


// 已学任务列表
var jobDialog;
function jobsAction(id) {
	jobDialog = $.dialog({
        type: 'blue',
        animationSpeed: 300,
        columnClass: 'col-md-10 col-md-offset-1',
        title: '已学任务列表',
        content: 'url:${basePath}/manage/record/job-list/'+id,
        onContentReady: function () {
            initMaterialInput();
        }
    });
}

// 已学课件列表
var courseDialog;
function courseListAction(stuId, jobId) {
	jobDialog = $.dialog({
		type: 'blue',
		animationSpeed: 300,
		columnClass: 'col-md-10 col-md-offset-1',
		title: '已学课件列表',
		content: 'url:${basePath}/manage/record/course-list/'+stuId+'/'+jobId,
		onContentReady: function () {
			initMaterialInput();
		}
	});
}
</script>
</body>
</html>