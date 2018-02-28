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
                                得分<br/>
                                <span id="score_get" class="left-time" style="font-size: 26px; font-weight: bold;font-style:italic;text-decoration: underline;"></span>
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

<%--
            <div id="notice-wrapper">
                <div class="exam-do-300-round-block-top">提示信息</div>
                <div id="notice-container">
                    <ul>
                        <li>点击“关闭面板”可将试卷左侧的面板关闭；</li>
                        <li>点击题号，可以快速跳到该题；</li>
                        <li>答题过程中，请不要刷新页面；</li>
                        <li>答题完成，请点击“提交试卷”完成考试；</li>
                        <li>如您未在限时内完成，系统将强制交卷。</li>
                    </ul>
                    <div class="clear"></div>
                </div>
                <div class="exam-do-300-round-bottom"></div>
            </div>--%>
            <div id="exam-do-floatbar">
                <a href="${ctx}/exam/list" target="_self" class="nextsubject">退出查看</a>
            </div>

            <%--<div id="exam-do-floatbar">
                <a href="#" target="_self" class="nextsubject">下一题</a>
                <a href="#" target="_self" class="previoussubject">上一题</a>
            </div>--%>
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

</div>
<div id="info-panel-hid">
    <div id="info-panel-msg"></div>
</div>



<script src="${ctx}/resources/js/jquery.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/jquery/jquery.ui.core.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/jquery/jquery.effects.core.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/jquery/jquery.effects.transfer.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/Map.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/prevent.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/js/exam_paper.js" type="text/javascript" charset="utf-8"></script>
<script>
    var ctx = '${ctx}';
    var paperDatastr = JSON.stringify(${stuExam.content});
    var paperData = eval('('+paperDatastr+')');

    var answerDatastr = JSON.stringify(${stuExam.answerSheet});
    var answerData = eval('('+answerDatastr+')');
    if(answerData != undefined){
        $('#score_get').text(answerData.pointRaw);
        var time = '${time}';
        if(time == '1'){
            layer.alert('您已经查看过试卷，不允许再次查看！',{skin: 'layui-layer-molv',shade: [1,'#696969'],icon:1, closeBtn: 0,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                location.href = ctx+'/exam/list';
            });
        }else{
            var timer = window.setTimeout('showTip()', 5*60*1000);
            var showTip = function(){
                layer.alert('查看试卷的时间结束了！',{skin: 'layui-layer-molv',shade: [0.9,'#696969'],closeBtn: 0,icon:1,shadeClose: false,title:'提示', offset: ['30%', '40%']}, function(){
                    location.href = ctx+'/exam/list';
                });
            };
        }
    }


</script>


</body>
</html>