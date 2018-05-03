var scroll_timeout = false;
var lastfocusLi = null;
var lastField = {
	"name": null,
	"type": null,
	"no": null
};
var confirm_b4_close = true;
var timer = null;
var submit_timeout = false;
var submitQueue = new Map();
var optionsSequence = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];

var answerMap = new Map();

$(function() {


	//加载试题
	if(answerData!=undefined) loadSubject();

	//上一题、下一题操作界面
	$(window).scroll(function() {
		if (scroll_timeout) {
			clearTimeout(scroll_timeout);
		}
		scroll_timeout = setTimeout(function() {
			checkFloater();
		}, 100);
		return true;
	});

	//题目选项卡
	$("div#numbercard-container ul li").on('mouseover mouseout', function(event) {
		if (event.type == 'mouseover') {
			$(this).addClass("hover");
			return false;
		} else {
			$(this).removeClass("hover");
			return false;
		}
	});

	//控制面板操作
	$("a.leftmenutrriger").on("click", function(e) {
		e.preventDefault();
		if ($(this).text() == "关闭面板") {
			$("#leftwrapper,#menuwapper").clearQueue().stop().animate({
				width: "0px",
				opacity: 0
			}, 600, function() {
				checkFloater();
			});
			$(this).text("打开面板");
		} else {
			$("#leftwrapper,#menuwapper").clearQueue().stop().animate({
				width: "300px",
				opacity: 1
			}, 600, function() {
				checkFloater();
			});
			$(this).text("关闭面板");
		}
		return false;
	});

	// 点击选项卡
	$("div#numbercard-container ul li").on("click", function(e) {
		e.preventDefault();
		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body');
		$body.stop();
		var ind = $("div#numbercard-container ul li").index(this);
		//moveing effect
		$body.animate({
			scrollTop: $("ul#exam-paper li").eq(ind).offset().top
		}, 500, function() {});
		//transfer effect
		$(this).effect(
			"transfer", {
				to: $("ul#exam-paper li").eq(ind),
				className: "ui-effects-tranaaaasfer"
			}, 700,
			function() {}
		);

		return false;
	});


});


/**
 * 时间formater
 *
 * @param timestamp
 * @returns {String}
 */
var toHHMMSS = function toHHMMSS(timestamp) {
    var sec_num = parseInt(timestamp, 10);
    var hours = Math.floor(sec_num / 3600);
    var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
    var seconds = sec_num - (hours * 3600) - (minutes * 60);

    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    var time = hours + ':' + minutes + ':' + seconds;
    return time;
};


//eyes focus
var eyeFocus = function() {
	//var middleEle=document.elementFromPoint($(window).scrollLeft()+getTotalWidth()/2,$(window).scrollTop()+getTotalHeight()/2);
	var middleEle = null;
	for (i = -60; i < 60; i += 10) {
		var middleEle = document.elementFromPoint($(window).scrollLeft() + getTotalWidth() / 2, getTotalHeight() / 2 + i);
		if (middleEle.id != "exam-paper" && middleEle.id != "workspace-container" && middleEle.id != "workspace") break;
	}

	while (middleEle != null) {
		if (middleEle.tagName != "LI") {
			middleEle = middleEle.parentNode;
		} else break;
	}
	if (middleEle != null) {
		var ind = $("ul#exam-paper li").index($(middleEle));
		if (ind != lastfocusLi) {
			if (lastfocusLi != null) $("div#numbercard-container ul li").eq(lastfocusLi).removeClass("selected");
			$("div#numbercard-container ul li").eq(ind).addClass("selected");
			lastfocusLi = ind;
		}
	}
}

var changeField = function(name, type, no) {
	//name: mytest_id & mypaper_id & subject_position & subject_id & subject_type
	//type: radio|checkbox|textarea|extra
	//no: li number
	var focusfield = {
		"name": name,
		"type": type,
		"no": no
	};
	if (lastField.name != focusfield.name) {
		if (lastField.name != null) {
			if (lastField.type == "extra") {
				//$("div#numbercard-container ul li").eq(lastField.no).addClass("submited");
			} else {
				submitQueue.put(lastField.name, lastField);
				//$("div#numbercard-container ul li").eq(lastField.no).addClass("submiting");
			}
		}

	}
	lastField = focusfield;
}

//check float potion
var checkFloater = function() {
	/*$("#exam-do-floatbar").css("position", "absolute");
	$("#exam-do-floatbar").css("left", $("#workspace").offset().left);
	positionOnBottom("#exam-do-floatbar");*/


	$("#menuwapper").css("position", "absolute");
	$("#menuwapper").css("left", $("#menuwapper").parent().offset().left);
	positionOnTop("#menuwapper");
}

