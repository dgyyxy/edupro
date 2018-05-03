<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="updateDialog" class="crudDialog">
    <form id="updateForm" method="post">
        <div class="form-group">
            <select name="questionTypeId" id="questionTypeId">
            </select>
        </div>
        <div class="form-group">
            <select name="difficulty" id="difficulty">
                <option value="0">请选择题目难度级别</option>
                <option value="1" <c:if test="${question.difficulty==1}">selected</c:if>>1</option>
                <option value="2" <c:if test="${question.difficulty==2}">selected</c:if>>2</option>
                <option value="3" <c:if test="${question.difficulty==3}">selected</c:if>>3</option>
            </select>
        </div>
        <div class="form-group">
            <select id="categoryId" name="questionCategoryId">

            </select>
        </div>
        <div class="form-group">
            <textarea id="questionContent" name="questionContent" class="form-control col-sm-6" rows="3" placeholder="试题内容"></textarea>
        </div>
        <fieldset class="anserSelect" hidden>
            <legend>选项设置</legend>
            <div class="form-group row" id="answerList">
                <div class="col-md-6 answer">
                    <label for="answerA">A</label>
                    <input id="answerA" type="text" class="form-control" name="answerA" maxlength="20">
                </div>
                <div class="col-md-6 answer">
                    <label for="answerB">B</label>
                    <input id="answerB" type="text" class="form-control" name="answerB" maxlength="20">
                </div>
                <div class="col-md-6 answer">
                    <label for="answerC">C</label>
                    <input id="answerC" type="text" class="form-control" name="answerC" maxlength="20">
                </div>
                <div class="col-md-6 answer">
                    <label for="answerD">D</label>
                    <input id="answerD" type="text" class="form-control" name="answerD" maxlength="20">
                </div>
            </div>
        </fieldset>
        <div class="form-group anserSelect row" hidden>
            <div class="col-md-3" style="margin-top: 15px;">
                <a class="btn btn-success btn-xs waves-effect" href="javascript:;" onclick="addAction()"><i class="glyphicon glyphicon-plus icon-plus"></i></a>
                <a class="btn btn-danger btn-xs waves-effect" href="javascript:;" onclick="removeAction()"><i class="glyphicon glyphicon-minus icon-minus"></i></a>
            </div>
        </div>

        <div class="form-group">
            <div id="answerSelectDiv" hidden>
                <select name="type" id="answerRight">
                    <option value="0">选择正确答案</option>
                </select>
            </div>
            <div id="answerRightMuit" class="row" hidden>
                <span>正确答案：</span>
                <input type="checkbox" class="answerR" value="A"/>&nbsp;A&nbsp;&nbsp;
                <input type="checkbox" class="answerR" value="B"/>&nbsp;B&nbsp;&nbsp;
                <input type="checkbox" class="answerR" value="C"/>&nbsp;C&nbsp;&nbsp;
                <input type="checkbox" class="answerR" value="D"/>&nbsp;D&nbsp;&nbsp;
            </div>
            <div id="answerContent" hidden>
                <textarea id="questionRightAnswer" name="questionRightAnswer" class="form-control" rows="3" placeholder="参考答案"></textarea>
            </div>
        </div>
        <div class="form-group text-right dialog-buttons">
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateSubmit();">保存</a>
            <a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
        </div>
    </form>
