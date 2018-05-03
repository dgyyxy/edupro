package com.edu.common.dao.pojo;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/4/22 0022.
 */
public class QueryTypeRate {
    private Integer questionTypeId;
    /**
     * 试题类型数量
     */
    private HashMap<Integer,Integer> questionTypeNum;
    /**
     * 试题类型分数
     */
    private HashMap<Integer,Float> questionTypePoint;

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
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
}
