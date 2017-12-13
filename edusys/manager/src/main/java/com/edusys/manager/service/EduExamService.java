package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduExamExample;
import com.edu.common.dao.pojo.ExamPassRate;

import java.util.List;

/**
* EduExamService接口
* Created by Gary on 2017/5/6.
*/
public interface EduExamService extends BaseService<EduExam, EduExamExample> {

    // 批量更新考试状态
    public void batchUpdateStatus(List<Integer> idList);

    // 批量更新考试为未发布
    public void batchUpdateUnPublishStatus(List<Integer> idList);

    // 批量更新考试为已结束
    public void batchUpdateEndExamStatus(List<Integer> idList);

    // 更新过期的考试为已结束
    public void updateStatus();

    public List<EduExam> selectExamingByExample(EduExamExample example);

    // 统计及格率
    List<ExamPassRate> selectPassRate();
}