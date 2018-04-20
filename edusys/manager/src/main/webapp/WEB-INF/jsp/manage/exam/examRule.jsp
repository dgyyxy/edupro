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
            <span style="font-size: 14px;font-weight: bold;">测试考试----题库分类对应题型分布设置</span>
        </div>
        <div class="col-md-3 text-right">
            <a class="waves-effect waves-button green" href="javascript:;" onclick="save()"><i class="zmdi zmdi-check"></i> 保存设置</a>
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
            <fieldset id="questionType" class="questionShowDiv" hidden>
                <legend id="showType">选择题型分布</legend>
                <div class="form-group add-ques-type">
                    <input type="hidden" class="ques-id" value="3">
                    <div class="col-md-6">
                        <label for="question3">是非题数</label>
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

                <div class="row">
                    <div class="col-md-12 text-right">
                        <a class="waves-effect waves-button green" href="javascript:;" onclick="composeEntity()"><i class="zmdi zmdi-check"></i> 确认</a>
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

    var entity = new Object();
    entity.questionTypeRates = new Array();

    var questionMap = new Map();

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
                    nodeId = node.nodeId;
                    $('#tree').treeview('selectNode', [ nodeId, { silent: true } ]);
                },
                onNodeSelected: function(event, node){
                    if(nodeId != undefined) $('#tree').treeview('unselectNode', [ nodeId, { silent: true } ]);
                    typeId = node.id;
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
        var qt = $('.add-ques-type');
        for (var i = 0; i < qt.length; i++) {
            $(qt[i]).find('.add-ques-amount').val('');
            $(qt[i]).find('.add-ques-score').val('');
            $(qt[i]).find('label').removeClass('active');
        }
    }

    function findEntity(typeId) {
        var qt = $('.add-ques-type');
        var questionType = questionMap.get('qtype_'+typeId);
        console.log(JSON.stringify(questionType));
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
        }

    }

    function composeEntity(){

        var qt = $('.add-ques-type');
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
        var questionType = new Object();
        questionType.questionTypeId = typeId;
        questionType.questionTypeNum = amountMap;
        questionType.questionTypePoint = pointMap;
        entity.questionTypeRates.push(questionType);
        questionMap.put('qtype_'+typeId, questionType);
        alert(JSON.stringify(entity));
    }

    function save(){
        alert(JSON.stringify(entity));
    }


</script>
</body>
</html>