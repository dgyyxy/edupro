<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>考试规则制定</title>
    <jsp:include page="../../common/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
    <div class="row">
        <div class="col-md-9">
            <span style="font-size: 14px;font-weight: bold;">考试名称：${exam.examName}，总分：${exam.totalPoint}，当前总分：<label id="currentPoint">0</label></span>
        </div>
        <div class="col-md-3 text-right">
            <a class="waves-effect waves-button green" href="javascript:;" onclick="sumbitFun()"><i class="zmdi zmdi-check"></i> 保存设置</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        题库分类
                    </h3>
                </div>
                <div class="panel-body">
                    <div id="tree"></div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <form id="createForm" method="post">
            <input type="hidden" name="examName" value="${exam.examName}"/>
            <input type="hidden" name="totalPoint" value="${exam.totalPoint}"/>
            <input type="hidden" name="examType" value="3"/>
            <input type="hidden" name="examRule" value="rule"/>
            <input type="hidden" name="passPoint" value="${exam.passPoint}"/>
            <input type="hidden" name="duration" value="${exam.duration}"/>
            <input type="hidden" name="startTime" value="${exam.startTime}"/>
            <input type="hidden" name="endTime" value="${exam.endTime}"/>
            <input type="hidden" name="disorganize" value="${exam.disorganize}"/>
            <input type="hidden" name="islook" value="${exam.islook}"/>
            </form>
            <fieldset id="questionType" class="questionShowDiv" hidden>
                <legend id="showType">选择题型分布</legend>
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
        </div>
    </div>

