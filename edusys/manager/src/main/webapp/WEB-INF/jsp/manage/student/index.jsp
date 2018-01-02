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
	<title>学员信息管理</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div class="row">
		<div class="col-md-3">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title">
						学员机构
					</h3>
				</div>
				<div class="panel-body">
					<div id="tree"></div>
				</div>
			</div>
		</div>
		<div class="col-md-9">
			<div id="toolbar">
				<shiro:hasPermission name="edu:student:create"><a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增学员</a></shiro:hasPermission>
				<shiro:hasPermission name="edu:student:update"><a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑学员</a></shiro:hasPermission>
				<shiro:hasPermission name="edu:student:delete"><a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除学员</a></shiro:hasPermission>
				<shiro:hasPermission name="edu:student:resetPassword"><a class="waves-effect waves-button orange" href="javascript:;" onclick="resetPasswordAction()"><i class="zmdi zmdi-key"></i> 重置密码</a></shiro:hasPermission>
				<shiro:hasPermission name="edu:student:import"><a class="waves-effect waves-button cyan" href="javascript:;" onclick="importAction()"><i class="zmdi zmdi-upload"></i> 学员导入</a></shiro:hasPermission>
				<shiro:hasPermission name="edu:student:export"><a class="waves-effect waves-button purple" href="javascript:;" onclick="exportAction()"><i class="zmdi zmdi-download"></i> 学员导出</a></shiro:hasPermission>
			</div>
			<table id="table"></table>
		</div>
	</div>

</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script src="${basePath}/resources/plugins/bootstrap-treeview.js"></script>
<script>
var typeId = 0;//类型Id
var typePid = 0;//父ID
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/student/list',
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
		idField: 'stuId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{field: 'ck', checkbox: true},
			{field: 'stuId', title: '编号', sortable: true, align: 'center'},
			{field: 'stuNo', title: '学号'},
			{field: 'stuName', title: '姓名'},
			{field: 'cardNo', title: '身份证件号'},
			{field: 'phone', title: '手机号'},
			{field: 'organizationName2', title: '所属机构'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

//搜索传参数
function queryParams(params){
	params.typeId = typeId;
	params.typePid = typePid;
	return params;
}

// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
		'<shiro:hasPermission name="edu:student:update"><a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a></shiro:hasPermission>　',
		'<shiro:hasPermission name="edu:student:delete"><a class="delete" href="javascript:;" onclick="deleteAction()" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a></shiro:hasPermission>'
    ].join('');
}

// 学员机构
function initTree() {
	$.getJSON('${basePath}/manage/organization/list', {limit: 100000}, function(json) {
		var datas = [];
		var list = json.rows;
		for(var i = 0; i< list.length; i++){
			var node = {}
			node.id = list[i].organizationId;
			node.pid = list[i].parentId;
			var nodeName = list[i].name;
			if(nodeName.length > 6) nodeName = nodeName.substring(0,5)+'...';
			node.text =  nodeName;

			node.level = list[i].level;
			if(node.level == 2) continue;
			node.nodes = [];
			var subList = json.rows;
			for(var j = 0; j< subList.length; j++){

				var subNode = {};
				subNode.id = subList[j].organizationId;
				subNode.pid = subList[j].parentId;
				var subName = subList[j].name;
				if(subName.length>5) subName = subName.substring(0,4)+'...';
				subNode.text = subName;

				subNode.level = subList[j].level;
				if(subNode.level == 1) continue;
				if(subNode.pid == node.id) node.nodes.push(subNode);
			}
			datas.push(node);
		}
		console.log(datas);
		var nodeId;
		$('#tree').treeview({
			data: datas,
			selectedBackColor: '#29A176',
			emptyIcon: 'glyphicon glyphicon-file',
			expandIcon: 'glyphicon glyphicon-folder-close',
			collapseIcon: 'glyphicon glyphicon-folder-open',
			onNodeUnselected: function(event, node){
				nodeId = node.nodeId;
				$('#tree').treeview('selectNode', [ nodeId, { silent: true } ]);
			},
			onNodeSelected: function(event, node){
				if(nodeId != undefined) $('#tree').treeview('unselectNode', [ nodeId, { silent: true } ]);
				typeId = node.id;
				typePid = node.pid;
				$table.bootstrapTable('refresh',{url:'${basePath}/manage/student/list'});
			}
		});
	});
}

$(function(){
	initTree();
});

// 新增
var createDialog;
function createAction() {
	createDialog = $.dialog({
        type: 'green',
        columnClass: 'col-md-6 col-md-offset-2',
        title: '新增学员信息',
		content: 'url:${basePath}/manage/student/create',
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
			title: '编辑学员信息',
			content: 'url:${basePath}/manage/student/update/' + rows[0].stuId,
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
			content: '确认删除该学员信息吗？',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();
						for (var i in rows) {
							ids.push(rows[i].stuId);
						}
						$.ajax({
							type: 'get',
							url: '${basePath}/manage/student/delete/' + ids.join("-"),
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

// 重置密码
var resetPasswordDialog;
function resetPasswordAction() {
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
        resetPasswordDialog = $.confirm({
			type: 'blue',
			animationSpeed: 300,
			title: false,
			content: '请确认是否重置密码？默认为证件号码后4位',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();
						for (var i in rows) {
							ids.push(rows[i].stuId);
						}
						$.ajax({
							type: 'get',
							url: '${basePath}/manage/student/resetPassword/' + ids.join("-"),
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
                                    resetPasswordDialog.close();
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

// 导入学员信息
var importDialog;
function importAction(){
    importDialog = $.dialog({
        type: 'green',
        columnClass: 'col-md-6 col-md-offset-2',
        title: '导入学员信息',
        content: 'url:${basePath}/manage/student/import',
        onContentReady: function () {
            initMaterialInput();
        }
    });
}

// 导出学员信息
var exportDialog;
function exportAction(){
    var rows = $table.bootstrapTable('getSelections');
    var content = "导出您所选择的学员列表";
    var urlstr = '${basePath}/manage/student/export';
    if(rows.length == 0){
        content = "导出全部学员列表";
        urlstr += '?isAll=1';
    }else{
        var ids = new Array();
        for (var i in rows) {
            ids.push(rows[i].stuId);
        }
        urlstr += '?isAll=0&ids='+ids.join("-");
    }
    exportDialog = $.confirm({
        type: 'blue',
        animationSpeed: 300,
        title: false,
        content: content,
        buttons: {
            confirm: {
                text: '确认',
                btnClass: 'waves-effect waves-button',
                action: function () {
                    location.href = urlstr;
                    /*$.ajax({
                        type: 'get',
                        url: urlstr,
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
                                exportDialog.close();
                                $table.bootstrapTable('refresh');
                            }
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            alertMsg(textStatus);
                        }
                    });*/
                }
            },
            cancel: {
                text: '取消',
                btnClass: 'waves-effect waves-button'
            }
        }
    });
}
</script>
</body>
</html>