<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <title>player exec</title>
</head>
<body style="margin: 0px; padding: 0px;">
<img src="${ctx}/resources/myimg/study_end.jpg" width="980" height="556">
<script src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript">
    window.onload = function(){
        var jobId = window.parent.parent.jobId;
        var courseId = window.parent.parent.courseId;
        var userId = window.parent.parent.userId;

        var status = window.parent.parent.status;
        if(jobId!=undefined && userId!=undefined){
            if(status != 2){
                //记录学习
                $.ajax({
                    type: 'get',
                    url: '${ctx}/course/study-end/' + userId + '/' + jobId + '/' + courseId,
                    success: function (result) {
                        //alert(result);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        //layer.msg(textStatus);
                    }
                });
            }
        }
    }

</script>
</body>
</html>