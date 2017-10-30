<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="importDialog" class="crudDialog">
    <form id="importForm" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <select id="pid" name="organizationId1">

            </select>
            <input type="hidden" name="organizationName1" id="organizationName1"/>
        </div>
        <div class="form-group row">
            <input id="lefile" type="file" name="importFile" style="display: none;"/>
            <div class="input-append">
                <input id="photoCover" class="input-large" type="text" style="height: 31px; width:200px; border-radius:5px; border:1px solid #CCCCCC; padding-left:10px;"/>
                <a class="btn" onclick="$('input[id=lefile]').click();" style ="background-color:#4CAF50; height:32px; color:#ffffff; width:100px;">请选择文件</a>
                <br/>
                <label style="color:red;">上传小于100M的文件！</label>
            </div>
        </div>
        <div class="form-group">
            请先点击 <a href="${basePath}/resources/templete/organ.xls" class="label label-primary">下载模板</a>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="importDialog.close();">取消</a>
        </div>
    </form>
</div>
<script type="text/javascript">

    $(function(){
        initSelect(0, 'pid');
    });

    function initSelect(pid, targetId) {
        $.getJSON('${basePath}/manage/organization/list', {pid: pid, limit: 10000}, function(json) {
            var datas = [];
            if(pid == 0) datas = [{id: 0, text: '请选择一级机构'}];
            for (var i = 0; i < json.rows.length; i ++) {
                var data = {};
                data.id = json.rows[i].organizationId;
                data.text = json.rows[i].name;
                datas.push(data);
            }
            $('#'+targetId).empty();
            $('#'+targetId).select2({
                width: 150,
                data : datas
            });
        });
    }

    $('input[id=lefile]').change(function () {
        $('#photoCover').val($(this).val());
    });

    function createSubmit() {
        var loading;
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/organization/import',
            processData: false,
            contentType: false,
            data: new FormData($('#importForm')[0]),
            beforeSend: function() {

                var pidval = $('#pid').select2('val');
                if (pidval == '0') {
                    alertMsg("请选择一级机构");
                    return false;
                }
                if ($('#photoCover').val()==''){
                    alertMsg("请选择上传文件！");
                    return false;
                }
                loading = $.dialog({
                    theme: 'white',
                    animation: 'rotateX',
                    closeAnimation: 'rotateX',
                    title: false,
                    closeIcon: false,
                    content: "正在处理，请稍等。。。"
                });
                this.data = new FormData($('#importForm')[0]);
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
                    importDialog.close();
                    $table.bootstrapTable('refresh');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
</script>
