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
var timeMap = new Map();

$(function() {



	// 检查漏题
	$("a#checkmissing_bt").click(function(e) {
		e.preventDefault();
		check_missing();
		return false;
	});

	//exam done
	$("a#exam_done_bt").click(function(e) {
		e.preventDefault();
		exam_done();
		return false;
	});

	//加载试题
	loadSubject();

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

	// 上一题、下一题操作
	$("a.nextsubject,a.previoussubject").on("click", function(e) {
		e.preventDefault();
		$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body');
		$body.stop();
		var _focusLi = lastfocusLi;
		if (_focusLi == null) _focusLi = 0;
		if ($(this).attr("class") == "nextsubject") _focusLi += 1;
		else {
			_focusLi -= 1;
			if ($("ul#exam-paper li").eq(_focusLi).offset().top == $(window).scrollTop()) _focusLi -= 1;
		}
		if (_focusLi < 0) _focusLi = 0;
		if (_focusLi >= $("ul#exam-paper li").length) _focusLi = $("ul#exam-paper li").length - 1;
		$body.animate({
				scrollTop: $("ul#exam-paper li").eq(_focusLi).offset().top
			},
			500,
			function() {}
		);
		return false;
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
			}, 10, function() {
				checkFloater();
			});
			$('#exam-do-floatbar-hover').show();
			//$(this).text("打开面板");
		} else {
			$("#leftwrapper,#menuwapper").clearQueue().stop().animate({
				width: "300px",
				opacity: 1
			}, 600, function() {
				checkFloater();
			});
			//$(this).text("关闭面板");
			$('#exam-do-floatbar-hover').hide();
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

	//checkbox radio hover
	$("div.exam-subject-option-container label").on('mouseover mouseout', function(event) {
		if (event.type == 'mouseover') {
			$(this).addClass("hover");
			return false;
		} else {
			$(this).removeClass("hover");
			return false;
		}
	});

	//radio checkbox textarea click event
	$("div.exam-subject-option-container label input:radio").on("click", function(e) {
		var no = $("ul#exam-paper li").index($(this).closest("li"));
		changeField($(this).attr("name"), "radio", no);
		$("div.exam-subject-option-container label input[type=radio][name='" + $(this).attr("name") + "']:not(:checked)").parent().removeClass("selected");
		if ($(this).is(':checked')) $(this).parent().addClass("selected");
		else $(this).parent().removeClass("selected");
		$(this).closest("li.exam-subject").find("div.exam-subject-answerpanel span.exam-subject-myanswer").text($(this).closest("label").find("span.exam-subject-optionNo").text());
		if ($(this).closest("label").find("span.exam-subject-optionNo").text() == '')
			$("div#numbercard-container ul li").eq(no).removeClass("submiting");
		else
			$("div#numbercard-container ul li").eq(no).addClass("submiting");
		//提交
		submitQuestion();
	});

	$("div.exam-subject-option-container label input:checkbox").on("click", function() {
		var no = $("ul#exam-paper li").index($(this).closest("li"));
		changeField($(this).attr("name"), "checkbox", no);
		if ($(this).is(':checked')) $(this).parent().addClass("selected");
		else $(this).parent().removeClass("selected");
		$("div.exam-subject-option-container label input[type=checkbox][name='" + $(this).attr("name") + "']:checked").parent().addClass("selected");

		var _vtmpstr = "";
		$("div.exam-subject-option-container label input[type=checkbox][name='" + $(this).attr("name") + "']:checked").each(
			function() {
				_vtmpstr += " " + $(this).closest("label").find("span.exam-subject-optionNo").text();

			}
		);
		$(this).closest("li.exam-subject").find("div.exam-subject-answerpanel span.exam-subject-myanswer").text(_vtmpstr);
		if (_vtmpstr != '') $("div#numbercard-container ul li").eq(no).addClass("submiting");
		else $("div#numbercard-container ul li").eq(no).removeClass("submiting");
		//提交
		submitQuestion();
	});

	$("div.exam-subject-answerpanel textarea").on("change", function() {
		var no = $("ul#exam-paper li").index($(this).closest("li"));
		changeField($(this).attr("name"), "textarea", $("ul#exam-paper li").index($(this).closest("li")));
		var textval = $(this).val();
		if (textval != '') $("div#numbercard-container ul li").eq(no).addClass("submiting");
		else $("div#numbercard-container ul li").eq(no).removeClass("submiting");
		//提交
		submitQuestion();
	});

	// 倒计时
	startTimer();
});