//stay bottom
var positionOnBottom = function(selecter) {
	var newLeft = $(selecter).offset().left;
	if ($(selecter).css("position") != "absolute") $(selecter).css("position", "absolute");
	var newTop = $(window).scrollTop() + getTotalHeight() - $(selecter).height();
	$(selecter).clearQueue().stop();
	$(selecter).animate({
		left: newLeft,
		top: newTop
	}, 500, function() {
		eyeFocus();
	});
}

//stay top
var positionOnTop = function(selecter) {
	var newLeft = $(selecter).offset().left;
	if ($(selecter).css("position") != "absolute") $(selecter).css("position", "absolute");
	var newTop = $(window).scrollTop();
	if ($(window).scrollTop() - $(selecter).offset().top > $(selecter).height() / 2 || $(selecter).offset().top > $(window).scrollTop() + getTotalHeight() / 2) {
		$(selecter).clearQueue().stop();
		$(selecter).animate({
			left: newLeft,
			top: newTop
		}, 800);
	}
}

//screen height	
var getTotalHeight = function() {
	if ($.support) {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
	} else {
		return self.innerHeight;
	}
}

//screen width
var getTotalWidth = function() {
	if ($.support) {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
	} else {
		return self.innerWidth;
	}
}

// 单选题
var construtRadioSubject = function(subjects, index) {
	if (subjects.length > 0) {
		for (var i = 0; i < subjects.length; i++) {
			var subject = subjects[i];
			var answerObj = answerMap.get('qid_'+subject.questionId);//获取答案
			var qindex = i + 1;
			var content = JSON.parse(subject.content);
			var bullet = '<li class="exam-subject"><div class="exam-subject-tip">第<span class="exam-subject-no">{no}</span>题，单选题（{score}分）</div><div class="exam-subject-title">{title}</div><div class="exam-subject-option-container">';
			bullet = bullet.replace(/{no}/ig, qindex);
			bullet = bullet.replace(/{score}/ig, subject.questionPoint);
			bullet = bullet.replace(/{title}/ig, content.title);
            bullet += '<input name="score" type="hidden" value="{scoreval}"/>';
            bullet = bullet.replace(/{scoreval}/ig, subject.questionPoint);
            bullet += '<input name="questionType" type="hidden" value="{questionTypeId}"/>';
            bullet = bullet.replace(/{questionTypeId}/ig, subject.questionTypeId);
            bullet += '<input name="questionId" type="hidden" value="{questionId}"/>';
            bullet = bullet.replace(/{questionId}/ig, subject.questionId);
			var option_checked_no = -1;
			var answered = (subject.answer != null && subject.answer.length > 0);
			var options = content.choiceList;
			for (var obj in options) { //
				bullet += '<label for="{optionID}" {label_selected}><span class="exam-subject-optionNo">{optionNo}</span><input type="radio" name="{fieldName}" value="{optionValue}" id="{optionID}" {option_checked}>{optionText}</label>';
				bullet = bullet.replace(/{optionID}/ig, qindex + '_' + obj);
				bullet = bullet.replace(/{optionNo}/ig, obj);
				bullet = bullet.replace(/{fieldName}/ig, subject.questionId + '_option');
				bullet = bullet.replace(/{optionText}/ig, options[obj]);
				bullet = bullet.replace(/{optionValue}/ig, obj);
				var label_selected = "";
				var option_checked = "";
				if(answerObj.answer==obj) label_selected = "class=\"selected hover\"";
				bullet = bullet.replace(/{label_selected}/ig, label_selected);
				bullet = bullet.replace(/{option_checked}/ig, option_checked);
			}
			var answer_text = subject.answer;
			bullet += '<div class="clear"></div></div><div class="exam-subject-answerpanel">正确的答案：<span class="exam-subject-myanswer">{answer_text}</span></div></li>';
			if (option_checked_no > -1) answer_text = optionsSequence[option_checked_no];
			bullet = bullet.replace(/{answer_text}/ig, answer_text);

			var cardlicls = "regular";
			if (qindex % 2 == 0) cardlicls = "particular";
			if (answered) cardlicls += " ";
			$("ul#exam-paper").append(bullet);
			$("div#numbercard-container ul").append('<li class="' + cardlicls + '">' + qindex + '</li>');
		}
	}
}

