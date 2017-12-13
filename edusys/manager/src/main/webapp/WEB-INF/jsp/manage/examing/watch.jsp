<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="watchDialog" class="crudDialog">
    <form id="watchForm" method="post">
        <div class="form-group">
            <label for="teacher">监考老师</label>
            <input type="text" id="teacher" name="teacher" value="${exam.teacher}" class="form-control" maxlength="20"/>
        </div>
        <div class="form-group">
            <textarea id="watch" class="form-control" name="watch" placeholder="监考情况(只允许输入400以内字)">${exam.watch}</textarea>
        </div>
        <div class="form-group text-right dialog-buttons" id="btngroup">
            <a class="waves-effect waves-button" href="javascript:;" onclick="watchSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="watchDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>
    var watchSubmit = function() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/examing/watch/${exam.id}',
            data: $('#watchForm').serialize(),
            success: function(result) {
                watchDialog.close();
                $subtable.bootstrapTable('refresh');
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
</script>