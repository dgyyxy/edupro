package com.edusys.manager.controller;

import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.dao.pojo.AnswerSheetItem;
import com.edu.common.util.Message;
import com.edu.common.util.RedisUtil;
import com.edusys.manager.service.EduPaperService;
import com.edusys.manager.service.EduStudentExamService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Gary on 2017/6/8.
 */
@Api("考试API管理")
@Controller
@RequestMapping("/api")
public class ExamApiController {

    @Autowired
    private EduPaperService paperService;

    @Autowired
    private EduStudentExamService studentExamService;

    @ApiOperation("获取试卷")
    @RequestMapping(value = "/exampaper/{id}", method = RequestMethod.GET)
    @ResponseBody
    public EduPaper getExamPaper(@PathVariable("id") int id){
        EduPaper paper = paperService.selectByPrimaryKey(id);
        return paper;
    }

    @ApiOperation("获取考试答卷")
    @RequestMapping(value = "/answersheet", method = RequestMethod.POST)
    @ResponseBody
    public Message submitAnswerSheet(@RequestBody AnswerSheet answerSheet){

        List<AnswerSheetItem> itemList = answerSheet.getAnswerSheetItems();

        //全部是客观题，则状态更改为已阅卷
        int approved = 0;
        String approvedStr = "";
        if(answerSheet.getPointPass() <= answerSheet.getPointRaw()){//考试通过
            approved = 2;
            approvedStr="及格";
        }else{
            approved = 3;
            approvedStr="不及格";
        }
        //保存当前考试得分及是否及格（redis）
        RedisUtil.set(answerSheet.getExamHistroyId()+"_key",answerSheet.getPointRaw()+"-"+approvedStr);
        Gson gson = new Gson();
        studentExamService.updateUserExamHist(answerSheet, gson.toJson(answerSheet),approved);

        return new Message();
    }



}
