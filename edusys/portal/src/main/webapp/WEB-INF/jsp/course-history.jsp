<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fieldset class="bform animated fadeInLeft">
    <legend><span id="jobName"></span>&nbsp;»&nbsp;已学课件列表&nbsp;&nbsp;&nbsp;<label id="backBtn" class="back-btn">返回</label></legend>
    <div class="fieldset-body">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>课件名</th>
                <th>课时</th>
                <th>用时</th>
                <th>学习状态</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="item">
            <tr>
                <td>${item.courseName}</td>
                <td>${item.duration}分钟</td>
                <td class="time">${item.time}</td>
                <td>
                    <c:if test="${item.status==0}">未学</c:if>
                    <c:if test="${item.status==1}">学习中</c:if>
                    <c:if test="${item.status==2}">已学完</c:if>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</fieldset>
<script>
    $(function(){
        $('#jobName').text(jobName);

        $('#backBtn').click(function(){
            loadFrame('study-history.html');
        });

        $('.time').each(function(){
            var timestr = $(this).text();
            $(this).text(millisecondToDate(timestr));
        });
    });
</script>



