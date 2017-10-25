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













