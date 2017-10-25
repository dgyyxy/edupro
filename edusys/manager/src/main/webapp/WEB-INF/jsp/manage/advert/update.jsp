﻿<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<jsp:include page="../../common/taglib.jsp" flush="true"/>
<div id="updateDialog" class="crudDialog">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <input id="imgfile" name="img" type="file" style="display: none;"/>
            <div class="input-append">
                <input id="imgurl" value="${advert.imgurl}" class="input-large fileinput" type="text" readonly/>
                <a class="btn btn-success btn-sm" onclick="$('input[id=imgfile]').click();">选择图片</a>
            </div>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>

    $('input[id=imgfile]').change(function () {
        $('#imgurl').val($(this).val());
    });

    function updateSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/advert/update/${advert.id}',
            processData: false,
            contentType: false,
            data: new FormData($('#updateForm')[0]),
            beforeSend: function() {
                if ($('#imgurl').val() == '') {
                    $('#imgurl').focus();
                    alertMsg("请选择图片！");
                    return false;
                }
            },
            success: function(result) {
                if (result.code != 1) {
                    if (result.data instanceof Array) {
                        alertMsg(value.errorMsg);
                    } else {
                        alertMsg(result.data.errorMsg);
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