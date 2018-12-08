package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentMapper;
import com.edu.common.dao.model.EduStudent;
import com.edu.common.dao.model.EduStudentExample;
import com.edu.common.util.ExportExcelUtils;
import com.edusys.manager.service.EduStudentService;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
* EduStudentService实现
* Created by Gary on 2017/4/12.
*/
@Service
@Transactional
@BaseService
public class EduStudentServiceImpl extends BaseServiceImpl<EduStudentMapper, EduStudent, EduStudentExample> implements EduStudentService {

    private static Logger _log = LoggerFactory.getLogger(EduStudentServiceImpl.class);

    @Autowired
    EduStudentMapper eduStudentMapper;

    @Override
    public int insertBatch(List<EduStudent> studentList) {
        return eduStudentMapper.insertBatch(studentList);
    }

    @Override
    public int exportExcel(String[] titles, ServletOutputStream outputStream, List<EduStudent> students) {
        // 创建一个workbook 对应的一个excel应用文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workbook.createSheet("学员列表01");
        sheet.setColumnWidth((short) 2, (short) 25*256);
        sheet.setColumnWidth((short) 3, (short) 15*256);
        sheet.setColumnWidth((short) 4, (short) 15*256);
        ExportExcelUtils exportExcelUtils = new ExportExcelUtils(workbook, sheet);
        XSSFCellStyle headStyle = exportExcelUtils.getHeadStyle();
        XSSFCellStyle bodyStyle = exportExcelUtils.getBodyStyle();
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        for(int i = 0; i < titles.length; i++){
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }

        //构建表体数据
        if(students!=null && students.size()>0){
            for (int j = 0; j<students.size(); j++){
                XSSFRow bodyRow = sheet.createRow(j + 1);
                EduStudent student = students.get(j);
                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(student.getStuNo());

                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(student.getStuName());

                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(student.getCardNo());

                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(student.getPhone());

                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(student.getOrganizationName2());
            }
        }

        try{
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                outputStream.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return 1;
    }

    @Override
    public long countLearnRecord(EduStudentExample example) {
        return eduStudentMapper.countLearnRecord(example);
    }

    @Override
    public List<Integer> selectIdByOrganId(int organId) {
        return eduStudentMapper.selectIdByOrganId(organId);
    }

    @Override
    public List<String> selectCardNos() {
        return eduStudentMapper.selectCardNos();
    }

    @Override
    public List<EduStudent> selectLearnRecordList(EduStudentExample example) {
        return eduStudentMapper.selectLearnRecordList(example);
    }
}