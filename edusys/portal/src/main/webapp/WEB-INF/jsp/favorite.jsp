<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="ipstr" value="${pageContext.request.getServerName()}"/>
<div class="row input-append animated fadeInLeft">
    <a href="javascript:void(0);" data-filter="*" class="selected"><i class="icon icon-reorder"></i> 您目前共收藏 <span class="badge badge-warning">${total}</span>&nbsp;个课件</a>
    &nbsp;&nbsp;
    <input type="text" id="courseName" placeholder="Search…" class="input-medium">
    <button id="searchBtn" class="btn btn-success" type="button"><i class="icon icon-search"></i>搜索</button>
</div>
<div class="row moco-course-list animated fadeInLeft">
    <c:forEach items="${list}" var="item">
    <div class="moco-course-wrap">
        <div class="moco-course-box">
            <%--<div class="course-card-learning">
                <c:if test="${item.status==0}">未学</c:if>
                <c:if test="${item.status==1}">学习中</c:if>
                <c:if test="${item.status==2}">已学完</c:if>
            </div>--%>
            <img alt="" src="http://${ipstr}/upload/courseware/${item.picture}" height="124" width="100%">
            <div class="moco-course-intro">
                <h3>
                    ${item.courseName}
                </h3>
                <p>${item.courseContent}</p>
            </div>
            <div class="moco-course-bottom">
                <span class="l color-red"><i class="icon icon-heart"></i></span>
                <span class="m playshow" data-name="${item.courseName}" data-job-id="${item.jobId}" data-id="${item.courseId}" data-uri="${item.uriStr}" data-time="${item.timeval}">进入学习</span>
                <span class="r">${item.duration}分</span>
            </div>
        </div>
    </div>
    </c:forEach>

</div>

<div class="row">
    <div class="row pagediv">
        ${pageHtml}
    </div>
</div>



<script>
    $(function(){
        $('#searchBtn').click(function(){
            var courseName = $('#courseName').val();
            loadFrame('${ctx}/info/favorite?courseName='+courseName);
        });

        var courseId = 0;
        var jobId = 0;

        //进入学习
        $('.playshow').each(function(){
            var button = $(this);
            $(this).click(function(){
                var uri = button.data('uri');
                courseId = button.data('id');
                jobId = button.data('job-id');

                var courseName = button.data('name');
                var filetype = uri.substring(uri.lastIndexOf('.')+1);

                uri = 'http://${ipstr}/upload/courseware/'+uri;

                if(filetype == 'mp4'){
                    uri = '${ctx}/course/player?uri='+encodeURI(uri);
                }else if(filetype == 'pdf'){
                    uri = 'http://${ipstr}/upload/courseware/'+button.data('uri');
                }else{
                    uri = 'http://${ipstr}/upload/'+button.data('uri');
                }

                layer.open({
                    type: 2,
                    title: courseName,
                    resize: true,
                    skin: 'layui-layer-molv',
                    shade: [0.9,'#696969'],
                    area: ['980px', '600px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: uri,
                    success: function(layero, index){
                        //禁用最小化按钮
                        $('a.layui-layer-min').hide();

                    }
                });

            });
        });
    });
</script>