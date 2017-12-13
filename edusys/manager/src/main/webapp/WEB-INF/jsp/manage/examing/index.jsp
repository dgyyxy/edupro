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
	<title>考试进行</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<a class="waves-effect waves-button blue" href="javascript:;" onclick="examStudentAction()"><i class="zmdi zmdi-accounts"></i> 考生列表</a>
		<a class="waves-effect waves-button orange" href="javascript:;" onclick="endExamAction()"><i class="zmdi zmdi-time-countdown"></i> 结束考试</a>
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
		url: '${basePath}/manage/examing/list',
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
			{field: 'startTime', title: '开考时间', formatter: 'dateFormatter'},
			{field: 'examPwd', title: '考试密码'},
			{field: 'stuNum', title: '应考人数', formatter: 'stuNumFormatter'},
			{field: 'examingCount', title: '在考人数', formatter: 'numFormatter1'},
			{field: 'submitCount', title: '交卷人数', formatter: 'numFormatter2'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});

// 格式化操作按钮
function actionFormatter(value, row, index) {
	return [
		'<a class="update" href="javascript:;" onclick="watchExamAction('+row.id+')" data-toggle="tooltip"><i class="glyphicon glyphicon-edit"></i>&nbsp;监考记录</a>'
	].join('');

}

function queryParams(params){
    return params;
}


function dateFormatter(value, row, index){
	return new Date(value).Format('yyyy-MM-dd HH:mm:ss')
}

function stuNumFormatter(value, row, index){
	return '<span class="label label-success">'+value+'</span>';
}

function numFormatter1(value, row, index){
	return '<span class="label label-warning">'+value+'</span>';
}

function numFormatter2(value, row, index){
	return '<span class="label label-primary">'+value+'</span>';
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
		examStudentDialog = $.dialog({
			type: 'blue',
			animationSpeed: 300,
			columnClass: 'col-md-10 col-md-offset-1',
			title: rows[0].examName+'--参考考生列表',
			content: 'url:${basePath}/manage/examing/student/'+rows[0].id,
			onContentReady: function () {
				initMaterialInput();
			}
		});
    }
}

var endExamDialog;
function endExamAction() {
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
			content: '确认结束考试吗？',
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
							url: '${basePath}/manage/exam/endExam/' + ids.join("-"),
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

// 监考记录
var watchDialog;
function watchExamAction(id) {
	watchDialog = $.dialog({
		type: 'green',
		animationSpeed: 300,
		columnClass: 'col-md-6 col-md-offset-3',
		title: '监考记录',
		content: 'url:${basePath}/manage/examing/watch/'+id,
		onContentReady: function () {
			initMaterialInput();
		}
	});
}
</script>
</body>
</html>