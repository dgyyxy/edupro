﻿<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="updateDialog" class="crudDialog">
    <form id="updateForm" method="post">
        <div class="form-group">
            <label for="name">名称</label>
            <input id="name" type="text" class="form-control" value="${job.name}" name="name" maxlength="20">
        </div>
        <div class="form-group">
            <select id="organizationId" name="organizationId">
            </select>
            <input type="hidden" name="organization" id="organization"/>
        </div>
        <div class="form-group">
            <label for="time1">开始时间</label>
            <input id="time1" autocomplete="off" name="time1" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'time2\')||\'2020-10-01\'}'})"/>
            <input id="startTime" name="startTime" type="hidden"/>
        </div>
        <div class="form-group">
            <label for="time2">结束时间</label>
            <input id="time2" autocomplete="off" name="time2" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'time1\')}',maxDate:'2020-10-01'})"/>
            <input id="endTime" name="endTime" type="hidden"/>
        </div>

        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>

    $(function(){

        var startTime = new Date(${job.startTime}).Format("yyyy-MM-dd HH:mm:ss");
        var endTime = new Date(${job.endTime}).Format("yyyy-MM-dd HH:mm:ss");
        $('#time1').val(startTime);
        $('#time2').val(endTime);
        initCourseType();
    });

    function initCourseType(){
        $.ajax({
            url: '${basePath}/manage/organization/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择开课机构'}];;
                for(var i = 0;i<rows.length; i++){
                    var data = {};
                    var obj = rows[i];
                    if(obj.level == 2){
                        continue;
                    }
                    data.text = obj.name;
                    data.children = [];
                    for(var j = 0; j< rows.length; j++){
                        var subData = {};
                        var _obj = rows[j];
                        if(_obj.level == 1){
                            continue;
                        }
                        subData.id = _obj.organizationId;
                        subData.text = _obj.name;
                        if(obj.organizationId == _obj.parentId){
                            data.children.push(subData);
                        }
                    }
                    datas.push(data);
                }
                $('#organizationId').select2({
                    width: 200,
                    data: datas
                });

                $('#organizationId').select2('val', ['${job.organizationId}'])
            }
        });
    }

    function updateSubmit() {
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/job/update/${job.id}',
            data: $('#updateForm').serialize(),
            beforeSend: function() {
                if ($('#name').val() == '') {
                    $('#name').focus();
                    alertMsg("请输入名称");
                    return false;
                }
                if ($('#time1').val() == '') {
                    alertMsg("请输入开始时间");
                    return false;
                }
                if ($('#time2').val() == '') {
                    alertMsg("请输入结束时间");
                    return false;
                }
                if ($('#organizationId').val()!=0 && $('#organizationId').val()!=""){
                    $('#organization').val($('#organizationId option:selected').text());
                }
                // 处理日期
                var starTime = $('#time1').val();
                $('#startTime').val(Date.parse(new Date(starTime.replace(/-/g, "/"))));
                var endTime = $('#time2').val();
                $('#endTime').val(Date.parse(new Date(endTime.replace(/-/g, "/"))));
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