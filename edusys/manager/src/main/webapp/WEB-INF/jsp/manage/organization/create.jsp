<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<jsp:include page="../../common/taglib.jsp" flush="true"/>
<div id="createDialog" class="crudDialog">
	<form id="createForm" method="post">
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_1" type="radio" name="level" value="1" checked>
                <label for="type_1">一级机构 </label>
            </div>
            <div class="radio radio-inline radio-info">
                <input id="type_2" type="radio" name="level" value="2">
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
			<input id="name" type="text" class="form-control" name="name" maxlength="20">
		</div>
		<div class="form-group">
			<label for="description">描述</label>
			<input id="description" type="text" class="form-control" name="description" maxlength="300">
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>

$(function() {
    $('#pid').val('');
    // 选择分类
    $('input:radio[name="level"]').change(function() {
        var type = $(this).val();
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
        $('#pid').select2({
            placeholder: '请选择机构',
            width: 230
        });
    }
}

function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/organization/create',
        data: $('#createForm').serialize(),
        beforeSend: function() {
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
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: value.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
					});
				} else {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: result.data.errorMsg == undefined ? result.data : result.data.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
				}
			} else {
				createDialog.close();
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