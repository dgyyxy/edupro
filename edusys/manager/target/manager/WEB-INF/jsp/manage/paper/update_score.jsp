<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8" %>
<div id="updateScoreDialog" class="crudDialog">
    <form id="updateScoreForm" method="post">
        <div class="form-group">
            <label for="score">分数</label>
            <input name="score" class="form-control" id="score" type="text"/>
        </div>
        <div class="form-line">

        </div>
        <div class="form-group text-right dialog-buttons">
            <button class="btn btn-sm btn-success btn-icon-text waves-effect" id="update-point-btn"><i
                    class="zmdi zmdi-check"></i>仅修改第<span
                    id="qt-point-target-index"></span>题
            </button>
            <button class="btn btn-sm btn-warning btn-icon-text waves-effect" id="update-point-type-btn"><i
                    class="zmdi zmdi-check-all"></i>批量更新该题型
            </button>
        </div>
    </form>
</div>
<script>
    $(function(){
        $("#update-point-btn").click(function(){
            var targetno = parseInt($("#qt-point-target-index").text());
            console.log(targetno);
            var newPoint = parseFloat($("#score").val());
            if(targetno<=0 || isNaN(targetno)||newPoint<=0||isNaN(newPoint)){
                return false;
            }else{
                $('#no_'+targetno).parent().parent().find(".question-point").text(newPoint);
                examing.refreshTotalPoint();
                updateScoreDialog.close();
                return false;
            }
        });

        $("#update-point-type-btn").click(function(){
            var targetno = parseInt($("#qt-point-target-index").text());
            var newPoint = parseFloat($("#score").val());
            if(targetno<=0 || isNaN(targetno)||newPoint<=0||isNaN(newPoint)){
                return false;
            }else{
                var qt_type = $('#no_'+targetno).parent().parent().find(".question-type").text();
                $("li.qt-" + qt_type + " .question-point").text(newPoint);
                examing.refreshTotalPoint();
                updateScoreDialog.close();
                return false;
            }
        });
    });
</script>