<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="examScoreDialog" class="crudDialog">
    <form id="exportForm" method="post">
        <div class="form-group">
            <label for="className">班级名称</label>
            <input type="text" id="className" name="className"  class="form-control" maxlength="20"/>
        </div>
        <div class="form-group">
            <label for="companyName">航空公司名称</label>
            <input type="text" id="companyName" name="companyName"  class="form-control" maxlength="20"/>
            <input type="hidden" name="passRate" value="${passRate}"/>
            <input type="hidden" name="isAll" value="1"/>
        </div>
        <div class="form-group text-right dialog-buttons" id="btngroup">
            <a class="waves-effect waves-button" href="javascript:;" onclick="exportSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="examScoreDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>
    var exportSubmit = function() {
        var className = $('#className').val();
        var companyName = $('#companyName').val();
        if(className == ''){
            alertMsg('请输入班级名称!');
            return false;
        }
        if(companyName == ''){
            alertMsg('请输入航空公司名称!');
            return false;
        }
        var paramstr = '?'+$('#exportForm').serialize();
        location.href = '${basePath}/manage/exam-history/export/${id}'+paramstr;
        examScoreDialog.close();
    }
</script>