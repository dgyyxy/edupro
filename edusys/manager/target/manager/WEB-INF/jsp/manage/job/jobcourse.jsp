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
        <%--<a class="waves-effect waves-button red" href="javascript:;" onclick="cancelAction()"><i class="zmdi zmdi-close"></i> 取消分配</a>--%>
        <div style="float: right;">
            <%--<select id="categorySelect" style="float:right;"></select>--%>
        </div>
    </div>
    <table id="courseList"></table>
</div>
<script>
    var $subtable = $('#courseList');
    $(function() {
        // 初始化课件类型列表
        //initCourseType();

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
            url: '${basePath}/manage/job/jobcourses/${jobId}',
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
//                {field: 'ck', checkbox: true},
                {field: 'id', title: '编号', sortable: true, align: 'center'},
                {field: 'courseware.name', title: '课件名称'},
                {field: 'courseware.category', title: '所属类别'},
                {field: 'courseware.time', title: '课时'},
                {field: 'sortNum', title: '排序编号'},
                {field: 'action', title: '操作', align: 'center', formatter: 'subactionFormatter', events: 'subactionEvents', clickToSelect: false}
            ]
        });

    });

    function initCourseType(){
        $.ajax({
            url: '${basePath}/manage/courseware/category/list',
            type: 'get',
            success: function(rows){
                var courseTypeList = rows.courseTypeList;
                var datas = [{id: 0,text:'请选择课件类别'}];
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
        if(typeId!=undefined) params.typeId = typeId;
        return params;
    }

    // 格式化操作按钮
    function subactionFormatter(value, row, index) {
        return [
            '<a class="edit ml10" href="javascript:up('+row.id+');" data-toggle="tooltip" title="Up"><i class="glyphicon glyphicon-arrow-up"></i></a>　',
            '<a class="remove ml10" href="javascript:down('+row.id+');" data-toggle="tooltip" title="Down"><i class="glyphicon glyphicon-arrow-down"></i></a>'
        ].join('');
    }

    // 排序向上
    function up(id){
        $.ajax({
            url: '${basePath}/manage/job/sort/up/'+id,
            type: 'get',
            success: function(result){
                $subtable.bootstrapTable('refresh');
            }
        });
    }

    // 排序向下
    function down(id){
        $.ajax({
            url: '${basePath}/manage/job/sort/down/'+id,
            type: 'get',
            success: function(result){
                $subtable.bootstrapTable('refresh');
            }
        });
    }

    // 删除
    var cancelDialog;
    function cancelAction() {
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
            cancelDialog = $.confirm({
                type: 'red',
                animationSpeed: 300,
                title: false,
                content: '确认取消分配吗？',
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
                                url: '${basePath}/manage/job/cancel/${jobId}/' + ids.join("-"),
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
                                        cancelDialog.close();
                                    }
                                    $subtable.bootstrapTable('refresh');
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