</div>
<script>

    var typeId = 0;
    $(function() {

        initQuestionType();

        initQuestionCategory();

        // 选择题目类型
        $('#questionTypeId').change(function(){
            typeId = $(this).val();
            initQuestion();
        });

        initOptions();

    });

    function initOptions(){
        var content = '${question.content}';
        var json = eval('(' + content + ')');
        $('#questionContent').text(json.title);
        $('#answerList').empty();
        var choiceList = json.choiceList;
        $.each(choiceList, function(name, value){
            var htmlstr = '<div class="col-md-6 answer">'
                    +'<label for="answer'+name+'">'+name+'</label>'
                    +'<input id="answer'+name+'" type="text" class="form-control" name="answer" value="'+value+'" maxlength="20">'
                    +'</div>'
            $('#answerList').append(htmlstr);
        });

    }

    // 获取表单提交数据
    function getData(){
        var obj = $('#updateForm').serializeObject();
        if($("#questionContent").val().length>30)
            obj.name = $("#questionContent").val().substring(0, 30);
        else
            obj.name = $("#questionContent").val();

        if (1 == typeId) {
            obj.answer = $("#answerRight").val();
        } else if (2 == typeId) {
            var checkboxs = $("#answerRightMuit input:checked");
            var tmp_v = "";
            for (var i = 0; i < checkboxs.length; i++) {
                tmp_v = tmp_v + checkboxs[i].value;
            }
            obj.answer = tmp_v;

        } else if (3 == typeId) {
            obj.answer = $("#answerSelectDiv select").val();
        } else {
            obj.answer = $("#questionRightAnswer").val();
        }
        obj.content = JSON.stringify(composeContent());
        return obj;
    }

    function updateSubmit() {
        if(verifyInput()){
            $.ajax({
                type: 'post',
                url: '${basePath}/manage/question/update/${question.id}',
                data: getData(),
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
                        updateDialog.close();
                        $table.bootstrapTable('refresh');
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    alertMsg(textStatus);
                }
            });
        }
    }

    function composeContent() {
        var content = new Object();
        content.title = $("#questionContent").val();
        var choiceMap = {};
        console.log(typeId);
        if (1 == typeId) {
            var answers = $(".answer");
            console.log(answers);
            for (var i = 0; i < answers.length; i++) {
                var answer = $(answers[i]);
                //选项标签
                choiceMap[answer.children("label").text()] = answer.children("input").val();
            }

        } else if (2 == typeId) {
            var answers = $(".answer");
            console.log(answers);
            for (var i = 0; i < answers.length; i++) {
                var answer = $(answers[i]);
                //选项标签
                choiceMap[answer.children("label").text()] = answer.children("input").val();
            }
        }
        content.choiceList = choiceMap;

        return content;
    }


    //加载试题类型
    function initQuestionType(){
        $.ajax({
            url: '${basePath}/manage/question/type/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var datas = [{id: 0, text: '请选择题型'}];
                for (var i = 0; i < result.rows.length; i ++) {
                    var data = {};
                    data.id = result.rows[i].id;
                    data.text = result.rows[i].name;
                    datas.push(data);
                }
                $('#questionTypeId').empty();
                $('#questionTypeId').select2({
                    width: 200,
                    data : datas
                });

                $('#questionTypeId').select2('val', ['${question.questionTypeId}']);
            }
        });
    }
    //加载试题分类
    function initQuestionCategory(){
        $.ajax({
            url: '${basePath}/manage/question/category/list',
            type: 'get',
            data: {'offset':0, 'limit':100000},
            success: function(result){
                var rows = result.rows;
                var datas = [{id: 0, text: '请选择试题分类'}];;
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
                $('#categoryId').select2({
                    width: 200,
                    data: datas
                });

                $('#categoryId').select2('val', ['${question.questionCategoryId}']);
            }
        });
    }

    //检验输入合法性
    function verifyInput(){
        //题型
        var question_type = $('#questionTypeId').val();
        //难度级别
        var difficulty = $('#difficulty').val();
        //试题分类
        var category = $('#categoryId').val();
        //试题内容
        var questionContent = $('#questionContent').val();

        if(question_type == 0){
            alertMsg('请选择题型！');
            return false;
        }
        if(difficulty == 0){
            alertMsg('请选择题目难度级别！');
            return false;
        }
        if(category == 0){
            alertMsg('请选择试题分类！');
            return false;
        }
        if(questionContent == ''){
            alertMsg('请输入试题内容！');
            return false;
        }

        var answers = $('#answerList').find('.answer');
        if(1 == question_type || 2 == question_type){//单选题或多选题
            for (var i = 0; i < answers.length; i++) {
                var answer = $(answers[i]);
                //选项标签
                if(answer.children("input").val()==''){
                    alertMsg('请输入选项内容');
                    return false;
                }
            }
            if(1 == question_type){//单选
                var answerRight = $('#answerRight').select2('val');
                if(answerRight == 0){
                    alertMsg('请选择正确答案！');
                    return false;
                }
            }
            if(2 == question_type){//多选
                var answerRightMuit =$('#answerRightMuit input:checked');
                if(answerRightMuit.length==0){
                    alertMsg('请选择至少一个正确答案！');
                    return false;
                }

            }
        }

        if(3 == question_type){//判断题
            var answerRight = $('#answerRight').val();
            if(answerRight == ''){
                alertMsg('请选择正确答案！');
                return false;
            }
        }

        if(4 == question_type){//填空题
            var questionAnswer = $("#questionRightAnswer").val();
            if(questionAnswer == ''){
                alertMsg('请输入正确答案！');
                return false;
            }
        }

        return true;

    }


    function initQuestion() {
        if(typeId > 0){
            if(typeId == 1){
                $('.anserSelect').show();
                $('#answerSelectDiv').show();
                $('#answerRightMuit').hide();
                $('#answerContent').hide();
                initAnswer(0);
            }else if(typeId == 2){
                $('.anserSelect').show();
                $('#answerSelectDiv').hide();
                $('#answerRightMuit').show();
                $('#answerContent').hide();
                answerMuit(0);
            }else if(typeId == 3){
                $('.anserSelect').hide();
                $('#answerSelectDiv').show();
                $('#answerContent').hide();
                $('#answerRightMuit').hide();
                var datas = [{id: '', text: '选择正确答案'},{id: 'T', text: '正确'},{id: 'F', text: '错误'}];

                $('#answerRight').empty();
                $('#answerRight').select2({
                    data : datas,
                    width : 150
                });

                $('#answerRight').select2('val', ['${question.answer}'])

            }else if(typeId == 4){
                $('.anserSelect').hide();
                $('#answerRightMuit').hide();
                $('#answerSelectDiv').hide();
                $('#answerContent').show();
                $('#questionRightAnswer').val('${question.answer}');
            }
        }
    }

    // 初始化正确答案选项
    function initAnswer(optlen) {
        var datas = [{id: 0, text: '选择正确答案'}];
        if(optlen ==0 ) optlen = $('#answerList').find('.answer').length;
        for(var i = 0;i < optlen; i++){
            datas.push(String.fromCharCode(65 + i));
        }
        $('#answerRight').empty();
        $('#answerRight').select2({
            data : datas,
            width : 150
        });
        $('#answerRight').select2('val', ['${question.answer}'])
    }

    // 多选题正确答案
    function answerMuit(optlen){

        if(optlen == 0){
            optlen = $('#answerList').find('.answer').length;
        }
        $('#answerRightMuit').empty();
        var htmlstr = '<span>正确答案：</span>';
        for(var i=0;i<optlen;i++){
            var optval = String.fromCharCode(65 + i);
            var valstr = '${question.answer}';
            var checkedstr = '';
            if(valstr.indexOf(optval)!=-1)
                checkedstr = 'checked';
            htmlstr += '<input type="checkbox" class="answerR" value="'+optval+'" '+checkedstr+'/>&nbsp;'+String.fromCharCode(65 + i)+'&nbsp;&nbsp;';
        }
        $('#answerRightMuit').append(htmlstr);
    }

    // 移除选项
    function removeAction() {
        var optlen = $('#answerList').find('.answer').length;
        if(optlen <= 2){
            alertMsg('选项不得少于2个！')
            return;
        }
        var optval = String.fromCharCode(65 + optlen-1);
        $('#answer'+optval).parent().remove();
        if(typeId == 1) initAnswer(optlen-1);
        else if(typeId == 2) answerMuit(optlen-1);
    }

    function addAction(){
        var optlen = $('#answerList').find('.answer').length;
        if(optlen >= 6){
            alertMsg('选项不得超于6个！');
            return;
        }
        var optval = String.fromCharCode(65 + optlen);
        var htmlstr = '<div class="col-md-6 answer">'
                +'<label for="answer'+optval+'">'+optval+'</label>'
                +'<input id="answer'+optval+'" type="text" class="form-control" name="answer" maxlength="20">'
                +'</div>'
        $('#answerList').append(htmlstr);
        if(typeId == 1) initAnswer(optlen+1);
        else if(typeId == 2) answerMuit(optlen+1);
    }

    function initKnowledge() {
        if (majorId != 0) {
            $.getJSON('major.json', {}, function(json) {
                var datas = [{id: 0, text: '请选择上级'}];
                var knowledges = json[majorId - 1].knowledge;
                for (var i = 0; i < knowledges.length; i ++) {
                    var data = {};
                    data.id = knowledges[i].id;
                    data.text = knowledges[i].name;
                    datas.push(data);
                }
                $('#knowledgeId').empty();
                $('#knowledgeId').select2({
                    data : datas,
                    width : 230
                });
            });
        } else {
            $('#knowledgeId').empty();
            $('#knowledgeId').select2({
                data : [{id: 0, text: '请选择上级'}]
            });
        }
    }
</script>