package com.edu.common.dao.pojo;

import java.io.Serializable;

/**
 * Created by Gary on 2017/11/26.
 *
 * 出错率、答题数统计实体类
 */
public class StatisticBean implements Serializable {

    private int questionId;//题目ID
    private int sum;//答题数
    private int errorCount;//答错数
    private String percentage;//错题率

    public StatisticBean() {
    }

    public StatisticBean(int questionId) {
        this.questionId = questionId;
    }

    public StatisticBean(int questionId, int sum, int errorCount) {
        this.questionId = questionId;
        this.sum = sum;
        this.errorCount = errorCount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "StatisticBean{" +
                "questionId=" + questionId +
                ", sum=" + sum +
                ", errorCount=" + errorCount +
                ", percentage='" + percentage + '\'' +
                '}';
    }
}
