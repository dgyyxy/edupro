package com.edu.common.dao.model;

import java.io.Serializable;

public class EduStudent implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated
     */
    private Integer stuId;

    /**
     * 姓名
     *
     * @mbg.generated
     */
    private String stuName;

    /**
     * 身份证号
     *
     * @mbg.generated
     */
    private String cardNo;

    /**
     * 学号
     *
     * @mbg.generated
     */
    private String stuNo;

    /**
     * 手机号
     *
     * @mbg.generated
     */
    private String phone;

    /**
     * 登录密码
     *
     * @mbg.generated
     */
    private String password;

    /**
     * 一级机构编号
     *
     * @mbg.generated
     */
    private Integer organizationId1;

    /**
     * 一级机构名称
     *
     * @mbg.generated
     */
    private String organizationName1;

    /**
     * 二级机构编号
     *
     * @mbg.generated
     */
    private Integer organizationId2;

    /**
     * 二级机构名称
     *
     * @mbg.generated
     */
    private String organizationName2;

    /**
     * 学员学习任务数
     */
    private Integer jobCount;

    //公司名称
    private String company;


    //学习记录属性
    private Integer studyCount;//已学任务数

    private Integer unStudyCount;//未学任务数

    private Integer sumTime;//总学时

    private static final long serialVersionUID = 1L;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Integer studyCount) {
        this.studyCount = studyCount;
    }

    public Integer getUnStudyCount() {
        return unStudyCount;
    }

    public void setUnStudyCount(Integer unStudyCount) {
        this.unStudyCount = unStudyCount;
    }

    public Integer getSumTime() {
        return sumTime;
    }

    public void setSumTime(Integer sumTime) {
        this.sumTime = sumTime;
    }

    public Integer getJobCount() {
        return jobCount;
    }

    public void setJobCount(Integer jobCount) {
        this.jobCount = jobCount;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOrganizationId1() {
        return organizationId1;
    }

    public void setOrganizationId1(Integer organizationId1) {
        this.organizationId1 = organizationId1;
    }

    public String getOrganizationName1() {
        return organizationName1;
    }

    public void setOrganizationName1(String organizationName1) {
        this.organizationName1 = organizationName1;
    }

    public Integer getOrganizationId2() {
        return organizationId2;
    }

    public void setOrganizationId2(Integer organizationId2) {
        this.organizationId2 = organizationId2;
    }

    public String getOrganizationName2() {
        return organizationName2;
    }

    public void setOrganizationName2(String organizationName2) {
        this.organizationName2 = organizationName2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", stuId=").append(stuId);
        sb.append(", stuName=").append(stuName);
        sb.append(", cardNo=").append(cardNo);
        sb.append(", stuNo=").append(stuNo);
        sb.append(", phone=").append(phone);
        sb.append(", password=").append(password);
        sb.append(", organizationId1=").append(organizationId1);
        sb.append(", organizationName1=").append(organizationName1);
        sb.append(", organizationId2=").append(organizationId2);
        sb.append(", organizationName2=").append(organizationName2);
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
        EduStudent other = (EduStudent) that;
        return (this.getStuId() == null ? other.getStuId() == null : this.getStuId().equals(other.getStuId()))
            && (this.getStuName() == null ? other.getStuName() == null : this.getStuName().equals(other.getStuName()))
            && (this.getCardNo() == null ? other.getCardNo() == null : this.getCardNo().equals(other.getCardNo()))
            && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getOrganizationId1() == null ? other.getOrganizationId1() == null : this.getOrganizationId1().equals(other.getOrganizationId1()))
            && (this.getOrganizationName1() == null ? other.getOrganizationName1() == null : this.getOrganizationName1().equals(other.getOrganizationName1()))
            && (this.getOrganizationId2() == null ? other.getOrganizationId2() == null : this.getOrganizationId2().equals(other.getOrganizationId2()))
            && (this.getOrganizationName2() == null ? other.getOrganizationName2() == null : this.getOrganizationName2().equals(other.getOrganizationName2()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStuId() == null) ? 0 : getStuId().hashCode());
        result = prime * result + ((getStuName() == null) ? 0 : getStuName().hashCode());
        result = prime * result + ((getCardNo() == null) ? 0 : getCardNo().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getOrganizationId1() == null) ? 0 : getOrganizationId1().hashCode());
        result = prime * result + ((getOrganizationName1() == null) ? 0 : getOrganizationName1().hashCode());
        result = prime * result + ((getOrganizationId2() == null) ? 0 : getOrganizationId2().hashCode());
        result = prime * result + ((getOrganizationName2() == null) ? 0 : getOrganizationName2().hashCode());
        return result;
    }
}