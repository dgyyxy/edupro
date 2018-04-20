<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="createTypeDialog" class="crudDialog">
    <form id="createTypeForm" method="post">
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_1" type="radio" name="level" value="1" checked>
                <label for="type_1">一级课件类别 </label>
            </div>
            <div class="radio radio-inline radio-info">
                <input id="type_2" type="radio" name="level" value="2">
                <label for="type_2">二级课件类别 </label>
            </div>
        </div>
        <div class="form-group">
			<span class="type2" hidden>
				<select id="pid" name="pid">
                </select>
			</span>
        </div>
        <br/>
        <div class="form-group">
            <label for="name">课件类别名称</label>
            <input id="name" type="text" class="form-control" name="name" maxlength="20">
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="createTypeSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="createTypeDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>

    var pidType = 0;
    var type = 1;
    $(function() {
        // 选择分类
        $('input:radio[name="level"]').change(function() {
            type = $(this).val();
            initType();
        });
    });
    function initType() {
        // 显示对应必填项
        $('.type1,.type2').hide(0, function () {
            $.getJSON('${basePath}/manage/courseware/category/list', {pid: 0}, function(json) {
                var datas = [{id: 0, text: '请选择课件类型'}];
                for (var i = 0; i < json.courseTypeList.length; i ++) {
                    var data = {};
                    data.id = json.courseTypeList[i].id;
                    data.text = json.courseTypeList[i].name;
                    datas.push(data);
                }
                $('.type' + type).show();
                $('#pid').empty();
                $('#pid').select2({
                    width: 230,
                    data : datas
                });
            });
        });
    }


    function createTypeSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/courseware/category/create',
            data: $('#createTypeForm').serialize(),
            beforeSend: function() {
                if ($('#name').val() == '') {
                    $('#name').focus();
                    alertMsg("请输入课件类型名称！");
                    return false;
                }
                if (type == 2) {
                    if ($('#pid').val() == 0) {
                        alertMsg("请选择上级！");
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
                    createTypeDialog.close();
                    initTree();
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
</script>
