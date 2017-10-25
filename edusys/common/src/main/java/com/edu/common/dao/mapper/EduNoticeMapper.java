package com.edu.common.dao.mapper;

import java.util.List;

import com.edu.common.dao.model.EduNotice;
import com.edu.common.dao.model.EduNoticeExample;
import org.apache.ibatis.annotations.Param;

public interface EduNoticeMapper {
    long countByExample(EduNoticeExample example);

    int deleteByExample(EduNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduNotice record);

    int insertSelective(EduNotice record);

    List<EduNotice> selectByExample(EduNoticeExample example);

    EduNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduNotice record, @Param("example") EduNoticeExample example);

    int updateByExample(@Param("record") EduNotice record, @Param("example") EduNoticeExample example);

    int updateByPrimaryKeySelective(EduNotice record);

    int updateByPrimaryKey(EduNotice record);
}