package com.edusys.front.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.util.*;
import com.edusys.front.service.ExamService;
import com.edusys.front.service.StudentExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Gary on 2017/5/16.
 * 考试Controller
 */
@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExamService examService;

    @Autowired
    private StudentExamService studentExamService;

    @Autowired
    private AmqpTemplate qmqpTemplate;

    /**
     * 考试列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "8", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       HttpServletRequest request, ModelMap modelMap){
        Integer offset = (page-1)*limit;
        //更新为已结束的考试状态
        examService.updateExamStatus();
        //更新为进行中的考试状态
        examService.updateExaming();

        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");

        List<EduExam> list = examService.selectExamListByStu(student.getStuId(), limit, offset);
        long total = examService.countByStu(student.getStuId());

        List<EduExam> newList = new ArrayList<>();
        //公共权限考试
        if(list!=null){
            if(list.size()>0){
                for(EduExam exam : list){
                    if(exam.getStuId() != student.getStuId()){
                        exam.setStatus(null);
                        exam.setStuId(null);
                        exam.setPointGet(null);
                    }
                    newList.add(exam);
                }
            }
        }

        Paginator paginator = new Paginator(total, page, limit, request);
        String htmlstr = paginator.getHtml();
        modelMap.put("list", newList);
        modelMap.put("pageHtml", htmlstr);
        modelMap.put("total", total);
        return "/exam-list.jsp";
    }

    /**
     * 验证考试密码是否通过
     * @return
     */
    @RequestMapping(value = "/pwd", method = RequestMethod.POST)
    @ResponseBody
    public Object password(String exampwd, int examId, int stuId){
        int stuExamId = examService.exampwd(examId, stuId);
        EduStudentExam studentExam = null;
        EduExam exam = examService.selectByPrimaryKey(examId);
        EduPaper paper = new EduPaper();
        String pass = "fail";

        Map<String, Object> result = new HashMap<>();
        if(exam.getExamPwd().equals(exampwd)){
            pass = "success";

            //将该考生加入到该考试中,考试处于进行中
            if(exam.getAuthority()!=null){
                if(stuExamId == 0){
                    studentExam = new EduStudentExam();
                    studentExam.setStuId(stuId);
                    studentExam.setIslook(exam.getIslook());
                    studentExam.setExamId(exam.getId());
                    if(exam.getPaperId() != null){
                        paper = examService.getPaperById(exam.getPaperId());
                    }
                    studentExam.setPaperId(paper.getId());
                    studentExam.setContent(paper.getContent());
                    studentExam.setDuration(exam.getDuration());
                    studentExam.setApproved(0);//审核通过
                    studentExam.setDisorganize(exam.getDisorganize());//是否打乱题目显示顺序
                    studentExam.setPoint(exam.getTotalPoint());
                    long time = System.currentTimeMillis();
                    studentExam.setCreateTime(time);
                    studentExamService.insertSelective(studentExam);
                    int num = exam.getStuNum()==null ? 0 : 1;
                    exam.setStuNum(num+1);
                    examService.updateByPrimaryKeySelective(exam);
                }else{
                    studentExam = studentExamService.selectByPrimaryKey(stuExamId);
                }
            }else{
                studentExam = studentExamService.selectByPrimaryKey(stuExamId);
            }

            //按照考试规则组卷
            if(exam.getExamType() == 1 && exam.getPaperRule()!=null){
                Gson gson = new Gson();
                paper = gson.fromJson(exam.getPaperRule(), EduPaper.class);
                try {
                    examService.createPaper(paper, exam, studentExam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //按照考试规则组卷-题库分类
            if(exam.getExamType() == 3 && exam.getPaperRule()!=null){
                Gson gson = new Gson();
                paper = gson.fromJson(exam.getPaperRule(), EduPaper.class);
                try {
                    examService.createPaperRule(paper, exam, studentExam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int examStatus = studentExam.getApproved();
            double score = studentExam.getPointGet()==null ? 0 : studentExam.getPointGet();
            result.put("stuExamId", studentExam.getId());
            result.put("examStatus", examStatus);
            result.put("score",score);
        }
        result.put("status", pass);
        return result;
    }

    /**
     * 加载考试界面
     * @param stuExamId
     * @return
     */
    @RequestMapping(value = "/examdo/{stuExamId}/{examId}", method = RequestMethod.GET)
    public String examdo(@PathVariable("stuExamId") int stuExamId, @PathVariable("examId") int examId, ModelMap modelMap, HttpServletRequest request){
        EduStudentExam stuExam = examService.getStudentExamById(stuExamId);
        EduExam eduExam = examService.selectByPrimaryKey(examId);
        String sessionTime = "paperTime-"+stuExam.getExamId()+"-"+stuExam.getStuId();
        HttpSession session = request.getSession();
        if(session.getAttribute(sessionTime)!=null){
            session.removeAttribute(sessionTime);
        }
        if(stuExam.getApproved() == 1){//考试进行中
            String answerData = RedisUtil.get("answerData-"+stuExam.getId());
            if(answerData != null){
                stuExam.setAnswerSheet(answerData);
            }
        }

        if(stuExam.getApproved() == 0) {
            stuExam.setApproved(1);//考试进行中
            studentExamService.updateByPrimaryKeySelective(stuExam);
        }
        modelMap.put("stuExam", stuExam);
        modelMap.put("exam", eduExam);
        return "/exam-do.jsp";
    }

    /**
     * 查看试卷
     * @param stuExamId
     * @param examId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/exampaper/{stuExamId}/{examId}", method = RequestMethod.GET)
    public String exampaper(@PathVariable("stuExamId") int stuExamId, @PathVariable("examId") int examId, ModelMap modelMap, HttpServletRequest request){
        EduStudentExam stuExam = examService.getStudentExamById(stuExamId);
        String sessionTime = "paperTime-"+stuExam.getExamId()+"-"+stuExam.getStuId();
        EduExam eduExam = examService.selectByPrimaryKey(examId);
        modelMap.put("stuExam", stuExam);
        modelMap.put("exam", eduExam);
        HttpSession session = request.getSession();
        Object sessionObj = session.getAttribute(sessionTime);
        long time = 0;
        if(sessionObj==null) {
            request.getSession().setAttribute(sessionTime, 0);//第一次查看试卷
        }else{
            time = 1;
        }
        modelMap.put("time", time);
        return "/exam-paper.jsp";
    }

    /**
     * 考试完成--交卷
     * @param answerSheet
     * @return
     */
    @RequestMapping(value = "/student/exam-submit", method = RequestMethod.POST)
    public @ResponseBody Message finishExam(@RequestBody AnswerSheet answerSheet) {

        Message message = new Message();
        ObjectMapper om = new ObjectMapper();
        try {
            qmqpTemplate.convertAndSend(Constants.ANSWERSHEET_DATA_QUEUE, om.writeValueAsBytes(answerSheet));
        } catch (AmqpException e) {
            e.printStackTrace();
            message.setResult("交卷失败");
            message.setMessageInfo(e.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            message.setResult("交卷失败");
            message.setMessageInfo(e.toString());
        }
        return message;
    }

    /**
     * 提交试卷后获取该试卷的得分情况
     * @param stuExamId
     * @return
     */
    @RequestMapping(value = "/query/score/{stuExamId}", method = RequestMethod.GET)
    public @ResponseBody Message queryScore(@PathVariable("stuExamId") int stuExamId){
        Message message = new Message();
        String key =  stuExamId+"_key";
        String val = RedisUtil.get(key);
        message.setResult(val);
        return message;
    }

    /**
     * 每答一道都进行提交
     * @param answerSheet
     * @return
     */
    @RequestMapping(value = "/student/question-submit", method = RequestMethod.POST)
    public @ResponseBody Message submitQuestion(@RequestBody AnswerSheet answerSheet){
        int stuExamId = answerSheet.getExamHistroyId();
        EduStudentExam studentExam = studentExamService.selectByPrimaryKey(stuExamId);
        Message message = new Message();
        if(studentExam.getApproved()==4){//考试已提交试卷（强制提交）
            message.setResult("fail");
        }else if(studentExam.getApproved()==2 && studentExam.getApproved()==3){
            message.setResult("complete");
        }else if(studentExam.getApproved()<=1){
            int approved = 1;
            Gson gson = new Gson();
            //保存在Redis
            RedisUtil.set("answerData-"+stuExamId, gson.toJson(answerSheet));
            //examService.updateExamQuestion(answerSheet, gson.toJson(answerSheet), approved);
            message.setResult("success");
        }
        return message;
    }
}
