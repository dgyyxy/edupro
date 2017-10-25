package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduCourseware;
import com.edu.common.dao.model.EduCoursewareExample;
import com.edu.common.dao.model.EduCoursewareType;
import com.edu.common.dao.model.EduCoursewareTypeExample;
import com.edu.common.util.PropertiesFileUtil;
import com.edu.common.validator.NotNullValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduCoursewareService;
import com.edusys.manager.service.EduCoursewareTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/4/26.
 * 课件Controller
 */
@Controller
@Api(value = "课件管理", description = "课件和课件类别管理")
@RequestMapping("/manage/courseware")
public class CoursewareController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(CoursewareController.class);

    @Autowired
    private EduCoursewareService coursewareService;
    @Autowired
    private EduCoursewareTypeService coursewareTypeService;


    @ApiOperation("课件管理首页")
    @RequiresPermissions("edu:courseware:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/manage/courseware/index.jsp";
    }

    @ApiOperation("课件列表")
    @RequiresPermissions("edu:courseware:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "typeId") Integer typeId,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search){
        EduCoursewareExample coursewareExample = new EduCoursewareExample();
        EduCoursewareExample.Criteria criteria = coursewareExample.createCriteria();
        if(null != typeId && 0 != typeId){
            criteria.andCategoryIdEqualTo(typeId);
        }
        coursewareExample.setOffset(offset);
        coursewareExample.setLimit(limit);
        coursewareExample.setOrderByClause("id desc");

        //模糊查询
        if(StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            coursewareExample.or(coursewareExample.createCriteria().andNameLike(search));
        }

        List<EduCourseware> rows = coursewareService.selectByExample(coursewareExample);
        long total = coursewareService.countByExample(coursewareExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("课件新增")
    @RequiresPermissions("edu:courseware:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(){
        return "/manage/courseware/create.jsp";
    }

    /*@ApiOperation("课件新增处理操作")
    @RequiresPermissions("edu:courseware:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduCourseware courseware, HttpServletRequest request){
        ComplexResult result = FluentValidator.checkAll()
                .on(courseware.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        //取得上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        CommonsMultipartFile img = (CommonsMultipartFile)multipartRequest.getFile("img");
        CommonsMultipartFile file = (CommonsMultipartFile)multipartRequest.getFile("file");

        PropertiesFileUtil propertiesFileUtil = PropertiesFileUtil.getInstance("config");
        String realPath = propertiesFileUtil.get("uploadDir");

        if(img.getSize() > 0){
            String imgName = "img" + new Date().getTime() + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf("."), img.getOriginalFilename().length());
            ;
            try {
                FileUtils.copyInputStreamToFile(img.getInputStream(), new File(realPath + "/img/", imgName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            courseware.setPicture("/courseware/img/" + imgName);
        }
        if(file.getSize()>0) {
            String fileName = "file" + new Date().getTime() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."), file.getOriginalFilename().length());
            ;
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath + "/file/", fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            courseware.setUriStr("/courseware/file/" + fileName);
        }

        int count = coursewareService.insertSelective(courseware);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }*/

    @ApiOperation("课件新增处理操作")
    @RequiresPermissions("edu:courseware:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduCourseware courseware, HttpServletRequest request){
        ComplexResult result = FluentValidator.checkAll()
                .on(courseware.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }

        int count = coursewareService.insertSelective(courseware);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("修改课件")
    @RequiresPermissions("edu:courseware:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap){
        EduCourseware courseware = coursewareService.selectByPrimaryKey(id);
        EduCoursewareType coursewareType = coursewareTypeService.selectByPrimaryKey(courseware.getCategoryId());
        modelMap.put("courseware", courseware);
        modelMap.put("coursewareType", coursewareType);
        return "/manage/courseware/update.jsp";
    }

    /*@ApiOperation("修改课件")
    @RequiresPermissions("edu:courseware:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduCourseware courseware, HttpServletRequest request){
        ComplexResult result = FluentValidator.checkAll()
                .on(courseware.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if(!result.isSuccess()){
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        courseware.setId(id);

        //取得上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        CommonsMultipartFile img = (CommonsMultipartFile)multipartRequest.getFile("img");
        CommonsMultipartFile file = (CommonsMultipartFile)multipartRequest.getFile("file");

        PropertiesFileUtil propertiesFileUtil = PropertiesFileUtil.getInstance("config");
        String realPath = propertiesFileUtil.get("uploadDir");

        if(img.getSize() > 0){
            String imgName = "img" + new Date().getTime() + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf("."), img.getOriginalFilename().length());
            ;
            try {
                FileUtils.copyInputStreamToFile(img.getInputStream(), new File(realPath + "/img/", imgName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            courseware.setPicture("/courseware/img/" + imgName);
        }
        if(file.getSize()>0) {
            String fileName = "file" + new Date().getTime() + file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."), file.getOriginalFilename().length());
            ;
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath + "/file/", fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            courseware.setUriStr("/courseware/file/" + fileName);
        }

        int count = coursewareService.updateByPrimaryKeySelective(courseware);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }*/

    @ApiOperation("修改课件")
    @RequiresPermissions("edu:courseware:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduCourseware courseware, HttpServletRequest request){
        ComplexResult result = FluentValidator.checkAll()
                .on(courseware.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if(!result.isSuccess()){
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        courseware.setId(id);

        int count = coursewareService.updateByPrimaryKeySelective(courseware);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除课件")
    @RequiresPermissions("edu:courseware:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = coursewareService.deleteByPrimaryKeys(ids);

        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            EduCoursewareExample coursewareExample = new EduCoursewareExample();
            coursewareExample.createCriteria().andIdEqualTo(Integer.parseInt(idStr));
            coursewareService.deleteByExample(coursewareExample);
        }
        return new SysResult(SysResultConstant.SUCCESS, count);
    }



    @ApiOperation("课件类型列表")
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    @ResponseBody
    public Object type_list(@RequestParam(required = false, value = "pid") Integer pid){
        EduCoursewareTypeExample coursewareTypeExample = new EduCoursewareTypeExample();
        coursewareTypeExample.setOffset(0);
        coursewareTypeExample.setLimit(100000);
        if(null != pid){
            coursewareTypeExample.createCriteria().andPidEqualTo(pid);
        }
        List<EduCoursewareType> coursewareTypeList = coursewareTypeService.selectByExample(coursewareTypeExample);
        Map<String, Object> result = new HashMap<>();
        result.put("courseTypeList", coursewareTypeList);
        return result;
    }

    @ApiOperation("课件类别新增")
    @RequestMapping(value = "/category/create", method = RequestMethod.GET)
    public String type_create(){
        return "/manage/courseware/category/create.jsp";
    }

    @ApiOperation("课件类别新增处理操作")
    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    @ResponseBody
    public Object type_create(EduCoursewareType coursewareType){
        ComplexResult result = FluentValidator.checkAll()
                .on(coursewareType.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if(!result.isSuccess()){
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }

        int count = coursewareTypeService.insertSelective(coursewareType);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("修改课件类型")
    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.GET)
    public String type_update(@PathVariable("id") int id, ModelMap modelMap){
        EduCoursewareType coursewareType = coursewareTypeService.selectByPrimaryKey(id);
        modelMap.put("coursewareType", coursewareType);
        return "/manage/courseware/category/update.jsp";
    }

    @ApiOperation("修改课件类型")
    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object type_update(@PathVariable("id") int id, EduCoursewareType coursewareType){
        ComplexResult result = FluentValidator.checkAll()
                .on(coursewareType.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if(!result.isSuccess()){
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        if(coursewareType.getLevel()==1){
            coursewareType.setPid(0);
        }
        coursewareType.setId(id);
        int count = coursewareTypeService.updateByPrimaryKey(coursewareType);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("删除课件类型")
    @RequestMapping(value = "/category/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object type_delete(@PathVariable("id") int id){
        int count = coursewareTypeService.deleteByPrimaryKey(id);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }
}
