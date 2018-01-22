<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="createDialog" class="crudDialog">
	<form id="createForm" method="post">
		<div class="form-group">
			<label for="stuName">姓名</label>
			<input id="stuName" type="text" class="form-control" name="stuName" maxlength="20">
		</div>
        <div class="form-group">
			<label for="stuNo">学号</label>
			<input id="stuNo" type="text" class="form-control" name="stuNo" maxlength="20">
		</div>
        <div class="form-group">
			<label for="password">密码</label>
			<input id="password" type="text" class="form-control" name="password" maxlength="20">
		</div>
        <div class="form-group">
			<label for="cardNo">身份证号</label>
			<input id="cardNo" type="text" class="form-control" name="cardNo" maxlength="20">
		</div>
        <div class="form-group">
			<label for="phone">手机号</label>
			<input id="phone" type="text" class="form-control" name="phone" maxlength="20">
		</div>

		<div class="form-group">
            <select id="pid" name="organizationId1">

            </select>
            <select id="subId" name="organizationId2">
                <option value="0">请选择上级</option>
            </select>
            <input type="hidden" name="organizationName1" id="organizationName1"/>
            <input type="hidden" name="organizationName2" id="organizationName2"/>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
		</div>

	</form>
</div>
<script>

$(function(){
    initSelect(0, 'pid');

    $('#subId').select2({width:150});

    $('#pid').on('select2:select', function(){
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

        if(pid == 0) {
            if(typePid==0) $('#pid').select2('val', [''+typeId+'']);
            else $('#pid').select2('val', [''+typePid+'']);
        }else{
            if(typePid==0) $('#subId').select2('val', ['0']);
            else $('#subId').select2('val', [''+typeId+'']);
        }
    });
}
function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/student/create',
        data: $('#createForm').serialize(),
        beforeSend: function() {
            if ($('#stuName').val() == '') {
                $('#stuName').focus();
                alertMsg("请输入学员姓名");
                return false;
            }
            if ($('#stuNo').val() == '') {
                $('#stuNo').focus();
                alertMsg("请输入学号");
                return false;
            }
            if ($('#passowrd').val() == '') {
                $('#password').focus()
                alertMsg("请输入密码");
                return false;
            }
            if ($('#cardNo').val() == '') {
                $('#cardNo').focus();
                alertMsg("请输入身份证号");
                return false;
            }
            if ($('#subId').val() == '0') {
                $('#subId').focus();
                alertMsg("请选择所属二级机构");
                return false;
            }
            //获取选中的机构名称
            $('#organizationName1').val($('#pid option:selected').text());
            $('#organizationName2').val($('#subId option:selected').text());
            this.data = $('#createForm').serialize();
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