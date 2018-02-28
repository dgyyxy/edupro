var updateScoreDialog; //修改每道题目的分值
var addMoreQuestionsDialog;//新增更多的题目
var updateNameDialog;   //修改试卷名称

$(function () {
    examing.initial();
});

var examing = {

    initial: function initial() {
        this.refreshNavi();
        this.bindNaviBehavior();
        this.addNumber();

        this.updateSummery();
        this.bindQuestionFilter();
        this.bindfocus();
        this.bindOpenModal();
        this.addRemoveBtn();
        this.bindRemoveQustionFromPaper();
        this.blindChangePoint();
        this.bindSavePaper();
        this.bindChangePaperName();

    },
    bindNaviBehavior: function bindNaviBehavior() {

        var nav = $("#question-navi");
        var naviheight = $("#question-navi").height() - 33;


        $("#exampaper-footer").height($("#question-navi").height());

        nav.css({
            position: 'fixed',
            bottom: '0px',
            "z-index": '1'
        });


        $("#question-navi-controller").click(function () {
            if ($("#question-navi-content").is(":visible")) {
                $("#question-navi-content").hide();
            } else {
                $("#question-navi-content").show();

            }
        });

    },
    /**
     * 对题目重新编号排序
     */
    addNumber: function addNumber() {
        //单选题
        var singleQuestions = $("li.qt-singlechoice");
        var multipleQuestions = $("li.qt-multiplechoice");
        var trueorfalseQuestions = $("li.qt-trueorfalse");
        var fillblankQuestions = $("li.qt-fillblank");
        var i = 0;
        if(trueorfalseQuestions.length>0){
            trueorfalseQuestions.each(function(){
                $(this).find(".question-no").text(i + 1 +".");
                $(this).find(".question-no").attr('id', 'no_'+(i+1));
                i++;
            });
        }

        if(singleQuestions.length>0){
            singleQuestions.each(function(){
                $(this).find(".question-no").text(i + 1 +".");
                $(this).find(".question-no").attr('id', 'no_'+(i+1));
                i++;
            });
        }
        if(multipleQuestions.length>0){
            multipleQuestions.each(function(){
                $(this).find(".question-no").text(i + 1 +".");
                $(this).find(".question-no").attr('id', 'no_'+(i+1));
                i++;
            });
        }
        if(fillblankQuestions.length>0){
            fillblankQuestions.each(function(){
                $(this).find(".question-no").text(i + 1 +".");
                $(this).find(".question-no").attr('id', 'no_'+(i+1));
                i++;
            });
        }
    },
    /**
     * 切换考题类型事件
     */
    bindQuestionFilter: function bindQuestionFilter() {
        $("#exampaper-desc").delegate("span.exampaper-filter-item", "click", function () {
            var qtype = $(this).find(".efi-qcode").text();
            examing.doQuestionFilt(qtype);
        });
    },
    /**
     * 刷新试题导航
     */
    refreshNavi: function refreshNavi() {
        $("#question-navi-content").empty();
        var questions = $("li.question");
        var qcount = questions.length;
        $('#exampaper-amount').text(qcount);
        questions.each(function (index) {
            var btnhtml = "<a class=\"question-navi-item\">" + (index + 1) + "</a>";
            $("#question-navi-content").append(btnhtml);
        });
    },
    /**
     * 更新题目简介信息
     */
    updateSummery: function updateSummery() {
        if ($(".question").length === 0) {
            $("#exampaper-desc").empty();
            $("#exampaper-total-point").text(0);
            return false;
        }
        var questiontypes = this.questiontypes;

        var summery = "";
        for (var i = 0; i < questiontypes.length; i++) {
            var question_sum_q = $("." + questiontypes[i].code).length;
            if (question_sum_q == 0) {
                continue;
            } else {
                summery = summery + "<span class=\"exampaper-filter-item efi-" + questiontypes[i].code + "\">"
                    + questiontypes[i].name + "[<span class=\"efi-tno\">"
                    + $("." + questiontypes[i].code).length + "</span>]<span class=\"efi-qcode\" style=\"display:none;\">"
                    + questiontypes[i].code + "</span></span>";
            }
        }
        $("#exampaper-desc").html(summery);

        examing.doQuestionFilt($($(".exampaper-filter-item")[0]).find(".efi-qcode").text());


        examing.refreshTotalPoint();
    },
    /**
     * 计算总分
     */
    refreshTotalPoint: function refreshTotalPoint() {
        var question_sum_p_all = 0;
        var point_array = $(".question-point");
        for (var i = 0; i < point_array.length; i++) {
            var pointtmp = parseFloat($(point_array[i]).text());

            if (isNaN(pointtmp)) {
                continue;
            } else {
                question_sum_p_all = question_sum_p_all + pointtmp * 10;
            }
        }
        $("#exampaper-total-point").text(question_sum_p_all / 10);
    },

    /**
     *切换到指定的题型
     */
    doQuestionFilt: function doQuestionFilt(questiontype) {

        if ($("#exampaper-desc .efi-" + questiontype).hasClass("efi-selected")) {
            return false;
        } else {
            var questions = $("li.question");
            questions.hide();
            $("#exampaper-body ." + questiontype).show();

            $(".exampaper-filter-item").removeClass("efi-selected");
            $("#exampaper-desc .efi-" + questiontype).addClass("efi-selected");
        }
    },
    questiontypes: new Array({
        "name": "是非题",
        "code": "qt-trueorfalse"
    }, {
        "name": "单选题",
        "code": "qt-singlechoice"
    }, {
        "name": "多选题",
        "code": "qt-multiplechoice"
    }, {
        "name": "填空题",
        "code": "qt-fillblank"
    }),

    bindOpenModal: function bindOpenModal() {
        //添加更多题目
        $("#add-more-qt-to-paper").click(function () {
            addMoreQuestionsDialog = $.dialog({
                type: 'blue',
                title: '添加题目',
                columnClass: 'col-md-11 col-md-offset-1',
                content: 'url:'+basePath+'/manage/paper/questionList',
                onContentReady: function(){
                    initMaterialInput();
                    $('select').select2({
                        width: 150
                    });
                }
            })
        });
    },
    /**
     * 绑定考题focus事件(点击考题导航)
     */
    bindfocus: function bindfocus() {
        $("#question-navi").delegate("a.question-navi-item ", "click", function () {
            var clickindex = $("a.question-navi-item").index(this);
            var targetQuestion;
            var questionNos = $('span.question-no');
            questionNos.each(function(){
                var textstr = $(this).text();
                if(clickindex+1+'.' == textstr) targetQuestion = $(this).parent().parent();
            });

            var targetQuestionType = $(targetQuestion).find(".question-type").text();
            examing.doQuestionFilt("qt-" + targetQuestionType);
            examing.scrollToElement($(targetQuestion));
        });
    },

    addRemoveBtn: function () {
        var deletehtml = "<a class=\"tmp-ques-remove\" title=\"删除此题\">删除</a>";
        $(".question-title").append(deletehtml);

    },

    bindRemoveQustionFromPaper: function bindRemoveQustionFromPaper() {
        $("#exampaper-body").on("click", "a.tmp-ques-remove", function () {
            $(this).parent().parent().remove();
            examing.refreshNavi();
            examing.addNumber();
            examing.updateSummery();
        });
    },
    scrollToElement: function scrollToElement(selector, time, verticalOffset) {
        time = typeof (time) != 'undefined' ? time : 500;
        verticalOffset = typeof (verticalOffset) != 'undefined' ? verticalOffset : 0;
        element = $(selector);
        offset = element.offset();
        offsetTop = offset.top + verticalOffset;
        $('html, body').animate({
            scrollTop: offsetTop
        }, time);
    },
    // 修改每道题的分值
    blindChangePoint: function blindChangePoint() {
        $("#exampaper-body").on("click", "span.question-point", function () {
            var questions = $("li.question");
            var indexno = questions.index($(this).parent().parent().parent());
            var questionNoStr = $(this).parent().prevAll('.question-no').text();
            updateScoreDialog = $.dialog({
                type: 'blue',
                title: '修改分数',
                content: 'url:'+basePath+'/manage/paper/updateScore',
                onContentReady: function () {
                    initMaterialInput();
                    $('#score').val($(this).text());//设置分数值
                    $("#qt-point-target-index").text(questionNoStr.substring(0, questionNoStr.length-1));
                }
            });
        });
    },

    // 修改试卷名称
    bindChangePaperName: function (){
        $("#paper_name_update").on("click", function(){
            updateNameDialog = $.dialog({
                type: 'blue',
                title: '修改试卷名称',
                content: 'url:'+basePath+'/manage/paper/updateName',
                onContentReady: function () {
                    initMaterialInput();
                    $('#paperName').focus();
                    $('#paperName').val($("#exampaper-title-name").text());//设置分数值
                }
            });
        });
    },

    composeEntity: function composeEntity() {
        var forms = $(".question");
        var map = new Object();
        forms.each(function () {
            var question_point = $(this).find("span.question-point").text();
            var question_id = $(this).find("span.question-id").text();
            map[question_id] = question_point;
        });
        return map;
    },

    bindSavePaper: function bindSavePaper() {
        $("#save-paper-btn").click(function () {
            var map = examing.composeEntity();
            var count = 0;
            for (var k in map) {
                if (map.hasOwnProperty(k)) {
                    ++count;
                }
            }
            if (examing.getType($("#exampaper-total-point").text()) == "float") {
                alertMsg("总分不能有小数");
                return false;
            }
            var tag = 0;
            $('.question span.question-point').each(function(){
                if($(this).text() == 0){
                    tag = 1;
                }
            });

            if(tag == 1){
                alertMsg("当前试卷不允许有0分题存在，请修改！");
                return false;
            }

            var totalPoint = $('#totalPoint').text();
            var nowTotalPoint = $('#exampaper-total-point').text();

            if(totalPoint!=nowTotalPoint){
                alertMsg("当前试卷总分"+nowTotalPoint+"与设定总分"+totalPoint+"分不一致，请修改各题分值！")
                return false;
            }

            if (count != $(".question").length) {
                alertMsg("存在重复的题目，请检查");
                return false;
            } else {
                var data = {};
                data.questionPointMapStr = JSON.stringify(map);
                data.paperName = $('#exampaper-title-name').text();
                $.ajax({
                    type: 'post',
                    url: basePath + '/manage/paper/update/' + $("#exampaper-id").text(),
                    data: data,
                    success: function(result) {
                        if (result.code != 1) {
                            if (result.data instanceof Array) {
                                $.each(result.data, function(index, value) {
                                    alertMsg(value.errorMsg);
                                });
                            } else {
                                alertMsg(result.data.errorMsg);
                            }
                        } else {
                            location.href = basePath+'/manage/paper/index';
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alertMsg(textStatus);
                    }
                });
            }
        });
    },
    getType: function getType(input) {
        var m = (/[\d]+(\.[\d]+)?/).exec(input);
        if (m) {
            // Check if there is a decimal place
            if (m[1]) {
                return 'float';
            }
            else {
                return 'int';
            }
        }
        return 'string';
    }
};













