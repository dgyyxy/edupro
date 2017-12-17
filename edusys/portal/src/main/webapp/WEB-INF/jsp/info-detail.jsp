<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
    i {
        font-style: normal;
        font-weight: bold;
    }
</style>
<div class="info-item-list l row animated fadeInLeft">
    <div class="row">
        <div class="col-md-10"><a href="javascript:void(0);" style="float:right;cursor:pointer;" data-toggle="modal" data-target="#issuesSet">安全问题</a></div>
        <div class="col-md-1"><a href="javascript:void(0);" style="float:right;cursor:pointer;" data-toggle="modal" data-target="#updatePassword">修改密码</a></div>
        <div class="col-md-1"><a href="javascript:void(0);" style="float:right;cursor:pointer;" data-toggle="modal" data-target="#editStudent">完善资料</a></div>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-user"></i>&nbsp;</a>&nbsp;姓名：</i>
        <span>${student.stuName}</span>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-credit-card"></i>&nbsp;</a>&nbsp;身份证号：</i>
        <span>${student.cardNo}</span>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-hospital"></i>&nbsp;</a>&nbsp;学号：</i>
        <span id="stuNoSpan">${student.stuNo}</span>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-phone"></i>&nbsp;</a>&nbsp;手机号：</i>
        <span id="phoneSpan">${student.phone}</span>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-sitemap"></i>&nbsp;</a>&nbsp;一级机构：</i>
        <span>${student.organizationName1}</span>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-sitemap"></i>&nbsp;</a>&nbsp;二级机构：</i>
        <span>${student.organizationName2}</span>
    </div>
    <div class="info-item col-md-6">
        <i class="info-label text-right"><a class="btn-default btn-small btn-circle" href="javascript:void(0);">&nbsp;<i class="icon-sitemap"></i>&nbsp;</a>&nbsp;公司名称：</i>
        <span>${student.company}</span>
    </div>
</div>

