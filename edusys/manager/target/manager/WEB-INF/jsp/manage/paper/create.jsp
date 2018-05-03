<%@ page contentType="text/html; charset=utf-8" %>
<div id="createDialog" class="crudDialog">
    <form id="createForm" method="post">
        <div class="form-group">
            <label for="name">试卷名称</label>
            <input type="text" id="name" name="name" class="form-control" maxlength="20"/>
        </div>
        <div class="form-group">
            <select id="category" name="categoryId">
            </select>
        </div>
        <div class="form-group">
            <select name="paperType" id="type">
                <option value="0">请选择组卷方式</option>
                <option value="1">手动组卷</option>
                <option value="2">自动组卷</option>
            </select>
        </div>
        <div class="form-group">
            <label for="total_point">总分</label>
            <input type="text" id="total_point" name="totalPoint" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="pass_point">及格分</label>
            <input type="text" id="pass_point" name="passPoint" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="duration">时长(分钟)</label>
            <input type="text" id="duration" name="duration" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group questionCategoryDiv" hidden>
            <select id="questionCategory" multiple="multiple" name="questionCategoryIds">
            </select>
        </div>
        <fieldset id="questionType" class="fieldshow" hidden>
            <legend>选择题型分布</legend>
            <div class="form-group add-ques-type">
                <input type="hidden" class="ques-id" value="3">
                <div class="col-md-6">
                    <label for="question3">判断题数</label>
                    <input type="text" id="question3" class="form-control add-ques-amount"/>
                </div>
                <div class="col-md-6">
                    <label for="score3">每道题分值</label>
                    <input type="text" id="score3" class="form-control add-ques-score"/>
                </div>
            </div>
            <div class="form-group add-ques-type">
                <input type="hidden" class="ques-id" value="1">
                <div class="col-md-6">
                    <label for="question1">单选题数</label>
                    <input type="text" id="question1" class="form-control add-ques-amount"/>
                </div>
                <div class="col-md-6">
                    <label for="score1">每道题分值</label>
                    <input type="text" id="score1" class="form-control add-ques-score"/>
                </div>
            </div>
            <div class="form-group add-ques-type">
                <input type="hidden" class="ques-id" value="2">
                <div class="col-md-6">
                    <label for="question2">多选题数</label>
                    <input type="text" id="question2" class="form-control add-ques-amount"/>
                </div>
                <div class="col-md-6">
                    <label for="score2">每道题分值</label>
                    <input type="text" id="score2" class="form-control add-ques-score"/>
                </div>
            </div>
            <div class="form-group add-ques-type">
                <input type="hidden" class="ques-id" value="4">
                <div class="col-md-6">
                    <label for="question4">填空题数</label>
                    <input type="text" id="question4" class="form-control add-ques-amount"/>
                </div>
                <div class="col-md-6">
                    <label for="score4">每道题分值</label>
                    <input type="text" id="score4" class="form-control add-ques-score"/>
                </div>
            </div>
        </fieldset>
        <div class="form-group now_total_point" hidden>
            <label>当前总分</label>
            <input type="text" id="now_total_point" maxlength="20" class="form-control" readonly/>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" id="submitBtn" href="javascript:;">下一步</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>
    var type = 0;

    var paperCreate = {
        // 初始化
        initial: function () {
            paperCreate.typeChange();
            paperCreate.initCategory();
            paperCreate.bindChangeAmount();
            paperCreate.submitForm();
        },
        // 提交表单
        submitForm: function(){
            $('#submitBtn').click(function(){
                if(!paperCreate.verifyInput()){
                    return;
                }
                $.ajax({
                    type: 'post',
                    url: '${basePath}/manage/paper/create',
                    data: paperCreate.composeEntity(),
                    beforeSend: function() {
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
                            createDialog.close();
                            location.href = '${basePath}/manage/paper/update/'+result.data;
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alertMsg(textStatus);
                    }
                });
            });
        },
        bindChangeAmount : function(){
            $(".add-ques-amount").change(function(){
                paperCreate.calculateTotalPoints();
            });
            $(".add-ques-score").change(function(){
                paperCreate.calculateTotalPoints();
            });
        },
        // 验证试卷名称
        verifyInput: function () {
            var name = $('#name').val();//名称
            var category = $('#category').val();//试卷分类
            var typeId = $('#type').val();//组卷方式
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
            if(typeId == 0){
                alertMsg('请选择组卷方式！');
                return false;
            }
            if(totalPoint==''){
                alertMsg('请输入总分！')
                return false;
            }else if(isNaN(parseInt(totalPoint)) || paperCreate.checkFloat(totalPoint)=='float'){
                alertMsg('总分一定为正整数！')
                return false;
            }
            if(passPoint==''){
                alertMsg('请输入及格分数！')
                return false;
            }else if(isNaN(parseInt(passPoint)) || paperCreate.checkFloat(passPoint)=='float'){
                alertMsg('及格分数一定为正整数！')
                return false;
            }

            if(parseInt(passPoint) > parseInt(totalPoint)){
                alertMsg('及格分应该小于总分！')
                return false;
            }



            if(type == 2){
                var nowTotalPoint = $('#now_total_point').val();
                if(totalPoint != nowTotalPoint){
                    alertMsg('当前试卷总分'+nowTotalPoint+'与设定总分'+totalPoint+'分不一致，请修改各题分值！');
                    return false;
                }

                var questionCategories = $('#questionCategory option:selected');
                if(questionCategories.length==0 || questionCategories.length>5){
                    alertMsg('请选择题库分类并且不得超过5个！');
                    return false;
                }

            }
            return true;
        },
        calculateTotalPoints : function(){
            var qt = $(".add-ques-type");
            var amount = 0;

            for(var i = 0 ; i< qt.length;i++){
                var itemamount = parseInt($(qt[i]).find(".add-ques-amount").val());
                var itemscore = parseFloat($(qt[i]).find(".add-ques-score").val());

                if(isNaN(itemamount)||isNaN(itemscore)){
                    continue;
                }else{
                    amount = amount +  itemamount * itemscore * 10;
                }

            }

            $("#now_total_point").val(amount / 10);


        },
        // 表单提交数据
        composeEntity : function (){
            var entity = $('#createForm').serializeObject();

            var qt = $('.add-ques-type')
            // 获取题目题型数量
            var amountMap = new Object();
            // 获取题目题型分值
            var pointMap = new Object();
            for(var i=0;i<qt.length;i++){
                var itemamount = parseInt($(qt[i]).find('.add-ques-amount').val());
                var itemscore = parseInt($(qt[i]).find('.add-ques-score').val());
                var itemsid = $(qt[i]).find('.ques-id').val();
                if(isNaN(itemamount) || isNaN(itemscore)){
                    continue;
                }else{
                    amountMap[itemsid] = itemamount;
                    pointMap[itemsid] = itemscore;
                }
            }
            if(type == 2){
                entity.questionTypeNum = amountMap;
                entity.questionTypePoint = pointMap;
            }

            var questionCategories = $('#questionCategory option:selected');
            var rateMap = new Object();
            questionCategories.each(function(){
               rateMap[$(this).val()] = 0;
            });
            entity.questionTypeRate = rateMap;
            return entity;
        },
        // 选择自动组卷和手动组卷显示不同的操作
        typeChange: function () {
            $('#type').change(function () {
                type = $(this).val();

                if(type == 0){
                    $('#questionType').hide();
                    $('.questionCategoryDiv').hide();
                    $('#questionCategory').empty();//清空题库分类
                    $('.now_total_point').hide();
                }else if(type == 1){
                    $('#questionType').hide();
                    $('.questionCategoryDiv').hide();
                    $('.now_total_point').hide();
                    $('#questionCategory').empty();//清空题库分类
                }else if(type == 2){
                    $('#questionType').show();
                    $('.questionCategoryDiv').show();
                    $('.now_total_point').show();
                    paperCreate.initQuestionCategory();
                }
            });
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
                }
            });
        },
        // 初始化题库分类
        initQuestionCategory: function () {
            $.ajax({
                url: '${basePath}/manage/question/category/list',
                type: 'get',
                data: {'offset':0, 'limit':100000},
                success: function(result){
                    var rows = result.rows;
                    var datas = [];
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
                    $('#questionCategory').empty();
                    $('#questionCategory').select2({
                        width: 200,
                        allowClear: true,
                        placeholder: '请选择题库分类',
                        data: datas
                    });
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
        paperCreate.initial();
    });

</script>