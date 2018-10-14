<%@ page contentType="text/html; charset=utf-8" %>
<div id="createDialog" class="crudDialog">
    <form id="createForm" method="post">
        <div class="form-group">
            <label for="name">考试名称</label>
            <input type="text" id="name" name="examName" class="form-control" maxlength="50"/>
        </div>
        <div class="form-group">
            <select name="examType" id="type">
                <option value="0">请选择考卷设置方式</option>
                <option value="2">选择试卷</option>
                <option value="1">考题范围</option>
                <option value="3">题库选题</option>
            </select>
        </div>
        <div class="form-group questionShowDiv1" hidden>
            <label for="total_point">总分</label>
            <input type="text" id="total_point" name="totalPoint" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group questionShowDiv1" hidden>
            <label for="pass_point">及格分</label>
            <input type="text" id="pass_point" name="passPoint" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group">
            <label for="duration">时长(分钟)</label>
            <input type="text" id="duration" name="duration" maxlength="20" class="form-control"/>
        </div>
        <div class="form-group questionShowDiv" hidden>
            <select id="questionCategory" multiple="multiple" name="questionCategoryIds">
            </select>
        </div>
        <fieldset id="questionType" class="questionShowDiv" hidden>
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
        <div class="form-group questionShowDiv" hidden>
            <label>当前总分</label>
            <input type="text" id="now_total_point" maxlength="20" class="form-control" readonly/>
        </div>

        <div class="form-group paperSelectDiv" hidden>
            <select id="paperCategorySelect" name="paperCategory">
            </select>
            <select id="paperSelect" multiple="multiple" name="paperIds">
            </select>
        </div>
        <div class="form-group">
            <select id="organizationSelect" multiple="multiple" name="organizationIds">
            </select>
        </div>
        <div class="form-group">
            <label for="time1">起始时间</label>
            <input id="time1" autocomplete="off" name="startTime" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'time2\')||\'2020-10-01\'}'})"/>
        </div>

        <div class="form-group">
            <label for="time2">截止时间</label>
            <input id="time2" autocomplete="off" name="endTime" class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'time1\')}',maxDate:'2020-10-01'})"/>
        </div>
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="type_0" type="radio" name="disorganize" value="0" checked>
                <label for="type_0">打乱题目显示顺序 </label>
            </div>
            <div class="radio radio-inline">
                <input id="type_1" type="radio" name="disorganize" value="1">
                <label for="type_1">不打乱题目显示顺序 </label>
            </div>
        </div>
        <div class="radio">
            <div class="radio radio-inline radio-success">
                <input id="islook_0" type="radio" name="islook" value="0" checked>
                <label for="type_0">不允许查看试卷 </label>
            </div>
            <div class="radio radio-inline">
                <input id="islook_1" type="radio" name="islook" value="1">
                <label for="type_1">允许查看试卷 </label>
            </div>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" id="submitBtn" href="javascript:;">下一步</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="createDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>
    var type = 0;

    var examCreate = {
        // 初始化
        initial: function () {
            examCreate.typeChange();
            examCreate.bindChangeAmount();
            examCreate.submitForm();
            examCreate.initOrganization();
            examCreate.initPaperCategory();
        },
        // 提交表单
        submitForm: function(){
            $('#submitBtn').click(function(){

                if(!examCreate.verifyInput()){
                    return;
                }
                $.ajax({
                    type: 'post',
                    url: '${basePath}/manage/exam/create',
                    data: examCreate.composeEntity(),
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
                            if(result.data!='success'){
                                var examobj = result.data.exam;
                                examobj.startTime = new Date(examobj.startTime).Format('yyyy-MM-dd HH:mm:ss');
                                examobj.endTime = new Date(examobj.endTime).Format('yyyy-MM-dd HH:mm:ss');
                                var organizationIdstr = result.data.organizationIdstr;
                                console.log(organizationIdstr);
                                var paramstr = $.param(examobj)+'&organizationIdstr='+organizationIdstr;
                                location.href = '${basePath}/manage/exam/rule?'+paramstr;
                            }else{
                                createDialog.close();
                                $table.bootstrapTable('refresh');
                            }
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
                examCreate.calculateTotalPoints();
            });
            $(".add-ques-score").change(function(){
                examCreate.calculateTotalPoints();
            });
        },
        // 验证试卷名称
        verifyInput: function () {
            var name = $('#name').val();//名称
            var paperSelect = $('#paperSelect option:selected');//选择试卷
            var typeId = $('#type').val();//组卷方式
            var passPoint = $('#pass_point').val();//及格分
            var totalPoint = $('#total_point').val();//总分
            var nowTotalPoint = $('#now_total_point').val();
            var duration = $('#duration').val();//时长
            var startTime = $('#time1').val();//起始时间
            var endTime = $('#time2').val();//截止时间
            var organizations = $('#organizationSelect option:selected');//选择参考机构

            if(name == ''){
                alertMsg('请输入试卷名称！')
                return false;
            }
            if(typeId == 0){
                alertMsg('请选择考卷设置方式！');
                return false;
            }
            if(duration == ''){
                alertMsg('请输入考试时长！');
                return false;
            }
            if(startTime == '' || endTime == ''){
                alertMsg('请输入考试起始时间和截止时间！');
                return false;
            }

            // 题目组考卷
            if(typeId == 1) {
                if(totalPoint==''){
                    alertMsg('请输入总分！')
                    return false;
                }else if(isNaN(parseInt(totalPoint)) || examCreate.checkFloat(totalPoint)=='float'){
                    alertMsg('总分一定为正整数！')
                    return false;
                }

                if (passPoint == '') {
                    alertMsg('请输入及格分数！')
                    return false;
                } else if (isNaN(parseInt(passPoint)) || examCreate.checkFloat(passPoint) == 'float') {
                    alertMsg('及格分数一定为正整数！')
                    return false;
                }

                var questionCategories = $('#questionCategory option:selected');
                if (parseInt(passPoint) > parseInt(totalPoint)) {
                    alertMsg('及格分应该小于总分！')
                    return false;
                }
                if (questionCategories.length == 0 || questionCategories.length > 5) {
                    alertMsg('请选择题库分类并且不得超过5个！');
                    return false;
                }
                if(totalPoint != nowTotalPoint){
                    alertMsg('当前试卷总分'+nowTotalPoint+'与设定总分'+totalPoint+'分不一致，请修改各题分值！');
                    return false;
                }
            }else if(typeId == 2){
                if(paperSelect.length == 0){
                    alertMsg('请选择试卷！');
                    return false;
                }
            }else if(typeId == 3){
                if(totalPoint==''){
                    alertMsg('请输入总分！')
                    return false;
                }else if(isNaN(parseInt(totalPoint)) || examCreate.checkFloat(totalPoint)=='float'){
                    alertMsg('总分一定为正整数！')
                    return false;
                }

                if (passPoint == '') {
                    alertMsg('请输入及格分数！')
                    return false;
                } else if (isNaN(parseInt(passPoint)) || examCreate.checkFloat(passPoint) == 'float') {
                    alertMsg('及格分数一定为正整数！')
                    return false;
                }

                var questionCategories = $('#questionCategory option:selected');
                if (parseInt(passPoint) > parseInt(totalPoint)) {
                    alertMsg('及格分应该小于总分！')
                    return false;
                }
            }

            /*if(organizations.length==0){
                alertMsg('请选择参考机构！')
                return false;
            }*/
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
            $("#now_total_point").parent().find('label').addClass('active');

            $("#now_total_point").val(amount / 10);


        },
        // 表单提交数据
        composeEntity : function (){
            var entity = $('#createForm').serializeObject();
            entity.organizationIds = JSON.stringify(entity.organizationIds);
            entity.paperIds = JSON.stringify(entity.paperIds);
            if (type == 1) {
                var qt = $('.add-ques-type')
                // 获取题目题型数量
                var amountMap = new Object();
                // 获取题目题型分值
                var pointMap = new Object();
                for (var i = 0; i < qt.length; i++) {
                    var itemamount = parseInt($(qt[i]).find('.add-ques-amount').val());
                    var itemscore = parseInt($(qt[i]).find('.add-ques-score').val());
                    var itemsid = $(qt[i]).find('.ques-id').val();
                    if (isNaN(itemamount) || isNaN(itemscore)) {
                        continue;
                    } else {
                        amountMap[itemsid] = itemamount;
                        pointMap[itemsid] = itemscore;
                    }
                }
                entity.questionTypeNum = amountMap;
                entity.questionTypePoint = pointMap;

                var questionCategories = $('#questionCategory option:selected');
                var rateMap = new Object();
                questionCategories.each(function () {
                    rateMap[$(this).val()] = 0;
                });

                entity.questionTypeRate = rateMap;
            }
            return entity;
        },
        // 选择自动组卷和手动组卷显示不同的操作
        typeChange: function () {
            $('#type').change(function () {
                type = $(this).val();

                if(type == 0){
                    $('.questionShowDiv').hide();
                    $('.paperSelectDiv').hide();
                    $('.questionShowDiv1').hide();
                    $('#questionCategory').empty();
                }else if(type == 1){
                    $('.paperSelectDiv').hide();
                    $('.questionShowDiv').show();
                    $('.questionShowDiv1').show();
                    examCreate.initQuestionCategory();
                    $('#paperSelect').empty();//清空试卷
                }else if(type == 2){
                    $('.paperSelectDiv').show();
                    $('.questionShowDiv').hide();
                    $('.questionShowDiv1').hide();
                    var typeId = $('#paperCategorySelect').select2('val');
                    examCreate.initPaper(typeId);
                }else if(type == 3){
                    $('.questionShowDiv1').show();
                    $('.questionShowDiv').hide();
                    $('.paperSelectDiv').hide();
                    $('#questionCategory').empty();
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
                        width: 300,
                        allowClear: true,
                        placeholder: '请选择题库分类',
                        data: datas
                    });
                }
            });
        },
        // 试卷列表
        initPaper: function(typeId){
            $.getJSON('${basePath}/manage/paper/list', {limit: 10000, status: 1, typeId: typeId}, function(json) {
                var datas = [];
                for (var i = 0; i < json.rows.length; i ++) {
                    var data = {};
                    data.id = json.rows[i].id;
                    data.text = json.rows[i].name;
                    datas.push(data);
                }
                $('#paperSelect').empty();
                $('#paperSelect').select2({
                    width: 200,
                    placeholder: '请选择试卷',
                    data : datas
                });
            });
        },
        // 初始组织机构
        initOrganization: function () {
            $.ajax({
                url: '${basePath}/manage/organization/list',
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
                            subData.id = _obj.organizationId;
                            subData.text = _obj.name;
                            if(obj.organizationId == _obj.parentId){
                                data.children.push(subData);
                            }
                        }
                        datas.push(data);
                    }
                    $('#organizationSelect').empty();
                    $('#organizationSelect').select2({
                        width: 300,
                        allowClear: true,
                        placeholder: '请选择参考机构',
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
        },
        initPaperCategory: function(){
            $.ajax({
                url: '${basePath}/manage/paper/category/list',
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
                    $('#paperCategorySelect').empty();
                    $('#paperCategorySelect').select2({
                        width: 200,
                        placeholder: '请选择试卷分类',
                        data: datas
                    });
                }
            });
        }
    };

    $(function () {
        examCreate.initial();

        $('#paperCategorySelect').change(function(){
            examCreate.initPaper($(this).val());
        });
    });

</script>