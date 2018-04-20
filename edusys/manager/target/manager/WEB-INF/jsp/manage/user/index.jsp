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
	<title>用户管理</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<shiro:hasPermission name="sys:user:create"><a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增用户</a></shiro:hasPermission>
        <shiro:hasPermission name="sys:user:update"><a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑用户</a></shiro:hasPermission>
        <shiro:hasPermission name="sys:user:delete"><a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除用户</a></shiro:hasPermission>
        <shiro:hasPermission name="sys:user:role"><a class="waves-effect waves-button cyan" href="javascript:;" onclick="roleAction()"><i class="zmdi zmdi-accounts"></i> 用户角色</a></shiro:hasPermission>
	</div>
	<table id="table"></table>
</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/user/list',
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
		idField: 'userId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{field: 'ck', checkbox: true},
			{field: 'userId', title: '编号', sortable: true, align: 'center'},
            {field: 'username', title: '帐号'},
			{field: 'realname', title: '姓名'},
			{field: 'phone', title: '电话'},
			{field: 'type', title: '类型', align: 'center', formatter: 'typeFormatter'},
			{field: 'roleName', title: '角色',  align: 'center'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

function queryParams(params){
    return params;
}

// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
		'<shiro:hasPermission name="sys:user:update"><a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a></shiro:hasPermission>　',
		'<shiro:hasPermission name="sys:user:delete"><a class="delete" href="javascript:;" onclick="deleteAction()" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a></shiro:hasPermission>'
    ].join('');
}

// 格式化用户类型
function typeFormatter(value, row, index){
    if (value==1){
        return '<span class="label label-primary">管理员</span>';
    } else {
        return '<span class="label label-success">老师</span>';
    }
}

// 新增
var createDialog;
function createAction() {
	createDialog = $.dialog({
        type: 'green',
		animationSpeed: 300,
		title: '新增用户',
		content: 'url:${basePath}/manage/user/create',
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
			animationSpeed: 300,
			title: '编辑用户',
			content: 'url:${basePath}/manage/user/update/' + rows[0].userId,
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
			content: '确认删除该用户吗？',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();
						for (var i in rows) {
							ids.push(rows[i].userId);
						}
						$.ajax({
							type: 'get',
							url: '${basePath}/manage/user/delete/' + ids.join("-"),
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
// 用户角色
var roleDialog;
var roleUserId;
function roleAction() {
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
		roleUserId = rows[0].userId;
		roleDialog = $.dialog({
            type: 'blue',
			animationSpeed: 300,
			title: '用户角色',
			content: 'url:${basePath}/manage/user/role/' + roleUserId,
			onContentReady: function () {
				initMaterialInput();
				$('select').select2({
					placeholder: '请选择用户角色',
					allowClear: true
				});
			}
		});
	}
}
</script>
</body>
</html>