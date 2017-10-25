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
	<title>学习任务管理</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<shiro:hasPermission name="edu:job:create"><a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增学习任务</a></shiro:hasPermission>
		<shiro:hasPermission name="edu:job:courseware"><a class="waves-effect waves-button cyan" href="javascript:;" onclick="coursewareAction()"><i class="zmdi zmdi-accounts"></i> 分配课件</a></shiro:hasPermission>
        <shiro:hasPermission name="edu:job:update"><a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑学习任务</a></shiro:hasPermission>
        <shiro:hasPermission name="edu:job:delete"><a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除学习任务</a></shiro:hasPermission>
	</div>
	<table id="table"></table>
</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script src="${basePath}/resources/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/job/list',
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
            {field: 'name', title: '名称'},
			{field: 'startTime', title: '开始时间', formatter: 'dateFormatter'},
			{field: 'endTime', title: '结束时间', formatter: 'dateFormatter'},
			{field: 'time', title: '课时'},
			{field: 'teacher', title: '开课老师'},
			{field: 'organization', title: '开课机构'},
			{field: 'coursewareCount', title: '课件数', formatter: 'courseFormatter'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

function queryParams(params){
    return params;
}

function courseFormatter(value, row, index){
    var htmlstr = '<span class="label label-warning">'+value+'</span>';
    if (value > 0) {
        htmlstr = '<span onclick="jobcourseAction('+row.id+');" class="label label-primary" style="cursor:pointer;">'+value+'</span>';
    }
    return htmlstr;
}

// 日期格式化
function dateFormatter(value, row, index){
    return new Date(value).Format("yyyy-MM-dd HH:mm:ss");
}

// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
		'<a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
		'<a class="delete" href="javascript:;" onclick="deleteAction()" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}

// 新增
var createDialog;
function createAction() {
	createDialog = $.dialog({
        type: 'green',
        columnClass: 'col-md-5 col-md-offset-2',
		animationSpeed: 300,
		title: '新增学习任务',
		content: 'url:${basePath}/manage/job/create',
		onContentReady: function () {
			initMaterialInput();
		}
	});
}
// 编辑
var updateDialog;
function updateAction() {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length != 1) {
		$.confirm({
			title: false,
			content: '请选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		updateDialog = $.dialog({
            type: 'blue',
            columnClass: 'col-md-6 col-md-offset-2',
			animationSpeed: 300,
			title: '编辑学习任务',
			content: 'url:${basePath}/manage/job/update/' + rows[0].id,
			onContentReady: function () {
				initMaterialInput();
			}
		});
	}
}
// 删除
var deleteDialog;
function deleteAction() {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length == 0) {
		$.confirm({
			title: false,
			content: '请至少选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		deleteDialog = $.confirm({
			type: 'red',
			animationSpeed: 300,
			title: false,
			content: '确认删除该学习任务吗？',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();
						for (var i in rows) {
							ids.push(rows[i].id);
						}
						$.ajax({
							type: 'get',
							url: '${basePath}/manage/job/delete/' + ids.join("-"),
							success: function(result) {
								if (result.code != 1) {
									if (result.data instanceof Array) {
										$.each(result.data, function(index, value) {
                                            alertMsg(value.errorMsg);
										});
									} else {
                                        alertMsg(result.data);
									}
								} else {
									deleteDialog.close();
									$table.bootstrapTable('refresh');
								}
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
                                alertMsg(textStatus);
							}
						});
					}
				},
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	}
}
// 学习任务课件选择
var coursewareDialog;
var id;
function coursewareAction() {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length != 1) {
		$.confirm({
			title: false,
			content: '请选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		id = rows[0].id;
        coursewareDialog = $.dialog({
            type: 'blue',
            animationSpeed: 300,
            columnClass: 'col-md-10 col-md-offset-1',
            title: '分配课件',
            content: 'url:${basePath}/manage/job/courseware/'+id,
            onContentReady: function () {
                initMaterialInput();
            },
			onClose: function(){
				$table.bootstrapTable('refresh');
			}
		});
	}
}

// 已分配的课件列表
var jobcourseDialog;
function jobcourseAction(id) {
    jobcourseDialog = $.dialog({
        type: 'blue',
        animationSpeed: 300,
        columnClass: 'col-md-10 col-md-offset-1',
        title: '已分配课件列表',
        content: 'url:${basePath}/manage/job/jobcourse/'+id,
        onContentReady: function () {
            initMaterialInput();
        },
		onClose: function(){
			$table.bootstrapTable('refresh');
		}
    });
}
</script>
</body>
</html>