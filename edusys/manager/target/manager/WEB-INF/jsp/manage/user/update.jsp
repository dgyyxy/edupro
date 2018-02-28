<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="updateDialog" class="crudDialog">
	<form id="updateForm" method="post">
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_0" type="radio" name="type" value="1" <c:if test="${user.type==1}">checked</c:if>>
                <label for="type_0">管理员 </label>
            </div>
            <div class="radio radio-inline">
                <input id="type_1" type="radio" name="type" value="2" <c:if test="${user.type==2}">checked</c:if>>
                <label for="type_1">老师 </label>
            </div>
        </div>
        <%--<div class="form-group">
			<span class="organizationSpan" hidden>
				<select id="organizationSelect" name="organizationId">
                </select>
			</span>
        </div>--%>
		<div class="form-group">
			<label for="username">帐号</label>
			<input id="username" type="text" class="form-control" name="username" maxlength="20" value="${user.username}">
		</div>
        <div class="form-group">
            <label for="password">密码</label>
            <input id="password" type="text" class="form-control" name="password" maxlength="20" value="${user.password}">
        </div>
		<div class="form-group">
			<label for="realname">姓名</label>
			<input id="realname" type="text" class="form-control" name="realname" maxlength="20" value="${user.realname}">
		</div>
		<div class="form-group">
			<label for="phone">电话</label>
			<input id="phone" type="text" class="form-control" name="phone" maxlength="20" value="${user.phone}">
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>

var organizationId = '${organizationId}';

$(function(){
    // 选择分类
    $('input:radio[name="type"]').change(function() {
        var type = $(this).val();
        //initOrganization(type);
    });

    initSelect();
});

function initSelect(){
    var type = $('input:radio[name="type"][checked]').val();
    //initOrganization(type)
}

function initOrganization(type){
    console.log(type);
    // 显示对应必填项
    $('#organizationSelect').empty();
    if(type == 2){
        $('.organizationSpan').show();
        $.getJSON('${basePath}/manage/organization/list', {pid: 0, limit: 10000}, function(json) {
            var datas = [];
            for (var i = 0; i < json.rows.length; i ++) {
                var data = {};
                data.id = json.rows[i].organizationId;
                data.text = json.rows[i].name;
                datas.push(data);
            }
            $('#organizationSelect').select2({
                width: 230,
                data : datas
            });
            $('#organizationSelect').select2('val', ['${organizationId}']);
        });
    }else{
        $('.organizationSpan').hide();
    }
}

function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/user/update/${user.userId}',
        data: $('#updateForm').serialize(),
        beforeSend: function() {
            if ($('#username').val() == '') {
                $('#username').focus();
                alertMsg("请输入用户名！");
                return false;
            }
            if ($('#password').val() == '' || $('#password').val().length < 5) {
                $('#password').focus();
                alertMsg("请输入大于5个字符的密码！");
                return false;
            }
            if ($('#phone').val() == '') {
                $('#phone').focus();
                alertMsg("请输入电话号码！");
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