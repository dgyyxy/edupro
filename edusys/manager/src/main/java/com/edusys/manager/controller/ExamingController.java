package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduExamExample;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduExamService;
import com.edusys.manager.service.EduStudentExamService;
import com.edusys.manager.service.EduStudentService;
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
 * Created by Gary on 2017/8/16.
 *
 * 考试进行
 */
@Api("考试进行管理")
@Controller
@RequestMapping("/manage/examing")
public class ExamingController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(ExamingController.class);

    @Autowired
    private EduExamService examService;

    @Autowired
    private EduStudentExamService studentExamService;

    @ApiOperation("考试进行首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/examing/index.jsp";
    }

    @ApiOperation(value = "考试进行列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search) {
        EduExamExample examExample = new EduExamExample();
        EduExamExample.Criteria criteria = examExample.createCriteria();
        examExample.setOffset(offset);
        examExample.setLimit(limit);
        examExample.setOrderByClause("ee.id DESC");

        criteria.andApprovedEqualTo(2);
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            criteria.andExamNameLike(search);
        }
        List<EduExam> rows = examService.selectExamingByExample(examExample);

        long total = examService.countByExample(examExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("参考的考生列表")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    public String student(@PathVariable("id") int id, ModelMap map){
        map.put("id", id);
        return "/manage/examing/students.jsp";
    }

    @ApiOperation("参考的考生列表")
    @RequestMapping(value = "/student/list-{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object studentList(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                              @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                              @RequestParam(required = false, value = "sort") String sort,
                              @RequestParam(required = false, value = "order") String order,
                              @PathVariable("id") int id, String search){
        EduStudentExamExample studentExamExample = new EduStudentExamExample();
        EduStudentExamExample.Criteria criteria = studentExamExample.createCriteria();
        studentExamExample.setOffset(offset);
        studentExamExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            studentExamExample.setOrderByClause(sort + " " + order);
        }

        criteria.andExamIdEqualTo(id);

        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
        }
        List<EduStudentExam> rows = studentExamService.selectByExample(studentExamExample);
        long total = studentExamService.countByExample(studentExamExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("强制交卷")
    @RequestMapping(value = "/stopexam/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object stopExam(@PathVariable("ids") String ids){
        String[] idArray = ids.split("-");
        List<Integer> examIds = new ArrayList<>();

        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            examIds.add(Integer.parseInt(idStr));
        }

        studentExamService.stopExamOperate(examIds);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }
}
