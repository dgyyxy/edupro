<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="submain">
    <div id="subtoolbar">
        <!--<a class="waves-effect waves-button orange" href="javascript:void(0);" onclick="stopExamAction()"><i class="zmdi zmdi-settings"></i> 强制交卷</a>-->
    </div>
    <table id="subtable"></table>
</div>
<script>
    var examId = '${id}';
    var $subtable = $('#subtable');
    $(function() {
        // bootstrap table初始化
        $subtable.bootstrapTable({
            url: '${basePath}/manage/examing/student/list-'+examId,
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
                {field: 'ck', checkbox: true},
                {field: 'id', title: '编号', sortable: true, align: 'center'},
                {field: 'stuName', title: '姓名'},
                {field: 'idcard', title: '身份证件号'},
                {field: 'stuOrgan', title: '所属机构'},
                {field: 'proctor', title:'原因', visible: false},
                {field: 'approved', title: '考试状态', formatter: 'statusFormatter'},
                {field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
            ]
        });
    });

    // 格式化操作按钮
    function actionFormatter(value, row, index) {
        var id = row.id;
        var approved = row.approved;
        var proctor = row.proctor;
        if(approved == 1){
            return [
                '<a class="update" href="javascript:;" onclick="proctorAction('+id+',0)" data-toggle="tooltip"><i class="glyphicon glyphicon-edit"></i>&nbsp;强制交卷</a>'
            ].join('');
        }
        if(proctor!=null && proctor!=''){
            return [
                '<a class="update" href="javascript:;" onclick="proctorAction('+id+',1)" data-toggle="tooltip"><i class="zmdi zmdi-file-text"></i>&nbsp;查看原因</a>'
            ].join('');
        }
        return '';

    }

    function queryParams(params){
        return params;
    }

    function statusFormatter(value, row, index){
        if(value == 0){
            return '<span class="label label-default">未登录</span>';
        }else if(value == 1){
            return '<span class="label label-primary">考试中</span>';
        }else if(value == 2){
            return '<span class="label label-success">已交卷</span>'
        }else if(value == 3){
            return '<span class="label label-success">已交卷</span>'
        }else if(value == 4){
            return '<span class="label label-warning">已强制交卷</span>'
        }
    }

    // 监考强制交卷
    var proctorDialog;
    function proctorAction(id, tag) {
        proctorDialog = $.dialog({
            type: 'green',
            animationSpeed: 300,
            columnClass: 'col-md-6 col-md-offset-3',
            title: '强制交卷',
            content: 'url:${basePath}/manage/examing/proctor/'+id,
            onContentReady: function () {
                if(tag == 1) {//查看
                    $('#btngroup').hide();
                    $('#proctor').attr('readonly',true);
                }
                initMaterialInput();
            }
        });
    }

    /*//强制交卷
    var stopExamDialog;
    function stopExamAction() {
        var rows = $subtable.bootstrapTable('getSelections');
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
            stopExamDialog = $.confirm({
                type: 'red',
                animationSpeed: 300,
                title: false,
                content: '请确认是否对该考生执行强制交卷？',
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
                                if(approved != 1){
                                    deltag = 1;
                                    break;
                                }
                            }
                            if (deltag == 0){
                                $.ajax({
                                    type: 'get',
                                    url: '${basePath}/manage/examing/stopexam/' + ids.join("-"),
                                    success: function(result) {
                                        stopExamDialog.close();
                                        $subtable.bootstrapTable('refresh');
                                    },
                                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                                        alertMsg(textStatus);
                                    }
                                });
                            }else{
                                alertMsg("只有当考试状态处于进行中，才允许强制交卷！")
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
    }*/

</script>