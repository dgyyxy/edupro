<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="updateDialog" class="crudDialog">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="name">课件名称</label>
            <input id="name" type="text" class="form-control" name="name" value="${courseware.name}" maxlength="20">
        </div>
        <div class="form-group">
            <select id="pid" name="pid">
            </select>
            <select id="subId" name="subId">
                <option value="0">请选择课件类型</option>
            </select>
            <input name="categoryId" type="hidden" id="categoryId"/>
            <input name="category" type="hidden" id="category"/>
        </div>
        <div class="form-group">
            <input id="imgfile" name="img" type="file" style="display: none;"/>
            <div class="input-append">
                <input id="imgurl" name="picture" class="input-large fileinput" value="${courseware.picture}" type="text" readonly/>
                <a class="btn btn-success btn-sm" onclick="$('input[id=imgfile]').click();">选择课件图片</a>
                <span style="color:red;">上传图片小于50M!</span>
            </div>
        </div>
        <div class="form-group">
            <input id="coursefile" name="file" type="file" style="display: none;"/>
            <div class="input-append">
                <input id="course" name="uriStr" class="input-large fileinput" type="text" value="${courseware.uriStr}"/>
                <a class="btn btn-success btn-sm" onclick="$('input[id=coursefile]').click();">选择课件文件(MP4\软件包)</a>
                <span style="color:red;">上传文件小于100M!</span>
            </div>
        </div>

        <div class="form-group">
            <label for="time">播放时长</label>
            <input name="time" value="${courseware.time}" type="text" class="form-control input-mask" data-mask="00:00:00" maxlength="8" autocomplete="off">
        </div>
        <div class="form-group">
            <textarea id="content" name="content" class="form-control" rows="3" placeholder="课件简介">${courseware.content}</textarea>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>

    var courseTypeId = '${coursewareType.id}';
    var courseTypepId = '${coursewareType.pid}';

    $('input[id=imgfile]').change(function () {
        $('#imgurl').val($(this).val());
    });

    $('input[id=coursefile]').change(function () {
        $('#course').val($(this).val());
    });

    $(function(){
        initType(0, "pid");

        if(courseTypepId=='0'){
            initType(parseInt(courseTypeId), "subId");
        }else{
            initType(parseInt(courseTypepId), "subId");
        }

        $("#pid").change(function(){
            var pid = $(this).val();
            if(pid == 0){
                var datas = [{id: 0, text: '请选择上一级'}];
                $('#subId').empty();
                $('#subId').select2({
                    width: 150,
                    data : datas
                });
            }else{
                initType(pid, "subId");
            }
        });
    });

    function initType(pid, targetId){
        $.getJSON('${basePath}/manage/courseware/category/list', {pid: pid}, function(json) {
            var datas = [{id: 0, text: '请选择课件类型'}];
            for (var i = 0; i < json.courseTypeList.length; i ++) {
                var data = {};
                data.id = json.courseTypeList[i].id;
                data.text = json.courseTypeList[i].name;
                datas.push(data);
            }
            $('#'+targetId).empty();
            $('#'+targetId).select2({
                width: 150,
                data : datas
            });
            if(pid == 0) {
                if(courseTypepId=='0') $('#pid').select2('val', courseTypeId);
                else $('#pid').select2('val', courseTypepId);
            }else{
                if(courseTypepId=='0') $('#subId').select2('val', 0);
                else $('#subId').select2('val', courseTypeId);
            }

        });
    }

    function updateSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/courseware/update/${courseware.id}',
            processData: false,
            contentType: false,
            data: new FormData($('#updateForm')[0]),
            beforeSend: function() {
                if ($('#pid').val() == 0) {
                    $('#pid').focus();
                    alertMsg("请选择课件类型");
                    return false;
                }
                if($('#subId').val() == 0) {
                    $('#categoryId').val($('#pid').val());
                    $('#category').val($('#pid option:selected').text());
                }else{
                    $('#categoryId').val($('#subId').val());
                    $('#category').val($('#subId option:selected').text());
                }
                this.data = new FormData($('#updateForm')[0]);
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
                    $table.bootstrapTable('refresh', {url:'${basePath}/manage/courseware/list?typeId='+typeId});
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }
</script>
