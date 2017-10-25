package com.edu.common.dao.pojo;

import java.util.Date;
import java.util.List;

/**
 * Created by Gary on 2017/5/9.
 */
public class AnswerSheet {
    private int examHistroyId;
    private int examId;
    private int examPaperId;
    private int duration;
    private List<AnswerSheetItem> answerSheetItems;
    private float pointMax;
    private float pointRaw ;
    private Date startTime;
    private float pointPass;

    public float getPointPass() {
        return pointPass;
    }

    public void setPointPass(float pointPass) {
        this.pointPass = pointPass;
    }

    public int getExamHistroyId() {
        return examHistroyId;
    }

    public void setExamHistroyId(int examHistroyId) {
        this.examHistroyId = examHistroyId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(int examPaperId) {
        this.examPaperId = examPaperId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<AnswerSheetItem> getAnswerSheetItems() {
        return answerSheetItems;
    }

    public void setAnswerSheetItems(List<AnswerSheetItem> answerSheetItems) {
        this.answerSheetItems = answerSheetItems;
    }

    public float getPointMax() {
        return pointMax;
    }

    public void setPointMax(float pointMax) {
        this.pointMax = pointMax;
    }

    public float getPointRaw() {
        return pointRaw;
    }

    public void setPointRaw(float pointRaw) {
        this.pointRaw = pointRaw;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
