package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.QueryTypeRate;
import com.edu.common.util.RandomUtil;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/10.
 * 考试管理Controller
 */
@SuppressWarnings("ALL")
@Api("考试管理")
@Controller
@RequestMapping("/manage/exam")
public class ExamController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private EduExamService examService;

    @Autowired
    private EduPaperService paperService;

    @Autowired
    private EduQuestionService questionService;

    @Autowired
    private EduStudentService studentService;

    @Autowired
    private EduStudentExamService studentExamService;

    @ApiOperation("试卷设置首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("edu:exam:read")
    public String index() {
        return "/manage/exam/index.jsp";
    }

    @ApiOperation(value = "考试列表")
    @RequiresPermissions("edu:exam:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search) {
        // 更新已结束的考试状态
        examService.updateStatus();

        EduExamExample examExample = new EduExamExample();
        EduExamExample.Criteria criteria = examExample.createCriteria();
        examExample.setOffset(offset);
        examExample.setLimit(limit);
        examExample.setOrderByClause("id DESC");
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            examExample.or(examExample.createCriteria().andExamNameLike(search));
        }
        List<EduExam> rows = examService.selectByExample(examExample);

        long total = examService.countByExample(examExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("考试新增界面")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "/manage/exam/create.jsp";
    }

    @ApiOperation("考试新增保存操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduExam exam, EduPaper paper, HttpServletRequest request ) {
        // 参与学员
        String organizationIdstr = request.getParameter("organizationIds");
        Object questionTypeRateList = request.getParameter("questionTypeRateList");
        Type type = new TypeToken<String[]>() {
        }.getType();
        Gson gson = new Gson();

        String examType = request.getParameter("examType");
        String examRule = request.getParameter("examRule");
        long time = System.currentTimeMillis();
        if (examType.equals("1")) {//随机抽取试题并进行组卷
            paper.setCreateTime(time);
            Subject subject = SecurityUtils.getSubject();
            String username = (String) subject.getPrincipal();
            paper.setCreator(username);
            paper.setName("");
            paper.setPaperType(3);
            //保存创建试卷规则
            exam.setPaperRule(gson.toJson(paper));
            // 创建试卷
            try {
                paperService.createExamPaper(paper);
            } catch (Exception e) {
                return new SysResult(SysResultConstant.FAILED, e.getMessage());
            }

        } else if (examType.equals("2")) {//随机抽取试卷
            String paperIdStr = request.getParameter("paperIds");
            //处理数组只有一个元素
            if (paperIdStr.indexOf("[") == -1 && paperIdStr.indexOf("]") == -1) {
                paperIdStr = "[" + paperIdStr + "]";
            }
            String[] paperIds = gson.fromJson(paperIdStr, type);
            if (paperIds.length > 0) {
                int paperId = RandomUtil.getArrayRandItem(paperIds);
                paper = paperService.selectByPrimaryKey(paperId);
            }
        } else if (examType.equals("3") && examRule == null) {//跳转到考试规则设置页面
            Map map = new HashMap();
            map.put("exam", exam);
            map.put("organizationIdstr", organizationIdstr);
            return new SysResult(SysResultConstant.SUCCESS, map);
        }
        if (StringUtils.equals(examRule,"rule")) {//按照考试题库分类组卷考试
            System.out.println("");
            _log.info("create exam with rule::::"+questionTypeRateList.toString());
            List<QueryTypeRate> list = gson.fromJson(questionTypeRateList.toString(), new TypeToken<List<QueryTypeRate>>() {}.getType());
            paper.setCreateTime(time);
            paper.setQueryTypeRateList(list);
            Subject subject = SecurityUtils.getSubject();
            String username = (String) subject.getPrincipal();
            paper.setCreator(username);
            paper.setName("");
            paper.setPaperType(3);
            //保存创建试卷规则
            exam.setPaperRule(gson.toJson(paper));
            // 创建试卷
            try {
                paperService.createExamRulePaper(paper);
            } catch (Exception e) {
                return new SysResult(SysResultConstant.FAILED, e.getMessage());
            }
        }

        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();

        exam.setAmount(paper.getAmount());
        exam.setCreator(username);
        exam.setApproved(1);//已发布
        exam.setPassPoint(paper.getPassPoint());
        exam.setCreateTime(time);
        exam.setPaperId(paper.getId());
        exam.setTotalPoint(paper.getTotalPoint());
        //exam.setDuration(paper.getDuration());
        exam.setExamPwd(RandomUtil.getFourRandNum());



        //不选择机构，默认所有考生都有权限参加该考试
        if(organizationIdstr == null || organizationIdstr.equals("null")){
            exam.setAuthority("all");
            //保存考试信息
            examService.insertSelective(exam);
        }else{
            //处理数组只有一个元素
            if (organizationIdstr.indexOf("[") == -1 && organizationIdstr.indexOf("]") == -1) {
                organizationIdstr = "[" + organizationIdstr + "]";
            }
            String[] organIds = gson.fromJson(organizationIdstr, type);
            List<Integer> ids = new ArrayList<>();
            if (organIds.length > 0) {
                for (String str : organIds) {
                    ids.add(Integer.parseInt(str));
                }
            }
            EduStudentExample studentExample = new EduStudentExample();
            EduStudentExample.Criteria criteria = studentExample.createCriteria();
            criteria.andOrganizationId2In(ids);
            List<EduStudent> students = studentService.selectByExample(studentExample);
            exam.setStuNum(students.size()); //保存考生数量

            List<Integer> stuIds = new ArrayList<>();
            for (int i = 0; i < students.size(); i++) {
                stuIds.add(students.get(i).getStuId());
            }

            //保存考试信息
            examService.insertSelective(exam);

            if (stuIds.size() > 0) {
                studentExamService.examByStudents(stuIds, paper, exam);
            }
        }

        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation("考试修改界面")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") Integer id, ModelMap modelMap) {
        EduExam eduExam = examService.selectByPrimaryKey(id);
        modelMap.put("exam", eduExam);
        return "/manage/exam/update.jsp";
    }

    @ApiOperation("考试修改操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") Integer id, EduExam eduExam){
        eduExam.setId(id);
        int count = examService.updateByPrimaryKeySelective(eduExam);

        studentExamService.updateStudentExamByExamId(eduExam);

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除考试")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        String[] idArray = ids.split("-");
        List<Integer> examIds = new ArrayList<>();

        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            examIds.add(Integer.parseInt(idStr));
        }
        EduStudentExamExample esee = new EduStudentExamExample();
        EduStudentExamExample.Criteria criteria = esee.createCriteria();
        criteria.andExamIdIn(examIds);
        int count = studentExamService.deleteByExample(esee);
        count = examService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }


    @ApiOperation(value = "考试发布")
    @RequestMapping(value = "/publish/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object publish(@PathVariable("ids") String ids) {

        String[] idArray = ids.split("-");
        List<Integer> examIds = new ArrayList<>();

        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            examIds.add(Integer.parseInt(idStr));
        }

        examService.batchUpdateStatus(examIds);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "取消考试发布")
    @RequestMapping(value = "/unpublish/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object unpublish(@PathVariable("ids") String ids) {

        String[] idArray = ids.split("-");
        List<Integer> examIds = new ArrayList<>();

        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            examIds.add(Integer.parseInt(idStr));
        }

        examService.batchUpdateUnPublishStatus(examIds);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "结束考试")
    @RequestMapping(value = "/endExam/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object endExam(@PathVariable("ids") String ids) {

        String[] idArray = ids.split("-");
        List<Integer> examIds = new ArrayList<>();

        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            examIds.add(Integer.parseInt(idStr));
        }

        examService.batchUpdateEndExamStatus(examIds);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "考试参考人员页面")
    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
    public String students(@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.put("examId", id);
        return "/manage/exam/students.jsp";
    }

    @ApiOperation(value = "考试参考学员列表")
    @RequestMapping(value = "/studentList/{examId}", method = RequestMethod.GET)
    @ResponseBody
    public Object student_list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                               @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                               @RequestParam(required = false, value = "sort") String sort,
                               @RequestParam(required = false, value = "order") String order,
                               @PathVariable("examId") int examId,
                               String search) {
        EduStudentExamExample studentExamExample = new EduStudentExamExample();
        EduStudentExamExample.Criteria criteria = studentExamExample.createCriteria();
        studentExamExample.setOffset(offset);
        studentExamExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            studentExamExample.setOrderByClause(sort + " " + order);
        }

        criteria.andExamIdEqualTo(examId);

        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            studentExamExample.or(studentExamExample.createCriteria().andExamPasswordLike(search));
        }
        List<EduStudentExam> rows = studentExamService.selectByExample(studentExamExample);
        long total = studentExamService.countByExample(studentExamExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "考试参考机构页面")
    @RequestMapping(value = "/organ/{id}", method = RequestMethod.GET)
    public String organ_choice(@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.put("examId", id);
        return "/manage/exam/organ-choice.jsp";
    }

    @ApiOperation(value = "考试参与机构处理")
    @RequestMapping(value = "/organ/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object organ_choice(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        EduExam exam = examService.selectByPrimaryKey(id);
        // 参与学员
        String[] organIds = request.getParameterValues("organizationIds");
        List<Integer> ids = new ArrayList<>();
        if (organIds.length > 0) {
            for (String str : organIds) {
                ids.add(Integer.parseInt(str));
            }
        }
        EduStudentExample studentExample = new EduStudentExample();
        EduStudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andOrganizationId2In(ids);
        List<EduStudent> students = studentService.selectByExample(studentExample);

        List<Integer> stuIds = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            stuIds.add(students.get(i).getStuId());
        }
        if (stuIds.size() > 0) {
            exam.setStuNum(exam.getStuNum() + students.size());

            EduPaper paper = paperService.selectByPrimaryKey(exam.getPaperId());

            studentExamService.examByStudents(stuIds, paper, exam);
            examService.updateByPrimaryKeySelective(exam);//更新参考人员数量
        } else {
            return new SysResult(SysResultConstant.FAILED, "nostudent");
        }

        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "考试参考机构页面")
    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
    public String student_choice(@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.put("examId", id);
        return "/manage/exam/student-choice.jsp";
    }

    @ApiOperation(value = "学员列表")
    @RequestMapping(value = "/student/list/{examId}", method = RequestMethod.GET)
    @ResponseBody
    public Object student_list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            @PathVariable("examId") Integer examId,int organId, String search) {
        EduStudentExample studentExample = new EduStudentExample();
        EduStudentExample.Criteria criteria = studentExample.createCriteria();
        studentExample.setOffset(offset);
        studentExample.setLimit(limit);
        studentExample.setOrderByClause("stu_id DESC");

        List<Integer> stuIdList = studentExamService.getStuIdList(examId);

        if (stuIdList != null && stuIdList.size() > 0) {
            criteria.andStuIdNotIn(stuIdList);
        }

        if (organId>0){
            criteria.andOrganizationId2EqualTo(organId);
        }

        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "'%" + search + "%'";
            StringBuffer sb = new StringBuffer();
            sb.append(" (stu_name like " + search);
            sb.append(" or stu_no like " + search);
            sb.append(" or card_no like " + search);
            sb.append(" or organization_name2 like " + search + ")");
            criteria.andOrString(sb.toString());
        }

        List<EduStudent> rows = studentService.selectByExample(studentExample);
        long total = studentService.countByExample(studentExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }


    @ApiOperation(value = "选择考试参与学员处理")
    @RequestMapping(value = "/student/{examId}/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object student_choice(@PathVariable("ids") String ids, @PathVariable("examId") int examId,
                                 HttpServletRequest request, HttpServletResponse response) {
        EduExam exam = examService.selectByPrimaryKey(examId);

        String[] idArray = ids.split("-");
        List<Integer> stuIds = new ArrayList<>();

        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            stuIds.add(Integer.parseInt(idStr));
        }
        exam.setStuNum(exam.getStuNum() + stuIds.size());
        EduPaper paper = new EduPaper();
        if(exam.getPaperRule() == null || exam.getPaperRule().equals("")){
            paper = paperService.selectByPrimaryKey(exam.getPaperId());
        }

        studentExamService.examByStudents(stuIds, paper, exam);
        examService.updateByPrimaryKeySelective(exam);//更新参考人员数量
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "制定考试规则")
    @RequestMapping(value = "/rule", method = RequestMethod.GET)
    public String exam_rule(EduExam exam, ModelMap map, String organizationIdstr){
        map.put("exam", exam);
        map.put("organizationIdstr", organizationIdstr);
        return "/manage/exam/examRule.jsp";
    }

    @ApiOperation(value = "考试规则确认列表")
    @RequestMapping(value = "/ruleList", method = RequestMethod.GET)
    public String rule_list(){
        return "/manage/exam/ruleList.jsp";
    }

}
