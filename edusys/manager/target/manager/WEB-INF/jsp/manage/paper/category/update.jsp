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
                <input id="type_1" type="radio" name="level" value="1" <c:if test="${paperCategory.level==1}">checked</c:if>>
                <label for="type_1">一级分类 </label>
            </div>
            <div class="radio radio-inline radio-info">
                <input id="type_2" type="radio" name="level" value="2" <c:if test="${paperCategory.level==2}">checked</c:if>>
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
			<input id="name" type="text" class="form-control" name="name" maxlength="20" value="${paperCategory.name}">
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
var parentId = '${paperCategory.pid}';
$(function() {

    if(parentId == '0'){
        $('#pid').val('');
    }else{
        initType(2);
    }

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
        $('#pid').select2('val', 0);
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
                $('#pid').select2('val', parentId);
            }
        });

    }
}

function createSubmit() {
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/paper/category/update/${paperCategory.id}',
        data: $('#updateForm').serialize(),
        beforeSend: function() {
            if ($('#name').val() == '') {
                $('#name').focus();
                alertMsg("请输入试卷分类名称！");
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