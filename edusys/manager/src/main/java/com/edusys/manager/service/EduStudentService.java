package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduStudent;
import com.edu.common.dao.model.EduStudentExample;
import com.edu.common.dao.pojo.ExamPassRate;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
* EduStudentService接口
* Created by Gary on 2017/4/12.
*/
public interface EduStudentService extends BaseService<EduStudent, EduStudentExample> {

    int insertBatch(List<EduStudent> studentList);

    int exportExcel(String[] titles, ServletOutputStream outputStream, List<EduStudent> students);

    List<EduStudent> selectLearnRecordList(EduStudentExample studentExample);

    long countLearnRecord(EduStudentExample example);

    List<Integer> selectIdByOrganId(int organId);

}