<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="exportDialog" class="crudDialog">
    <form id="exportForm" method="post">
        <div class="form-group">
            <select id="organId" name="organId">
            </select>
        </div>
        <div class="form-group">
            <select id="jobId" name="jobId">
            </select>
        </div>
        <div class="form-group text-right dialog-buttons" id="btngroup">
            <a class="waves-effect waves-button" href="javascript:;" onclick="exportSubmit();">导出</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="examScoreDialog.close();">取消</a>
        </div>

    </form>
</div>
<script>
    var exportSubmit = function() {
        var jobId = $('#jobId').val();
        var organId = $('#organId').val();
        if(organId == 0){
            alertMsg("请选择机构！");
            return;
        }
        if(jobId == 0){
            alertMsg("请输入学习任务！");
            return;
        }
        var paramstr = '?'+$('#exportForm').serialize();
        location.href='${basePath}/manage/record/exportdo'+paramstr;
        exportDialog.close();
    }

    $(function(){
        initCourseType();
        initJob();
    });

    function initCourseType(){
        $.ajax({
            url: '${basePath}/manage/organization/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择机构'}];;
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
                $('#organId').select2({
                    width: 200,
                    data: datas
                });
            }
        });
    }

    function initJob(){
        $.ajax({
            url: '${basePath}/manage/job/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请输入学习任务'}];
                for(var i = 0;i<rows.length; i++) {
                    var data = {};
                    var obj = rows[i];
                    data.id = obj.id;
                    data.text = obj.name;
                    datas.push(data);
                }
                $('#jobId').select2({
                    width: 230,
                    data: datas
                });
            }
        });
    }
</script>