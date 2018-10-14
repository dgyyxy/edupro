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

        <div class="form-group">
            <label for="name">课件名称</label>
            <input id="name" type="text" class="form-control" name="name" maxlength="20">
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
            <input id="imgfile" name="img" type="file"/>
            <input type="hidden" id="picture" name="picture"/>
            <%--<span style="color:red;float:left;">上传图片小于50M!</span>--%>
        </div>
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_0" type="radio" name="type" value="1" checked>
                <label for="type_0">MP4视频或PDF文件上传 </label>
            </div>
            <div class="radio radio-inline">
                <input id="type_1" type="radio" name="type" value="2">
                <label for="type_1">课件包上传 </label>
            </div>
        </div>
        <div class="form-group" id="mp4">
            <input id="coursefile" name="file" type="file"/>
            <input type="hidden" id="mp4url"/>
            <%--<span style="color:red;float:left;">上传文件小于100M!</span>--%>
        </div>
        <div class="form-group" id="courseZip" hidden>
            <input id="zipfile" name="file" type="file"/>
            <%--<span style="color:red;float:left;">上传文件小于100M!</span>--%>
            <%--<input id="softurl" type="hidden" class="form-control">--%>
            <input id="uriStr" name="uriStr" type="hidden" class="form-control">
        </div>
        <div class="form-group" id="softDiv" hidden>
            <label for="softurl">请输入课件包地址</label>
            <input id="softurl"type="text" class="form-control input-mask">
        </div>
        <div class="form-group">
            <label for="time">播放时长(分钟)</label>
            <input id="time" name="time" type="text" class="form-control input-mask" data-mask="00:00:00" maxlength="8" autocomplete="off">
        </div>
        <div class="form-group">
            <textarea id="content" name="content" class="form-control" rows="3" placeholder="课件简介"></textarea>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>

    $(function(){

        initType(0, "pid");

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

        fileUpload('imgfile', '选择上传课件图片', '50MB', "请选择jpg|png|gif图片文件", '*.jpg;*.png;*.gif', 'picture');
        fileUpload('coursefile', '选择上传MP4或PDF文件', '500MB', '请选择mp4视频文件或PDF文件', '*.mp4;*.pdf', 'mp4url');
        fileUpload('zipfile', '选择上传zip课件压缩包', '500MB', '请选择上传zip课件压缩包', '*.zip', 'softurl');

        // 选择分类
        $('input:radio[name="type"]').change(function() {
            var type = $(this).val();
            if(type == 1){
                $('#mp4').show();
                $('#courseZip').hide();
                $('#soft').hide();
                $('#softDiv').hide();
            }else if(type == 2){
                $('#mp4').hide();
                $('#courseZip').show();
                $('#soft').show();
                $('#softDiv').show();
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
                if(typePid==0) $('#pid').select2('val', [''+typeId+'']);
                else $('#pid').select2('val', [''+typePid+'']);
            }else{
                if(typePid==0) $('#subId').select2('val', ['0']);
                else $('#subId').select2('val', [''+typeId+'']);
            }
        });
    }

    function createSubmit() {
        var loading;
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/courseware/create',
            processData: false,
            contentType: false,
            data: new FormData($('#createForm')[0]),
            beforeSend: function() {

                if ($('#name').val() == ''){
                    alertMsg('请输入课件名称！');
                    return false;
                }
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
                var typeval = $('input:radio[name="type"]:checked').val();

                var urlstrVal = '';

                if(typeval == 1) {
                    if ($('#mp4url').val() == '') {
                        alertMsg('上传Mp4课件文件！');
                        return false;
                    }
                    urlstrVal = $('#mp4url').val();
                }else if(typeval == 2) {

                    if ($('#softurl').val() == '') {
                        alertMsg('请上传课件包或者输入课件包地址！');
                        return false;
                    }
                    urlstrVal = $('#softurl').val();
                }
                $('#uriStr').val(urlstrVal);
                var timeval = $('#time').val();
                if(timeval == '' || isNaN(timeval)){
                    alertMsg("输入的课件时长,只能为数字！");
                    return false;
                }

                if(parseInt(timeval)==0){
                    alertMsg("输入的课件时长不能为0");
                    return false;
                }
                if($('#picture').val() == ''){
                    alertMsg('请上传课件图片！');
                    return false;
                }


                this.data = new FormData($('#createForm')[0]);
                console.log(this.data);
                loading = $.dialog({
                    theme: 'white',
                    animation: 'rotateX',
                    closeAnimation: 'rotateX',
                    title: false,
                    closeIcon: false,
                    content: "正在处理，请稍等。。。"
                });
            },
            success: function(result) {
                loading.close();
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
                    $table.bootstrapTable('refresh', {url:'${basePath}/manage/courseware/list?typeId='+typeId});
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }

    function fileUpload(targetId, textstr, fileSize, fileTypeDesc, fileTypeExts, formval) {
        $("#"+targetId).uploadify({
            "uploader": "${basePath}/manage/upload",
            "method": "post",
            "progressData": "percentage",
            "swf": "${basePath}/resources/plugins/uploadify/uploadify.swf",
            "buttonText": textstr,
            "width": 180,
            "multi": true,
            "auto":true,
            "fileTypeDesc":fileTypeDesc,
            "fileTypeExts":fileTypeExts,
            "fileSizeLimit": fileSize,
            "preventCaching": true,
            "progressData": "percentage",
            "removeCompleted": false,
            "queueSizeLimit": 1,
            "uploadLimit": 1,
            "successTimeout": 600,
            "onUploadSuccess": function(file, data, response) {
                /*alert('The file ' + file.name
                        + ' was successfully uploaded with a response of '
                        + response + ':' + data);*/
                data = JSON.parse(data);

                $('#'+formval).val(data.newName);
                var cancel=$('#'+targetId+'-queue .uploadify-queue-item[id="' + file.id + '"]').find(".cancel a");
                if (cancel) {
                    $(cancel).attr("deletefileid",file.id);
                    cancel.click(function () {
                        //我的处理逻辑
                        //1.首先调用ajax 传递文件名到后台,后台删除对应的文件(这个我就不写了)
                        //2.从后台返回的为true,表明删除成功;返回false,表明删除失败
                        $.ajax({
                            type: 'post',
                            url: '${basePath}/manage/del-file',
                            dataType: 'json',
                            data: {filename: data.newName},
                            success: function(result){
                               if(result.result=="success"){
                                   var deletefileid = cancel.attr("deletefileid");
                                   $("#"+targetId).uploadify("cancel",deletefileid);//将上传队列中的文件删除.
                                   var swfuploadify = $("#"+targetId).data("uploadify");
                                   $("#"+targetId).uploadify('settings', 'uploadLimit', swfuploadify.settings.uploadLimit - 1);//删除最大上传文件数量
                                   $('#'+formval).val("");
                               }
                            }
                        });

                    });
                }
            },
            "onUploadError": function(file, errorCode, errorMsg,
                                      errorString) {
                switch(errorCode) {
                    case -100:
                        //alertMsg("上传的文件数量已经超出系统限制的"+$('#'+targetId).uploadify('settings','queueSizeLimit')+"个文件！");
                        break;
                    case -110:
                        alertMsg("文件 ["+file.name+"] 大小超出系统限制的"+$('#'+targetId).uploadify('settings','fileSizeLimit')+"大小！");
                        break;
                    case -120:
                        alertMsg("文件 ["+file.name+"] 大小异常！");
                        break;
                    case -130:
                        alertMsg("文件 ["+file.name+"] 类型不正确！");
                        break;
                }
            },
            "onSelectError": function(file, errorCode, errorMsg) {
                var msgText = "";
                switch (errorCode) {
                    case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                        msgText += "请先删除已上传的文件再进行上传操作！";
                        break;
                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                        msgText += "文件大小超过限制( " + $('#'+targetId).uploadify('settings','fileSizeLimit') + " )";
                        break;
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                        msgText += "文件大小为0";
                        break;
                    case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                        msgText += "文件格式不正确，仅限 " + $('#'+targetId).uploadify('settings','fileTypeExts');
                        break;
                    default:
                        msgText += "错误代码：" + errorCode + "\n" + errorMsg;
                }
                alertMsg(msgText);
            },
            "onCancel": function(event, queueId, fileObj,data){
                alertMsg("取消---"+data);
            }
        });

    }


</script>
