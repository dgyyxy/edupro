package com.edusys.front.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.validator.LengthValidator;
import com.edusys.front.common.SysResult;
import com.edusys.front.common.SysResultConstant;
import com.edusys.front.interfaces.SameUrlData;
import com.edusys.front.listener.MemoryData;
import com.edusys.front.listener.SessionListener;
import com.edusys.front.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/15.
 * 首页Controller
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController{

    private Logger _log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EduNoticeService noticeService;

    @Autowired
    private EduAdvertService advertService;

    @Autowired
    private IssuesService issuesService;

    /**
     * 获取安全问题列表
     * @return
     */
    @RequestMapping(value = "issues", method = RequestMethod.GET)
    @ResponseBody
    public Object getIssuesList(HttpServletRequest request){
        EduStudentAnswerExample studentAnswerExample = new EduStudentAnswerExample();
        EduStudentAnswerExample.Criteria criteria = studentAnswerExample.createCriteria();
        criteria.andStuIdIsNull();
        criteria.andCardNoIsNull();
        criteria.andAnswerIsNull();
        List<EduStudentAnswer> list = issuesService.selectByExample(studentAnswerExample);
        EduStudentAnswer studentAnswer = new EduStudentAnswer();

        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        if(student!=null){
            studentAnswerExample = new EduStudentAnswerExample();
            criteria = studentAnswerExample.createCriteria();
            criteria.andStuIdEqualTo(student.getStuId());
            criteria.andCardNoEqualTo(student.getCardNo());
            List<EduStudentAnswer> studentAnswers = issuesService.selectByExample(studentAnswerExample);
            if(studentAnswers.size()>0){
                studentAnswer = studentAnswers.get(0);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("answer", studentAnswer.getAnswer());
        return result;
    }

    /**
     * 忘记密码处理
     * @param cardno
     * @param request
     * @return
     */
    @RequestMapping(value = "resetpwd", method = RequestMethod.POST)
    @ResponseBody
    public Object resetpwd(String cardno, HttpServletRequest request){
        String[] questionIds = request.getParameterValues("questionId");
        String[] answers = request.getParameterValues("answer");

        String question = StringUtils.join(questionIds, ",");
        String answer = StringUtils.join(answers, ",");

        EduStudentAnswerExample studentAnswerExample = new EduStudentAnswerExample();
        EduStudentAnswerExample.Criteria criteria = studentAnswerExample.createCriteria();
        criteria.andCardNoEqualTo(cardno);
        List<EduStudentAnswer> list = issuesService.selectByExample(studentAnswerExample);
        EduStudentAnswer studentAnswer = null;
        if(list!=null && list.size()>0){
            studentAnswer = list.get(0);

            if(studentAnswer.getQuestion().equals(question)
                    && studentAnswer.getAnswer().equals(answer)){
                //安全问题回答正确后，重置密码
                EduStudentExample studentExample = new EduStudentExample();
                studentExample.createCriteria().andCardNoEqualTo(cardno);
                List<EduStudent> students = studentService.selectByExample(studentExample);
                if(students!=null && students.size()>0){
                    EduStudent student = students.get(0);
                    student.setPassword(student.getCardNo().substring(student.getCardNo().length() - 4));
                    studentService.updateByPrimaryKeySelective(student);
                }
                return new SysResult(SysResultConstant.INVALID_USERNAME, "success");
            }else{
                return new SysResult(SysResultConstant.INVALID_USERNAME, "issues_fail");
            }
        }else{
            return new SysResult(SysResultConstant.INVALID_USERNAME, "cardno_fail");
        }
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map){
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        int organId = 0;

        //广告轮询图片
        List<EduAdvert> advertList = advertService.selectByExample(new EduAdvertExample());

        //公告
        EduNoticeExample noticeExample = new EduNoticeExample();
        noticeExample.setOrderByClause("id desc");
        if(student!=null){
            organId = student.getOrganizationId2();
            noticeExample.or(noticeExample.createCriteria().andOrganIdEqualTo(0));
            noticeExample.or(noticeExample.createCriteria().andOrganIdEqualTo(organId));
        }else{
            noticeExample.createCriteria().andOrganIdEqualTo(0);
        }
        List<EduNotice> noticeList = noticeService.selectByExample(noticeExample);

        map.put("advertList", advertList);
        map.put("noticeList", noticeList);
        map.put("advertCount", advertList.size());

        return "/index.jsp";
    }


    /**
     * 登录操作
     * @return
     */
    @RequestMapping(value = "login/done", method = RequestMethod.POST)
    @ResponseBody
    public Object login_done(String account, String password, HttpServletRequest request){
        HttpSession session = request.getSession();

        ComplexResult result = FluentValidator.checkAll()
                .on(account, new LengthValidator(1, 20, "身份证号或者学生账号"))
                .on(password, new LengthValidator(1, 20, "密码"))
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }

        EduStudentExample ese = new EduStudentExample();
        //学号
        ese.or(ese.createCriteria().andStuNoEqualTo(account));
        //身份证号
        ese.or(ese.createCriteria().andCardNoEqualTo(account));

        List<EduStudent> students = studentService.selectByExample(ese);
        if(students!=null && students.size()>0){
            EduStudent student = students.get(0);
            String stuId = student.getStuId().toString();
            if(student!=null){
                if(student.getPassword().equals(password)){
                    //保存学员信息在session中
                    String sessionId = request.getRequestedSessionId();
                    session.setAttribute("user", student);
                    String userkey = "student-"+student.getStuId();
                    if(!MemoryData.getSessionIDMap().containsKey(userkey)){
                        MemoryData.getSessionIDMap().put(userkey, sessionId);
                    }else if(MemoryData.getSessionIDMap().containsKey(userkey)
                            && !StringUtils.equals(sessionId, MemoryData.getSessionIDMap().get(userkey))){
                        MemoryData.getSessionIDMap().remove(userkey);
                        MemoryData.getSessionIDMap().put(userkey, sessionId);
                    }
                    return new SysResult(SysResultConstant.SUCCESS, "success");
                }else{
                    return new SysResult(SysResultConstant.INVALID_PASSWORD, "fail");
                }
            }
        }

        return new SysResult(SysResultConstant.INVALID_USERNAME, "fail");
    }

    /**
     * 校验身份证是否存在
     * @param cardNo
     * @return
     */
    @RequestMapping(value = "/register/valid", method = RequestMethod.GET)
    @ResponseBody
    public Object vaildStudent(String cardNo){
        EduStudentExample studentExample = new EduStudentExample();
        studentExample.createCriteria().andCardNoEqualTo(cardNo);
        long count = studentService.countByExample(studentExample);
        if(count>0) {
            return new SysResult(SysResultConstant.FAILED, "fail");
        }
        return new SysResult(SysResultConstant.INVALID_USERNAME, "success");
    }


    /**
     * 注册
     * @param student
     * @return
     */
    @SameUrlData
    @RequestMapping(value = "/register/done", method = RequestMethod.POST)
    @ResponseBody
    public Object register(EduStudent student,HttpServletRequest request){
        ComplexResult result = FluentValidator.checkAll()
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }

        String[] questionId = request.getParameterValues("questionId");
        String[] answers = request.getParameterValues("answer");

        EduStudentExample studentExample = new EduStudentExample();
        studentExample.createCriteria().andCardNoEqualTo(student.getCardNo());
        long count = studentService.countByExample(studentExample);
        if(count>0){
            return new SysResult(SysResultConstant.FAILED, "fail");
        }else{
            studentService.insertSelective(student);
            EduStudentAnswer studentAnswer = new EduStudentAnswer();
            studentAnswer.setCardNo(student.getCardNo());
            studentAnswer.setStuId(student.getStuId());
            if(questionId.length>0){
                String questionstr = StringUtils.join(questionId, ",");
                studentAnswer.setQuestion(questionstr);
            }
            if(answers.length>0){
                String answerstr = StringUtils.join(answers, ",");
                studentAnswer.setAnswer(answerstr);
            }
            issuesService.insertSelective(studentAnswer);
            return new SysResult(SysResultConstant.INVALID_USERNAME, "success");
        }
    }

    /**
     * 组织机构列表
     * @return
     */
    @RequestMapping(value = "/organ/list", method = RequestMethod.GET)
    @ResponseBody
    public Object organizations(int pid){
        EduOrganizationExample eoe = new EduOrganizationExample();
        EduOrganizationExample.Criteria criteria = eoe.createCriteria();
        criteria.andParentIdEqualTo(pid);
        List<EduOrganization> list = organizationService.selectByExample(eoe);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        return result;
    }


    /**
     * 安全退出
     * @param request
     * @return
     */
    @RequestMapping(value = "/login/out", method = RequestMethod.GET)
    public String login_out(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            //EduStudent student = (EduStudent) session.getAttribute("user");
            //SessionListener.sessionContext.getSessionMap().remove(student.getStuId().toString());
            session.removeAttribute("user");
            MemoryData.removeAll();
        }
        response.sendRedirect("/index");
        return null;
    }


}
