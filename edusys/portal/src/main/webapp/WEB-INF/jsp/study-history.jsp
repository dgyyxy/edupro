<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="alert alert-success alert-dismissable">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    <strong>提示：</strong> 请点击“课件数”展开课件列表。
</div>
<fieldset class="aform animated fadeInLeft">
    <legend>您已完成<i style="color:red;">${finishCount}</i>个学习任务，已学<i style="color:red;">${studyCount}</i>个课件，总学时：<i style="color:red;" class="time">${sumTime}</i>
    </legend>
    <div class="fieldset-body">
        <table class="table table-striped table-hover" id="jobtables">
            <thead>
            <tr>
                <th>任务名称</th>
                <th>学时</th>
                <th>学习进度</th>
                <th>用时</th>
                <th>已学课件数</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="item">
            <tr>
                <td>${item.jobName}</td>
                <td>${item.duration}分钟</td>
                <td class="percent">${item.courseNum}-${item.totalCourse}</td>
                <td class="time">${item.time}</td>
                <td><span class="label label-success" style="cursor: pointer;" data-job-name="${item.jobName}" data-user-id="${item.stuId}" data-job-id="${item.jobId}">${map[item.jobId]}</span></td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</fieldset>
<script>
    $(function(){
        $('.time').each(function(){
            var timestr = $(this).text();
            $(this).text(millisecondToDate(timestr));
        });

        $('.percent').each(function(){
            var array = $(this).text().split('-');
            $(this).text(getPercent(array[0],array[1]));
        });
    });
</script>