// 提交试卷
var exam_done = function() {
	changeField(null, null, null);
	confirm_b4_close = false;
	var alertication = '您可能还有&nbsp;<i style="color:red;">' + unfinishedCount() + '&nbsp; </i>道题目没有完成.要提交试卷吗？';
	if (unfinishedCount() <= 1) alertication = "您已经答完所有题目，现在要交卷吗？";

	layer.confirm(alertication, {
		skin: 'layui-layer-molv',
		icon: 13,
		title: '交卷提示',
		offset: ['30%', '40%']
	}, function(index) {
		layer.close(index);
        finishExam(0);
	});

}

//每答一道题都进行提交
var submitQuestion = function(){
	var answerSheetItems = genrateAnswerSheet();
	var data = new Object();
	var examHistoryId = $('#hist-id').val();
	data.examHistroyId = examHistoryId;
	data.answerSheetItems = answerSheetItems;
	data.examId = $('#exam-id').val();
	data.examPaperId = $('#paper-id').val();

	var request = $.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : ctx+"/exam/student/question-submit",
		data : JSON.stringify(data)
	});

	request.done(function(message, tst, jqXHR) {
		if(message.result == 'fail'){
			layer.msg('您正在被强制提交试卷中。。。', {skin: 'layui-layer-molv',icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: ['30%', '40%'], time:10000}) ;
			finishExam(1);
		}else if(message.result == 'complete'){
			layer.alert('您已提交试卷，得分'+getPoint+'分',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,btn: ['进入考试中心'],shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
				layer.closeAll();
				location.href = '${ctx}/exam/list';
			});
		}
	});

	request.fail(function(jqXHR, textStatus) {
		console.log("系统繁忙请稍后尝试");
	});
}


//结束考试
var finishExam = function(tag){
    var answerSheetItems = genrateAnswerSheet();
    var data = new Object();
    var examHistoryId = $('#hist-id').val();
    data.examHistroyId = examHistoryId;
    data.answerSheetItems = answerSheetItems;
    var timeStr = $('#exam-clock').text();
    var time = timeStr.split(':');
    var hours = parseInt(time[0]);
    var minutes = parseInt(time[1]);
    var seconds = parseInt(time[2]);
    var timestamp = parseInt($("#exam-timestamp").text())*60;
    //data.duration = timestamp - (hours * 3600 + minutes * 60 + seconds);
    data.examId = $('#exam-id').val();
    data.examPaperId = $('#paper-id').val();
	data.startTime = startTime;
	layer.closeAll();
    layer.open({
        type: 3,
        title: ['', false],
        offset: ['30%', '50%']
    });
    var request = $.ajax({
        headers : {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json'
        },
        type : "POST",
        url : ctx+"/exam/student/exam-submit",
        data : JSON.stringify(data)
    });

    request.done(function(message, tst, jqXHR) {

        layer.closeAll('loading');
        if (message.result == "success") {
			//停止倒计时
			window.clearInterval(timer);
			if(islook == '0'){
				if(tag == 0){
					layer.alert('试卷已经提交完毕.请点击按钮退出页面.',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
						location.href = ctx+'/';
					});
				}else{
					layer.alert('您已经被强制提交试卷.请点击按钮退出页面.',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
						location.href = ctx+'/';
					});
				}

			}else if(islook == '1'){
				if(tag == 0){
					layer.alert('试卷已经提交完毕.请点击按钮查看试卷（限时5分钟）.',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,btn: ['查看试卷'],icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
						location.href = ctx+'/exam/exampaper/'+seId+'/'+examId;
					});
				}else{
					layer.alert('您已经被强制提交试卷.请点击按钮查看试卷（限时5分钟）.',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,btn: ['查看试卷'],icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
						location.href = ctx+'/exam/exampaper/'+seId+'/'+examId;
					});
				}

			}

        } else {
            layer.msg(message.result);
        }
    });
    request.fail(function(jqXHR, textStatus) {
        layer.closeAll('loading');
        layer.msg("系统繁忙请稍后尝试");
    });

};

