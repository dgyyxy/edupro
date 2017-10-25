package com.edu.common.dao.model;

import java.io.Serializable;

public class EduStuJobCourse implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 学员编号
     *
     * @mbg.generated
     */
    private Integer stuId;

    /**
     * 学习任务编号
     *
     * @mbg.generated
     */
    private Integer jobId;

    /**
     * 课件编号
     *
     * @mbg.generated
     */
    private Integer courseId;

    /**
     * 0：未学，1：进行中，2：学完
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 学习时长
     *
     * @mbg.generated
     */
    private Integer time;

    /**
     * 0：未收藏，1：已收藏
     *
     * @mbg.generated
     */
    private Integer favorite;

    /**
     * 已学课件数量
     *
     * @mbg.generated
     */
    private Integer courseNum;

    /**
     * 总课件数
     *
     * @mbg.generated
     */
    private Integer totalCourse;

    //任务名称
    private String jobName;

    //课件名称
    private String courseName;

    //学时
    private int duration;

    //课件描述
    private String courseContent;

    private String picture;

    private String uriStr;

    private Integer timeval;

    private Integer studycount;

    public Integer getStudycount() {
        return studycount;
    }

    public void setStudycount(Integer studycount) {
        this.studycount = studycount;
    }

    public String getUriStr() {
        return uriStr;
    }

    public void setUriStr(String uriStr) {
        this.uriStr = uriStr;
    }

    public Integer getTimeval() {
        return timeval;
    }

    public void setTimeval(Integer timeval) {
        this.timeval = timeval;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(Integer courseNum) {
        this.courseNum = courseNum;
    }

    public Integer getTotalCourse() {
        return totalCourse;
    }

    public void setTotalCourse(Integer totalCourse) {
        this.totalCourse = totalCourse;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", stuId=").append(stuId);
        sb.append(", jobId=").append(jobId);
        sb.append(", courseId=").append(courseId);
        sb.append(", status=").append(status);
        sb.append(", time=").append(time);
        sb.append(", favorite=").append(favorite);
        sb.append(", courseNum=").append(courseNum);
        sb.append(", totalCourse=").append(totalCourse);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EduStuJobCourse other = (EduStuJobCourse) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStuId() == null ? other.getStuId() == null : this.getStuId().equals(other.getStuId()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getCourseId() == null ? other.getCourseId() == null : this.getCourseId().equals(other.getCourseId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
            && (this.getFavorite() == null ? other.getFavorite() == null : this.getFavorite().equals(other.getFavorite()))
            && (this.getCourseNum() == null ? other.getCourseNum() == null : this.getCourseNum().equals(other.getCourseNum()))
            && (this.getTotalCourse() == null ? other.getTotalCourse() == null : this.getTotalCourse().equals(other.getTotalCourse()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStuId() == null) ? 0 : getStuId().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getCourseId() == null) ? 0 : getCourseId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        result = prime * result + ((getFavorite() == null) ? 0 : getFavorite().hashCode());
        result = prime * result + ((getCourseNum() == null) ? 0 : getCourseNum().hashCode());
        result = prime * result + ((getTotalCourse() == null) ? 0 : getTotalCourse().hashCode());
        return result;
    }
}