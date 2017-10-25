package com.edu.common.dao.model;

import java.io.Serializable;

public class EduQuestion implements Serializable {
    /**
     * 编号ID
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 题库名称
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 题库内容
     *
     * @mbg.generated
     */
    private String content;

    /**
     * 题型编号
     *
     * @mbg.generated
     */
    private Integer questionTypeId;

    /**
     * 试题考试时间
     *
     * @mbg.generated
     */
    private Integer duration;

    /**
     * 题库分类
     *
     * @mbg.generated
     */
    private Integer questionCategoryId;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Long createTime;


    //所属分类名称
    private String categoryName;

    //题型
    private String typeName;

    /**
     * 创建者
     *
     * @mbg.generated
     */
    private String creator;

    private Integer exposeTimes;

    private Integer rightTimes;

    private Integer wrongTimes;

    /**
     * 难度级别
     *
     * @mbg.generated
     */
    private Integer difficulty;

    /**
     * 答案
     *
     * @mbg.generated
     */
    private String answer;

    private float point;

    private static final long serialVersionUID = 1L;

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(Integer questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getExposeTimes() {
        return exposeTimes;
    }

    public void setExposeTimes(Integer exposeTimes) {
        this.exposeTimes = exposeTimes;
    }

    public Integer getRightTimes() {
        return rightTimes;
    }

    public void setRightTimes(Integer rightTimes) {
        this.rightTimes = rightTimes;
    }

    public Integer getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(Integer wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", content=").append(content);
        sb.append(", questionTypeId=").append(questionTypeId);
        sb.append(", duration=").append(duration);
        sb.append(", questionCategoryId=").append(questionCategoryId);
        sb.append(", createTime=").append(createTime);
        sb.append(", creator=").append(creator);
        sb.append(", exposeTimes=").append(exposeTimes);
        sb.append(", rightTimes=").append(rightTimes);
        sb.append(", wrongTimes=").append(wrongTimes);
        sb.append(", difficulty=").append(difficulty);
        sb.append(", answer=").append(answer);
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
        EduQuestion other = (EduQuestion) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getQuestionTypeId() == null ? other.getQuestionTypeId() == null : this.getQuestionTypeId().equals(other.getQuestionTypeId()))
            && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
            && (this.getQuestionCategoryId() == null ? other.getQuestionCategoryId() == null : this.getQuestionCategoryId().equals(other.getQuestionCategoryId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getExposeTimes() == null ? other.getExposeTimes() == null : this.getExposeTimes().equals(other.getExposeTimes()))
            && (this.getRightTimes() == null ? other.getRightTimes() == null : this.getRightTimes().equals(other.getRightTimes()))
            && (this.getWrongTimes() == null ? other.getWrongTimes() == null : this.getWrongTimes().equals(other.getWrongTimes()))
            && (this.getDifficulty() == null ? other.getDifficulty() == null : this.getDifficulty().equals(other.getDifficulty()))
            && (this.getAnswer() == null ? other.getAnswer() == null : this.getAnswer().equals(other.getAnswer()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getQuestionTypeId() == null) ? 0 : getQuestionTypeId().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getQuestionCategoryId() == null) ? 0 : getQuestionCategoryId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getExposeTimes() == null) ? 0 : getExposeTimes().hashCode());
        result = prime * result + ((getRightTimes() == null) ? 0 : getRightTimes().hashCode());
        result = prime * result + ((getWrongTimes() == null) ? 0 : getWrongTimes().hashCode());
        result = prime * result + ((getDifficulty() == null) ? 0 : getDifficulty().hashCode());
        result = prime * result + ((getAnswer() == null) ? 0 : getAnswer().hashCode());
        return result;
    }
}