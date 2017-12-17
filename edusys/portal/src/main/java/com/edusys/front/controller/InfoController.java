package com.edusys.front.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.util.Paginator;
import com.edusys.front.common.SysResult;
import com.edusys.front.common.SysResultConstant;
import com.edusys.front.service.IssuesService;
import com.edusys.front.service.StuJobCourseService;
import com.edusys.front.service.StudentExamService;
import com.edusys.front.service.StudentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/16.
 * 个人中心Controller
 */
@Controller
@RequestMapping("/info")
public class InfoController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private StuJobCourseService stuJobCourseService;

    @Autowired
    private StudentExamService studentExamService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private IssuesService issuesService;

    /**
     * 个人中心
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        modelMap.put("student", student);
        return "/info-index.jsp";
    }

    /**
     * 个人中心--详情
     *
     * @return
     */
    @RequestMapping(value = "/info-detail", method = RequestMethod.GET)
    public String info_detail(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        modelMap.put("student", student);
        return "/info-detail.jsp";
    }

    /**
     * 完善个人信息及修改密码
     * @param phone
     * @param stuNo
     * @return
     */
    @RequestMapping(value = "/edit-info", method = RequestMethod.POST)
    @ResponseBody
    public Object updateStudent(String phone, String stuNo, String oldpwd, String repwd, HttpServletRequest request){
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");

        if(StringUtils.isNotBlank(phone)){
            student.setPhone(phone);
        }
        if(StringUtils.isNotBlank(stuNo)){
            student.setStuNo(stuNo);
        }
        if(StringUtils.isNotBlank(oldpwd)){
            if(!student.getPassword().equals(oldpwd)){
                return new SysResult(SysResultConstant.FAILED, "fail");
            }
        }
        if(StringUtils.isNotBlank(repwd)){
            student.setPassword(repwd);
        }
        studentService.updateByPrimaryKeySelective(student);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    /**
     * 个人中心--学习记录
     *
     * @return
     */
    @RequestMapping(value = "/study-history", method = RequestMethod.GET)
    public String study_history(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectJobsByStuId(student.getStuId());
        int study = 0;
        int studyCount = 0;
        int finishCount = 0;
        int sumTime = 0;
        List<EduStuJobCourse> list = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        if (stuJobCourseList != null && stuJobCourseList.size() > 0) {
            for (EduStuJobCourse esjc : stuJobCourseList) {
                if(!map.containsKey(esjc.getJobId())) studyCount = 0;
                if(esjc.getTotalCourse()>0) {
                    if (esjc.getCourseNum() == esjc.getTotalCourse()) {
                        finishCount++;
                    }
                    sumTime = sumTime + esjc.getTime();
                    list.add(esjc);
                }else{
                    studyCount = studyCount + 1;
                    study++;
                }
                map.put(esjc.getJobId(), studyCount);
            }
        }
        modelMap.put("map", map);
        modelMap.put("sumTime", sumTime);//总用时
        modelMap.put("studyCount", study);//已学课件数
        modelMap.put("finishCount", finishCount);//已完成任务数
        modelMap.put("list", list);
        return "/study-history.jsp";
    }

    /**
     * 个人中心--课件学习记录
     *
     * @return
     */
    @RequestMapping(value = "/course-history/{userId}/{jobId}", method = RequestMethod.GET)
    public String course_history(@PathVariable("userId") int userId, @PathVariable("jobId") int jobId, ModelMap modelMap) {
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectCoursesByStuId(userId, jobId);
        modelMap.put("list", stuJobCourseList);
        return "/course-history.jsp";
    }

    /**
     * 个人中心--课件收藏记录
     *
     * @return
     */
    @RequestMapping(value = "/favorite", method = RequestMethod.GET)
    public String favorite(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                           @RequestParam(required = false, defaultValue = "8", value = "limit") int limit,
                           HttpServletRequest request, ModelMap modelMap, String courseName) {
        Integer offset = (page-1)*limit;
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectFavoriteList(student.getStuId(), courseName, limit, offset);
        long total = stuJobCourseService.favoriteCountBy(student.getStuId(), courseName);

        Paginator paginator = new Paginator(total, page, limit, request);
        String htmlstr = paginator.getHtml();

        modelMap.put("list", stuJobCourseList);
        modelMap.put("pageHtml", htmlstr);
        modelMap.put("total", total);
        return "/favorite.jsp";
    }

    /**
     * 个人中心--考试记录
     *
     * @return
     */
    @RequestMapping(value = "/exam-history", method = RequestMethod.GET)
    public String exam_history(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        List<EduStudentExam> eduStudentExamList = studentExamService.selectStuExamHistoryList(student.getStuId());
        modelMap.put("list", eduStudentExamList);
        return "/exam-history.jsp";
    }

    @RequestMapping(value = "/issues-setting", method = RequestMethod.POST)
    @ResponseBody
    public Object issuesSet(HttpServletRequest request){
        String[] questionId = request.getParameterValues("questionId");
        String[] answers = request.getParameterValues("answer");
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        EduStudentAnswer studentAnswer = issuesService.selectByPrimaryKey(student.getStuId());
        if(studentAnswer == null){
            studentAnswer = new EduStudentAnswer();
            studentAnswer.setStuId(student.getStuId());
            studentAnswer.setCardNo(student.getCardNo());
        }
        if(questionId.length>0){
            String questionstr = StringUtils.join(questionId, ",");
            studentAnswer.setQuestion(questionstr);
        }
        if(answers.length>0){
            String answerstr = StringUtils.join(answers, ",");
            studentAnswer.setAnswer(answerstr);
        }
        if(studentAnswer.getId()!=null){
            issuesService.updateByPrimaryKeySelective(studentAnswer);
        }else{
            issuesService.insertSelective(studentAnswer);
        }
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

}
