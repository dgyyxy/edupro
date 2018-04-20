<%@ page contentType="text/html; charset=utf-8"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="submain" class="crudDialog">
    <div id="subtoolbar" style="float:left;">

    </div>
    <div style="float:left;margin-top:12px;margin-left:5px;">
        <a class="waves-effect waves-button green" href="javascript:;" onclick="choiceStuAction()"><i class="zmdi zmdi-accounts"></i> 选择参考学员</a>
        <select style="height: 30px;" id="organizationSelect" name="organizationId">
        </select>
    </div>
    <table id="subtable"></table>
</div>
<script>
    var $subtable = $('#subtable');
    $(function() {
        // bootstrap table初始化
        $subtable.bootstrapTable({
            url: '${basePath}/manage/exam/student/list/${examId}',
            height: getHeight(),
            striped: true,
            search: true,
            strictSearch: true,
            queryParams: 'queryParams',
            showRefresh: false,
            showColumns: false,
            minimumCountColumns: 2,
            clickToSelect: true,
            detailView: false,
            pagination: true,
            paginationLoop: false,
            sidePagination: 'server',
            silentSort: false,
            smartDisplay: false,
            escape: true,
            idField: 'stuId',
            maintainSelected: true,
            toolbar: '#subtoolbar',
            columns: [
                {field: 'ck', checkbox: true},
                {field: 'stuId', title: '编号', sortable: true, align: 'center'},
                {field: 'stuNo', title: '学号'},
                {field: 'stuName', title: '姓名'},
                {field: 'cardNo', title: '身份证号'},
                {field: 'phone', title: '手机号'},
                {field: 'organizationName2', title: '所属机构'}
            ]
        });


        $('#organizationSelect').change(function(){
            $subtable.bootstrapTable('refresh');
        });
    });

    function queryParams(params){
        var organId = $('#organizationSelect').val() == null ? 0 : $('#organizationSelect').val();
        params.organId = organId;
        return params;
    }

    //选择参考学员
    var choiceStuAction = function(){
        var rows = $subtable.bootstrapTable('getSelections');
        if(rows.length == 0){
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
        }else{
            var ids = new Array();
            for (var i in rows) {
                ids.push(rows[i].stuId);
            }
            $.ajax({
                type: 'get',
                url: '${basePath}/manage/exam/student/${examId}/' + ids.join("-"),
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
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alertMsg(textStatus);
                }
            });
        }
    }

    var initOrganization = function () {
        $.ajax({
            url: '${basePath}/manage/organization/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择考生机构'}];;
                for(var i = 0;i<rows.length; i++){
                    var data = {};
                    var obj = rows[i];
                    if(obj.level == 2){
                        continue;
                    }
                    data.text = obj.name;
                    data.children = [];
                    for(var j = 0; j< rows.length; j++){
                        var subData = {};
                        var _obj = rows[j];
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
                $('#organizationSelect').empty();
                $('#organizationSelect').select2({
                    width: 200,
                    allowClear: true,
                    placeholder: '请选择参考机构',
                    data: datas
                });
            }
        });
    }

    //初始化参考机构
    initOrganization();

</script>