<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="航空CBT在线学习系统">
    <meta name="author" content="Gary Duan，gary_duan@yeah.net">
    <title>航空CBT在线学习系统</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/exam-do.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/js/layer/skin/default/layer.css">
</head>
<div id="bodywrapper">
    <div id="leftwrapper">
        <div id="menuwapper">
            <div id="myinfor-wrapper">
                <input type="hidden" id="hist-id" value="${stuExam.id}"/>
                <input type="hidden" id="exam-id" value="${stuExam.examId}"/>
                <input type="hidden" id="paper-id" value="${stuExam.paperId}"/>
                <div class="exam-do-300-round-top"></div>
                <div id="myinfor-container">

                    <div id="myinfor-title">&nbsp;${exam.examName}</div>
                    <table width="298" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td width="172" height="17"><label>姓名：</label><span>${user.stuName}</span></td>
                            <td width="55" rowspan="4" align="center" style="border-left: 1px solid green;">
                                剩余时间
                                <span id="exam-clock" class="left-time"></span>
                            </td>
                        </tr>
                        <tr>
                            <td height="17"><label>身份证号：</label><span>${user.cardNo}</span></td>
                        </tr>
                        <tr>
                            <td height="17"><label>总分：</label>
                                <span>${stuExam.point}</span></td>
                        </tr>
                        <tr>
                            <td><label>限时：</label>
                                <span id="exam-timestamp" style="display: none">${stuExam.duration}</span> <span>${stuExam.duration}</span>（分钟）</td>
                        </tr>
                    </table>

                </div>
                <div class="exam-do-300-round-bottom"></div>
            </div>

            <div id="numbercard-wrapper">
                <div class="exam-do-300-round-block-top">题号</div>
                <div id="numbercard-container">
                    <ul>
                        <li class="regular submiting">1</li>
                        <li class="particular submiting">2</li>
                        <li class="regular">3</li>
                        <li class="particular">4</li>
                    </ul>
                    <div class="clear"></div>
                </div>
                <div class="exam-do-300-round-bottom"></div>

            </div>

            <div id="buttons-wrapper">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="33%" align="center"><a href="#" target="_self" id="checkmissing_bt"><img src="${ctx}/resources/img/exam-do-check-bt.png" width="94" height="25" border="0" /></a></td>
                        <td width="36%" align="center"><a href="#" target="_self" id="exam_done_bt"><img src="${ctx}/resources/img/exam-do-submit-bt.png" width="94" height="25" border="0" /></a></td>
                    </tr>
                </table>
            </div>

            <div id="notice-wrapper">
                <div class="exam-do-300-round-block-top">提示信息</div>
                <div id="notice-container">
                    <ul>
                        <li>点击“关闭面板”可将试卷左侧的面板关闭；</li>
                        <li>点击题号，可以快速跳到该题；</li>
                        <li>答题时严禁刷新和离开页面，否则中止考试；</li>
                        <li>答题完成，请点击“提交试卷”完成考试；</li>
                        <li>如您未在限时内完成，系统将强制交卷。</li>
                    </ul>
                    <div class="clear"></div>
                </div>
                <div class="exam-do-300-round-bottom"></div>
            </div>

            <div id="exam-do-floatbar">
                <a href="#" target="_self" class="previoussubject">上一题</a>
                <a href="#" target="_self" class="nextsubject">下一题</a>
                <a href="#" target="_self" class="leftmenutrriger">关闭面板</a>
            </div>
            <div id="exam-do-floor"></div>
        </div>
        <div class="clear">&nbsp;</div>
    </div>
    <div id="workspace">
        <div class="exam-do-700-round-top"></div>
        <div id="workspace-container">
            <ul id="exam-paper">
                <!--subject repeat end-->
            </ul>
            <div class="clear"></div>
        </div>

        <div class="exam-do-700-round-bottom"></div>
    </div>
    <div class="clear"><!--nothing--></div>
    <div id="exam-do-floatbar-hover" hidden>
        <a href="#" target="_self" class="leftmenutrriger">打开面板</a>
    </div>
</div>
<div id="info-panel-hid">
    <div id="info-panel-msg"></div>
</div>

<script>
    var startTime = new Date();
    var ctx = '${ctx}';
    var paperDatastr = JSON.stringify(${stuExam.content});
    var islook = '${exam.islook}';
    var seId = '${stuExam.id}';
    var examId = '${stuExam.examId}';
    var paperData = eval('('+paperDatastr+')');
    var answerDatastr = JSON.stringify(${stuExam.answerSheet});
    var answerData = null;
    if(answerDatastr != undefined) answerData = eval('('+answerDatastr+')');
    var disorganize = '${exam.disorganize}';//是否打乱试题顺序

    var examStatus = parseInt('${stuExam.approved}');
    var getPoint = '${stuExam.pointGet}';
    if(examStatus>1){

        layer.alert('您已提交试卷，得分${stuExam.pointGet}分',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:0,btn: ['进入考试中心'],shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
            layer.closeAll();
            location.href = '${ctx}/exam/list';
        });
    }

    if(document.addEventListener){
        document.addEventListener('webkitvisibilitychange', function(){
            var show = document.webkitVisibilityState;
            if(show === 'hidden'){
                layer.open({
                    skin: 'layui-layer-molv',
                    title: '提醒',
                    content: "严禁在考试时离开本页面，否则系统将自动交卷并中止您的考试！",
                    offset: ['30%', '40%']
                });
                return;
            }
        });
    }


</script>

<script src="${ctx}/resources/js/jquery.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/jquery/jquery.ui.core.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/jquery/jquery.effects.core.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/jquery/jquery.effects.transfer.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/Map.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/prevent.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/exam_do.js" type="text/javascript" charset="utf-8"></script>

<script>
    jQuery(document).ready(function () {
        if (window.history && window.history.pushState) {
            $(window).on('popstate', function () {
                /// 当点击浏览器的 后退和前进按钮 时才会被触发，
                window.history.pushState('forward', null, '');
                window.history.forward(1);
            });
        }
        //
        window.history.pushState('forward', null, '');  //在IE中必须得有这两行
        window.history.forward(1);
    });
</script>
</body>
</html>