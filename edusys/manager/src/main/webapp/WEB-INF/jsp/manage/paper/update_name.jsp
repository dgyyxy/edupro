<%@ page contentType="text/html; charset=utf-8" %>
<div id="updateNameDialog" class="crudDialog">
    <form id="updateNameForm" method="post">
        <div class="form-group">
            <label for="paperName">试卷名称</label>
            <input name="paperName" class="form-control" id="paperName" type="text"/>
        </div>
        <div class="form-line">

        </div>
        <div class="form-group text-right dialog-buttons">
            <span class="btn btn-sm btn-success btn-icon-text waves-effect" id="update-name-btn">
                保存
            </span>
        </div>
    </form>
</div>
<script>
    $(function(){
        $('#update-name-btn').click(function(){
            var paperName = $('#paperName').val();
            if(paperName==''){
                alertMsg('请输入试卷名称！');
                return;
            }
            $('#exampaper-title-name').text(paperName);
            updateNameDialog.close();
        });
    });
</script>