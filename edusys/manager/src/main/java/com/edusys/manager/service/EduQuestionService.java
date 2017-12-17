package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduQuestion;
import com.edu.common.dao.model.EduQuestionExample;
import com.edu.common.dao.pojo.StatisticBean;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;
import java.util.Map;

/**
* EduQuestionService接口
* Created by Gary on 2017/5/6.
*/
public interface EduQuestionService extends BaseService<EduQuestion, EduQuestionExample> {

    public void uploadQuestions(CommonsMultipartFile file, int categoryId, String username);

    public List<String> selectQuestionNameList(Integer qcId);

    //统计题目出错率(出错率、答题数)
    public Map<Integer, StatisticBean> statisticalQuestion();

    public void updateQuestionBatch(List<EduQuestion> questionList);

}