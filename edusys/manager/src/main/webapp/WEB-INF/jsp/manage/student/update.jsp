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
        <div class="form-group">
            <label for="stuName">姓名</label>
            <input id="stuName" type="text" class="form-control" value="${student.stuName}" name="stuName" maxlength="20">
        </div>
        <div class="form-group">
            <label for="stuNo">学号</label>
            <input id="stuNo" type="text" class="form-control" value="${student.stuNo}" name="stuNo" maxlength="20">
        </div>
        <div class="form-group">
            <label for="cardNo">身份证号1</label>
            <input id="cardNo" type="text" class="form-control" value="${student.cardNo}" name="cardNo" maxlength="20">
        </div>
        <div class="form-group">
            <label for="phone">手机号</label>
            <input id="phone" type="text" class="form-control" value="${student.phone}" name="phone" maxlength="20">
        </div>

        <div class="form-group">
            <select id="pid" name="organizationId1">

            </select>
            <select id="subId" name="organizationId2">
                <option value="0">请选择上级</option>
            </select>
            <input type="hidden" name="organizationName1" id="organizationName1" value="${student.organizationName1}"/>
            <input type="hidden" name="organizationName2" id="organizationName2" value="${student.organizationName2}"/>
        </div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>

    $(function(){

        initUpdateSelect(0, 'pid');

        initUpdateSelect(parseInt('${student.organizationId1}'), 'subId');

        $('#pid').on('select2:select', function(){
            var pid = $(this).val();
            initSelect(pid, 'subId');
        });

    });

    //菜单
    function initUpdateSelect(pid, targetId) {
        $.getJSON('${basePath}/manage/organization/list', {pid: pid, limit: 10000}, function(json) {
           var datas = [];
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
            if(pid == 0){
                $('#pid').select2('val', ['${student.organizationId1}'])
            }else{
                $('#subId').select2('val', ['${student.organizationId2}']);
            }
        });
    }

    function updateSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/student/update/${student.stuId}',
            data: $('#updateForm').serialize(),
            beforeSend: function() {
                if ($('#name').val() == '') {
                    $('#name').focus();
                    alertMsg("请输入学员姓名");
                    return false;
                }
                // if ($('#stuNo').val() == '') {
                //     $('#stuNo').focus();
                //     alertMsg("请输入学号");
                //     return false;
                // }
                if ($('#cardNo').val() == '') {
                    $('#cardNo').focus();
                    alertMsg("请输入身份证号");
                    return false;
                }
                if ($('#subId').val() == '') {
                    $('#subId').focus();
                    alertMsg("请选择所属二级机构");
                    return false;
                }
                //获取选中的机构名称
                $('#organizationName1').val($('#pid option:selected').text());
                $('#organizationName2').val($('#subId option:selected').text());
                this.data = $('#updateForm').serialize();
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