/**
 * 收集考生答案
 */
var genrateAnswerSheet = function(){
    var answerSheetItems = new Array();
    var questions = $('.exam-subject');
    for(var i=0; i<questions.length; i++){
        var answerSheetItem = new Object();
        answerSheetItem.questionId = $(questions[i]).find('input[name="questionId"]').val();
        answerSheetItem.questionPoint = 0;
        answerSheetItem.questionTypeId = $(questions[i]).find('input[name="questionType"]').val();
		var textareas = $(questions[i]).find('textarea');
		if(textareas.length>0){
			answerSheetItem.answer = $(textareas).val();
		}else{
			answerSheetItem.answer = $(questions[i]).find('.exam-subject-myanswer').text();
			answerSheetItem.answer = answerSheetItem.answer.trim().replace(/\s/g,"");
		}
        answerSheetItems.push(answerSheetItem);
    }
    return answerSheetItems;
}

/**
 * 开始倒计时
 */
var startTimer = function() {
    var timestamp = localStorage.getItem(seId+'-key');
	if(timestamp==null || timestamp == undefined  || timestamp<0){
		timestamp = parseInt($("#exam-timestamp").text())*60;
	}
	console.log(timestamp);
    timer = setInterval(function() {
        $("#exam-timestamp").text(timestamp);
        $("#exam-clock").text(toHHMMSS(timestamp));
        if(timestamp < 600){
            var exam_clock = $("#question-time");
            exam_clock.removeClass("question-time-normal");
            exam_clock.addClass("question-time-warning");
        }
        var period = timestamp % 60;
        //console.log("period :" + period);
        timestamp-- || examTimeOut(timer);
		localStorage.setItem(seId+'-key', timestamp);//保存当前考试时间
    }, 1000);
};

// 保存答案
var saveAnswerSheet = function(){
    console.log("answerSheet saving...");
    var answerSheetItems = genrateAnswerSheet();
    var data = new Object();
    var examHistroyId = $("#hist-id").val();
    data.examHistroyId = examHistroyId;
    data.answerSheetItems = answerSheetItems;
    var timeStr = $("#exam-clock").text();
    var time = timeStr.split(":");
    var hours = parseInt(time[0]);
    var minutes = parseInt(time[1]);
    var seconds = parseInt(time[2]);
    data.duration = hours * 3600 + minutes * 60 + seconds;
    data.examId=$("#exam-id").val();
    data.examPaperId=$("#paper-id").val();

    localStorage.answerSheet = JSON.stringify(data);

};

/**
 * 考试时间到
 * @param int
 */
var examTimeOut = function(int){
	localStorage.timestamp = null;
    clearInterval(int);
    finishExam(0);
};

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


// 检查漏题
var check_missing = function() {
	changeField(null, null, null);
	var _cots = $("div#numbercard-container ul li:not(.submited)");
	var panel = '';
	if (unfinishedCount() > 0) panel = '您可能还有&nbsp;<i style="color:red;">' + unfinishedCount() + '</i>&nbsp; 道题目没有完成';
	else panel = '恭喜您，已经答完所有题目！';
	layer.open({
		skin: 'layui-layer-molv',
		title: '检查遗漏',
		content: panel,
		offset: ['30%', '40%']
	});
}

