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
		<a class="waves-effect waves-button orange" href="javascript:backJob(${stuId});"><i class="zmdi zmdi-view-comfy"></i> 返回已学任务列表</a>
	</div>
	<table id="subtable"></table>
</div>
<script>
var $subtable = $('#subtable');
$(function() {
	// bootstrap table初始化
	$subtable.bootstrapTable({
		url: '${basePath}/manage/record/course-record/${stuId}/${jobId}',
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
		toolbar: '#subtoolbar',
		columns: [
			{field: 'id', title: '编号', sortable: true, align: 'center'},
            {field: 'courseName', title: '课件名称'},
			{field: 'duration', title: '学时', formatter: 'durationFormatter'},
			{field: 'time', title: '用时', formatter: 'timeFormatter'},
			{field: 'status', title: '状态', formatter: 'statusFormatter'}
		]
	});
});

function backJob(jobId){
	courseDialog.close();
	jobsAction(jobId);
}

function durationFormatter(value, row, index){
	return value+'分钟';
}

//学习课件数量
function statusFormatter(value, row, index){
	if(value==1)
		return '<span class="label label-default">学习中</span>';
	else
		return '<span class="label label-primary">已学完</span>';
}

function queryParams(params){
    return params;
}
</script>