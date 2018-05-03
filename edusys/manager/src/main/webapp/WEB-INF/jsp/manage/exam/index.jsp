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
	<title>考试管理</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
    <style type="text/css">
        .timeinput {
            width: 130px;
            height: 24px
        }
    </style>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增考试</a>
        <a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑考试</a>
        <a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除考试</a>
        <%--<a class="waves-effect waves-button cyan" href="javascript:;" onclick="publishAction()"><i class="zmdi zmdi-cloud-box"></i> 考试发布</a>
        <a class="waves-effect waves-button orange" href="javascript:;" onclick="unpublishAction()"><i class="zmdi zmdi-cloud-box"></i> 取消发布</a>--%>
        <a class="waves-effect waves-button purple" href="javascript:;" onclick="examStudentAction()"><i class="zmdi zmdi-accounts"></i> 考生名单</a>
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
		url: '${basePath}/manage/exam/list',
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
            {field: 'examName', title: '考试名称'},
			{field: 'paperName', title: '试卷名称'},
			{field: 'examPwd', title: '考试密码'},
			{field: 'startTime', title: '起始时间', formatter: 'dateFormatter'},
			{field: 'endTime', title: '截止时间', formatter: 'dateFormatter'},
			{field: 'approved', title: '状态', formatter: 'statusFormatter'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

function queryParams(params){
    return params;
}

function dateFormatter(value, row, index){
    return new Date(value).Format('yyyy-MM-dd HH:mm:ss')
}

// 状态格式化 0 未发布, 1 已发布, 2 进行中, 3 已结束
function statusFormatter(value, row, index){
    if(value == 0){
        return '<span class="label label-default">未发布</span>';
    }else if(value == 1){
        return '<span class="label label-success">已发布</span>';
    }else if(value == 2){
		return '<span class="label label-primary">进行中</span>';
	}else if(value == 3){
		return '<span class="label label-warning">已结束</span>'
	}
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
        columnClass: 'col-md-6 col-md-offset-3',
        animationSpeed: 300,
		title: '新增考试',
		content: 'url:${basePath}/manage/exam/create',
		onContentReady: function () {
            initMaterialInput();
            $('select').select2({
                width: 200
            });
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
			type: 'green',
			columnClass: 'col-md-6 col-md-offset-3',
			animationSpeed: 300,
			title: '修改考试',
			content: 'url:${basePath}/manage/exam/update/' + rows[0].id,
			onContentReady: function () {
				initMaterialInput();
				$('select').select2({
					width: 200
				});
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
			content: '确认删除该考试吗？',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();

						for (var i in rows) {
							ids.push(rows[i].id);
						}

						var deltag = 0;
						for (var j in rows){
							var approved = rows[j].approved;
							if(approved == 2){
								deltag = 1;
								break;
							}
						}
						if (deltag == 0){
							$.ajax({
								type: 'get',
								url: '${basePath}/manage/exam/delete/' + ids.join("-"),
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
						}else{
							alertMsg("已发布的考试不允许删除！")
						}

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

// 考试发布
var publishDialog;
function publishAction(){
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
        var ids = new Array();
        for (var i in rows) {
            ids.push(rows[i].id);
        }
        $.ajax({
            type: 'get',
            url: '${basePath}/manage/exam/publish/' + ids.join("-"),
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
                    $table.bootstrapTable('refresh');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
}

// 撤消考试发布
var unpublishDialog;
function unpublishAction(){
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
		var ids = new Array();
		for (var i in rows) {
			ids.push(rows[i].id);
		}
		$.ajax({
			type: 'get',
			url: '${basePath}/manage/exam/unpublish/' + ids.join("-"),
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
					$table.bootstrapTable('refresh');
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alertMsg(textStatus);
			}
		});
	}
}

// 安排考生
var examStudentDialog;
function examStudentAction() {
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
        location.href = '${basePath}/manage/exam/students/' + rows[0].id;
    }
}
</script>
</body>
</html>