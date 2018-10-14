package com.edu.common.dao.pojo;

/**
 * Created by Gary.Duan on 2018/9/12.<br >
 * 导出学习记录列表
 */
public class ExportStudyVo {
    private String stuName;//学员姓名
    private String cardno;//证件号
    private String organStr;//所属机构
    private String jobName;//学习任务
    private int stuTime;//学习时长
    private int courseNum;//已学课件
    private int totalCourse;//课件总数

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getOrganStr() {
        return organStr;
    }

    public void setOrganStr(String organStr) {
        this.organStr = organStr;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getStuTime() {
        return stuTime;
    }

    public void setStuTime(int stuTime) {
        this.stuTime = stuTime;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public int getTotalCourse() {
        return totalCourse;
    }

    public void setTotalCourse(int totalCourse) {
        this.totalCourse = totalCourse;
    }
}
