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
    <div id="subtoolbar" style="width:770px;">
        <a class="waves-effect waves-button red" href="${basePath}/manage/exam-history/index"><i class="zmdi zmdi-arrow-back"></i> 返回列表</a>
        <a class="waves-effect waves-button green" href="javascript:void(0);" onclick="exportAction()"><i class="zmdi zmdi-download"></i> 导出成绩</a>
        <div style="float: right">
            &nbsp;&nbsp;&nbsp;<select id="organSelect"></select>&nbsp;&nbsp;
            <select id="compareSelect"></select><div class="col-sm-2" style="float:right;margin-left:0px;padding-left:0px;top: 3px;left:-146px;"><input type="text" id="score" placeholder="成绩" class="form-control input-sm" maxlength="4"/></div>
            <a id="search" style="left: 100px;" class="waves-effect waves-button blue" href="javascript:void(0);"><i class="zmdi zmdi-search"></i> 搜索</a>
        </div>
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
            search: false,
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
                {field: 'ck', checkbox: true},
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
        //分数比较
        initCompare();
        //初始化组织机构
        initOrgan();

        //搜索
        $('#search').click(function(){
            //机构
            var organId = $('#organSelect').select2('val');
            //成绩 1:大于，2:等于，3:小于
            var compare = $('#compareSelect').select2('val');
            var score = $('#score').val();
            if(score == '') score = 0;
            if(!/^\d+$/.test(score)){
                alertMsg('输入的成绩必须为正整数！请确认');
                return false;
            }

            $subtable.bootstrapTable('refresh', {url: '${basePath}/manage/exam-history/student/list-'+examId+'?organId='+organId+'&compare='+compare+'&score='+score});
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

    function initCompare(){
        var datas = [{id: 1,text:'成绩大于'},{id: 2,text:'成绩等于'},{id: 3,text:'成绩小于'}];
        $('#compareSelect').select2({
            width: 80,
            data: datas
        });
    }
    //初始机构
    function initOrgan(){
        $.ajax({
            url: '${basePath}/manage/organization/list',
            type: 'get',
            success: function(resp){
                var organList = resp.rows;
                var datas = [{id: 0,text:'请选择机构'}];
                for(var i = 0;i<organList.length; i++){
                    var data = {};
                    var obj = organList[i];
                    if(obj.level == 2){
                        continue;
                    }
                    data.text = obj.name;
                    data.children = [];
                    for(var j = 0; j< organList.length; j++){
                        var subData = {};
                        var _obj = organList[j];
                        if(_obj.level == 1){
                            continue;
                        }
                        subData.id = _obj.organizationId;
                        subData.text = _obj.name;
                        if(obj.organizationId == _obj.parentId){
                            data.children.push(subData);
                        }
                    }
                    datas.push(data);
                }
                $('#organSelect').select2({
                    width: 150,
                    data: datas
                });
            }
        });
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

    // 导出学员成绩
    var exportDialog;
    function exportAction(){
        var rows = $subtable.bootstrapTable('getSelections');
        var content = "导出选中学员成绩";
        var urlstr = '${basePath}/manage/exam-history/export';
        if(rows.length == 0){
            content = "导出全部学员成绩";
            urlstr += '?isAll=1';
        }else{
            var ids = new Array();
            for (var i in rows) {
                ids.push(rows[i].id);
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