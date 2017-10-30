package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduQuestion;
import com.edu.common.dao.model.EduQuestionExample;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

/**
* EduQuestionService接口
* Created by Gary on 2017/5/6.
*/
public interface EduQuestionService extends BaseService<EduQuestion, EduQuestionExample> {

    public void uploadQuestions(CommonsMultipartFile file, int categoryId, String username);

    public List<String> selectQuestionNameList(Integer qcId);

}