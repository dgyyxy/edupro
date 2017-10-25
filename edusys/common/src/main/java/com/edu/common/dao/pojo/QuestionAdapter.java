package com.edu.common.dao.pojo;

import com.edu.common.dao.model.EduQuestion;
import com.edu.common.dao.model.QuestionResult;
import com.google.gson.Gson;

import java.util.Iterator;

/**
 * Created by Gary on 2017/5/9.
 */
public class QuestionAdapter {

    private EduQuestion question;
    private QuestionContent questionContent;
    private AnswerSheetItem answerSheetItem;
    private QuestionResult questionResult;
    private String baseUrl;

    public String pointStrFormat(float point){

        if(point > (int)point){
            return point + "";
        }
        return (int)point + "";
    }
    /**
     *
     * @param question
     *            试题
     * @param answerSheetItem
     *            答题卡
     * @param questionResult
     *            试题描述
     */
    public QuestionAdapter(EduQuestion question, AnswerSheetItem answerSheetItem,
                           QuestionResult questionResult, String baseUrl) {

        this.question = question;
        this.answerSheetItem = answerSheetItem;
        this.questionResult = questionResult;
        Gson gson = new Gson();
        this.questionContent = gson.fromJson(question.getContent(), QuestionContent.class);

        this.baseUrl = baseUrl;
    }

    public QuestionAdapter(AnswerSheetItem answerSheetItem,
                           QuestionResult questionResult, String baseUrl) {
        this.answerSheetItem = answerSheetItem;
        this.questionResult = questionResult;
        Gson gson = new Gson();
        this.questionContent = gson.fromJson(question.getContent(), QuestionContent.class);
        this.baseUrl = baseUrl;
    }

    public QuestionAdapter(QuestionResult questionResult,
                           String baseUrl) {
        this.questionResult = questionResult;
        Gson gson = new Gson();
        this.questionContent = gson.fromJson(questionResult.getContent(), QuestionContent.class);
        this.baseUrl = baseUrl;
    }

    /**
     * 组卷专用
     *
     * @return
     */
    public String getStringFromXML() {
        StringBuilder sb = new StringBuilder();

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("<li class=\"question qt-singlechoice\">");
                break;
            case 2:
                sb.append("<li class=\"question qt-multiplechoice\">");
                break;
            case 3:
                sb.append("<li class=\"question qt-trueorfalse\">");
                break;
            case 4:
                sb.append("<li class=\"question qt-fillblank\">");
                break;
            default:
                break;
        }

