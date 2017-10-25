<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="createDialog" class="crudDialog">
	<form id="createForm" method="post">
		<div class="radio">
			<div class="radio radio-inline radio-success">
				<input id="type_1" type="radio" name="type" value="1" checked>
				<label for="type_1">目录 </label>
			</div>
			<div class="radio radio-inline radio-info">
				<input id="type_2" type="radio" name="type" value="2">
				<label for="type_2">菜单 </label>
			</div>
			<div class="radio radio-inline radio-warning">
				<input id="type_3" type="radio" name="type" value="3">
				<label for="type_3">按钮 </label>
			</div>
		</div>
		<div class="form-group">
			<span class="type2 type3" hidden>
				<select id="pid" name="pid">
					<option value="0">请选择上级</option>
				</select>
			</span>
		</div>
		<div class="form-group">
			<label for="name">名称</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="20">
		</div>
		<div class="form-group type2 type3" hidden>
			<label for="permissionValue">权限值</label>
			<input id="permissionValue" type="text" class="form-control" name="permissionValue" maxlength="50">
		</div>
		<div class="form-group type2 type3" hidden>
			<label for="uri">路径</label>
			<input id="uri" type="text" class="form-control" name="uri" maxlength="100">
		</div>
		<div class="form-group type1 type3">
			<label for="icon">图标</label>
			<input id="icon" type="text" class="form-control" name="icon" maxlength="50" value="zmdi zmdi-widgets">
		</div>
		<div class="radio">
			<div class="radio radio-inline radio-success">
				<input id="status_1" type="radio" name="status" value="1" checked>
				<label for="status_1">正常 </label>
			</div>
			<div class="radio radio-inline">
				<input id="status_0" type="radio" name="status" value="0">
				<label for="status_0">锁定 </label>
			</div>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
var pidType = 0;
var type = 1;
$(function() {
	// 选择分类
	$('input:radio[name="type"]').change(function() {
		type = $(this).val();
		initType();
	});
});
function initType() {
	// 显示对应必填项
	$('.type1,.type2,.type3').hide(0, function () {
		$('.type' + type).show();
	});
	// 级联菜单
	if (type == 2) {
		pidType = 1;
		initPid();
	}
	if (type == 3) {
		pidType = 2
		initPid();
	}
}
function initPid() {
    $.getJSON('${basePath}/manage/permission/list', {type: pidType, limit: 10000}, function(json) {
        console.log(json);
        var datas = [{id: 0, text: '请选择上级'}];
        for (var i = 0; i < json.rows.length; i ++) {
            var data = {};
            data.id = json.rows[i].permissionId;
            data.text = json.rows[i].name;
            datas.push(data);
        }
        $('#pid').empty();
        $('#pid').select2({
            width: 230,
            data : datas
        });
    });
}
function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/permission/create',
        data: $('#createForm').serialize(),
        beforeSend: function() {
			if (type == 1) {
				if ($('#name').val() == '') {
					$('#name').focus();
                    alertMsg("请输入权限名称！");
					return false;
				}
			}
			if (type == 2 || type == 3) {
				if ($('#pid').val() == 0) {
                    alertMsg("请选择上级！");
					return false;
				}
				if ($('#name').val() == '') {
					$('#name').focus();
                    alertMsg("请输入权限名称！");
					return false;
				}
				if ($('#permissionValue').val() == '') {
					$('#permissionValue').focus();
                    alertMsg("请输入权限值！");
					return false;
				}
				if ($('#uri').val() == '') {
					$('#uri').focus();
                    alertMsg("请输入权限URI！");
					return false;
				}
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