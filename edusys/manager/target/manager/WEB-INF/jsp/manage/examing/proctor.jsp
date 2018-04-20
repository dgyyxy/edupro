<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="proctorDialog" class="crudDialog">
    <form id="proctorForm" method="post">
        <div class="form-group">
            <textarea id="proctor" class="form-control" name="proctor" placeholder="强制交卷原因(只允许输入400以内字)">${studentExam.proctor}</textarea>
        </div>
        <div class="form-group text-right dialog-buttons" id="btngroup">
            <a class="waves-effect waves-button" href="javascript:;" onclick="proctorSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="proctorDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>
    var proctorSubmit = function() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/examing/proctor/${studentExam.id}',
            data: $('#proctorForm').serialize(),
            success: function(result) {
                proctorDialog.close();
                $subtable.bootstrapTable('refresh');
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
</script>