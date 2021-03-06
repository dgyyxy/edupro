﻿<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="createDialog" class="crudDialog">
    <form id="createForm" method="post">
        <div class="form-group">
            <label for="question">请输入安全问题</label>
            <input type="text" id="question" name="question" maxlength="20" class="form-control"/>
        </div>

        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>

    function createSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/issues/create',
            data: $('#createForm').serialize(),
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
                    createDialog.close();
                    $table.bootstrapTable('refresh');
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }

</script>