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
	<title>考试记录</title>
	<jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<a class="waves-effect waves-button blue" href="javascript:void(0);" onclick="examStudentAction()"><i class="zmdi zmdi-accounts"></i> 学生成绩列表</a>
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
			url: '${basePath}/manage/exam-history/list',
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
				{field: 'passRate', title: '及格率'},
				{field: 'startTime', title: '起始时间', formatter: 'dateFormatter'},
				{field: 'endTime', title: '截止时间', formatter: 'dateFormatter'}
			]
		});
	});

	function queryParams(params){
		return params;
	}

	function dateFormatter(value, row, index){
		return new Date(value).Format('yyyy-MM-dd HH:mm:ss')
	}

	// 学生成绩列表
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
			location.href = '${basePath}/manage/exam-history/student/'+rows[0].id;
			/*examStudentDialog = $.dialog({
				type: 'blue',
				animationSpeed: 300,
				columnClass: 'col-md-10 col-md-offset-1',
				title: rows[0].examName+'--学生成绩列表',
				content: 'url:${basePath}/manage/exam-history/student/'+rows[0].id,
				onContentReady: function () {
					initMaterialInput();
				}
			});*/
		}
	}
</script>
</body>
</html>