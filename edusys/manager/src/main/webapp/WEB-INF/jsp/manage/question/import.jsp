<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="importDialog" class="crudDialog">
    <form id="importForm" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <select id="categoryId" name="questionCategoryId">

            </select>
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
            请先点击 <a href="${basePath}/resources/templete/question.xls" class="label label-primary">下载模板</a>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="importDialog.close();">取消</a>
        </div>
    </form>
</div>
<script type="text/javascript">

    $(function(){
        initQuestionCategory();
    });

    //加载试题分类
    function initQuestionCategory(){
        $.ajax({
            url: '${basePath}/manage/question/category/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择试题分类'}];;
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
                        subData.id = _obj.id;
                        subData.text = _obj.name;
                        if(obj.id == _obj.pid){
                            data.children.push(subData);
                        }
                    }
                    datas.push(data);
                }
                $('#categoryId').select2({
                    width: 200,
                    data: datas
                });
            }
        });
    }

    $('input[id=lefile]').change(function () {
        $('#photoCover').val($(this).val());
    });

    function createSubmit() {
        var loading;
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/question/import',
            processData: false,
            contentType: false,
            data: new FormData($('#importForm')[0]),
            beforeSend: function() {

                if ($('#categoryId').val() == 0) {
                    $('#categoryId').focus();
                    alertMsg("请选择试题分类！");
                    return false;
                }
                if ($('#photoCover').val() == '') {
                    alertMsg("请选择上传文件！");
                    return false;
                }
                this.data = new FormData($('#importForm')[0]);
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
