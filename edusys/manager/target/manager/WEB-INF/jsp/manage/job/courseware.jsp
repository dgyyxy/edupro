<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="main">
    <div id="subtoolbar" style="width:405px;">
        <a id="comfirmAllot" class="waves-effect waves-button green " href="javascript:;" onclick="allotSubmitAction()"><i class="zmdi zmdi-plus"></i> 确认分配</a>
        <a id="delAllot" class="waves-effect waves-button red " href="javascript:;" onclick="delallotAction()" style="display:none;"><i class="zmdi zmdi-close"></i> 删除已分配</a>
        <a id="allotList" class="waves-effect waves-button blue " href="javascript:;" onclick="allotListAction()"><i class="zmdi zmdi-storage"></i> 已分配列表</a>
        <a id="unallotList" class="waves-effect waves-button blue " href="javascript:;" onclick="unallotListAction()" style="display:none;"><i class="zmdi zmdi-arrow-back"></i> 返回</a>
        <div style="float: right;">
            <select id="categorySelect" style="float:right;"></select>
        </div>
    </div>
    <table id="courseList"></table>
</div>
<script>
    var tag = 1;//未分配课件列表
    var $subtable = $('#courseList');
    $(function() {
        // 初始化课件类型列表
        initCourseType();

        // 选择课件类型列表进行搜索
        $('#categorySelect').change(function(){
            $subtable.bootstrapTable('refresh');
        });

        $(document).on('focus', 'input[type="text"]', function() {
            $(this).parent().find('label').addClass('active');
        }).on('blur', 'input[type="text"]', function() {
            if ($(this).val() == '') {
                $(this).parent().find('label').removeClass('active');
            }
        });
        // bootstrap table初始化
        $subtable.bootstrapTable({
            url: '${basePath}/manage/job/courseList/${jobId}',
            height: getHeight(),
            striped: true,
            search: true,
            strictSearch: true,
            queryParams: 'subqueryParams',
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
                {field: 'name', title: '课件名称'},
                {field: 'category', title: '所属类别'},
                {field: 'time', title: '课时'},
                {field: 'uriStr', title: '路径', formatter: 'suburiFormatter'}
            ]
        });

    });

    function initCourseType(){
        $.ajax({
            url: '${basePath}/manage/courseware/category/list',
            type: 'get',
            success: function(rows){
                var courseTypeList = rows.courseTypeList;
                var datas = [{id: 0,text:'请选择课件类型'}];
                for(var i = 0;i<courseTypeList.length; i++){
                    var data = {};
                    var obj = courseTypeList[i];
                    if(obj.level == 2){
                        continue;
                    }
                    data.text = obj.name;
                    data.children = [];
                    for(var j = 0; j< courseTypeList.length; j++){
                        var subData = {};
                        var _obj = courseTypeList[j];
                        if(_obj.level == 1){
                            continue;
                        }
                        subData.id = _obj.id;
                        subData.text = _obj.name;
                        if(obj.id == _obj.pid){
                            data.children.push(subData);
                        }
                    }
                    datas.push(data);
                }
                $('#categorySelect').select2({
                    width: 150,
                    data: datas
                });
            }
        });
    }

    //搜索传参数
    function subqueryParams(params){
        var typeId = $("#categorySelect").val();
        params.tag = tag;//未分配列表标识
        if(typeId!=undefined) params.typeId = typeId;
        return params;
    }

    function suburiFormatter(value, row, index){
        return value.substr(value.lastIndexOf('/'), value.length);
    }

    //获取已分配列表
    function allotListAction(){
        $('#comfirmAllot').hide();
        $('#allotList').hide();
        $('#unallotList').show();
        $('#delAllot').show();
        tag = 2;//已分配课件列表
        $subtable.bootstrapTable('refresh', {url: '${basePath}/manage/job/courseList/${jobId}?tag='+tag});

    }

    //未分配课件列表
    function unallotListAction(){
        $('#comfirmAllot').show();
        $('#allotList').show();
        $('#unallotList').hide();
        $('#delAllot').hide();
        tag = 1;
        $subtable.bootstrapTable('refresh', {url: '${basePath}/manage/job/courseList/${jobId}?tag='+tag});
    }

    //删除已分配的课件
    var delallotDialog
    function delallotAction(){
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
            delallotDialog = $.confirm({
                type: 'red',
                animationSpeed: 300,
                title: false,
                content: '确认删除已分配课件吗？',
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
                                url: '${basePath}/manage/job/delAllot/${jobId}/' + ids.join("-"),
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
                                        delallotDialog.close();
                                    }
                                    $subtable.bootstrapTable('refresh', {url: '${basePath}/manage/job/courseList/${jobId}?tag='+tag});
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

    // 确认分配课件
    function allotSubmitAction(){
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
            var ids = new Array();
            for (var i in rows) {
                ids.push(rows[i].id);
            }
            $.ajax({
                type: 'post',
                url: '${basePath}/manage/job/courseware/' + ids.join("-") + '/${jobId}',
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
                        $subtable.bootstrapTable('refresh');
                        //coursewareDialog.close();
                        //$table.bootstrapTable('refresh');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alertMsg(textStatus);
                }
            });
        }
    }
</script>
</body>
</html>
