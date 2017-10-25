<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="importDialog" class="crudDialog">
    <form id="importForm" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <select id="pid" name="organizationId1">

            </select>
            <select id="subId" name="organizationId2">
                <option value="0">请选择上级</option>
            </select>
            <input type="hidden" name="organizationName1" id="organizationName1"/>
            <input type="hidden" name="organizationName2" id="organizationName2"/>
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
            请先点击 <a href="${basePath}/resources/templete/students.xls" class="label label-primary">下载模板</a>
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

        $('#subId').select2({width:150});

        $('#pid').change(function(){
            var pid = $(this).val();
            initSelect(pid, 'subId');
        });
    });

    function initSelect(pid, targetId) {
        $.getJSON('${basePath}/manage/organization/list', {pid: pid, limit: 10000}, function(json) {
            var datas = [];
            if(pid == 0) datas = [{id: 0, text: '请选择机构'}];
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
            url: '${basePath}/manage/student/import',
            processData: false,
            contentType: false,
            data: new FormData($('#importForm')[0]),
            beforeSend: function() {
                loading = $.dialog({
                    theme: 'white',
                    animation: 'rotateX',
                    closeAnimation: 'rotateX',
                    title: false,
                    closeIcon: false,
                    content: "正在处理，请稍等。。。"
                });
                if ($('#subId').val() == '0') {
                    $('#subId').focus();
                    loading.close();
                    alertMsg("请选择所属二级机构");
                    return false;
                }
                //获取选中的机构名称
                if($('#pid').val != '0')
                    $('#organizationName1').val($('#pid option:selected').text());
                if($('#subId').val != '0')
                    $('#organizationName2').val($('#subId option:selected').text());
                //this.data = $('#importForm').serialize();
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
                console.log(result);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
</script>
