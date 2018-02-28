<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="updateDialog" class="crudDialog">
    <form id="updateForm" method="post">
        <div class="form-group">
            <textarea id="content" class="form-control" name="content" placeholder="公告内容(只允许输入400以内字)">${notice.content}</textarea>
        </div>
        <div class="form-group">
            <select id="organizationId" name="organId">
            </select>
        </div>

        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>

    $(function(){
        initCourseType();
    });

    function initCourseType(){
        $.ajax({
            url: '${basePath}/manage/organization/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择公告机构'}];;
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
                $('#organizationId').select2({
                    width: 200,
                    data: datas
                });

                $('#organizationId').select2('val',[${notice.organId}]);
            }
        });
    }

    function updateSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/notice/update/${notice.id}',
            data: $('#updateForm').serialize(),
            beforeSend: function() {
                if ($('#content').val() == '') {
                    $('#content').focus();
                    alertMsg("请输入公告内容");
                    return false;
                }
            },
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
                    updateDialog.close();
                    $table.bootstrapTable('refresh');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }

</script>