var unfinishedCount = function() {
	changeField(null, null, null);
	var _cots = $("div#numbercard-container ul li:not(.submiting)");
	//var _cots = $("div#numbercard-container ul li:not(.submited)");
	//var _v = _cots.length - submitQueue.size();
    var _v = _cots.length;
	return _v < 0 ? 0 : _v;
}


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
	$("#exam-do-floatbar-hover").css("position", "absolute");
	$("#exam-do-floatbar-hover").css("left", $("#workspace").offset().left-92);
	positionOnTop("#exam-do-floatbar-hover");


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

			var answer_text = "";
			if(answerObj != undefined) answer_text = answerObj.answer;

			for (var obj in options) { //
				bullet += '<label for="{optionID}" {label_selected}><span class="exam-subject-optionNo">{optionNo}</span><input type="radio" name="{fieldName}" value="{optionValue}" id="{optionID}" {option_checked}>{optionText}</label>';
				bullet = bullet.replace(/{optionID}/ig, qindex + '_' + obj);
				bullet = bullet.replace(/{optionNo}/ig, obj);
				bullet = bullet.replace(/{fieldName}/ig, subject.questionId + '_option');
				bullet = bullet.replace(/{optionText}/ig, options[obj]);
				bullet = bullet.replace(/{optionValue}/ig, obj);
				var label_selected = "";
				var option_checked = "";
				if(answer_text==obj) label_selected = "class=\"selected\"";
				bullet = bullet.replace(/{label_selected}/ig, label_selected);
				bullet = bullet.replace(/{option_checked}/ig, option_checked);
			}

			bullet += '<div class="clear"></div></div><div class="exam-subject-answerpanel">您的答案：<span class="exam-subject-myanswer">{answer_text}</span></div></li>';
			if (option_checked_no > -1) answer_text = optionsSequence[option_checked_no];
			bullet = bullet.replace(/{answer_text}/ig, answer_text);

			var cardlicls = "regular";
			if (qindex % 2 == 0) cardlicls = "particular";
			if (answered) cardlicls += " ";
			if (answer_text!="") cardlicls += " submiting";
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
			var bullet = '<li class="exam-subject"><div class="exam-subject-tip">第<span class="exam-subject-no">{no}</span>题，是非题（{score}分）</div><div class="exam-subject-title">{title}</div><div class="exam-subject-option-container">';
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
			var options = {"T":"正确","F":"错误"};

			var answer_text = "";
			if(answerObj != undefined) answer_text = answerObj.answer;

			for (var obj in options) { //
				bullet += '<label for="{optionID}" {label_selected}><span class="exam-subject-optionNo">{optionNo}</span><input type="radio" name="{fieldName}" value="{optionValue}" id="{optionID}" {option_checked}>{optionText}</label>';
				bullet = bullet.replace(/{optionID}/ig, qindex + '_' + obj);
				bullet = bullet.replace(/{optionNo}/ig, obj);
				bullet = bullet.replace(/{fieldName}/ig, subject.questionId + '_option');
				bullet = bullet.replace(/{optionText}/ig, options[obj]);
				bullet = bullet.replace(/{optionValue}/ig, obj);
				var label_selected = "";
				var option_checked = "";
				if(answer_text == obj) label_selected = "class=\"selected\"";
				bullet = bullet.replace(/{label_selected}/ig, label_selected);
				bullet = bullet.replace(/{option_checked}/ig, option_checked);
			}

			bullet += '<div class="clear"></div></div><div class="exam-subject-answerpanel">您的答案：<span class="exam-subject-myanswer">{answer_text}</span></div></li>';
			if (option_checked_no > -1) answer_text = optionsSequence[option_checked_no];
			bullet = bullet.replace(/{answer_text}/ig, answer_text);

			var cardlicls = "regular";
			if (qindex % 2 == 0) cardlicls = "particular";
			if (answered) cardlicls += " ";
			if (answer_text!="") cardlicls += " submiting";
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

			var answer_text = "";
			if(answerObj != undefined) answer_text = answerObj.answer;

			for (var obj in options) { //
				bullet += '<label for="{optionID}" {label_selected}><span class="exam-subject-optionNo">{optionNo}</span><input type="checkbox" name="{fieldName}" value="{optionValue}" id="{optionID}" {option_checked}>{optionText}</label>';
				bullet = bullet.replace(/{optionID}/ig, qindex + '_' + obj);
				bullet = bullet.replace(/{optionNo}/ig, obj);
				bullet = bullet.replace(/{fieldName}/ig, subject.questionId + '_option');
				bullet = bullet.replace(/{optionText}/ig, options[obj]);
				bullet = bullet.replace(/{optionValue}/ig, obj);
				var label_selected = "";
				var option_checked = "";

				if(answer_text.indexOf(obj) > 0) {
					label_selected = "class=\"selected\"";
					option_checked = "checked";
				}

				bullet = bullet.replace(/{label_selected}/ig, label_selected);
				bullet = bullet.replace(/{option_checked}/ig, option_checked);
			}
			bullet += '<div class="clear"></div></div><div class="exam-subject-answerpanel">您的答案：<span class="exam-subject-myanswer">{answer_text}</span></div></li>';
			bullet = bullet.replace(/{answer_text}/ig, answer_text);

			var cardlicls = "regular";
			if (qindex % 2 == 0) cardlicls = "particular";
			if (answered) cardlicls += " ";
			if (answer_text!="") cardlicls += " submiting";
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
			var bullet = '<li class="exam-subject"><div class="exam-subject-tip">第<span class="exam-subject-no">{no}</span>题，填空题（{score}分）</div><div class="exam-subject-title">{title}</div>';

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

			var answer_text = "";
			if(answerObj != undefined) answer_text = answerObj.answer;

			var answered = (subject.answer!=null&&subject.answer.length>0);
			if(subject.answer != null && subject.answer.length>0) option_checked_text=subject.answer;
			bullet+='<div class="exam-subject-answerpanel">你的答案：<br/><textarea name="{fieldName}" id="{fieldName}">{textareaValue}</textarea></div>';
			bullet=bullet.replace(/{fieldName}/ig,"question_"+subject.questionId);
			bullet=bullet.replace(/{textareaValue}/ig,answer_text);

			var cardlicls = "regular";
			if(qindex % 2 == 0) cardlicls = "particular";
			if(answered) cardlicls+=" ";
			if(answer_text!="") cardlicls+=" submiting";
			$("ul#exam-paper").append(bullet);
			$("div#numbercard-container ul").append('<li class="'+cardlicls+'">'+qindex+'</li>');
		}
	}
}

