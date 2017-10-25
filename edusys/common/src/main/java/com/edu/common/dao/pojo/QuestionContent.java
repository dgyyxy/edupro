package com.edu.common.dao.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.LinkedHashMap;

/**
 * Created by Gary on 2017/5/9.
 */
@XStreamAlias("QuestionContent")
public class QuestionContent {

    @XStreamAlias("title")
    private String title;
    @XStreamAlias("choiceList")
    private LinkedHashMap<String, String> choiceList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LinkedHashMap<String, String> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(LinkedHashMap<String, String> choiceList) {
        this.choiceList = choiceList;
    }
}
