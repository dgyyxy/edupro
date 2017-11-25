package com.edu.common.dao.pojo;

/**
 * Created by Gary on 2017/11/6.
 */
public class ExamPassRate {

    //考试ID
    private Integer examId;
    //及格率
    private Double passRate;

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Double getPassRate() {
        return passRate;
    }

    public void setPassRate(Double passRate) {
        this.passRate = passRate;
    }
}
