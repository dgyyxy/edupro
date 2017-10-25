package com.edu.common.dao.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class EduExam implements Serializable {
    /**
     * 编号IDpackage com.edu.common.dao.model;
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 考试名称
     *
     * @mbg.generated
     */
    private String examName;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Long createTime;

    /**
     * 开始时间
     *
     * @mbg.generated
     */
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date startTime;

    /**
     * 结束时间
     *
     * @mbg.generated
     */
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date endTime;

    /**
     * 创建者
     *
     * @mbg.generated
     */
    private String creator;

    /**
     * 0 未审核, 1 审核通过, 2 审核不通过
     *
     * @mbg.generated
     */
    private Integer approved;

    /**
     * 试卷ID
     *
     * @mbg.generated
     */
    private Integer paperId;

    /**
     * 考试密码
     *
     * @mbg.generated
     */
    private Integer examPassword;

    /**
     * 是否打乱试题顺序 0:是，1:否
     *
     * @mbg.generated
     */
    private Integer disorganize;

    /**
     * 总分
     *
     * @mbg.generated
     */
    private Integer totalPoint;

    /**
     * 题量
     *
     * @mbg.generated
     */
    private Integer amount;

    /**
     * 及格线
     *
     * @mbg.generated
     */
    private Integer passPoint;

    /**
     * 考生人数
     *
     * @mbg.generated
     */
    private Integer stuNum;

    /**
     * 考试总时长
     *
     * @mbg.generated
     */
    private Integer duration;

    /**
     * 是否允许查看试卷：0：不允许;1：允许
     *
     * @mbg.generated
     */
    private Integer islook;

    /**
     * 考试试卷名称
     */
    private String paperName;

    /**
     * 考试设置方式
     */
    private Integer examType;

    //考试密码
    private String examPwd;

    private Integer status;//0:未登录，1:考试中，2:及格，3:不及格，4:已强制交卷

    private Integer pointGet;//得分

    private int examingCount;//在考人数

    private int submitCount;//交卷人数

    public Integer getPointGet() {
        return pointGet;
    }

    public void setPointGet(Integer pointGet) {
        this.pointGet = pointGet;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getExamingCount() {
        return examingCount;
    }

    public void setExamingCount(int examingCount) {
        this.examingCount = examingCount;
    }

    public int getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(int submitCount) {
        this.submitCount = submitCount;
    }

    public String getExamPwd() {
        return examPwd;
    }

    public void setExamPwd(String examPwd) {
        this.examPwd = examPwd;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    private static final long serialVersionUID = 1L;

    public Integer getIslook() {
        return islook;
    }

    public void setIslook(Integer islook) {
        this.islook = islook;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getExamPassword() {
        return examPassword;
    }

    public void setExamPassword(Integer examPassword) {
        this.examPassword = examPassword;
    }

    public Integer getDisorganize() {
        return disorganize;
    }

    public void setDisorganize(Integer disorganize) {
        this.disorganize = disorganize;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(Integer passPoint) {
        this.passPoint = passPoint;
    }

    public Integer getStuNum() {
        return stuNum;
    }

    public void setStuNum(Integer stuNum) {
        this.stuNum = stuNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", examName=").append(examName);
        sb.append(", createTime=").append(createTime);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", creator=").append(creator);
        sb.append(", approved=").append(approved);
        sb.append(", paperId=").append(paperId);
        sb.append(", examPassword=").append(examPassword);
        sb.append(", disorganize=").append(disorganize);
        sb.append(", totalPoint=").append(totalPoint);
        sb.append(", amount=").append(amount);
        sb.append(", passPoint=").append(passPoint);
        sb.append(", stuNum=").append(stuNum);
        sb.append(", islook=").append(islook);
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
        EduExam other = (EduExam) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getExamName() == null ? other.getExamName() == null : this.getExamName().equals(other.getExamName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getApproved() == null ? other.getApproved() == null : this.getApproved().equals(other.getApproved()))
            && (this.getPaperId() == null ? other.getPaperId() == null : this.getPaperId().equals(other.getPaperId()))
            && (this.getExamPassword() == null ? other.getExamPassword() == null : this.getExamPassword().equals(other.getExamPassword()))
            && (this.getDisorganize() == null ? other.getDisorganize() == null : this.getDisorganize().equals(other.getDisorganize()))
            && (this.getTotalPoint() == null ? other.getTotalPoint() == null : this.getTotalPoint().equals(other.getTotalPoint()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getPassPoint() == null ? other.getPassPoint() == null : this.getPassPoint().equals(other.getPassPoint()))
            && (this.getStuNum() == null ? other.getStuNum() == null : this.getStuNum().equals(other.getStuNum()))
            && (this.getIslook() == null ? other.getIslook() == null : this.getIslook().equals(other.getIslook()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getExamName() == null) ? 0 : getExamName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getApproved() == null) ? 0 : getApproved().hashCode());
        result = prime * result + ((getPaperId() == null) ? 0 : getPaperId().hashCode());
        result = prime * result + ((getExamPassword() == null) ? 0 : getExamPassword().hashCode());
        result = prime * result + ((getDisorganize() == null) ? 0 : getDisorganize().hashCode());
        result = prime * result + ((getTotalPoint() == null) ? 0 : getTotalPoint().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getPassPoint() == null) ? 0 : getPassPoint().hashCode());
        result = prime * result + ((getStuNum() == null) ? 0 : getStuNum().hashCode());
        result = prime * result + ((getIslook() == null) ? 0 : getIslook().hashCode());
        return result;
    }
}