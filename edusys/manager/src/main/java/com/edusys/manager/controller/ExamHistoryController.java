package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.dao.pojo.QuestionAdapter;
import com.edusys.manager.service.EduExamService;
import com.edusys.manager.service.EduStudentExamService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/8/16.
 *
 * 考试记录
 */
@Api("考试记录管理")
@Controller
@RequestMapping("/manage/exam-history")
public class ExamHistoryController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(ExamHistoryController.class);

    @Autowired
    private EduExamService examService;

    @Autowired
    private EduStudentExamService studentExamService;

    @ApiOperation("考试记录首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/examHistory/index.jsp";
    }

    @ApiOperation(value = "考试记录列表")
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
        examExample.setOrderByClause("id DESC");

        criteria.andApprovedEqualTo(3);
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            criteria.andExamNameLike(search);
        }
        List<EduExam> rows = examService.selectByExample(examExample);

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
        return "/manage/examHistory/students.jsp";
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


    @ApiOperation("查看试卷")
    @RequestMapping(value = "/paper/{id}", method = RequestMethod.GET)
    public String paperView(@PathVariable("id") int id, ModelMap modelMap, HttpServletRequest request){
        String strurl = "http://" + request.getServerName() // 服务器地址
                + ":" + request.getServerPort() + "/";

        EduStudentExam studentExam = studentExamService.selectByPrimaryKey(id);
        Gson gson = new Gson();
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotBlank(studentExam.getContent())){

            List<QuestionResult> questionResults = gson.fromJson(studentExam.getContent(), new TypeToken<List<QuestionResult>>(){}.getType());
            for(QuestionResult qr : questionResults){
                QuestionAdapter adapter = new QuestionAdapter(qr, strurl);
                sb.append(adapter.getStringFromXML2());
            }
        }

        EduExam exam = examService.selectByPrimaryKey(studentExam.getExamId());
        modelMap.put("htmlStr", sb);
        modelMap.put("stuExam", studentExam);
        modelMap.put("exam", exam);
        return "/manage/examHistory/paper.jsp";
    }

}
