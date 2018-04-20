<%@ page contentType="text/html; charset=utf-8" %>
<div id="examOrganDialog" class="crudDialog">
    <form id="examOrganForm" method="post">
        <div class="form-group">
            <select id="organizationSelect" multiple="multiple" name="organizationIds">
            </select>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" id="submitBtn" href="javascript:;">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="examOrganDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>
    var examOrganCreate = {
        // 初始化
        initial: function () {
            examOrganCreate.submitForm();
            examOrganCreate.initOrganization();
        },
        // 提交表单
        submitForm: function(){
            $('#submitBtn').click(function(){
                var loading;
                $.ajax({
                    type: 'post',
                    url: '${basePath}/manage/exam/organ/${examId}/',
                    data: $('#examOrganForm').serialize(),
                    beforeSend: function() {
                        loading = $.dialog({
                            theme: 'white',
                            animation: 'rotateX',
                            closeAnimation: 'rotateX',
                            title: false,
                            closeIcon: false,
                            content: "正在处理，请稍等。。。"
                        });
                    },
                    success: function(result) {
                        loading.close();
                        if (result.code != 1) {
                            if (result.data instanceof Array) {
                                $.each(result.data, function(index, value) {
                                    alertMsg(value.errorMsg);
                                });
                            } else {
                                alertMsg(result.data);
                            }
                        } else {
                            examOrganDialog.close();
                            $table.bootstrapTable('refresh');
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alertMsg(textStatus);
                    }
                });
            });
        },
        // 初始组织机构
        initOrganization: function () {
            $.ajax({
                url: '${basePath}/manage/organization/list',
                type: 'get',
                data: {'offset':0, 'limit':100000},
                success: function(result){
                    var rows = result.rows;
                    var datas = [];
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
    };

    $(function () {
        examOrganCreate.initial();
    });
</script>