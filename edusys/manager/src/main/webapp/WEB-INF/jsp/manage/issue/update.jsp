<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="updateDialog" class="crudDialog">
    <form id="updateForm" method="post">
        <div class="form-group">
            <label for="question">请输入安全问题</label>
            <input type="text" id="question" name="question" value="${studentAnswer.question}" maxlength="20" class="form-control"/>
        </div>

        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>
    function updateSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/issues/update/${studentAnswer.id}',
            data: $('#updateForm').serialize(),
            beforeSend: function() {
                if ($('#question').val() == '') {
                    $('#question').focus();
                    alertMsg("请输入安全问题");
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