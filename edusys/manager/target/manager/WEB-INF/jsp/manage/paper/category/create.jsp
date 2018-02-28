<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="createDialog" class="crudDialog">
	<form id="createForm" method="post">
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_1" type="radio" name="level" value="1" checked>
                <label for="type_1">一级分类 </label>
            </div>
            <div class="radio radio-inline radio-info">
                <input id="type_2" type="radio" name="level" value="2">
                <label for="type_2">二级分类 </label>
            </div>
        </div>
        <div class="form-group">
			<span class="type2" hidden>
				<select id="pid" name="pid">
                    <c:forEach var="item" items="${paperCategories}">
                        <option value="${item.id}">${item.name}</option>
                    </c:forEach>
                </select>
			</span>
        </div>
		<div class="form-group">
			<label for="name">名称</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="20">
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
        $.ajax({
            url: '${basePath}/manage/paper/category/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择一级分类'}];
                for(var i = 0;i<rows.length; i++) {
                    var data = {};
                    var obj = rows[i];
                    if (obj.level == 2) {
                        continue;
                    }
                    data.id = obj.id;
                    data.text = obj.name;
                    datas.push(data);
                }
                $('#pid').empty();
                $('#pid').select2({
                    width: 230,
                    data: datas
                });
            }
        });

    }
}

function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/paper/category/create',
        data: $('#createForm').serialize(),
        beforeSend: function() {
            if ($('#pid').val() == 0){
                alertMsg("请选择一级分类！");
                return false;
            }
            if ($('#name').val() == '') {
                $('#name').focus();
                alertMsg("请输入分类名称！");
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
                    alertMsg(result.data.errorMsg);
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