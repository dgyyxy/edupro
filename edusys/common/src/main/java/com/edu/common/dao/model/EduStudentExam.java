package com.edu.common.dao.model;

import java.io.Serializable;
import java.util.Date;

public class EduStudentExam implements Serializable {
    /**
     * 编号ID
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 学生编号ID
     *
     * @mbg.generated
     */
    private Integer stuId;

    /**
     * 开始时间
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     * 考试ID
     *
     * @mbg.generated
     */
    private Integer examId;

    /**
     * 试卷ID
     *
     * @mbg.generated
     */
    private Integer paperId;

    private Integer enabled;

    /**
     * 总分
     *
     * @mbg.generated
     */
    private Integer point;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Long createTime;

    /**
     * 考试时间
     *
     * @mbg.generated
     */
    private Integer duration;

    /**
     * 得分
     *
     * @mbg.generated
     */
    private Float pointGet;

    /**
     * 提交时间
     *
     * @mbg.generated
     */
    private Date submitTime;

    /**
     * 0 未审核 1 通过 2 未通过
     *
     * @mbg.generated
     */
    private Integer approved;

    /**
     * 考试密码
     *
     * @mbg.generated
     */
    private String examPassword;

    /**
     * 是否打乱试题顺序 0:是，1:否
     *
     * @mbg.generated
     */
    private Integer disorganize;

    /**
     * 是否允许查看试卷：0：不允许;1：允许
     *
     * @mbg.generated
     */
    private Integer islook;


    /**
     * 内容
     *
     * @mbg.generated
     */
    private String content;

    /**
     * 答案内容
     *
     * @mbg.generated
     */
    private String answerSheet;

    //考试名称
    private String examName;

    //题量
    private int amount;

    private String idcard;

    //监考强制交卷原因
    private String proctor;

    public String getProctor() {
        return proctor;
    }

    public void setProctor(String proctor) {
        this.proctor = proctor;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private String stuName;//学员姓名
    private String stuOrgan;//学员所属机构

    private static final long serialVersionUID = 1L;

    public Integer getIslook() {
        return islook;
    }

    public void setIslook(Integer islook) {
        this.islook = islook;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuOrgan() {
        return stuOrgan;
    }

    public void setStuOrgan(String stuOrgan) {
        this.stuOrgan = stuOrgan;
    }

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Float getPointGet() {
        return pointGet;
    }

    public void setPointGet(Float pointGet) {
        this.pointGet = pointGet;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public String getExamPassword() {
        return examPassword;
    }

    public void setExamPassword(String examPassword) {
        this.examPassword = examPassword;
    }

    public Integer getDisorganize() {
        return disorganize;
    }

    public void setDisorganize(Integer disorganize) {
        this.disorganize = disorganize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(String answerSheet) {
        this.answerSheet = answerSheet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", stuId=").append(stuId);
        sb.append(", startTime=").append(startTime);
        sb.append(", examId=").append(examId);
        sb.append(", paperId=").append(paperId);
        sb.append(", enabled=").append(enabled);
        sb.append(", point=").append(point);
        sb.append(", createTime=").append(createTime);
        sb.append(", duration=").append(duration);
        sb.append(", pointGet=").append(pointGet);
        sb.append(", submitTime=").append(submitTime);
        sb.append(", approved=").append(approved);
        sb.append(", examPassword=").append(examPassword);
        sb.append(", disorganize=").append(disorganize);
        sb.append(", islook=").append(islook);
        sb.append(", content=").append(content);
        sb.append(", answerSheet=").append(answerSheet);
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
        EduStudentExam other = (EduStudentExam) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStuId() == null ? other.getStuId() == null : this.getStuId().equals(other.getStuId()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getExamId() == null ? other.getExamId() == null : this.getExamId().equals(other.getExamId()))
            && (this.getPaperId() == null ? other.getPaperId() == null : this.getPaperId().equals(other.getPaperId()))
            && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()))
            && (this.getPoint() == null ? other.getPoint() == null : this.getPoint().equals(other.getPoint()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
            && (this.getPointGet() == null ? other.getPointGet() == null : this.getPointGet().equals(other.getPointGet()))
            && (this.getSubmitTime() == null ? other.getSubmitTime() == null : this.getSubmitTime().equals(other.getSubmitTime()))
            && (this.getApproved() == null ? other.getApproved() == null : this.getApproved().equals(other.getApproved()))
            && (this.getExamPassword() == null ? other.getExamPassword() == null : this.getExamPassword().equals(other.getExamPassword()))
            && (this.getDisorganize() == null ? other.getDisorganize() == null : this.getDisorganize().equals(other.getDisorganize()))
                && (this.getIslook() == null ? other.getIslook() == null : this.getIslook().equals(other.getIslook()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getAnswerSheet() == null ? other.getAnswerSheet() == null : this.getAnswerSheet().equals(other.getAnswerSheet()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStuId() == null) ? 0 : getStuId().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getExamId() == null) ? 0 : getExamId().hashCode());
        result = prime * result + ((getPaperId() == null) ? 0 : getPaperId().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        result = prime * result + ((getPoint() == null) ? 0 : getPoint().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getPointGet() == null) ? 0 : getPointGet().hashCode());
        result = prime * result + ((getSubmitTime() == null) ? 0 : getSubmitTime().hashCode());
        result = prime * result + ((getApproved() == null) ? 0 : getApproved().hashCode());
        result = prime * result + ((getExamPassword() == null) ? 0 : getExamPassword().hashCode());
        result = prime * result + ((getDisorganize() == null) ? 0 : getDisorganize().hashCode());
        result = prime * result + ((getIslook() == null) ? 0 : getIslook().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getAnswerSheet() == null) ? 0 : getAnswerSheet().hashCode());
        return result;
    }
}