// 是非题
var construtDetermineSubject = function(subjects, index) {
	if (subjects.length > 0) {
		for (var i = 0; i < subjects.length; i++) {
			var subject = subjects[i];
			var answerObj = answerMap.get('qid_'+subject.questionId);//获取答案
			var qindex = index + i + 1;
			var content = JSON.parse(subject.content);
			var bullet = '<li class="exam-subject"><div class="exam-subject-tip">第<span class="exam-subject-no">{no}</span>题，判断题（{score}分）</div><div class="exam-subject-title">{title}</div><div class="exam-subject-option-container">';
			bullet = bullet.replace(/{no}/ig, qindex);
			bullet = bullet.replace(/{title}/ig, content.title);
			bullet = bullet.replace(/{score}/ig, subject.questionPoint);
            bullet += '<input name="score" type="hidden" value="{scoreval}"/>';
            bullet = bullet.replace(/{scoreval}/ig, subject.questionPoint);
            bullet += '<input name="questionType" type="hidden" value="{questionTypeId}"/>';
            bullet = bullet.replace(/{questionTypeId}/ig, subject.questionTypeId);
            bullet += '<input name="questionId" type="hidden" value="{questionId}"/>';
            bullet = bullet.replace(/{questionId}/ig, subject.questionId);
			var option_checked_no = -1;
			var answered = (subject.answer != null && subject.answer.length > 0);
			var options = {"T":"对","F":"错"};
			for (var obj in options) { //
				bullet += '<label for="{optionID}" {label_selected}><span class="exam-subject-optionNo">{optionNo}</span><input type="radio" name="{fieldName}" value="{optionValue}" id="{optionID}" {option_checked}>{optionText}</label>';
				bullet = bullet.replace(/{optionID}/ig, qindex + '_' + obj);
				bullet = bullet.replace(/{optionNo}/ig, obj);
				bullet = bullet.replace(/{fieldName}/ig, subject.questionId + '_option');
				bullet = bullet.replace(/{optionText}/ig, options[obj]);
				bullet = bullet.replace(/{optionValue}/ig, obj);
				var label_selected = "";
				var option_checked = "";
				if(answerObj.answer == obj) label_selected = "class=\"selected hover\"";
				bullet = bullet.replace(/{label_selected}/ig, label_selected);
				bullet = bullet.replace(/{option_checked}/ig, option_checked);
			}
			var answer_text = subject.answer;
			bullet += '<div class="clear"></div></div><div class="exam-subject-answerpanel">正确的答案：<span class="exam-subject-myanswer">{answer_text}</span></div></li>';
			if (option_checked_no > -1) answer_text = optionsSequence[option_checked_no];
			bullet = bullet.replace(/{answer_text}/ig, answer_text);

			var cardlicls = "regular";
			if (qindex % 2 == 0) cardlicls = "particular";
			if (answered) cardlicls += " ";
			$("ul#exam-paper").append(bullet);
			$("div#numbercard-container ul").append('<li class="' + cardlicls + '">' + qindex + '</li>');
		}
	}
}

// 多选题
var construtCheckboxSubject = function(subjects, index) {
	if (subjects.length > 0) {
		for (var i = 0; i < subjects.length; i++) {
			var subject = subjects[i];
			var answerObj = answerMap.get('qid_'+subject.questionId);//获取答案
			console.log('=='+answerObj);
			var qindex = index + i + 1;
			var content = JSON.parse(subject.content);
			var bullet = '<li class="exam-subject"><div class="exam-subject-tip">第<span class="exam-subject-no">{no}</span>题，多选题（{score}分）</div><div class="exam-subject-title">{title}</div><div class="exam-subject-option-container">';
			bullet = bullet.replace(/{no}/ig, qindex);
			bullet = bullet.replace(/{title}/ig, content.title);
			bullet = bullet.replace(/{score}/ig, subject.questionPoint);
            bullet += '<input name="score" type="hidden" value="{scoreval}"/>';
            bullet = bullet.replace(/{scoreval}/ig, subject.questionPoint);
            bullet += '<input name="questionType" type="hidden" value="{questionTypeId}"/>';
            bullet = bullet.replace(/{questionTypeId}/ig, subject.questionTypeId);
            bullet += '<input name="questionId" type="hidden" value="{questionId}"/>';
            bullet = bullet.replace(/{questionId}/ig, subject.questionId);
			var option_checked_no = -1;
			var answered = (subject.answer != null && subject.answer.length > 0);
			var option_checked_text = "";
			var options = content.choiceList;
			for (var obj in options) { //
				bullet += '<label for="{optionID}" {label_selected}><span class="exam-subject-optionNo">{optionNo}</span><input type="checkbox" name="{fieldName}" value="{optionValue}" id="{optionID}" {option_checked}>{optionText}</label>';
				bullet = bullet.replace(/{optionID}/ig, qindex + '_' + obj);
				bullet = bullet.replace(/{optionNo}/ig, obj);
				bullet = bullet.replace(/{fieldName}/ig, subject.questionId + '_option');
				bullet = bullet.replace(/{optionText}/ig, options[obj]);
				bullet = bullet.replace(/{optionValue}/ig, obj);
				var label_selected = "";
				var option_checked = "";

				if(answerObj.answer.indexOf(obj) != -1) {
					label_selected = "class=\"selected hover\"";
					option_checked = "checked";
				}

				bullet = bullet.replace(/{label_selected}/ig, label_selected);
				bullet = bullet.replace(/{option_checked}/ig, option_checked);
			}
			var answer_text = subject.answer;
			bullet += '<div class="clear"></div></div><div class="exam-subject-answerpanel">正确的答案：<span class="exam-subject-myanswer">{answer_text}</span></div></li>';
			bullet = bullet.replace(/{answer_text}/ig, answer_text);

			var cardlicls = "regular";
			if (qindex % 2 == 0) cardlicls = "particular";
			if (answered) cardlicls += " ";
			$("ul#exam-paper").append(bullet);
			$("div#numbercard-container ul").append('<li class="' + cardlicls + '">' + qindex + '</li>');
		}
	}

}

