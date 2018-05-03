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
				{field: 'endTime', title: '截止时间', formatter: 'dateFormatter'},
				{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
			]
		});
	});

	// 格式化操作按钮
	function actionFormatter(value, row, index) {
		var id = row.id;
		var teacher = row.teacher;
		var passRate = row.passRate;
		var returnObj = [];
        returnObj.push('<a class="update" href="javascript:;" onclick="exportScoreAction('+id+', \''+passRate+'\')" data-toggle="tooltip"><i class="zmdi zmdi-download"></i>&nbsp;导出成绩</a>&nbsp;&nbsp;&nbsp;');
		if(teacher!=null && teacher!=''){
		    returnObj.push('<a class="update" href="javascript:;" onclick="watchExamAction('+id+')" data-toggle="tooltip"><i class="glyphicon glyphicon-eye-open"></i>&nbsp;监考记录</a>');
		}
		return returnObj.join('');

	}


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
				$('#btngroup').hide();
				$('#teacher').attr('readonly',true);
				$('#watch').attr('readonly',true);
				initMaterialInput();
			}
		});
	}

	//导出成绩
	var examScoreDialog;
	function exportScoreAction(id, passRate) {
	    passRate = passRate.substr(0, passRate.length-1);
	    examScoreDialog = $.dialog({
			type: 'green',
            animationSpeed: 300,
            columnClass: 'col-md-6 col-md-offset-3',
            title: '导出考试成绩',
			content: 'url:${basePath}/manage/exam-history/exportPage/'+id+'?passRate='+passRate,
			onContentReady: function(){
			    initMaterialInput();
            }
		});
	}
</script>
</body>
</html>