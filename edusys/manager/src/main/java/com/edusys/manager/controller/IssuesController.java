package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduNotice;
import com.edu.common.dao.model.EduNoticeExample;
import com.edu.common.dao.model.EduStudentAnswer;
import com.edu.common.dao.model.EduStudentAnswerExample;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduStudentAnswerService;
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

import java.util.*;

/**
 * Created by Gary on 2017/10/30.
 *
 * 学员找回密码安全问题管理
 */
@Controller
@Api(value = "学员找回密码安全问题管理", description = "学员找回密码安全问题管理")
@RequestMapping("/manage/issues")
public class IssuesController extends BaseController {
    private static Logger _log = LoggerFactory.getLogger(IssuesController.class);

    @Autowired
    private EduStudentAnswerService studentAnswerService;

    @ApiOperation("学员找回密码安全问题管理首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/manage/issue/index.jsp";
    }

    @ApiOperation("安全问题管理列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search){

        EduStudentAnswerExample studentAnswerExample = new EduStudentAnswerExample();
        EduStudentAnswerExample.Criteria criteria = studentAnswerExample.createCriteria();
        studentAnswerExample.setOffset(offset);
        studentAnswerExample.setLimit(limit);
        studentAnswerExample.setOrderByClause("id desc");
        //模糊查询
        if(StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            criteria.andQuestionLike(search);
        }

        criteria.andStuIdIsNull();

        List<EduStudentAnswer> rows = studentAnswerService.selectByExample(studentAnswerExample);
        long total = studentAnswerService.countByExample(studentAnswerExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("安全问题管理新增页面")
    @RequiresPermissions("edu:issues:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(){
        return "/manage/issue/create.jsp";
    }

    @ApiOperation("安全问题管理新增处理操作")
    @RequiresPermissions("edu:issues:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduStudentAnswer studentAnswer){
        int count = studentAnswerService.insertSelective(studentAnswer);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("安全问题修改页面")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap map){
        EduStudentAnswer studentAnswer = studentAnswerService.selectByPrimaryKey(id);
        map.put("studentAnswer", studentAnswer);
        return "/manage/issue/update.jsp";
    }

    @ApiOperation("安全问题修改处理操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduStudentAnswer studentAnswer){
        studentAnswer.setId(id);
        int count = studentAnswerService.updateByPrimaryKeySelective(studentAnswer);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除安全问题")
    @RequiresPermissions("edu:issues:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        String[] idArray = ids.split("-");
        List<Integer> delIds = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            delIds.add(Integer.parseInt(idStr));
        }
        EduStudentAnswerExample studentAnswerExample = new EduStudentAnswerExample();
        EduStudentAnswerExample.Criteria criteria = studentAnswerExample.createCriteria();
        criteria.andIdIn(delIds);
        int count = studentAnswerService.deleteByExample(studentAnswerExample);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

}
