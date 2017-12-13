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
	<title>试题管理</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div class="row">
		<div class="col-md-3">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title">
						题库分类
					</h3>
				</div>
				<div class="panel-body">
					<div id="tree"></div>
				</div>
			</div>
		</div>
		<div class="col-md-9">
			<div id="toolbar">
				<a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增试题</a>
				<a class="waves-effect waves-button cyan" href="javascript:;" onclick="importAction()"><i class="zmdi zmdi-accounts"></i> 导入试题</a>
				<a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑试题</a>
				<a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除试题</a>
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
		url: '${basePath}/manage/question/list',
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
            {field: 'name', title: '试题'},
			{field: 'typeName', title: '类型'},
			{field: 'categoryName', title: '所属分类'},
			{field: 'creator', title: '创建者'},
			{field: 'difficulty', title: '难度级别', align: 'center'},
			{field: 'errorCount', title: '出题率', sortable: true, align: 'center', formatter: 'errorRateFormatter'},
			{field: 'qsum', title: '答题数', align: 'center'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

function queryParams(params){
    params.categoryId = typeId;
    params.typeId = 0;
    params.difficulty = 0;
    return params;
}

// 题库分类
function initTree() {
	$.getJSON('${basePath}/manage/question/category/list', {}, function(json) {
		var datas = [];
		var list = json.rows;
		for(var i = 0; i< list.length; i++){
			var node = {}
			node.id = list[i].id;
			node.pid = list[i].pid;
			var nodeName = list[i].name;
			if(nodeName.length > 6) nodeName = nodeName.substring(0,5)+'...';
			node.text =  nodeName;

			node.level = list[i].level;
			if(node.level == 2) continue;
			node.nodes = [];
			var subList = json.rows;
			for(var j = 0; j< subList.length; j++){

				var subNode = {};
				subNode.id = subList[j].id;
				subNode.pid = subList[j].pid;
				var subName = subList[j].name;
				if(subName.length>5) subName = subName.substring(0,4)+'...';
				subNode.text = subName;

				subNode.level = subList[j].level;
				if(subNode.level == 1) continue;
				if(subNode.pid == node.id) node.nodes.push(subNode);
			}
			datas.push(node);
		}
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
				$table.bootstrapTable('refresh',{url:'${basePath}/manage/question/list'});
			}
		});
	});
}

$(function(){
	initTree();
});

//出错率
function errorRateFormatter(value, row, index){
	return getPercent(value, row.qsum);
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
        columnClass: 'col-md-10 col-md-offset-1',
		animationSpeed: 300,
		title: '新增试题',
		content: 'url:${basePath}/manage/question/create',
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
            type: 'blue',
			columnClass: 'col-md-10 col-md-offset-1',
			animationSpeed: 300,
			title: '编辑试题',
			content: 'url:${basePath}/manage/question/update/' + rows[0].id,
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
			content: '确认删除该试题吗？',
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
							url: '${basePath}/manage/question/delete/' + ids.join("-"),
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


// 导入试题
var importDialog;
function importAction(){
    importDialog = $.dialog({
        type: 'green',
        columnClass: 'col-md-6 col-md-offset-2',
        title: '导入试题',
        content: 'url:${basePath}/manage/question/import',
        onContentReady: function () {
            initMaterialInput();
        }
    });
}

</script>
</body>
</html>