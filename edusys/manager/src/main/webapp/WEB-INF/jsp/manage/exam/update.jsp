<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="updDialog" class="crudDialog">
    <form id="updateForm" method="post">
        <div class="form-group">
            <label for="name">考试名称</label>
            <input type="text" id="name" value="${exam.examName}" name="examName" class="form-control" maxlength="50"/>
        </div>
        <div class="form-group questionShowDiv" hidden>
            <label for="pass_point">及格分</label>
            <input type="text" id="pass_point" name="passPoint" value="${exam.passPoint}" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="duration">时长(分钟)</label>

            <input type="text" id="duration" name="duration" value="${exam.duration}" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <fmt:formatDate var="startTime" pattern="yyyy-MM-dd HH:mm:ss" value="${exam.startTime}"/>
            <label for="time1">起始时间</label>
            <input id="time1" name="startTime" value="${startTime}" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'time2\')||\'2020-10-01\'}'})"/>
        </div>

        <div class="form-group">
            <fmt:formatDate var="endTime" pattern="yyyy-MM-dd HH:mm:ss" value="${exam.endTime}"/>
            <label for="time2">截止时间</label>
            <input id="time2" name="endTime" value="${endTime}" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'time1\')}',maxDate:'2020-10-01'})"/>
        </div>
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_0" type="radio" name="disorganize" value="0" <c:if test="${exam.disorganize==0}">checked</c:if> >
                <label for="type_0">打乱题目显示顺序 </label>
            </div>
            <div class="radio radio-inline">
                <input id="type_1" type="radio" name="disorganize" value="1" <c:if test="${exam.disorganize==1}">checked</c:if> >
                <label for="type_1">不打乱题目显示顺序 </label>
            </div>
        </div>
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="islook_0" type="radio" name="islook" value="0" <c:if test="${exam.islook==0}">checked</c:if> >
                <label for="type_0">不允许查看试卷 </label>
            </div>
            <div class="radio radio-inline">
                <input id="islook_1" type="radio" name="islook" value="1" <c:if test="${exam.islook==1}">checked</c:if> >
                <label for="type_1">允许查看试卷 </label>
            </div>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" id="submitBtn" href="javascript:;">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>
    var type = 0;

    var examCreate = {
        // 初始化
        initial: function () {
            examCreate.submitForm();
        },
        // 提交表单
        submitForm: function(){
            $('#submitBtn').click(function(){
                if(!examCreate.verifyInput()){
                    return;
                }
                $.ajax({
                    type: 'post',
                    url: '${basePath}/manage/exam/update/${exam.id}',
                    data: examCreate.composeEntity(),
                    beforeSend: function() {
                    },
                    success: function(result) {
                        updateDialog.close();
                        $table.bootstrapTable('refresh');
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alertMsg(textStatus);
                    }
                });
            });
        },
        // 验证试卷名称
        verifyInput: function () {
            var name = $('#name').val();//名称
            var passPoint = $('#pass_point').val();//及格分
            var duration = $('#duration').val();//时长
            var startTime = $('#time1').val();//起始时间
            var endTime = $('#time2').val();//截止时间

            if(name == ''){
                alertMsg('请输入试卷名称！')
                return false;
            }
            if(passPoint == '' || examCreate.checkFloat(passPoint) == 'string'){
                alertMsg('及格分只能为数字！');
                return false;
            }
            if(startTime == '' || endTime == ''){
                alertMsg('请输入考试起始时间和截止时间！');
                return false;
            }

            return true;
        },
        // 表单提交数据
        composeEntity : function (){
            var entity = $('#updateForm').serializeObject();
            return entity;
        },
        checkFloat : function (input) {
            var m = (/[\d]+(\.[\d]+)?/).exec(input);
            if (m) {
                // Check if there is a decimal place
                if (m[1]) { return 'float'; }
                else { return 'int'; }
            }
            return 'string';
        }
    };

    $(function () {
        examCreate.initial();
    });
</script>