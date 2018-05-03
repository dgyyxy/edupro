package com.edu.common.dao.model;

import com.edu.common.dao.pojo.QueryTypeRate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class EduPaper implements Serializable {
    /**
     * 编号ID
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 试卷名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 试卷考试时间
     *
     * @mbg.generated
     */
    private Integer duration;

    /**
     * 总分
     *
     * @mbg.generated
     */
    private Integer totalPoint;

    /**
     * 及格分
     *
     * @mbg.generated
     */
    private Integer passPoint;

    /**
     * 分类ID
     *
     * @mbg.generated
     */
    private Integer categoryId;

    /**
     * 试卷状态， 0未完成 -> 1已完成 -> 2已发布 -> 3通过审核 （已发布和通过审核的无法再修改）
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Long createTime;

    /**
     * 试卷简介
     *
     * @mbg.generated
     */
    private String summary;

    /**
     * 创建者
     *
     * @mbg.generated
     */
    private String creator;

    /**
     * 组卷方式,1:手动组卷;2:自动组卷
     *
     * @mbg.generated
     */
    private Integer paperType;

    /**
     * 题量
     *
     * @mbg.generated
     */
    private Integer amount;

    /**
     * 试卷内容
     *
     * @mbg.generated
     */
    private String content;

    /**
     * 试卷答案，用答题卡的结构保存
     *
     * @mbg.generated
     */
    private String answerSheet;

    /**
     * 试题类型数量
     */
    private HashMap<Integer,Integer> questionTypeNum;
    /**
     * 试题类型分数
     */
    private HashMap<Integer,Float> questionTypePoint;
    /**
     * 试题知识点比例
     */
    private HashMap<Integer,Float> questionTypeRate;

    private String categoryName;

    private List<QueryTypeRate> queryTypeRateList;

    private static final long serialVersionUID = 1L;

    public List<QueryTypeRate> getQueryTypeRateList() {
        return queryTypeRateList;
    }

    public void setQueryTypeRateList(List<QueryTypeRate> queryTypeRateList) {
        this.queryTypeRateList = queryTypeRateList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public HashMap<Integer, Integer> getQuestionTypeNum() {
        return questionTypeNum;
    }

    public void setQuestionTypeNum(HashMap<Integer, Integer> questionTypeNum) {
        this.questionTypeNum = questionTypeNum;
    }

    public HashMap<Integer, Float> getQuestionTypePoint() {
        return questionTypePoint;
    }

    public void setQuestionTypePoint(HashMap<Integer, Float> questionTypePoint) {
        this.questionTypePoint = questionTypePoint;
    }

    public HashMap<Integer, Float> getQuestionTypeRate() {
        return questionTypeRate;
    }

    public void setQuestionTypeRate(HashMap<Integer, Float> questionTypeRate) {
        this.questionTypeRate = questionTypeRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(Integer passPoint) {
        this.passPoint = passPoint;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getPaperType() {
        return paperType;
    }

    public void setPaperType(Integer paperType) {
        this.paperType = paperType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
        sb.append(", name=").append(name);
        sb.append(", duration=").append(duration);
        sb.append(", totalPoint=").append(totalPoint);
        sb.append(", passPoint=").append(passPoint);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", summary=").append(summary);
        sb.append(", creator=").append(creator);
        sb.append(", paperType=").append(paperType);
        sb.append(", amount=").append(amount);
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
        EduPaper other = (EduPaper) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
            && (this.getTotalPoint() == null ? other.getTotalPoint() == null : this.getTotalPoint().equals(other.getTotalPoint()))
            && (this.getPassPoint() == null ? other.getPassPoint() == null : this.getPassPoint().equals(other.getPassPoint()))
            && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getPaperType() == null ? other.getPaperType() == null : this.getPaperType().equals(other.getPaperType()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getAnswerSheet() == null ? other.getAnswerSheet() == null : this.getAnswerSheet().equals(other.getAnswerSheet()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getTotalPoint() == null) ? 0 : getTotalPoint().hashCode());
        result = prime * result + ((getPassPoint() == null) ? 0 : getPassPoint().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getPaperType() == null) ? 0 : getPaperType().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getAnswerSheet() == null) ? 0 : getAnswerSheet().hashCode());
        return result;
    }
}