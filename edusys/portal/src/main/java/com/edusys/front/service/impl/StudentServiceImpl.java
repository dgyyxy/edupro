package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentMapper;
import com.edu.common.dao.model.EduStudent;
import com.edu.common.dao.model.EduStudentExample;
import com.edusys.front.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Gary on 2017/5/16.
 */
@Service
@Transactional
@BaseService
public class StudentServiceImpl extends BaseServiceImpl<EduStudentMapper, EduStudent, EduStudentExample> implements StudentService {

    private static Logger _log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private EduStudentMapper eduStudentMapper;

}
