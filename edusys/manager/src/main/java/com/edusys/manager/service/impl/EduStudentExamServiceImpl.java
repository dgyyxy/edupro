package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentExamMapper;
import com.edu.common.dao.mapper.EduStudentMapper;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.util.ExportExcelUtils;
import com.edu.common.util.RandomUtil;
import com.edusys.manager.service.EduStudentExamService;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
* EduStudentExamService实现
* Created by Gary on 2017/5/10.
*/
@Service
@Transactional
@BaseService
public class EduStudentExamServiceImpl extends BaseServiceImpl<EduStudentExamMapper, EduStudentExam, EduStudentExamExample> implements EduStudentExamService {

    private static Logger _log = LoggerFactory.getLogger(EduStudentExamServiceImpl.class);

    @Autowired
    EduStudentExamMapper eduStudentExamMapper;

    @Autowired
    EduStudentMapper eduStudentMapper;


    /**
     * 选取开考学员
     * @param stuIds
     * @param paper
     * @param exam
     */
    @Override
    public void examByStudents(List<Integer> stuIds, EduPaper paper, EduExam exam) {
        if(stuIds.size()>0){
            List<EduStudentExam> studentExams = new ArrayList<EduStudentExam>();
            for (Integer id : stuIds){
                EduStudentExam studentExam = new EduStudentExam();
                studentExam.setStuId(id);
                studentExam.setIslook(exam.getIslook());
                studentExam.setExamId(exam.getId());
                studentExam.setPaperId(paper.getId());
                studentExam.setContent(paper.getContent());
                studentExam.setDuration(exam.getDuration());
                studentExam.setApproved(0);//审核通过
                studentExam.setExamPassword(RandomUtil.getFourRandNum());
                studentExam.setDisorganize(exam.getDisorganize());//是否打乱题目显示顺序
                studentExam.setPoint(exam.getTotalPoint());
                long time = System.currentTimeMillis();
                studentExam.setCreateTime(time);

                studentExams.add(studentExam);
            }
            //批量新增
            eduStudentExamMapper.insertBatch(studentExams);
        }
    }

    @Override
    public int exportExcel(String[] titles, ServletOutputStream outputStream, List<EduStudentExam> studentExamList, String examName, String className, String companyName, String passRate, EduExam exam) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 创建一个workbook 对应的一个excel应用文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workbook.createSheet("学员成绩01");
        sheet.setColumnWidth((short) 0, (short) 15*256);
        sheet.setColumnWidth((short) 1, (short) 25*256);
        sheet.setColumnWidth((short) 2, (short) 30*256);
        sheet.setColumnWidth((short) 3, (short) 15*256);
        sheet.setColumnWidth((short) 4, (short) 15*256);
        sheet.setColumnWidth((short) 5, (short) 25*256);
        sheet.setColumnWidth((short) 6, (short) 25*256);
        sheet.setColumnWidth((short) 7, (short) 25*256);
        sheet.setColumnWidth((short) 8, (short) 25*256);
        sheet.setColumnWidth((short) 9, (short) 15*256);
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
        if(studentExamList!=null && studentExamList.size()>0){
            for (int j = 0; j<studentExamList.size(); j++){
                XSSFRow bodyRow = sheet.createRow(j + 1);
                EduStudentExam studentExam = studentExamList.get(j);

                cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(j+1);

                String stuNameCard = studentExam.getStuName();
                String stuName = stuNameCard.substring(0, stuNameCard.indexOf("("));
                String cardNo = stuNameCard.substring(stuNameCard.indexOf("(")+1, stuNameCard.length()-1);

                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(stuName);

                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(cardNo);

                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(examName);

                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(studentExam.getPointGet()==null? 0 : studentExam.getPointGet());

                String stuOrgan = studentExam.getStuOrgan();
                String[] organs = stuOrgan.split("--");

                cell = bodyRow.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(tag ? className : "");

                cell = bodyRow.createCell(6);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(tag ? companyName : "");

                cell = bodyRow.createCell(7);
                cell.setCellStyle(bodyStyle);
                /*Date examDate = studentExam.getSubmitTime();
                String examDateStr = "";
                if(examDate!=null){
                    examDateStr = sdf.format(studentExam.getSubmitTime());
                }*/
                cell.setCellValue(tag ? sdf.format(exam.getStartTime()) : "");

                cell = bodyRow.createCell(8);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(tag ? sdf.format(exam.getEndTime()) : "");

                cell = bodyRow.createCell(9);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(tag ? passRate+'%' : "");

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
        return 1;
    }

    /**
     * 通过获取参考学员列表
     * @param examId
     * @return
     */
    @Override
    public List<Integer> getStuIdList(Integer examId) {
        return eduStudentExamMapper.getStuIdList(examId);
    }

    @Override
    public void updateUserExamHist(AnswerSheet answerSheet, String answerSheetStr, int approved) {
        eduStudentExamMapper.updateUserExamHist(answerSheet, answerSheetStr, approved);
    }

    @Override
    public void updateStudentExamByExamId(EduExam record) {
        eduStudentExamMapper.updateStudentExamByExamId(record);
    }

    @Override
    public void stopExamOperate(List<Integer> idList) {
        eduStudentExamMapper.stopExamOperate(idList);
    }


    public static void main(String[] args) {
        String str = "黄超(310222200811112000)";
        System.out.println(str.indexOf("(")-1);
        System.out.println(str.substring(0, str.indexOf("(")));
        System.out.println(str.substring(str.indexOf("(")+1, str.length()-1));

        str = "艾克斯学院--超人班";
        String[] array = str.split("--");
        System.out.println(array[0]+"--"+array[1]);
    }
}