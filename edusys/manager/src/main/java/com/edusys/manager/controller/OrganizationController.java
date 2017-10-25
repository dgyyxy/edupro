package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduOrganization;
import com.edu.common.dao.model.EduOrganizationExample;
import com.edu.common.validator.LengthValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduOrganizationService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织机构Controller
 * Created by Gary on 2017/4/6.
 */
@Controller
@Api(value = "组织机构管理", description = "组织机构管理")
@RequestMapping("manage/organization")
public class OrganizationController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private EduOrganizationService organizationService;

    @ApiOperation(value = "组织首页")
    @RequiresPermissions("sys:organization:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/organization/index.jsp";
    }

    @ApiOperation(value = "组织列表")
    @RequiresPermissions("sys:organization:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "pid") Integer pid,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduOrganizationExample organizationExample = new EduOrganizationExample();
        EduOrganizationExample.Criteria criteria = organizationExample.createCriteria();
        if(null != pid){
            criteria.andParentIdEqualTo(pid);
        }
        organizationExample.setOffset(offset);
        organizationExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            organizationExample.setOrderByClause(sort + " " + order);
        }
        // 模糊查询
        if (StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            organizationExample.or(organizationExample.createCriteria().andNameLike(search));
            organizationExample.or(organizationExample.createCriteria().andDescriptionLike(search));
        }
        List<EduOrganization> rows = organizationService.selectByExample(organizationExample);
        List<EduOrganization> list = new ArrayList<>();
        if(rows!=null && rows.size()>0){
            for(EduOrganization organization : rows){
                if(organization.getParentId()>0){
                    organization.setParentName(organizationService.selectByPrimaryKey(organization.getParentId()).getName());
                }
                list.add(organization);
            }
        }
        long total = organizationService.countByExample(organizationExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增组织")
    @RequiresPermissions("sys:organization:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap modelMap) {
        EduOrganizationExample organizationExample = new EduOrganizationExample();
        organizationExample.createCriteria().andParentIdEqualTo(0);
        List<EduOrganization> organizations = organizationService.selectByExample(organizationExample);
        modelMap.put("organizations", organizations);
        return "/manage/organization/create.jsp";
    }

    @ApiOperation(value = "新增组织")
    @RequiresPermissions("sys:organization:create")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object create(EduOrganization organization) {
        ComplexResult result = FluentValidator.checkAll()
                .on(organization.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        long time = System.currentTimeMillis();
        organization.setCtime(time);
        int count = organizationService.insertSelective(organization);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除组织")
    @RequiresPermissions("sys:organization:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = organizationService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改组织")
    @RequiresPermissions("sys:organization:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        EduOrganization organization = organizationService.selectByPrimaryKey(id);
        EduOrganizationExample organizationExample = new EduOrganizationExample();
        organizationExample.createCriteria().andParentIdEqualTo(0);
        List<EduOrganization> organizations = organizationService.selectByExample(organizationExample);
        modelMap.put("organizations", organizations);
        modelMap.put("organization", organization);
        return "/manage/organization/update.jsp";
    }

    @ApiOperation(value = "修改组织")
    @RequiresPermissions("sys:organization:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduOrganization organization) {
        ComplexResult result = FluentValidator.checkAll()
                .on(organization.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        organization.setOrganizationId(id);
        int count = organizationService.updateByPrimaryKeySelective(organization);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

}
