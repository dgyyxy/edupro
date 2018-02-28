<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="permissionDialog" class="crudDialog">
	<form id="permissionForm" method="post">
		<div class="form-group">
			<ul id="ztree" class="ztree"></ul>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="permissionSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="permissionDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
var oldDatas = [];
var setting = {
	check: {
		enable: true,
		// 勾选关联父，取消关联子
		chkboxType: { "Y" : "p", "N" : "s" }
	},
	async: {
		enable: true,
		url: '${basePath}/manage/permission/role/' + roleId,
		dataFilter: function(treeId, parentNode, datas) {
			var nodes = [];
			for (var i = 0; i < datas.permissions.length; i ++) {
				var node = {};
				node.id = datas.permissions[i].permissionId;
				node.pId = datas.permissions[i].pid;
				node.name = datas.permissions[i].name;
				node.open = true;
				// 是否已拥有
				for (var j = 0; j < datas.rolePermissions.length; j ++) {
					if (datas.permissions[i].permissionId == datas.rolePermissions[j].permissionId) {
                        var checkNode = {};
                        checkNode.id = datas.permissions[i].permissionId;
                        oldDatas.push(checkNode);
						node.checked = true;
					}
				}
				nodes.push(node)
			}
			return nodes;
		}
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
function initTree() {
	$.fn.zTree.init($('#ztree'), setting);
}

function getCheckNodes(){
    var checkNodes = [];
    var zTree = $.fn.zTree.getZTreeObj("ztree")
    var checkDatas = zTree.getCheckedNodes(true);
    for (var i=0; i<checkDatas.length; i++){
        var node = {};
        if(checkDatas[i].checked){
            node.id = checkDatas[i].id;
            checkNodes.push(node);
        }
    }
    return checkNodes;
}

function permissionSubmit() {
    var loading;
    $.ajax({
        type: 'post',
        url: '${basePath}/manage/role/permission/' + roleId,
        data: {datas: JSON.stringify(getCheckNodes()),oldDatas:JSON.stringify(oldDatas), roleId: roleId},
        beforeSend: function(){
            loading = $.dialog({
                theme: 'white',
                animation: 'rotateX',
                closeAnimation: 'rotateX',
                title: false,
                closeIcon: false,
                content: "正在处理，请稍等。。。"
            });
        },
        success: function(result) {
            loading.close();
			if (result.code != 1) {
				if (result.data instanceof Array) {
					$.each(result.data, function(index, value) {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: value.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
					});
				} else {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: result.data.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
				}
			} else {
				permissionDialog.close();
				$table.bootstrapTable('refresh');
			}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            loading.close();
			$.confirm({
				theme: 'dark',
				animation: 'rotateX',
				closeAnimation: 'rotateX',
				title: false,
				content: textStatus,
				buttons: {
					confirm: {
						text: '确认',
						btnClass: 'waves-effect waves-button waves-light'
					}
				}
			});
        }
    });
}
</script>