</div>
<jsp:include page="../../common/js.jsp" flush="true"/>
<script src="${basePath}/resources/plugins/bootstrap-treeview.js"></script>
<script src="${basePath}/resources/js/Map.js"></script>
<script>
    var typeId = 0;//类型Id
    var typePid = 0;//父ID
    var typeName = '';

    var totalAmount = 0;//当前总分

    var preAmount = 0;

    var entity = new Object();
    entity.questionTypeRates = new Array();

    var showList = new Array();


    var questionMap = new Map();

    var operate = 0;//是否针对每一个分类题型分布进行保存，0：没确认，1：确认保存

    // 题库分类
    function initTree() {
        $.getJSON('${basePath}/manage/question/category/list', {limit: 100000}, function(json) {
            var datas = [];
            var list = json.rows;
            for(var i = 0; i< list.length; i++){
                var node = {}
                node.id = list[i].id;
                node.pid = list[i].pid;
                var nodeName = list[i].name;
                if(nodeName.length > 6) nodeName = nodeName.substring(0,5)+'...';
                node.text =  nodeName;

                node.level = list[i].level;
                if(node.level == 2) continue;
                node.nodes = [];
                var subList = json.rows;
                for(var j = 0; j< subList.length; j++){

                    var subNode = {};
                    subNode.id = subList[j].id;
                    subNode.pid = subList[j].pid;
                    var subName = subList[j].name;
                    if(subName.length>11) subName = subName.substring(0,10)+'...';
                    subNode.text = subName;

                    subNode.level = subList[j].level;
                    if(subNode.level == 1) continue;
                    if(subNode.pid == node.id) node.nodes.push(subNode);
                }
                datas.push(node);
            }
            var nodeId;
            var myTree = $('#tree').treeview({
                data: datas,
                selectedBackColor: '#29A176',
                emptyIcon: 'glyphicon glyphicon-file',
                expandIcon: 'glyphicon glyphicon-folder-close',
                collapseIcon: 'glyphicon glyphicon-folder-open',
                onNodeUnselected: function(event, node){
                    composeEntity();
                    nodeId = node.nodeId;
                    $('#tree').treeview('selectNode', [ nodeId, { silent: true } ]);
                },
                onNodeSelected: function(event, node){
                    if(node.level == 1){
                        $('#tree').treeview('unselectNode', [ node.nodeId, { silent: true } ]);
                        $('#tree').treeview('toggleNodeExpanded', [ node.nodeId, { silent: true } ]);
                        return;
                    }
                    if(nodeId != undefined) $('#tree').treeview('unselectNode', [ nodeId, { silent: true } ]);
                    typeId = node.id;
                    typeName = node.text;
                    typePid = node.pid;
                    $('#showType').text(node.text+'--题型分布');
                    clearEntity();
                    findEntity(typeId);
                    $('#questionType').show();
                }
            });
            $('#tree').treeview('collapseAll', { silent: true });
        });
    }

    $(function(){
        initTree();
    });

    function clearEntity(){
        var currentAmount = calcTotalPoint(false);
        var qt = $('.add-ques-type');
        for (var i = 0; i < qt.length; i++) {
            $(qt[i]).find('.add-ques-amount').val('');
            $(qt[i]).find('.add-ques-score').val('');
            $(qt[i]).find('label').removeClass('active');
        }
        if(operate == 0){
            totalAmount = $('#currentPoint').text();
            var totalPoint = parseInt(totalAmount) - currentAmount;
            $("#currentPoint").text(totalPoint);
        }

    }

    function findEntity(typeId) {
        var qt = $('.add-ques-type');
        var questionType = questionMap.get('qtype_'+typeId);
        if(questionType!=undefined){
            var amountvals = questionType.questionTypeNum;
            for(var key in amountvals){
                $('#question'+key).val(amountvals[key]);
                $('#question'+key).parent().find('label').addClass('active');
            }

            var pointvals = questionType.questionTypePoint;
            for(var key in pointvals){
                $('#score'+key).val(pointvals[key]);
                $('#score'+key).parent().find('label').addClass('active');
            }
            operate = 1;
            preAmount = calcTotalPoint(false);
        }else{
            preAmount = 0;
            operate = 0;
        }

    }

    function composeEntity(){

        var qt = $('.add-ques-type');
        // 获取题目题型数量
        var amountMap = new Object();
        // 获取题目题型分值
        var pointMap = new Object();
        var tag = 0;
        for (var i = 0; i < qt.length; i++) {
            var itemamount = parseInt($(qt[i]).find('.add-ques-amount').val());
            var itemscore = parseInt($(qt[i]).find('.add-ques-score').val());
            var itemsid = $(qt[i]).find('.ques-id').val();
            if (isNaN(itemamount) || isNaN(itemscore)) {
                tag++;
                continue;
            } else {
                amountMap[itemsid] = itemamount;
                pointMap[itemsid] = itemscore;
            }
        }
        //删除重复项
        if(tag!=4) removeObject();

        var questionType = new Object();
        var showObj = new Object();
        showObj.id = typeId;
        showObj.categoryName = typeName;
        //判断题
        showObj.question1 = amountMap["3"] == undefined ? 0 : amountMap["3"];
        showObj.point1 = pointMap["3"] == undefined ? 0 : pointMap["3"];
        //单选题
        showObj.question2 = amountMap["1"] == undefined ? 0 : amountMap["1"];
        showObj.point2 = pointMap["1"] == undefined ? 0 : pointMap["1"];
        //多选题
        showObj.question3 = amountMap["2"] == undefined ? 0 : amountMap["2"];
        showObj.point3 = pointMap["2"] == undefined ? 0 : pointMap["2"];
        //填空题
        showObj.question4 = amountMap["4"] == undefined ? 0 : amountMap["4"];
        showObj.point4 = pointMap["4"] == undefined ? 0 : pointMap["4"];
        if(tag!=4) showList.push(showObj);

        questionType.questionTypeId = typeId;
        questionType.questionTypeNum = amountMap;
        questionType.questionTypePoint = pointMap;
        if(tag!=4) entity.questionTypeRates.push(questionType);
        questionMap.put('qtype_'+typeId, questionType);
        totalAmount = $('#currentPoint').text();
        operate = 1;
    }

    function removeObject(){
        var arrays = entity.questionTypeRates;
        for(var i=0; i<arrays.length;i++){
            var obj = arrays[i];
            if(obj.questionTypeId == typeId){
                arrays.splice(i, 1);
                entity.questionTypeRates = arrays;
                break;
            }

        }
        for(var i=0; i<showList.length;i++){
            var obj = showList[i];
            if(obj.id == typeId){
                showList.splice(i, 1);
                break;
            }
        }
    }

    function calcTotalPoint(tag){
        var totalPoint = parseInt(totalAmount);
        totalPoint = totalPoint - preAmount;
        var amount = 0;
        var qt = $(".add-ques-type");
        for(var i = 0 ; i< qt.length;i++){
            var itemamount = parseInt($(qt[i]).find(".add-ques-amount").val());
            var itemscore = parseFloat($(qt[i]).find(".add-ques-score").val());

            if(isNaN(itemamount)||isNaN(itemscore)){
                continue;
            }else{
                amount = amount +  itemamount * itemscore * 10;
            }
        }
        if(tag){
            totalPoint = totalPoint + (amount/10);
            $("#currentPoint").text(totalPoint);
        }
        return (amount/10)
    }

     function bindChangeAmount(){
        $(".add-ques-amount").change(function(){
            calcTotalPoint(true);
        });
        $(".add-ques-score").change(function(){
            calcTotalPoint(true);
        });
    }

    $(function () {
        bindChangeAmount();
    })

    function composeObject(){
        var object = $('#createForm').serializeObject();
        object.startTime = new Date(object.startTime).Format('yyyy-MM-dd HH:mm:ss');
        object.endTime = new Date(object.endTime).Format('yyyy-MM-dd HH:mm:ss');
        object.organizationIds = '${organizationIdstr}';
        object.questionTypeRateList = JSON.stringify(entity.questionTypeRates);
        return object;
    }

    function sumbitFun (){
        composeEntity();
        var totalPoint = '${exam.totalPoint}';
        var currentScore = $("#currentPoint").text() == "" ? 0 : parseInt($("#currentPoint").text());
        if(currentScore == 0){
            alertMsg('请输入相应的题型分布！');
            return;
        }
        if(currentScore != parseInt(totalPoint)){
            alertMsg('当前试卷总分'+totalAmount+'与设定总分'+totalPoint+'分不一致，请修改各题分值！');
            return;
        }

        console.log(composeObject());

        ruleListAction();
    }

    // 确认考试发布规则
    var ruleListDialog;
    function ruleListAction() {
        ruleListDialog = $.dialog({
            type: 'green',
            columnClass: 'col-md-10 col-md-offset-2',
            animationSpeed: 300,
            title: '确认考试发布规则',
            content: 'url:${basePath}/manage/exam/ruleList',
            onContentReady: function () {
            }
        });
    }

    function submitRule(){
        $.ajax({
            type: 'post',
            url: '${basePath}/manage/exam/create',
            data: composeObject(),
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
                    ruleListDialog.close();
                    location.href = '${basePath}/manage/exam/index';
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertMsg(textStatus);
            }
        });
    }


</script>
</body>
</html>