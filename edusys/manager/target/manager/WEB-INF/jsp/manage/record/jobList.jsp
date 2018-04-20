<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="submain">
	<div id="subtoolbar">
	</div>
	<table id="subtable"></table>
</div>
<script>
var $subtable = $('#subtable');
$(function() {
	// bootstrap table初始化
	$subtable.bootstrapTable({
		url: '${basePath}/manage/record/job-record/${stuId}',
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
			{field: 'ck', checkbox: true},
			{field: 'id', title: '编号', sortable: true, align: 'center'},
            {field: 'jobName', title: '任务名称'},
            {field: 'duration', title: '学时', formatter: 'durationFormatter'},
			{field: 'totalCourse', title: '学习进度', formatter: 'percentFormatter'},
			{field: 'time', title: '用时', formatter: 'timeFormatter'},
			{field: 'studycount', title: '已学课件', formatter: 'studyCourseCountFormatter'}
		]
	});
});

function durationFormatter(value, row, index){
	return value+'分钟';
}

//进度格式化
function percentFormatter(value, row, index){
	return getPercent(row.courseNum, value);
}

//学习课件数量
function studyCourseCountFormatter(value, row, index){
	if(value>0)
		return '<span onclick="coursesAction('+row.stuId+','+row.jobId+');" class="label label-primary" style="cursor:pointer;">'+value+'</span>';
	else
		return '<span class="label label-default">'+value+'</span>';
}

// 格式化操作按钮
function actionFormatter(value, row, index) {
	return [
		'<a class="update" href="javascript:;" onclick="courseListAction()" data-toggle="tooltip" title="Detail"><i class="glyphicon glyphicon-detail"></i></a>　',
	].join('');
}

function queryParams(params){
    return params;
}

//进入已学的课件列表
function coursesAction(stuId, jobId){
	jobDialog.close();
	courseListAction(stuId, jobId);
}
</script>