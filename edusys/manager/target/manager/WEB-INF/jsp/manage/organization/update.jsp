<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="updateDialog" class="crudDialog">
	<form id="updateForm" method="post">
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_1" type="radio" name="level" value="1" <c:if test="${organization.level==1}">checked</c:if>>
                <label for="type_1">一级机构 </label>
            </div>
            <div class="radio radio-inline radio-info">
                <input id="type_2" type="radio" name="level" value="2" <c:if test="${organization.level==2}">checked</c:if>>
                <label for="type_2">二级机构 </label>
            </div>
        </div>
        <div class="form-group">
			<span class="type2" hidden>
				<select id="pid" name="parentId">
                    <c:forEach var="organization" items="${organizations}">
                        <option value="${organization.organizationId}">${organization.name}</option>
                    </c:forEach>
                </select>
			</span>
        </div>
		<div class="form-group">
			<label for="name">名称</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="20" value="${organization.name}">
		</div>
		<div class="form-group">
			<label for="description">描述</label>
			<input id="description" type="text" class="form-control" name="description" maxlength="300" value="${organization.description}">
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
var parentId = '${organization.parentId}';
var type;
$(function() {

    if(parentId == '0'){
        $('#pid').val('');
    }else{
        initType(2);
    }

    // 选择分类
    $('input:radio[name="level"]').change(function() {
        type = $(this).val();
        initType(type);
    });
});
function initType(type) {
    // 显示对应必填项
    if(type==1){
        $('.type2').hide();
        $('#pid').val('');
    }else if(type==2){
        $('.type2').show();
        $('#pid').val(parentId);
        $('#pid').select2({
            placeholder: '请选择所属一级机构',
            width: 230
        });
    }
}

function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/organization/update/${organization.organizationId}',
        data: $('#updateForm').serialize(),
        beforeSend: function() {
            if (type == 2){
                var organPid = $('#pid').val();
                if(organPid == 0 || organPid == null){
                    alertMsg("请选择所属一级机构！");
                    return false;
                }
            }
            if ($('#name').val() == '') {
                $('#name').focus();
                alertMsg("请输入机构名称！");
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
			$.confirm({
				theme: 'dark',
				animation: 'rotateX',
				closeAnimation: 'rotateX',
				title: false,
				content: textStatus,
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'waves-effect waves-button waves-light'
					}
				}
			});
        }
    });
}
</script>