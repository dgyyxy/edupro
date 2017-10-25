package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.util.MD5Util;
import com.edu.common.validator.LengthValidator;
import com.edu.common.validator.NotNullValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户Controller
 * Created by Gary on 2017/4/6.
 */
@Controller
@Api(value = "用户管理", description = "用户（管理员和老师）管理")
@RequestMapping("/manage/user")
public class UserController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EduUserService userService;
    @Autowired
    private EduRoleService roleService;
    @Autowired
    private EduOrganizationService organizationService;
    @Autowired
    private EduUserOrganizationService userOrganizationService;
    @Autowired
    private EduUserRoleService userRoleService;

    @ApiOperation(value = "用户首页")
    @RequiresPermissions("sys:user:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/user/index.jsp";
    }

    @ApiOperation(value = "用户列表")
    @RequiresPermissions("sys:user:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduUserExample userExample = new EduUserExample();
        userExample.setOffset(offset);
        userExample.setLimit(limit);
        //模糊查询
        if (StringUtils.isNotBlank(search)){
            String searchstr = "%"+search+"%";
            EduUserExample.Criteria criteria = userExample.createCriteria();
            userExample.or(userExample.createCriteria().andUsernameLike(searchstr));
            userExample.or(userExample.createCriteria().andPhoneLike(searchstr));
            userExample.or(userExample.createCriteria().andRealnameLike(searchstr));
        }
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            userExample.setOrderByClause(sort + " " + order);
        }
        List<EduUser> rows = userService.selectByExample(userExample);

        List<EduUser> userList = new ArrayList<>();

        if(rows!=null && rows.size()>0){
            for(int i =0;i<rows.size();i++){
                EduUser user = rows.get(i);
                List<String> roleNames = userService.getRoleNameByUserId(user.getUserId());
                String roleName = "";
                for(String str : roleNames){
                    roleName += str + ",";
                }
                if(roleName.lastIndexOf(",")>0){
                    roleName = roleName.substring(0, roleName.length()-1);
                }
                user.setRoleName(roleName);
                userList.add(user);
            }
        }

        long total = userService.countByExample(userExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", userList);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增用户")
    @RequiresPermissions("sys:user:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "/manage/user/create.jsp";
    }

    @ApiOperation(value = "新增用户")
    @RequiresPermissions("sys:user:create")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object create(EduUser user, Integer organizationId) {
        ComplexResult result = FluentValidator.checkAll()
                .on(user.getUsername(), new LengthValidator(1, 20, "帐号"))
                .on(user.getPassword(), new LengthValidator(5, 32, "密码"))
                .on(user.getPhone(), new NotNullValidator("电话号码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        EduUserExample userExample = new EduUserExample();
        //判断用户是否重复
        userExample.or(userExample.createCriteria().andUsernameEqualTo(user.getUsername()));
//        userExample.or(userExample.createCriteria().andPhoneEqualTo(user.getPhone()));
        List<EduUser> userList = userService.selectByExample(userExample);
        if(userList!=null && userList.size()>0){
            return new SysResult(SysResultConstant.FAILED, "用户名重复！");
        }

        long time = System.currentTimeMillis();
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        user.setSalt(salt);
//        user.setPassword(MD5Util.MD5(user.getPassword() + user.getSalt()));
        user.setCtime(time);
        int count = userService.insertSelective(user);
        //保存用户机构关系
        if(null != organizationId && 0 != organizationId){
            EduUserOrganization userOrganization = new EduUserOrganization();
            userOrganization.setOrganizationId(organizationId);
            userOrganization.setUserId(user.getUserId());
            userOrganizationService.insertSelective(userOrganization);
        }
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除用户")
    @RequiresPermissions("sys:user:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = userService.deleteByPrimaryKeys(ids);

        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            EduUserOrganizationExample userOrganizationExample = new EduUserOrganizationExample();
            userOrganizationExample.createCriteria().andUserIdEqualTo(Integer.parseInt(idStr));
            userOrganizationService.deleteByExample(userOrganizationExample);
        }
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改用户")
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        EduUser user = userService.selectByPrimaryKey(id);
        EduUserOrganizationExample userOrganizationExample = new EduUserOrganizationExample();
        EduUserOrganizationExample.Criteria criteria = userOrganizationExample.createCriteria();
        criteria.andUserIdEqualTo(id);
        List<EduUserOrganization> userOrganizationList = userOrganizationService.selectByExample(userOrganizationExample);
        Integer organizationId = 0;
        if(userOrganizationList.size()>0){
            organizationId = userOrganizationList.get(0).getOrganizationId();
        }
        modelMap.put("user", user);
        modelMap.put("organizationId", organizationId);
        return "/manage/user/update.jsp";
    }

    @ApiOperation(value = "修改用户")
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduUser user, Integer organizationId) {
        ComplexResult result = FluentValidator.checkAll()
                .on(user.getUsername(), new LengthValidator(1, 20, "帐号"))
                .on(user.getPassword(), new LengthValidator(5, 32, "密码"))
                .on(user.getPhone(), new NotNullValidator("电话号码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }

        EduUser eduUser = userService.selectByPrimaryKey(id);
        if(!eduUser.getUsername().equals(user.getUsername())){
            //判断用户是否重复
            EduUserExample userExample = new EduUserExample();
            if(!eduUser.getUsername().equals(user.getUsername()))
                userExample.or(userExample.createCriteria().andUsernameEqualTo(user.getUsername()));
            List<EduUser> userList = userService.selectByExample(userExample);
            if(userList!=null && userList.size()>0){
                return new SysResult(SysResultConstant.FAILED, "用户名重复！");
            }
        }

        // 不允许直接改密码
        /*if(user.getPassword()!=null && !user.getPassword().equals("")) {
            String salt = UUID.randomUUID().toString().replaceAll("-", "");
            user.setSalt(salt);
            user.setPassword(MD5Util.MD5(user.getPassword() + user.getSalt()));
        }
        if(user.getPassword().equals(""))
            user.setPassword(null);*/
        user.setUserId(id);
        int count = userService.updateByPrimaryKeySelective(user);
        if(null != organizationId && 0 != organizationId) {
            EduUserOrganizationExample userOrganizationExample = new EduUserOrganizationExample();
            userOrganizationExample.createCriteria().andUserIdEqualTo(id);
            userOrganizationExample.createCriteria().andOrganizationIdEqualTo(organizationId);
            List<EduUserOrganization> userOrganizationList = userOrganizationService.selectByExample(userOrganizationExample);
            if(userOrganizationList.size()>0){
                EduUserOrganization userOrganization = userOrganizationList.get(0);
                userOrganization.setUserId(id);
                userOrganization.setOrganizationId(organizationId);
                userOrganizationService.updateByPrimaryKeySelective(userOrganization);
            }
        }
        return new SysResult(SysResultConstant.SUCCESS, count);
    }


    @ApiOperation(value = "用户角色")
    @RequiresPermissions("sys:user:role")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public String role(@PathVariable("id") int id, ModelMap modelMap) {
        // 所有角色
        List<EduRole> roleList = roleService.selectByExample(new EduRoleExample());
        // 用户拥有角色
        EduUserRoleExample userRoleExample = new EduUserRoleExample();
        userRoleExample.createCriteria()
                .andUserIdEqualTo(id);
        List<EduUserRole> userRoles = userRoleService.selectByExample(userRoleExample);
        modelMap.put("roles", roleList);
        modelMap.put("userRoles", userRoles);
        return "/manage/user/role.jsp";
    }

    @ApiOperation(value = "用户角色")
    @RequiresPermissions("sys:user:role")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object role(@PathVariable("id") int id, HttpServletRequest request) {
        String[] roleIds = request.getParameterValues("roleId");
        // 删除旧记录
        EduUserRoleExample userRoleExample = new EduUserRoleExample();
        userRoleExample.createCriteria()
                .andUserIdEqualTo(id);
        userRoleService.deleteByExample(userRoleExample);
        // 增加新记录
        if (null != roleIds) {
            for (String roleId : roleIds) {
                if (StringUtils.isBlank(roleId)) {
                    continue;
                }
                EduUserRole upmsUserRole = new EduUserRole();
                upmsUserRole.setUserId(id);
                upmsUserRole.setRoleId(NumberUtils.toInt(roleId));
                userRoleService.insertSelective(upmsUserRole);
            }
        }
        return new SysResult(SysResultConstant.SUCCESS, "");
    }

}
