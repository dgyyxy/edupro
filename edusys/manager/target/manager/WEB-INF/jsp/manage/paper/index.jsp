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
	<title>试卷管理</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div class="row">
		<div class="col-md-3">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title">
						试卷分类
					</h3>
				</div>
				<div class="panel-body">
					<div id="tree"></div>
				</div>
			</div>
		</div>
		<div class="col-md-9">
			<div id="toolbar">
				<a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增试卷</a>
				<a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑试卷</a>
				<a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除试卷</a>
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
		url: '${basePath}/manage/paper/list',
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
            {field: 'name', title: '试卷名称'},
			{field: 'paperType', title: '组卷方式', formatter: 'typeFormatter'},
			{field: 'categoryName', title: '所属分类'},
			{field: 'duration', title: '时长'},
			{field: 'totalPoint', title: '总分'},
			{field: 'passPoint', title: '及格分'},
			{field: 'amount', title: '题量'},
			{field: 'status', title: '状态', formatter: 'statusFormatter'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

function queryParams(params){
	params.typeId = typeId;
	return params;
}

// 试卷分类
function initTree() {
	$.getJSON('${basePath}/manage/paper/category/list', {limit: 100000}, function(json) {
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
				$table.bootstrapTable('refresh',{url:'${basePath}/manage/paper/list'});
			}
		});
	});
}

$(function(){
	initTree();
});

// 状态格式化 0未完成 -> 1已完成 -> 2已发布 -> 3通过审核 （已发布和通过审核的无法再修改）
function statusFormatter(value, row, index){
    if(value == 0){
        return '<span class="label label-default">未完成</span>';
    }else if(value == 1){
        return '<span class="label label-success">已完成</span>'
    }else if(value == 2){
        return '<span class="label label-primary">已发布</span>';
    }
}

// 组卷方式格式化
function typeFormatter(value, row, index){
    switch (value){
        case 1:
            return '手动组卷';
        case 2:
            return '自动组卷';
        case 3:
            return '考试组卷';
    }
}

// 格式化操作按钮
function actionFormatter(value, row, index) {
    if(row.status==2){
        return [
            '<a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　'
        ].join('');
    }else{
        return [
            '<a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
            '<a class="delete" href="javascript:;" onclick="deleteAction()" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
        ].join('');
    }

}

// 新增
var createDialog;
function createAction() {
	createDialog = $.dialog({
        type: 'green',
        columnClass: 'col-md-6 col-md-offset-3',
        animationSpeed: 300,
		title: '新增试卷',
		content: 'url:${basePath}/manage/paper/create',
		onContentReady: function () {
            initMaterialInput();
            $('select').select2({
                width: 200
            });
		}
	});
}
// 编辑
var updatePaperDialog;
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
		updatePaperDialog = $.dialog({
			type: 'blue',
			columnClass: 'col-md-6 col-md-offset-3',
			animationSpeed: 300,
			title: '编辑试卷',
			content: 'url:${basePath}/manage/paper/updatePaper/'+rows[0].id,
			onContentReady: function () {
				initMaterialInput();
				$('select').select2({
					width: 200
				});
			}
		});
	}
}

//完善试卷
function perfectPaperAction(){
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
		var status = rows[0].status;
		if(status == 0){
			location.href = '${basePath}/manage/paper/update/'+rows[0].id;
		}else{
			alertMsg('该试卷已经完善！');
		}
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
			content: '确认删除该试卷吗？',
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
							url: '${basePath}/manage/paper/delete/' + ids.join("-"),
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
</script>
</body>
</html>