package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.ExamPassRate;
import com.edu.common.dao.pojo.QuestionAdapter;
import com.edu.common.util.NumberUtils;
import com.edusys.manager.service.EduExamService;
import com.edusys.manager.service.EduStudentExamService;
import com.edusys.manager.service.EduStudentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Gary on 2017/8/16.
 * <p>
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
    private EduStudentService studentService;

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
        //考试对应的及格率
        List<ExamPassRate> passRateList = examService.selectPassRate();
        Map<Integer, String> rateMap = new HashMap<>();
        if(passRateList!=null && passRateList.size()>0){
            for(ExamPassRate examPassRate : passRateList){
                double passRate = 0;
                if(examPassRate.getPassRate()!=null) passRate = examPassRate.getPassRate();
                rateMap.put(examPassRate.getExamId(), NumberUtils.formatDouble(passRate)+'%');
            }
        }

        List<EduExam> showList = new ArrayList<>();
        if (rows != null && rows.size() > 0) {
            for (EduExam exam : rows) {
                if(rateMap!=null && rateMap.size()>0)
                    exam.setPassRate(rateMap.get(exam.getId()));
                showList.add(exam);
            }
        }


        long total = examService.countByExample(examExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", showList);
        result.put("total", total);
        return result;
    }

    @ApiOperation("参考的考生列表")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    public String student(@PathVariable("id") int id, ModelMap map) {
        map.put("exam", examService.selectByPrimaryKey(id));
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
                              @PathVariable("id") int id, String search,
                              @RequestParam(required = false, defaultValue = "0") int organId,
                              @RequestParam(required = false, defaultValue = "0") int compare,
                              @RequestParam(required = false, defaultValue = "0") int score) {
        EduStudentExamExample studentExamExample = new EduStudentExamExample();
        EduStudentExamExample.Criteria criteria = studentExamExample.createCriteria();
        studentExamExample.setOffset(offset);
        studentExamExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            studentExamExample.setOrderByClause(sort + " " + order);
        }

        criteria.andExamIdEqualTo(id);

        List<Integer> stuIds = new ArrayList<>();
        //查询
        if (organId != 0) {
            stuIds = studentService.selectIdByOrganId(organId);
            criteria.andStuIdIn(stuIds);
        }
        if (score != 0) {
            float scoreFloat = Float.parseFloat(score + "");
            switch (compare) {
                case 1:
                    criteria.andPointGetGreaterThanOrEqualTo(scoreFloat);
                    break;
                case 2:
                    criteria.andPointGetEqualTo(scoreFloat);
                    break;
                case 3:
                    criteria.andPointGetLessThan(scoreFloat);
                    break;
            }
        }
        List<EduStudentExam> rows = studentExamService.selectByExample(studentExamExample);
        long total = studentExamService.countByExample(studentExamExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("导出成绩界面")
    @RequestMapping(value = "/exportPage/{id}", method = RequestMethod.GET)
    public String export(@PathVariable("id") int id, ModelMap map, String passRate) {
        map.put("id", id);
        map.put("passRate", passRate);
        return "/manage/examHistory/export.jsp";
    }

    @ApiOperation(value = "导出学员成绩")
    @RequestMapping(value = "/export/{examId}", method = RequestMethod.GET)
    public String export(HttpServletResponse response, String ids, int isAll, @PathVariable("examId") int examId, HttpServletRequest request) {
        response.setContentType("application/binary;charset=ISO8859_1");
        String className = request.getParameter("className");
        String companyName = request.getParameter("companyName");
        String passRate = request.getParameter("passRate");
        try {
            EduExam exam = examService.selectByPrimaryKey(examId);
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(exam.getExamName().getBytes(), "ISO8859_1") + "-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            List<EduStudentExam> studentExamList = new ArrayList<>();

            EduStudentExamExample studentExamExample = new EduStudentExamExample();
            EduStudentExamExample.Criteria criteria = studentExamExample.createCriteria();
            criteria.andExamIdEqualTo(examId);

            if (isAll == 0) {//只选择已勾选的学员导出
                if (StringUtils.isNotBlank(ids)) {
                    List<Integer> idList = new ArrayList<>();
                    String[] idArray = ids.split("-");
                    for (String idstr : idArray) {
                        idList.add(Integer.parseInt(idstr));
                    }
                    criteria.andIdIn(idList);
                }
            }
            studentExamList = studentExamService.selectByExample(studentExamExample);
            String[] titles = new String[]{"序号", "考生姓名", "考生身份证号", "考试名称", "得分", "班级名称", "航空公司", "考试开始日期", "考试结束日期", "及格率"};
            studentExamService.exportExcel(titles, outputStream, studentExamList, exam.getExamName(), className, companyName, passRate, exam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation("查看试卷")
    @RequestMapping(value = "/paper/{id}", method = RequestMethod.GET)
    public String paperView(@PathVariable("id") int id, ModelMap modelMap, HttpServletRequest request) {
        String strurl = "http://" + request.getServerName() // 服务器地址
                + ":" + request.getServerPort() + "/";

        EduStudentExam studentExam = studentExamService.selectByPrimaryKey(id);
        Gson gson = new Gson();
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(studentExam.getContent())) {

            List<QuestionResult> questionResults = gson.fromJson(studentExam.getContent(), new TypeToken<List<QuestionResult>>() {
            }.getType());
            for (QuestionResult qr : questionResults) {
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
