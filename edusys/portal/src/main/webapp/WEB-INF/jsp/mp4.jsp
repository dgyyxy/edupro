<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <title>player</title>
    <style type="text/css">
        video::-webkit-media-controls-enclosure {
            overflow: hidden;
        }

        video::-webkit-media-controls-panel {
            width: calc(100% + 30px);
        }

        video {
            object-fit: fill;
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body style="background-color:white;padding:0px;margin:0px;">
<div class="vid-wrappper">
    <!-- HTML5 video tag -->
    <video controls="controls" preload="auto" autoplay="autoplay">
        <!-- .mp4 file for native playback in IE9+, Firefox, Chrome, Safari and most mobile browsers -->
        <source src="${uri}" type="video/mp4"/>
        <!-- flash fallback for IE6, IE7, IE8 and Opera -->
        <object type="application/x-shockwave-flash"
                data="${ctx}/resources/swf/flowplayer-3.2.18.swf">
            <param name="movie" value="${ctx}/resources/swf/flowplayer-3.2.18.swf"/>
            <param name="allowFullScreen" value="true"/>
            <param name="wmode" value="transparent"/>
            <!-- note the encoded path to the image and video files, relative to the .swf! -->
            <!-- more on that here: http://en.wikipedia.org/wiki/Percent-encoding -->
            <param name="flashVars"
                   value="config={'playlist':[{'url':'${flashUrl}','autoPlay':false}]}"/>
        </object>
    </video>
</div>

<jsp:include page="common/js.jsp" flush="true"/>
</body>

<script>
    var md = document.getElementsByTagName("video")[0];
    md.addEventListener("ended", function () {
        var jobId = window.parent.jobId;
        var courseId = window.parent.courseId;
        var userId = window.parent.userId;

        var status = window.parent.status;
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

    })

</script>
</html>
