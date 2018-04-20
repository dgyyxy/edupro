<%@ page contentType="text/html; charset=utf-8" %>
<div id="updatePaperDialog" class="crudDialog">
    <form id="updatePaperForm" method="post">
        <div class="form-group">
            <label for="name">试卷名称</label>
            <input type="text" id="name" name="name" value="${paper.name}" class="form-control" maxlength="20"/>
        </div>
        <div class="form-group">
            <select id="category" name="categoryId">
            </select>
        </div>
        <div class="form-group">
            <label for="total_point">总分</label>
            <input type="text" id="total_point" name="totalPoint" value="${paper.totalPoint}" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="pass_point">及格分</label>
            <input type="text" id="pass_point" name="passPoint" value="${paper.passPoint}" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="duration">时长(分钟)</label>
            <input type="text" id="duration" name="duration" value="${paper.duration}" maxlength="20" class="form-control"/>
        </div>

        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" id="submitBtn" href="javascript:;">下一步</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updatePaperDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>
    var paperupdatePaper = {
        // 初始化
        initial: function () {
            paperupdatePaper.initCategory();
            paperupdatePaper.submitForm();
        },
        // 提交表单
        submitForm: function(){
            $('#submitBtn').click(function(){
                if(!paperupdatePaper.verifyInput()){
                    return;
                }
                $.ajax({
                    type: 'post',
                    url: '${basePath}/manage/paper/updatePaper/${paper.id}',
                    data: paperupdatePaper.composeEntity(),
                    beforeSend: function() {
                    },
                    success: function(result) {
                        updatePaperDialog.close();
                        location.href = '${basePath}/manage/paper/update/${paper.id}';
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
            var category = $('#category').val();//试卷分类
            var totalPoint = $('#total_point').val();//总分
            var passPoint = $('#pass_point').val();//及格分
            var duration = $('#duration').val();//时长
            if(name == ''){
                alertMsg('请输入试卷名称！')
                return false;
            }
            if(category == 0){
                alertMsg('请选择试卷分类！');
                return false;
            }
            if(totalPoint==''){
                alertMsg('请输入总分！')
                return false;
            }else if(isNaN(parseInt(totalPoint)) || paperupdatePaper.checkFloat(totalPoint)=='float'){
                alertMsg('总分一定为正整数！')
                return false;
            }
            if(passPoint==''){
                alertMsg('请输入及格分数！')
                return false;
            }else if(isNaN(parseInt(passPoint)) || paperupdatePaper.checkFloat(passPoint)=='float'){
                alertMsg('及格分数一定为正整数！')
                return false;
            }

            if(parseInt(passPoint) > parseInt(totalPoint)){
                alertMsg('及格分应该小于总分！')
                return false;
            }

            return true;
        },
        // 表单提交数据
        composeEntity : function (){
            var entity = $('#updatePaperForm').serializeObject();
            return entity;
        },
        // 初始化试卷分类
        initCategory: function(){
            $.ajax({
                url: '${basePath}/manage/paper/category/list',
                type: 'get',
                data: {'offset':0, 'limit':100000},
                success: function(result){
                    var rows = result.rows;
                    var datas = [{id: 0, text: '请选择试卷分类'}];;
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
                            subData.id = _obj.id;
                            subData.text = _obj.name;
                            if(obj.id == _obj.pid){
                                data.children.push(subData);
                            }
                        }
                        datas.push(data);
                    }
                    $('#category').empty();
                    $('#category').select2({
                        width: 200,
                        data: datas
                    });

                    $('#category').select2('val', ['${paper.categoryId}']);
                }
            });
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
        paperupdatePaper.initial();
    });

</script>