        sb.append("<div class=\"question-title\">");
        sb.append("<div class=\"question-title-icon\"></div>");
        sb.append("<span class=\"question-no\">").append("</span>");
        sb.append("<span class=\"question-type\" style=\"display: none;\">");

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("singlechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[单选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it1 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it1.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it1.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"radio\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append("<span class=\"question-li-text\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 2:
                sb.append("multiplechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[多选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it2 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it2.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it2.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"checkbox\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append("<span class=\"question-li-text\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 3:
                sb.append("trueorfalse").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[是非题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<ul class=\"question-opt-list\">");

                sb.append("<li class=\"question-list-item\">").append(
                        "<input type=\"radio\" value=\"T\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">正确</span>").append("</li>");

                sb.append("<li class=\"question-list-item\">").append("<input type=\"radio\" value=\"F\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">错误</span>").append("</li>");

                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 4:
                sb.append("fillblank").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[填空题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<textarea class=\"question-textarea\"></textarea>");
                sb.append("</form>");
                break;
            default:
                break;
        }
        sb.append("<div class=\"answer-desc\">");
        sb.append("<div class=\"answer-desc-summary\">");
        sb.append("<span>正确答案：</span>");
        if (questionResult.getQuestionTypeId() == 3) {
            if (questionResult.getAnswer().equals("T"))
                sb.append("<span class=\"answer_value\">").append("对").append("</span><br>");
            else if (questionResult.getAnswer().equals("F"))
                sb.append("<span class=\"answer_value\">").append("错").append("</span><br>");
            else
                sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                        .append("</span><br>");
        } else
            sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                    .append("</span><br>");
        sb.append("</div>");

        sb.append("</li>");
        return sb.toString();
    }

    /**
     * 组卷专用
     *
     * @return
     */
    public String getStringFromXML2() {
        StringBuilder sb = new StringBuilder();

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("<li class=\"question qt-singlechoice\">");
                break;
            case 2:
                sb.append("<li class=\"question qt-multiplechoice\">");
                break;
            case 3:
                sb.append("<li class=\"question qt-trueorfalse\">");
                break;
            case 4:
                sb.append("<li class=\"question qt-fillblank\">");
                break;
            default:
                break;
        }

        sb.append("<div class=\"question-title\">");
        sb.append("<div class=\"question-title-icon\"></div>");
        sb.append("<span class=\"question-no\">").append("</span>");
        sb.append("<span class=\"question-type\" style=\"display: none;\">");

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("singlechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[单选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it1 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it1.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it1.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"radio\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append("<span class=\"question-li-text\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 2:
                sb.append("multiplechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[多选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it2 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it2.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it2.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"checkbox\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append("<span class=\"question-li-text\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 3:
                sb.append("trueorfalse").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[是非题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<ul class=\"question-opt-list\">");

                sb.append("<li class=\"question-list-item\">").append(
                        "<input type=\"radio\" value=\"T\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">正确</span>").append("</li>");

                sb.append("<li class=\"question-list-item\">").append("<input type=\"radio\" value=\"F\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">错误</span>").append("</li>");

                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 4:
                sb.append("fillblank").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[填空题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<textarea class=\"question-textarea\"></textarea>");
                sb.append("</form>");
                break;
            default:
                break;
        }
        sb.append("<div class=\"answer-desc\">");
        sb.append("<div class=\"answer-desc-summary\">");
        sb.append("<span>正确答案：</span>");
        if (questionResult.getQuestionTypeId() == 3) {
            if (questionResult.getAnswer().equals("T"))
                sb.append("<span class=\"answer_value\">").append("正确").append("</span><br>");
            else if (questionResult.getAnswer().equals("F"))
                sb.append("<span class=\"answer_value\">").append("错误").append("</span><br>");
            else
                sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                        .append("</span><br>");
        } else
            sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                    .append("</span><br>");

        sb.append("</div>");

        sb.append("<div class=\"answer-desc-summary\">");
        sb.append("<span>您的答案：</span>");
        sb.append("<span class=\"answer_value\" id=\"question_answer_"+questionResult.getQuestionId()+"\">").append("").append("</span><br>");
        sb.append("</div>");


        sb.append("</li>");
        return sb.toString();
    }

    public String getReportStringFromXML(){
        StringBuilder sb = new StringBuilder();

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("<li class=\"question qt-singlechoice\">");
                break;
            case 2:
                sb.append("<li class=\"question qt-multiplechoice\">");
                break;
            case 3:
                sb.append("<li class=\"question qt-trueorfalse\">");
                break;
            case 4:
                sb.append("<li class=\"question qt-fillblank\">");
                break;
            default:
                break;
        }

        sb.append("<div class=\"question-title\">");
        sb.append("<div class=\"question-title-icon\"></div>");
        sb.append("<span class=\"question-no\">").append("</span>");
        sb.append("<span class=\"question-type\" style=\"display: none;\">");

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("singlechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[单选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it1 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it1.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it1.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"radio\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append("<span class=\"question-li-text\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 2:
                sb.append("multiplechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[多选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it2 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it2.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it2.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"checkbox\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 3:
                sb.append("trueorfalse").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[判断题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<ul class=\"question-opt-list\">");

                sb.append("<li class=\"question-list-item\">").append(
                        "<input type=\"radio\" value=\"T\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">正确</span>").append("</li>");

                sb.append("<li class=\"question-list-item\">").append("<input type=\"radio\" value=\"F\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">错误</span>").append("</li>");

                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 4:
                sb.append("fillblank").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[填空题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<textarea class=\"question-textarea\"></textarea>");
                sb.append("</form>");
                break;
            default:
                break;
        }
        sb.append("<div class=\"answer-desc\">");
        sb.append("<div class=\"answer-desc-summary\">");
        sb.append("<span>正确答案：</span>");
        if (questionResult.getQuestionTypeId() == 3) {
            if (questionResult.getAnswer().equals("T"))
                sb.append("<span class=\"answer_value\">").append("正确").append("</span><br>");
            else if (questionResult.getAnswer().equals("F"))
                sb.append("<span class=\"answer_value\">").append("错误").append("</span><br>");
            else
                sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                        .append("</span><br>");
        } else
            sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                    .append("</span><br>");

        sb.append("<span>  你的解答：</span>");
        if (answerSheetItem.getQuestionTypeId() == 3) {
            if (answerSheetItem.getAnswer().trim().equals("T"))
                sb.append("<span>").append("对").append("</span>");
            else if (answerSheetItem.getAnswer().trim().equals("F"))
                sb.append("<span>").append("错").append("</span>");
            else
                sb.append("<span>").append(answerSheetItem.getAnswer())
                        .append("</span>");
        } else
            sb.append("<span>").append(answerSheetItem.getAnswer())
                    .append("</span>");
        sb.append("</div>");
        sb.append("</li>");
        return sb.toString();
    }
    /**
     *
     * @param showAnswer
     *            是否显示正确的答案。如果有答题卡，会显示用户的答案
     * @param showPoint
     *            是否显示分数
     * @param showAnalysis
     *            是否显示分析和来源
     * @return
     */
    public String getStringFromXML(boolean showAnswer, boolean showPoint,
                                   boolean showAnalysis) {
        StringBuilder sb = new StringBuilder();

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("<li class=\"question qt-singlechoice\">");
                break;
            case 2:
                sb.append("<li class=\"question qt-multiplechoice\">");
                break;
            case 3:
                sb.append("<li class=\"question qt-trueorfalse\">");
                break;
            case 4:
                sb.append("<li class=\"question qt-fillblank\">");
                break;
            default:
                break;
        }

        sb.append("<div class=\"question-title\">");
        sb.append("<div class=\"question-title-icon\"></div>");
        sb.append("<span class=\"question-no\">").append("</span>");
        sb.append("<span class=\"question-type\" style=\"display: none;\">");

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("singlechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[单选题]</span>");
                if (showPoint){
                    sb.append("<span class=\"question-point-content\">");
                    sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                    sb.append("</span>");
                }

                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it1 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it1.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it1.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"radio\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 2:
                sb.append("multiplechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[多选题]</span>");
                if (showPoint){
                    sb.append("<span class=\"question-point-content\">");
                    sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                    sb.append("</span>");
                }
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it2 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it2.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it2.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"checkbox\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 3:
                sb.append("trueorfalse").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[判断题]</span>");
                if (showPoint){
                    sb.append("<span class=\"question-point-content\">");
                    sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                    sb.append("</span>");
                }
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<ul class=\"question-opt-list\">");

                sb.append("<li class=\"question-list-item\">").append(
                        "<input type=\"radio\" value=\"T\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">正确</span>").append("</li>");

                sb.append("<li class=\"question-list-item\">").append("<input type=\"radio\" value=\"F\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">错误</span>").append("</li>");

                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 4:
                sb.append("fillblank").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[填空题]</span>");
                if (showPoint){
                    sb.append("<span class=\"question-point-content\">");
                    sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                    sb.append("</span>");
                }
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId()).append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<textarea class=\"question-textarea\"></textarea>");
                sb.append("</form>");
                break;
            default:
                break;
        }
        sb.append("<div class=\"answer-desc\">");
        sb.append("<div class=\"answer-desc-summary\">");
        if (showAnswer) {

            sb.append("<span>正确答案：</span>");
            if (questionResult.getQuestionTypeId() == 3) {
                if (questionResult.getAnswer().equals("T"))
                    sb.append("<span class=\"answer_value\">").append("对").append("</span><br>");
                else if (questionResult.getAnswer().equals("F"))
                    sb.append("<span class=\"answer_value\">").append("错").append("</span><br>");
                else
                    sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                            .append("</span><br>");
            } else
                sb.append("<span class=\"answer_value\">").append(questionResult.getAnswer())
                        .append("</span><br>");
        }

        if (answerSheetItem != null) {

            sb.append("<span>  你的解答：</span>");
            if (answerSheetItem.getQuestionTypeId() == 3) {
                if (answerSheetItem.getAnswer().trim().equals("T"))
                    sb.append("<span>").append("对").append("</span>");
                else if (answerSheetItem.getAnswer().trim().equals("F"))
                    sb.append("<span>").append("错").append("</span>");
                else
                    sb.append("<span>").append(answerSheetItem.getAnswer())
                            .append("</span>");
            } else
                sb.append("<span>").append(answerSheetItem.getAnswer())
                        .append("</span>");

        }
        sb.append("</div>");
        sb.append("</li>");
        return sb.toString();
    }

    public String getUserExamPaper() {
        StringBuilder sb = new StringBuilder();

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("<li class=\"question qt-singlechoice\">");
                break;
            case 2:
                sb.append("<li class=\"question qt-multiplechoice\">");
                break;
            case 3:
                sb.append("<li class=\"question qt-trueorfalse\">");
                break;
            case 4:
                sb.append("<li class=\"question qt-fillblank\">");
                break;
            default:
                break;
        }

        sb.append("<div class=\"question-title\">");
        sb.append("<div class=\"question-title-icon\"></div>");
        sb.append("<span class=\"question-no\">").append("</span>");
        sb.append("<span class=\"question-type\" style=\"display: none;\">");

        switch (questionResult.getQuestionTypeId()) {
            case 1:
                sb.append("singlechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[单选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it1 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it1.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it1.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"radio\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 2:
                sb.append("multiplechoice").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[多选题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                Iterator<String> it2 = questionContent.getChoiceList().keySet()
                        .iterator();
                sb.append("<ul class=\"question-opt-list\">");
                while (it2.hasNext()) {
                    sb.append("<li class=\"question-list-item\">");
                    String key = it2.next();
                    String value = questionContent.getChoiceList().get(key);
                    sb.append("<input type=\"checkbox\" value=\"")
                            .append(key)
                            .append("\" name=\"question-radio1\" class=\"question-input\">");
                    sb.append(key).append(": ").append(value);
                    sb.append("</span>");
                    sb.append("</li>");
                }
                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 3:
                sb.append("trueorfalse").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[判断题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<ul class=\"question-opt-list\">");

                sb.append("<li class=\"question-list-item\">").append(
                        "<input type=\"radio\" value=\"T\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">正确</span>").append("</li>");

                sb.append("<li class=\"question-list-item\">").append("<input type=\"radio\" value=\"F\" name=\"question-radio2\" class=\"question-input\">")
                        .append("<span class=\"question-li-text\">错误</span>").append("</li>");

                sb.append("</ul>");
                sb.append("</form>");
                break;
            case 4:
                sb.append("fillblank").append("</span>");
                sb.append("<span class=\"question-type-id\" style=\"display: none;\">").append(questionResult.getQuestionTypeId()).append("</span>");
                sb.append("<span>[填空题]</span>");
                sb.append("<span class=\"question-point-content\">");
                sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("</span>");
                //sb.append("<span>(</span><span class=\"question-point\">").append(pointStrFormat(questionQueryResult.getQuestionPoint())).append("</span><span>分)</span>");
                sb.append("<span class=\"question-id\" style=\"display:none;\">")
                        .append(questionResult.getQuestionId())
                        .append("</span>");
                sb.append("</div>");
                sb.append("<form class=\"question-body\">");
                sb.append("<p class=\"question-body-text\">").append(questionContent.getTitle());
                sb.append("</p>");
                sb.append("<textarea class=\"question-textarea\"></textarea>");
                sb.append("</form>");
                break;
            default:
                break;

        }
        sb.append("</li>");
        return sb.toString();
    }
}
