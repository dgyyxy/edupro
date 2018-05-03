<%@ page contentType="text/html; charset=utf-8" %>
<div id="ruleListDialog" class="crudDialog">
    <div id="subtoolbar">
        <a class="waves-effect waves-button green" href="javascript:;" onclick="submitRule();"><i class="zmdi zmdi-check"></i> 确认发布</a>
        <a class="waves-effect waves-button orange" href="javascript:closeModel();"><i class="zmdi zmdi-view-comfy"></i> 返回编辑</a>
    </div>
    <table id="subtable"></table>
</div>
<script>
    var $subtable = $('#subtable');
    $(function() {
        // bootstrap table初始化
        $subtable.bootstrapTable({
            data: showList(),
            height: getHeight(),
            striped: true,
            search: false,
            strictSearch: false,
            showRefresh: false,
            showColumns: false,
            minimumCountColumns: 2,
            clickToSelect: false,
            detailView: false,
            pagination: false,
            paginationLoop: false,
            silentSort: false,
            smartDisplay: false,
            escape: true,
            idField: 'id',
            maintainSelected: true,
            toolbar: '#subtoolbar',
            columns: [
                {field: 'id', title: '序号', sortable: true, align: 'center'},
                {field: 'categoryName', title: '被选题库'},
                {field: 'question1', title: '判断题'},
                {field: 'point1', title: '分值'},
                {field: 'question2', title: '单选题'},
                {field: 'point2', title: '分值'},
                {field: 'question3', title: '多选题'},
                {field: 'point3', title: '分值'},
                {field: 'question4', title: '填空题'},
                {field: 'point4', title: '分值'}

            ]
        });
    });

    // 格式化操作按钮
    function actionFormatter(value, row, index) {
        return [
            '<a class="update" href="javascript:;" onclick="updateFun('+row.id+')" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
            '<a class="delete" href="javascript:;" onclick="deleteFun('+row.id+')" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
        ].join('');
    }

    function deleteFun(id){
        questionRuleMap.remove('qtype_'+id);
        showMap.remove('showQuestion_'+id);
        alert(showMap.size());
        $subtable.bootstrapTable('refresh', {data: showList()});
    }

    function closeModel(){
        clearEntity();
        findEntity(typeId);
        ruleListDialog.close();
    }

    function updateFun(id){
        ruleListDialog.close();
        $('#tree').treeview('selectNode', [ id, { silent: true } ]);
    }

</script>