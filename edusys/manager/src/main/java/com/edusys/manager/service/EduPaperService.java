package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.model.EduPaperExample;
import com.edu.common.dao.model.EduQuestion;

import java.util.HashMap;
import java.util.List;

/**
* EduPaperService接口
* Created by Gary on 2017/5/6.
*/
public interface EduPaperService extends BaseService<EduPaper, EduPaperExample> {

    //获取题目分类下面所有试题
    public HashMap<Integer, HashMap<Integer, List<EduQuestion>>> getQuestionMap(List<Integer> idList);

    // 获取试卷题目列表
    public HashMap<Integer, EduQuestion> getPaperQuestionMap(HashMap<Integer, HashMap<Integer, List<EduQuestion>>> questionMap,
                                EduPaper eduPaper) throws Exception;

    // 创建试卷
    public void createPaper(EduPaper paper) throws Exception;

    public void createExamPaper(EduPaper paper)  throws Exception;

}