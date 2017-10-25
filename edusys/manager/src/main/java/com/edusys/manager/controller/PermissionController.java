package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduPermission;
import com.edu.common.dao.model.EduPermissionExample;
import com.edu.common.dao.model.EduRolePermission;
import com.edu.common.validator.LengthValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduPermissionService;
import com.edusys.manager.service.SysApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限Controller
 * Created by Gary on 2017/4/1.
 */
@Controller
@Api(value = "权限管理", description = "权限管理模块")
@RequestMapping("/manage/permission")
public class PermissionController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private EduPermissionService permissionService;
    @Autowired
    private SysApiService apiService;

    @ApiOperation(value = "权限首页")
    @RequiresPermissions("sys:permission:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/manage/permission/index.jsp";
    }

    @ApiOperation(value = "权限列表")
    @RequiresPermissions("sys:permission:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
           @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
           @RequestParam(required = false, defaultValue = "0", value = "type") int type,
           @RequestParam(required = false, value = "sort") String sort,
           @RequestParam(required = false, value = "order") String order,
            String search){
        EduPermissionExample permissionExample = new EduPermissionExample();
        EduPermissionExample.Criteria criteria = permissionExample.createCriteria();
        if(0 != type){
            criteria.andTypeEqualTo((byte) type);
        }
        permissionExample.setOffset(offset);
        permissionExample.setLimit(limit);
        if(!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)){
            permissionExample.setOrderByClause(sort + " " + order);
        }
        if (StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            permissionExample.or(permissionExample.createCriteria().andNameLike(search));
            permissionExample.or(permissionExample.createCriteria().andPermissionValueLike(search));
        }
        List<EduPermission> rows = permissionService.selectByExample(permissionExample);
        long total = permissionService.countByExample(permissionExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;

    }

    @ApiOperation(value = "角色权限列表")
    @RequiresPermissions("sys:permission:read")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object role(@PathVariable("id") int id) {
        // 所有正常权限
        EduPermissionExample upmsPermissionExample = new EduPermissionExample();
        upmsPermissionExample.createCriteria()
                .andStatusEqualTo((byte) 1);
        upmsPermissionExample.setOrderByClause("orders asc");
        List<EduPermission> permissions = permissionService.selectByExample(upmsPermissionExample);
        // 角色已有权限
        List<EduRolePermission> rolePermissions = apiService.selectRolePermisstionByRoleId(id);
        // 返回结果集
        Map result = new HashMap();
        result.put("permissions", permissions);
        result.put("rolePermissions", rolePermissions);
        return result;
    }


    @ApiOperation(value = "新增权限")
    @RequiresPermissions("sys:permission:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap modelMap) {
        return "/manage/permission/create.jsp";
    }

    @ApiOperation(value = "新增权限")
    @RequiresPermissions("sys:permission:create")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object create(EduPermission permission) {
        ComplexResult result = FluentValidator.checkAll()
                .on(permission.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        long time = System.currentTimeMillis();
        permission.setCtime(time);
        permission.setOrders(time);
        int count = permissionService.insertSelective(permission);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除权限")
    @RequiresPermissions("sys:permission:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = permissionService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改权限")
    @RequiresPermissions("sys:permission:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        EduPermission permission = permissionService.selectByPrimaryKey(id);
        modelMap.put("permission", permission);
        return "/manage/permission/update.jsp";
    }

    @ApiOperation(value = "修改权限")
    @RequiresPermissions("sys:permission:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduPermission upmsPermission) {
        ComplexResult result = FluentValidator.checkAll()
                .on(upmsPermission.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        upmsPermission.setPermissionId(id);
        int count = permissionService.updateByPrimaryKeySelective(upmsPermission);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }
}
