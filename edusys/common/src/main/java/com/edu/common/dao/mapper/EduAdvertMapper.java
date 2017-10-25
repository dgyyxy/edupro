package com.edu.common.dao.mapper;

import java.util.List;

import com.edu.common.dao.model.EduAdvert;
import com.edu.common.dao.model.EduAdvertExample;
import org.apache.ibatis.annotations.Param;

public interface EduAdvertMapper {
    long countByExample(EduAdvertExample example);

    int deleteByExample(EduAdvertExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduAdvert record);

    int insertSelective(EduAdvert record);

    List<EduAdvert> selectByExample(EduAdvertExample example);

    EduAdvert selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduAdvert record, @Param("example") EduAdvertExample example);

    int updateByExample(@Param("record") EduAdvert record, @Param("example") EduAdvertExample example);

    int updateByPrimaryKeySelective(EduAdvert record);

    int updateByPrimaryKey(EduAdvert record);
}