package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edusys.manager.service.EduStudentExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/10.
 * 成绩管理Controller
 */
@Api("成绩管理")
@Controller
@RequestMapping("/manage/score")
public class ScoreController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    private EduStudentExamService studentExamService;

    @ApiOperation("成绩管理首页")
    @RequiresPermissions("edu:score:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/manage/score/index.jsp";
    }

    @ApiOperation(value = "成绩列表")
    @RequiresPermissions("edu:score:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search){
        EduStudentExamExample studentExamExample = new EduStudentExamExample();
        EduStudentExamExample.Criteria criteria = studentExamExample.createCriteria();
        studentExamExample.setOffset(offset);
        studentExamExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            studentExamExample.setOrderByClause(sort + " " + order);
        }
        // 模糊查询
        if (StringUtils.isNotBlank(search)){
        }
        List<EduStudentExam> rows = studentExamService.selectByExample(studentExamExample);
        long total = studentExamService.countByExample(studentExamExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

}