//填空题
var construtTextareaSubject = function(subjects, index){
	if (subjects.length > 0) {
		for (var i = 0; i < subjects.length; i++) {
			var subject = subjects[i];
			var answerObj = answerMap.get('qid_'+subject.questionId);//获取答案
			var qindex = index + i + 1;
			var content = JSON.parse(subject.content);
			var bullet = '<li class="exam-subject"><div class="exam-subject-tip">第<span class="exam-subject-no">{no}</span>题，问答题（{score}分）</div><div class="exam-subject-title">{title}</div>';

			bullet = bullet.replace(/{no}/ig, qindex);
			bullet = bullet.replace(/{title}/ig, content.title);
			bullet = bullet.replace(/{score}/ig, subject.questionPoint);
			bullet += '<input name="score" type="hidden" value="{scoreval}"/>';
			bullet = bullet.replace(/{scoreval}/ig, subject.questionPoint);
			bullet += '<input name="questionType" type="hidden" value="{questionTypeId}"/>';
			bullet = bullet.replace(/{questionTypeId}/ig, subject.questionTypeId);
			bullet += '<input name="questionId" type="hidden" value="{questionId}"/>';
			bullet = bullet.replace(/{questionId}/ig, subject.questionId);

			var option_checked_text="";
			var answer_text = subject.answer;
			var answered = (subject.answer!=null&&subject.answer.length>0);
			if(subject.answer != null && subject.answer.length>0) option_checked_text=subject.answer;
			bullet+='<div class="exam-subject-answerpanel">您的答案：<br/><textarea name="{fieldName}" id="{fieldName}">{answerObj}</textarea></div>';
			bullet+='<div class="exam-subject-answerpanel">正确的答案：<br/><textarea name="{fieldName}" id="{fieldName}">{answer_text}</textarea></div>';
			bullet=bullet.replace(/{fieldName}/ig,"question_"+subject.questionId);
			bullet=bullet.replace(/{answerObj}/ig,answerObj.answer);
			bullet=bullet.replace(/{answer_text}/ig,answer_text);

			var cardlicls="regular";
			if(qindex % 2 == 0) cardlicls = "particular";
			if(answered) cardlicls+=" ";
			$("ul#exam-paper").append(bullet);
			$("div#numbercard-container ul").append('<li class="'+cardlicls+'">'+qindex+'</li>');
		}
	}
}

// 加载试题
var loadSubject = function() {

	loadAnswers();

	$("ul#exam-paper").empty();
	$("div#numbercard-container ul").empty();
     var radioSubjectList = []; //单选题
     var checkSubjectList = []; //多选题
     var determineSubjectList = []; //是非题
	 var textareaSubjectList = []; //填空题

     for (var i = 0; i < paperData.length; i++) {
         var qtype = paperData[i].questionTypeId;
         switch (qtype) {
             case 1:
                 radioSubjectList.push(paperData[i]);
                 break;
             case 2:
                 checkSubjectList.push(paperData[i]);
                 break;
             case 3:
                 determineSubjectList.push(paperData[i]);
                 break;
			 case 4:
				 textareaSubjectList.push(paperData[i]);
         }
     }

     //分别加载各个题型
     construtRadioSubject(radioSubjectList, 0); //单选题
     construtCheckboxSubject(checkSubjectList, radioSubjectList.length); //多选题
	 construtDetermineSubject(determineSubjectList, radioSubjectList.length+checkSubjectList.length); //是非题
	 construtTextareaSubject(textareaSubjectList, radioSubjectList.length+checkSubjectList.length+determineSubjectList.length);//填空题

}

//加载试题答案
var loadAnswers = function(){
	var answerDatas = answerData.answerSheetItems;
	for(var i=0; i<answerDatas.length; i++){
		var qid = answerDatas[i].questionId;
		answerMap.put('qid_'+qid, answerDatas[i]);
	}
}

//debug
var debug = function(msg) {
	$("#notice-container").html(msg + "<br/>" + $("#notice-container").html());
}

//unload alert
$(window).bind('beforeunload', function(e) {
	window.location.href= ctx+'/index';
});