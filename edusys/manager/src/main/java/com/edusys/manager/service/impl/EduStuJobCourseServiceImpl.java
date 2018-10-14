package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStuJobCourseMapper;
import com.edu.common.dao.model.EduStuJobCourse;
import com.edu.common.dao.model.EduStuJobCourseExample;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.pojo.ExportStudyVo;
import com.edu.common.util.ExportExcelUtils;
import com.edu.common.util.NumberUtils;
import com.edu.common.util.TimeUtils;
import com.edusys.manager.service.EduStuJobCourseService;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * EduStuJobCourseService实现
 * Created by Gary on 2017/4/25.
 */
@Service
@Transactional
@BaseService
public class EduStuJobCourseServiceImpl extends BaseServiceImpl<EduStuJobCourseMapper, EduStuJobCourse, EduStuJobCourseExample> implements EduStuJobCourseService {

    private static Logger _log = LoggerFactory.getLogger(EduStuJobCourseServiceImpl.class);

    @Autowired
    EduStuJobCourseMapper eduStuJobCourseMapper;

    @Override
    public List<EduStuJobCourse> selectJobsByStuIdPage(Integer stuId, Integer limit, Integer offset, String search) {
        return eduStuJobCourseMapper.selectJobsByStuIdPage(stuId, limit, offset, search);
    }

    @Override
    public List<EduStuJobCourse> selectCoursesByStuIdPage(Integer stuId, Integer jobId, Integer limit, Integer offset, String search) {
        return eduStuJobCourseMapper.selectCoursesByStuIdPage(stuId, jobId, limit, offset, search);
    }

    @Override
    public long jobsCountByStuId(Integer stuId, String search) {
        return eduStuJobCourseMapper.jobsCountByStuId(stuId, search);
    }

    @Override
    public long courseCountByStuId(Integer stuId, Integer jobId, String search) {
        return eduStuJobCourseMapper.courseCountByStuId(stuId, jobId, search);
    }

    @Override
    public List<ExportStudyVo> exportLearnRecord(Integer organId, Integer jobId) {

        return eduStuJobCourseMapper.exportStudyCourseListBy(organId, jobId);
    }

    @Override
    public void exportOperate(String[] titles, ServletOutputStream outputStream, List<ExportStudyVo> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 创建一个workbook 对应的一个excel应用文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workbook.createSheet("学习记录01");
        sheet.setColumnWidth((short) 0, (short) 15*256);
        sheet.setColumnWidth((short) 1, (short) 25*256);
        sheet.setColumnWidth((short) 2, (short) 30*256);
        sheet.setColumnWidth((short) 3, (short) 30*256);
        sheet.setColumnWidth((short) 4, (short) 15*256);
        sheet.setColumnWidth((short) 5, (short) 25*256);
        sheet.setColumnWidth((short) 6, (short) 25*256);
        sheet.setColumnWidth((short) 7, (short) 15*256);
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

        boolean tag = true;
        //构建表体数据
        if(list!=null && list.size()>0){
            for (int j = 0; j<list.size(); j++){
                XSSFRow bodyRow = sheet.createRow(j + 1);
                ExportStudyVo esv = list.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(j+1);

                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(esv.getStuName());

                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(esv.getCardno());

                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(esv.getOrganStr());

                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(esv.getJobName());

                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(TimeUtils.formatTime(esv.getStuTime()));

                cell = bodyRow.createCell(6);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(NumberUtils.getPercentStr(esv.getCourseNum(),esv.getTotalCourse()));
                cell = bodyRow.createCell(7);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(esv.getCourseNum());

                tag = false;
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
    }
}