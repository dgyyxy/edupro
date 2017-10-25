package com.edu.common.dao.model;

/**
 * Created by Gary on 2017/5/8.
 */
public class QuestionResult {
    private int questionId;
    private String content;
    private String answer;
    private int questionTypeId;
    private float questionPoint;
    private int knowledgePointId;

    public QuestionResult() {
    }

    public QuestionResult(int questionId, String content, String answer, int questionTypeId, float questionPoint, int knowledgePointId) {
        this.questionId = questionId;
        this.content = content;
        this.answer = answer;
        this.questionTypeId = questionTypeId;
        this.questionPoint = questionPoint;
        this.knowledgePointId = knowledgePointId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(int questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public float getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(float questionPoint) {
        this.questionPoint = questionPoint;
    }

    public int getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(int knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }
}
