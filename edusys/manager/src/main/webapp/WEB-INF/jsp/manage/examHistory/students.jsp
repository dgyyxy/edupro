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
    <title>学生成绩列表</title>
    <jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
    <div id="subtoolbar">
        <a class="waves-effect waves-button blue" href="${basePath}/manage/exam-history/index"><i class="zmdi zmdi-arrow-back"></i> 返回列表</a>
    </div>
    <table id="subtable"></table>
</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script>
    var examId = '${id}';
    var $subtable = $('#subtable');
    $(function() {
        // bootstrap table初始化
        $subtable.bootstrapTable({
            url: '${basePath}/manage/exam-history/student/list-'+examId,
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
                {field: 'stuName', title: '姓名'},
                {field: 'stuOrgan', title: '所属机构'},
                {field: 'point', title: '总分'},
                {field: 'pointGet', title: '得分'},
                {field: 'approved', title: '是否通过', formatter: 'statusFormatter'},
                {field: 'duration', title: '考试时间'},
                {field: 'submitTime', title: '交卷时间', formatter: 'dateFormatter'},
                {field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
            ]
        });
    });

    function queryParams(params){
        return params;
    }

    function dateFormatter(value, row, index){
        if(value == null){
            return '-';
        }
        return new Date(value).Format('yyyy-MM-dd HH:mm:ss')
    }

    function statusFormatter(value, row, index){
        if(value == 0){
            return '<span class="label label-default">未登录</span>';
        }else if(value == 1){
            return '<span class="label label-primary">考试中</span>';
        }else if(value == 2){
            return '<span class="label label-success">及格</span>'
        }else if(value == 3){
            return '<span class="label label-danger">不及格</span>'
        }else if(value == 4){
            return '<span class="label label-warning">已强制交卷</span>'
        }
    }

    // 格式化操作按钮
    function actionFormatter(value, row, index) {
        var id = row.id;
        if(row.approved>1){
            return [
                '<a class="waves-effect waves-button green" href="javascript:;" onclick="lookPageAction('+id+')" data-toggle="tooltip" title="查看试卷">查看试卷</a>　'
            ].join('');
        }else{
            return '-';
        }

    }

    function lookPageAction(id){
        location.href = '${basePath}/manage/exam-history/paper/'+id;
    }


</script>
</body>
</html>