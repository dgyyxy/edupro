package com.edusys.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduRole;
import com.edu.common.dao.model.EduRoleExample;
import com.edu.common.dao.model.EduRolePermission;
import com.edu.common.dao.model.EduRolePermissionExample;
import com.edu.common.validator.LengthValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduRolePermissionService;
import com.edusys.manager.service.EduRoleService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 * Created by Gary on 2017/4/6.
 */
@Controller
@Api(value = "角色管理", description = "角色管理")
@RequestMapping("/manage/role")
public class RoleController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private EduRoleService roleService;
    @Autowired
    private EduRolePermissionService rolePermissionService;

    @ApiOperation(value = "角色首页")
    @RequiresPermissions("sys:role:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/role/index.jsp";
    }

    @ApiOperation(value = "角色列表")
    @RequiresPermissions("sys:role:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduRoleExample roleExample = new EduRoleExample();
        roleExample.setOffset(offset);
        roleExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            roleExample.setOrderByClause(sort + " " + order);
        }
        if (StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            roleExample.or(roleExample.createCriteria().andNameLike(search));
            roleExample.or(roleExample.createCriteria().andTitleLike(search));
        }
        List<EduRole> rows = roleService.selectByExample(roleExample);
        long total = roleService.countByExample(roleExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增角色")
    @RequiresPermissions("sys:role:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "/manage/role/create.jsp";
    }

    @ApiOperation(value = "新增角色")
    @RequiresPermissions("sys:role:create")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object create(EduRole role) {
        ComplexResult result = FluentValidator.checkAll()
                .on(role.getName(), new LengthValidator(1, 20, "名称"))
                .on(role.getTitle(), new LengthValidator(1, 20, "标题"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        long time = System.currentTimeMillis();
        role.setCtime(time);
        int count = roleService.insertSelective(role);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除角色")
    @RequiresPermissions("sys:role:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = roleService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改角色")
    @RequiresPermissions("sys:role:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        EduRole role = roleService.selectByPrimaryKey(id);
        modelMap.put("role", role);
        return "/manage/role/update.jsp";
    }

    @ApiOperation(value = "修改角色")
    @RequiresPermissions("sys:role:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduRole role) {
        ComplexResult result = FluentValidator.checkAll()
                .on(role.getName(), new LengthValidator(1, 20, "名称"))
                .on(role.getTitle(), new LengthValidator(1, 20, "标题"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        role.setRoleId(id);
        int count = roleService.updateByPrimaryKeySelective(role);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "角色权限")
    @RequiresPermissions("sys:role:permission")
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.GET)
    public String permission(@PathVariable("id") int id, ModelMap modelMap) {
        EduRole role = roleService.selectByPrimaryKey(id);
        modelMap.put("role", role);
        return "/manage/role/permission.jsp";
    }

    @ApiOperation(value = "角色权限")
    @RequiresPermissions("sys:role:permission")
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object permission(@PathVariable("id") int id, HttpServletRequest request) {
        JSONArray datas = JSONArray.parseArray(request.getParameter("datas"));
        JSONArray oldDatas = JSONArray.parseArray(request.getParameter("oldDatas"));

        // 减权限
        List<Integer> deleteIds = new ArrayList<>();
        if(oldDatas!=null && oldDatas.size()>0) {
            for (int i = 0; i < oldDatas.size(); i++) {
                JSONObject json = oldDatas.getJSONObject(i);
                deleteIds.add(json.getIntValue("id"));
            }
            EduRolePermissionExample rolePermissionExample = new EduRolePermissionExample();
            rolePermissionExample.createCriteria()
                    .andRoleIdEqualTo(id);
            rolePermissionExample.createCriteria()
                    .andPermissionIdIn(deleteIds);
            rolePermissionService.deleteByExample(rolePermissionExample);
        }

        //新增权限
        for (int i = 0; i < datas.size(); i ++) {
            JSONObject json = datas.getJSONObject(i);
            // 加权限
            EduRolePermission rolePermission = new EduRolePermission();
            rolePermission.setRoleId(id);
            rolePermission.setPermissionId(json.getIntValue("id"));
            rolePermissionService.insertSelective(rolePermission);
        }
        return new SysResult(SysResultConstant.SUCCESS, datas.size());
    }


}
