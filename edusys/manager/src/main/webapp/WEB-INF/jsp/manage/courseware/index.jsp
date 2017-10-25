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
    <title>课件管理</title>
    <jsp:include page="../../common/css.jsp" flush="true"/>

    <!-- uploadify -->
    <link href="${basePath}/resources/plugins/uploadify/uploadify.css" rel="stylesheet"/>
</head>
<body>
<div id="main">
    <div class="row">
        <div class="col-md-3">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        课件类别
                        <shiro:hasPermission name="edu:coursewaretype:create"><a href="javascript:;" onclick="createTypeAction()" style="float: right;"><i class="glyphicon glyphicon-plus icon-plus"></i></a></shiro:hasPermission>
                    </h3>
                </div>
                <div class="panel-body">
                    <div id="tree"></div>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div id="toolbar">
                <shiro:hasPermission name="edu:courseware:create"><a class="waves-effect waves-button green" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增课件</a></shiro:hasPermission>
                <shiro:hasPermission name="edu:courseware:update"><a class="waves-effect waves-button blue" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑课件</a></shiro:hasPermission>
                <shiro:hasPermission name="edu:courseware:delete"><a class="waves-effect waves-button red" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除课件</a></shiro:hasPermission>
            </div>
            <table id="table"></table>
        </div>
    </div>

</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script src="${basePath}/resources/plugins/bootstrap-treeview.js"></script>
<script src="${basePath}/resources/plugins/uploadify/jquery.uploadify.js"></script>
<script>
    var typeId = 0;//类型Id
    var typePid = 0;//父ID

    var $table = $('#table');
    $(function() {
        $(document).on('focus', 'input[type="text"]', function() {
            $(this).parent().find('label').addClass('active');
        }).on('blur', 'input[type="text"]', function() {
            if ($(this).val() == '') {
                $(this).parent().find('label').removeClass('active');
            }
        });
        // bootstrap table初始化
        $table.bootstrapTable({
            url: '${basePath}/manage/courseware/list',
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
                {field: 'name', title: '课件名称'},
                {field: 'category', title: '所属类别'},
                {field: 'time', title: '课时'},
                {field: 'uriStr', title: '路径'},
                {field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
            ]
        });

    });

    //搜索传参数
    function queryParams(params){
        return params;
    }

    function uriFormatter(value, row, index){
        return value.substr(value.lastIndexOf('/'), value.length);
    }

    // 格式化操作按钮
    function actionFormatter(value, row, index) {
        return [
            '<shiro:hasPermission name="edu:courseware:update"><a class="update" href="javascript:;" onclick="updateAction()" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a></shiro:hasPermission>　',
            '<shiro:hasPermission name="edu:courseware:delete"><a class="delete" href="javascript:;" onclick="deleteAction()" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a></shiro:hasPermission>'
        ].join('');
    }

    // 新增
    var createDialog;
    function createAction() {
        createDialog = $.dialog({
            type: 'green',
            animationSpeed: 300,
            columnClass: 'col-md-8 col-md-offset-1',
            title: '新增课件',
            content: 'url:${basePath}/manage/courseware/create',
            onContentReady: function(){
                initMaterialInput();
                $('select').select2({
                    width: 150
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
                animationSpeed: 300,
                columnClass: 'col-md-8 col-md-offset-1',
                title: '编辑课件',
                content: 'url:${basePath}/manage/courseware/update/'+rows[0].id,
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
                content: '确认删除该课件吗？',
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
                                url: '${basePath}/manage/courseware/delete/' + ids.join("-"),
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
                                        $table.bootstrapTable('refresh', {url:'${basePath}/manage/courseware/list?typeId='+typeId});
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

    // 课件类别
    function initTree() {
        $.getJSON('${basePath}/manage/courseware/category/list', {}, function(json) {
            var datas = [];
            var list = json.courseTypeList;
            for(var i = 0; i< list.length; i++){
                var node = {}
                node.id = list[i].id;
                node.pid = list[i].pid;
                var nodeName = list[i].name;
                if(nodeName.length > 6) nodeName = nodeName.substring(0,5)+'...';
                node.text =  nodeName;

                node.level = list[i].level;
                node.href = 'javascript:selectAction();';
                var operateHtml = "<span style='float:right;'><i class='glyphicon glyphicon-edit' onclick='updateTypeAction("+node.id+")'></i>&nbsp;&nbsp;<i class='glyphicon glyphicon-remove' onclick='deleteTypeAction("+node.id+")'></i></span>"
                node.text = node.text + operateHtml;
                if(node.level == 2) continue;
                node.nodes = [];
                var subList = json.courseTypeList;
                for(var j = 0; j< subList.length; j++){

                    var subNode = {};
                    subNode.id = subList[j].id;
                    subNode.pid = subList[j].pid;
                    var subName = subList[j].name;
                    if(subName.length>5) subName = subName.substring(0,4)+'...';
                    subNode.text = subName;

                    subNode.level = subList[j].level;
                    if(subNode.level == 1) continue;
                    operateHtml = "<span style='float:right;'><i class='glyphicon glyphicon-edit' onclick='updateTypeAction("+subNode.id+")'></i>&nbsp;&nbsp;<i class='glyphicon glyphicon-remove' onclick='deleteTypeAction("+subNode.id+")'></i></span>"
                    subNode.text = subNode.text + operateHtml;
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
                    $table.bootstrapTable('refresh',{url:'${basePath}/manage/courseware/list?typeId='+node.id});
                }
            });
        });
    }

    $(function(){
        initTree();
    });

    // 新增课件类别
    var createTypeDialog;
    function createTypeAction(){
        createTypeDialog = $.dialog({
            type: 'green',
            animationSpeed: 300,
            title: '新增课件类别',
            content: 'url:${basePath}/manage/courseware/category/create',
            onContentReady: function(){
                initMaterialInput();
            }
        });
    }

    // 编辑课件类别
    var updateTypeDialog;
    function updateTypeAction(id) {
        updateTypeDialog = $.dialog({
            type: 'blue',
            animationSpeed: 300,
            title: '编辑课件',
            content: 'url:${basePath}/manage/courseware/category/update/'+id,
            onContentReady: function () {
                initMaterialInput();
            }
        });
        //阻止冒泡
        window.event? window.event.cancelBubble = true : e.stopPropagation();
    }
    // 删除
    var deleteTypeDialog;
    function deleteTypeAction(id) {
        deleteTypeDialog = $.confirm({
            type: 'red',
            animationSpeed: 300,
            title: false,
            content: '确认删除该课件类型吗？',
            buttons: {
                confirm: {
                    text: '确认',
                    btnClass: 'waves-effect waves-button',
                    action: function () {
                        $.ajax({
                            type: 'get',
                            url: '${basePath}/manage/courseware/category/delete/' + id,
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
                                    deleteTypeDialog.close();
                                    initTree();
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
        //阻止冒泡
        window.event? window.event.cancelBubble = true : e.stopPropagation();
    }


</script>
</body>
</html>
