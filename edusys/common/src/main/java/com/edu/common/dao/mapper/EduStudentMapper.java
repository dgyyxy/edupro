package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduStudent;
import com.edu.common.dao.model.EduStudentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduStudentMapper {
    long countByExample(EduStudentExample example);

    int deleteByExample(EduStudentExample example);

    int deleteByPrimaryKey(Integer stuId);

    int insert(EduStudent record);

    int insertSelective(EduStudent record);

    List<EduStudent> selectByExample(EduStudentExample example);

    EduStudent selectByPrimaryKey(Integer stuId);

    int updateByExampleSelective(@Param("record") EduStudent record, @Param("example") EduStudentExample example);

    int updateByExample(@Param("record") EduStudent record, @Param("example") EduStudentExample example);

    int updateByPrimaryKeySelective(EduStudent record);

    int updateByPrimaryKey(EduStudent record);

    int insertBatch(List<EduStudent> studentList);

    List<EduStudent> selectLearnRecordList(EduStudentExample example);

    long countLearnRecord(EduStudentExample example);

    List<Integer> selectIdByOrganId(int organId);

    List<String> selectCardNos();
}