//加载试题答案
var loadAnswers = function() {
	if (answerData != null) {
		var answerDatas = answerData.answerSheetItems;
		for (var i = 0; i < answerDatas.length; i++) {
			var qid = answerDatas[i].questionId;
			answerMap.put('qid_' + qid, answerDatas[i]);
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

	 if(disorganize == 0){//打乱试题顺序
		 var arr = [];
		 for(var i=0; i<paperData.length; i++){
			 arr[i] = i;
		 }
		 arr.sort(function(){ return 0.5 - Math.random() });
		 for(var i=0; i<arr.length; i++){
			 var j = arr[i];
			 var qtype = paperData[j].questionTypeId;
			 switch (qtype) {
				 case 1:
					 radioSubjectList.push(paperData[j]);
					 break;
				 case 2:
					 checkSubjectList.push(paperData[j]);
					 break;
				 case 3:
					 determineSubjectList.push(paperData[j]);
					 break;
				 case 4:
					 textareaSubjectList.push(paperData[j]);
			 }
		 }
	 }else{
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
	 }


     //分别加载各个题型
     construtRadioSubject(radioSubjectList, 0); //单选题
     construtCheckboxSubject(checkSubjectList, radioSubjectList.length); //多选题
	 construtDetermineSubject(determineSubjectList, radioSubjectList.length+checkSubjectList.length); //是非题
	 construtTextareaSubject(textareaSubjectList, radioSubjectList.length+checkSubjectList.length+determineSubjectList.length);//填空题

}

//debug
var debug = function(msg) {
	$("#notice-container").html(msg + "<br/>" + $("#notice-container").html());
}

/*
//unload alert
$(window).bind('beforeunload', function(e) {
	if (confirm_b4_close) return "您正在强制刷新(或关闭)本页，可能会造成答题无法正常提交，影响到您最终的考试成绩。\n建议您取消本次刷新（或关闭）操作，通过页面的的'提交试卷'按钮退出考试。";
});*/
