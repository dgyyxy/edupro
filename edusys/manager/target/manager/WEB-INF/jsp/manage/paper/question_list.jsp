<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<div id="questionListDialog" class="crudDialog">
    <div class="form-group text-right dialog-buttons">
        <button id="add-list-to-exampaper" class="btn btn-success btn-sm">添加已选题</button>
        <button id="close-list" class="btn btn-danger btn-sm">取消</button>
    </div>
    <div id="searchbar">
        <div style="float: right;">
            <select id="difficultySelect">
                <option value="0">选择难度级别</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
            </select>
        </div>
        <div style="float: right;margin-left:5px;margin-right:5px;">
            <select id="typeSelect">
                <option value="0">选择题型</option>
            </select>
        </div>
        <div style="float: right;">
            <select id="categorySelect">
                <option value="0">选择分类</option>
            </select>
        </div>
    </div>
    <table id="questionListTable"></table>

</div>
<script>
    var $qltable = $('#questionListTable');
    $(function() {
        // bootstrap table初始化
        $qltable.bootstrapTable({
            url: '${basePath}/manage/question/list',
            height: getHeight(),
            striped: true,
            search: true,
            strictSearch: true,
            queryParams: 'queryParams',
            showRefresh: false,
            showColumns: false,
            minimumCountColumns: 2,
            clickToSelect: true,
            detailView: false,
            pagination: true,
            paginationLoop: false,
            sidePagination: 'server',
            silentSort: false,
            smartDisplay: false,
            escape: true,
            idField: 'id',
            maintainSelected: true,
            toolbar: '#searchbar',
            onLoadSuccess: function(){
                //数据加载完成后移除已被选中的题目
                delCheckQuestions();
            },
            columns: [
                {field: 'ck', checkbox: true},
                {field: 'id', title: '编号', sortable: true, align: 'center'},
                {field: 'name', title: '试题名称'},
                {field: 'typeName', title: '类型'},
                {field: 'categoryName', title: '所属分类'},
                {field: 'error_rate', title: '出错率', sortable: true,align: 'center', formatter: 'errorRateFormatter'},
                {field: 'qsum', title: '答题数', sortable: true, align: 'center'},
                {field: 'difficulty', title: '难度级别', align: 'center'}
            ]
        });

        $("#close-list").click(function(){
            addMoreQuestionsDialog.close();
        });

        initQuestionType();//初始化题型选项
        initQuestionCategory();//初始化题库分类选项

        $('#categorySelect').change(function(){
            $qltable.bootstrapTable('refresh');
        });

        $('#typeSelect').change(function(){
            $qltable.bootstrapTable('refresh');
        });

        $('#difficultySelect').change(function(){
            $qltable.bootstrapTable('refresh');
        });

        // 选择题目
        $("#add-list-to-exampaper").click(function () {
            var ids = new Array();
            var rows = $qltable.bootstrapTable('getSelections');
            if (rows.length == 0) {
                $.confirm({
                    title: false,
                    content: '请选择需要添加的试题!',
                    autoClose: 'cancel|3000',
                    backgroundDismiss: true,
                    buttons: {
                        cancel: {
                            text: '取消',
                            btnClass: 'waves-effect waves-button'
                        }
                    }
                });
            } else {
                for (var i in rows) {
                    ids.push(rows[i].id);
                }
                var data = {};
                data.idListStr = JSON.stringify(ids);
                $.ajax({
                    type: 'post',
                    url: basePath + '/manage/paper/batchAddQuestion',
                    data: data,
                    success: function(result) {
                        var questionList = result.rows;
                        for(var i=0;i<questionList.length;i++){
                            var question=questionList[i];
                            var deletehtml = "<a class=\"tmp-ques-remove\" title=\"删除此题\">删除</a>";
                            var newquestion = $('<div/>').html(question.content).contents();
                            newquestion.find(".question-title").append(deletehtml);
                            $("#exampaper-body").append(newquestion);
                        }
                        examing.refreshNavi();
                        examing.addNumber();
                        examing.updateSummery();

                        //addMoreQuestionsDialog.close();
                        $qltable.bootstrapTable('refresh');

                        var questions = $("li.question");
                        examing.scrollToElement($(questions[questions.length-1]));
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alertMsg(textStatus);
                    }
                });
            }
        });
    });

    //出错率
    function errorRateFormatter(value, row, index){
        return row.errorRate+'%';
    }

    // 搜索参数传递
    function queryParams(params){
        params.categoryId = $('#categorySelect').val();
        params.typeId = $('#typeSelect').val();
        params.difficulty = $('#difficultySelect').val();
        var questionIdList = $('#exampaper-body').find(".question-id");
        if(questionIdList.length>0){
            var idstr = '';
            var ids = $.map(questionIdList, function(item){
                idstr += '-'+parseInt($(item).text());
                return parseInt($(item).text());
            })
            params.idstr = idstr.substr(1);
        }
        return params;
    }

    // 删除已经被选中的题目行
    function delCheckQuestions(){
        var questionIdList = $('#exampaper-body').find(".question-id");
        if(questionIdList.length>0){
            var idstr = '';
            var ids = $.map(questionIdList, function(item){
                idstr += '-'+parseInt($(item).text());
                return parseInt($(item).text());
            })
            console.log(idstr.substr(1));
            $qltable.bootstrapTable('hideRow', {
                field: 'id',
                values: ids
            })
        }
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
                $('#typeSelect').empty();
                $('#typeSelect').select2({
                    width: 150,
                    data : datas
                });
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
                $('#categorySelect').empty();
                $('#categorySelect').select2({
                    width: 150,
                    data: datas
                });
            }
        });
    }
</script>