package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduPaperMapper;
import com.edu.common.dao.mapper.EduQuestionCategoryMapper;
import com.edu.common.dao.mapper.EduQuestionMapper;
import com.edu.common.dao.mapper.EduQuestionTypeMapper;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.dao.pojo.AnswerSheetItem;
import com.edu.common.util.Roulette;
import com.edusys.manager.service.EduPaperService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * EduPaperService实现
 * Created by Gary on 2017/5/6.
 */
@Service
@Transactional
@BaseService
public class EduPaperServiceImpl extends BaseServiceImpl<EduPaperMapper, EduPaper, EduPaperExample> implements EduPaperService {

    private static Logger _log = LoggerFactory.getLogger(EduPaperServiceImpl.class);

    @Autowired
    EduPaperMapper eduPaperMapper;

    @Autowired
    EduQuestionMapper questionMapper;

    @Autowired
    EduQuestionCategoryMapper questionCategoryMapper;

    @Autowired
    EduQuestionTypeMapper questionTypeMapper;

    /**
     * 获取试题分类对应的题目列表
     *
     * @param idList
     * @return
     */
    @Override
    public HashMap<Integer, HashMap<Integer, List<EduQuestion>>> getQuestionMap(List<Integer> idList) {
        HashMap<Integer, HashMap<Integer, List<EduQuestion>>> questionMap = new HashMap<Integer, HashMap<Integer, List<EduQuestion>>>();
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andQuestionCategoryIdIn(idList);
        List<EduQuestion> questionList = questionMapper.selectByExample(questionExample);
        for (EduQuestion question : questionList) {
            HashMap<Integer, List<EduQuestion>> hashMap = new HashMap<Integer, List<EduQuestion>>();
            List<EduQuestion> ql = new ArrayList<>();
            if (questionMap.containsKey(question.getQuestionCategoryId())) {
                hashMap = questionMap.get(question.getQuestionCategoryId());
            }
            if (hashMap.containsKey(question.getQuestionTypeId())) {
                ql = hashMap.get(question.getQuestionTypeId());
            }
            ql.add(question);
            hashMap.put(question.getQuestionTypeId(), ql);
            questionMap.put(question.getQuestionCategoryId(), hashMap);
        }
        return questionMap;
    }

    /**
     * 自动组卷
     *
     * @param questionMap
     * @param eduPaper
     */
    @Override
    public HashMap<Integer, EduQuestion> getPaperQuestionMap(HashMap<Integer, HashMap<Integer, List<EduQuestion>>> questionMap, EduPaper eduPaper) throws Exception {
        // 保存数据库中读取的每种题型的数量
        HashMap<Integer, Integer> questionTypeNumCheck = new HashMap<Integer, Integer>();
        Iterator<Integer> iterator1 = questionMap.keySet().iterator();
        // 遍历每一个题库分类
        while (iterator1.hasNext()) {
            int key = (Integer) iterator1.next();
            Iterator<Integer> iterator2 = questionMap.get(key).keySet()
                    .iterator();
            // 遍历题库分类下每一种题型
            while (iterator2.hasNext()) {
                // 题型ID
                int typeNum = (Integer) iterator2.next();
                // 如果题型校验Map包含这个题型ID
                if (questionTypeNumCheck.containsKey(typeNum))
                    questionTypeNumCheck.put(typeNum,
                            questionTypeNumCheck.get(typeNum)
                                    + questionMap.get(key).get(typeNum).size());
                else
                    questionTypeNumCheck.put(typeNum,
                            questionMap.get(key).get(typeNum).size());
            }
        }

        // 每种题型的数量
        HashMap<Integer, Integer> questionTypeNum = eduPaper.getQuestionTypeNum();
        //试题类型字典
        HashMap<Integer, String> typeMap = (HashMap<Integer, String>) getQuestionTypeMap();
        Iterator<Integer> iterator3 = questionTypeNum.keySet().iterator();
        while (iterator3.hasNext()) {
            int key = (Integer) iterator3.next();
            if (!questionTypeNumCheck.containsKey(key))
                throw new Exception("试题清单中无试题类型" + typeMap.get(key));
            if (questionTypeNum.get(key) > questionTypeNumCheck.get(key))
                throw new Exception("试题库中试题类型：" + typeMap.get(key) + "数量不足");
        }

        //试卷试题列表
        HashMap<Integer, EduQuestion> paperQuestionMap = new HashMap<Integer, EduQuestion>();
        // 设置题库分类的概率，默认平均
        List<Integer> resultList = new ArrayList<Integer>();

        HashMap<Integer, Float> hm = new HashMap<Integer, Float>();

        float sum = 0f;
        Iterator<Integer> itrate;
        // 题库分类概率分布
        HashMap<Integer, Float> questionTypeRate = eduPaper.getQuestionTypeRate();
        if (questionTypeRate != null) {
            itrate = questionTypeRate.keySet().iterator();
            while (itrate.hasNext()) {
                sum += questionTypeRate.get(itrate.next());
            }
        }

        // 如果没有提供题库分类概率，或者概率相加不等于1，则按平均概率计算
        if (questionTypeRate == null || sum != 1) {
            Iterator<Integer> it = questionMap.keySet().iterator();
            int count = 0;
            while (it.hasNext()) {
                int key = it.next();
                resultList.add(key);
                hm.put(count, 0f);
                count++;
            }
            it = questionMap.keySet().iterator();

            float avg = (float) (Math.round((1f / (float) count) * 1000)) / 1000;
            float dt = (float) (Math
                    .round(((1f - (float) (avg * (count - 1)))) * 1000)) / 1000;

            _log.info("dt = " + dt);
            _log.info("avg = " + avg);
            for (int i = 0; i < count; i++) {
                if (i == count - 1)
                    hm.put(i, dt);
                else
                    hm.put(i, avg);
                _log.info("题库分类" + i + "的选择概率:" + hm.get(i));
            }
        } else {
            Iterator<Integer> itrate1 = questionTypeRate.keySet().iterator();
            int count = 0;
            while (itrate1.hasNext()) {
                int key = itrate1.next();
                resultList.add(key);
                hm.put(count, questionTypeRate.get(key));
                count++;
            }
        }

        // 轮盘赌选择知识点
        Roulette<Integer> r = new Roulette<Integer>(resultList, hm);

        // 选择题型
        List<Integer> resultList1 = new ArrayList<Integer>();
        Iterator<Integer> it1 = questionTypeNum.keySet().iterator();
        HashMap<Integer, Float> hm1 = new HashMap<Integer, Float>();

        int count1 = 0;
        // 题目总数，通过questionNum计算
        int questionNum = 0;

        while (it1.hasNext()) {
            int key = it1.next();
            resultList1.add(key);
            // 获取题型数量
            count1++;
            // 获取题量
            questionNum += questionTypeNum.get(key);
        }
        _log.info("题型数量=" + count1);

        it1 = questionMap.keySet().iterator();

        // 每种题型的概率
        float avg1 = (float) (Math.round((1f / (float) count1) * 1000)) / 1000;
        // 所有题型的概率相加和1之间的差值加上平均值
        float dt1 = (float) (Math
                .round(((1f - (float) (avg1 * (count1 - 1)))) * 1000)) / 1000;

        for (int i = 0; i < count1; i++) {
            // 最后一种题型概率加上差值
            if (i == count1 - 1)
                hm1.put(i, dt1);
            else
                hm1.put(i, avg1);
            _log.info("题型" + i + "的选择概率:" + hm1.get(i));
        }
        // 轮盘赌选择题型
        Roulette<Integer> r1 = new Roulette<Integer>(resultList1, hm1);

        // 每种题型的分数
        HashMap<Integer, Float> questionTypePoint = eduPaper.getQuestionTypePoint();


        // 如果没有选择足够的题量，循环选择试题
        while (questionNum > paperQuestionMap.size()) {
            int categoryId = -1;
            int typeId = -1;
            try {
                categoryId = r.getResult();
                typeId = r1.getResult();
                List<EduQuestion> qs = questionMap.get(categoryId).get(typeId);
                if (qs == null) {
                    _log.info("categoryId=" + categoryId + "typeId=" + typeId);
                    _log.info(String.valueOf(questionMap.get(categoryId)));
                    continue;

                }

                Random random = new Random();
                int typeNum = questionTypeNum.get(typeId);
                if (typeNum > 0) {
                    EduQuestion q = qs.get(random.nextInt(qs.size()));

                    if (paperQuestionMap.containsKey(q.getId()))
                        continue;
                    if (questionTypePoint != null) {
                        if (questionTypePoint.containsKey(typeId)) {
                            q.setPoint(questionTypePoint.get(typeId));
                        }
                    }
                    paperQuestionMap.put(q.getId(), q);
                    typeNum--;
                    questionTypeNum.put(typeId, typeNum);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return paperQuestionMap;

    }

    /**
     * 创建试卷
     * @param paper
     */
    @Override
    public void createPaper(EduPaper paper)  throws Exception {
        List<Integer> idList = new ArrayList<Integer>();
        //处理题目分类
        Iterator<Integer> it = paper.getQuestionTypeRate().keySet().iterator();
        while(it.hasNext()){
            idList.add(it.next());
        }

        HashMap<Integer, HashMap<Integer, List<EduQuestion>>> questionMap = getQuestionMap(idList);

        // 创建试卷
        HashMap<Integer, EduQuestion> paperQuestionMap = getPaperQuestionMap(questionMap, paper);
        Iterator<Integer> qits = paperQuestionMap.keySet().iterator();
        List<Integer> qids = new ArrayList<Integer>();
        while(qits.hasNext()){
            qids.add(qits.next());
        }
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andIdIn(qids);
        List<EduQuestion> questionList = questionMapper.selectByExample(questionExample);
        List<QuestionResult> questionResults = new ArrayList<>();
        for(EduQuestion question : questionList){
            QuestionResult qr = new QuestionResult(question.getId(), question.getContent(),question.getAnswer(),
                    question.getQuestionTypeId(),paper.getQuestionTypePoint().get(question.getQuestionTypeId()),0);
            questionResults.add(qr);
        }
        Gson gson = new Gson();
        //试卷题量
        paper.setAmount(qids.size());
        paper.setContent(gson.toJson(questionResults));
        eduPaperMapper.insertSelective(paper);
    }

    /**
     * 创建试卷(快速创建考试使用)
     * @param paper
     */
    @Override
    public void createExamPaper(EduPaper paper)  throws Exception {
        List<Integer> idList = new ArrayList<Integer>();
        //处理题目分类
        Iterator<Integer> it = paper.getQuestionTypeRate().keySet().iterator();
        while(it.hasNext()){
            idList.add(it.next());
        }

        HashMap<Integer, HashMap<Integer, List<EduQuestion>>> questionMap = getQuestionMap(idList);

        // 创建试卷
        HashMap<Integer, EduQuestion> paperQuestionMap = getPaperQuestionMap(questionMap, paper);
        Iterator<Integer> qits = paperQuestionMap.keySet().iterator();
        List<Integer> qids = new ArrayList<Integer>();
        while(qits.hasNext()){
            qids.add(qits.next());
        }
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andIdIn(qids);
        List<EduQuestion> questionList = questionMapper.selectByExample(questionExample);
        List<QuestionResult> questionResults = new ArrayList<>();

        float sum = 0;
        AnswerSheet as = new AnswerSheet();
        as.setExamPaperId(0);
        List<AnswerSheetItem> asList = new ArrayList<AnswerSheetItem>();

        for(EduQuestion question : questionList){
            QuestionResult qr = new QuestionResult(question.getId(), question.getContent(),question.getAnswer(),
                    question.getQuestionTypeId(),paper.getQuestionTypePoint().get(question.getQuestionTypeId()),0);
            questionResults.add(qr);
            AnswerSheetItem item = new AnswerSheetItem();
            item.setAnswer(qr.getAnswer());
            item.setQuestionId(qr.getQuestionId());
            item.setPoint(qr.getQuestionPoint());
            item.setQuestionTypeId(qr.getQuestionTypeId());
            sum += qr.getQuestionPoint();
            asList.add(item);
        }
        Gson gson = new Gson();
        //试卷题量
        paper.setAmount(qids.size());
        paper.setContent(gson.toJson(questionResults));

        as.setPointMax(sum);
        as.setAnswerSheetItems(asList);
        as.setPointPass(paper.getPassPoint());
        String answerSheet = gson.toJson(as);
        paper.setAnswerSheet(answerSheet);
        eduPaperMapper.insertSelective(paper);

        /*as.setExamPaperId(paper.getId());
        answerSheet = gson.toJson(as);
        paper.setAnswerSheet(answerSheet);
        eduPaperMapper.updateByPrimaryKeySelective(paper);*/
    }



    /**
     * 获取题库分类Map
     * @return
     */
    private Map<Integer, String> getQuestionCategoryMap () {
        EduQuestionCategoryExample questionCategoryExample = new EduQuestionCategoryExample();
        EduQuestionCategoryExample.Criteria criteria = questionCategoryExample.createCriteria();
        criteria.andPidGreaterThan(0);
        List<EduQuestionCategory> questionCategories = questionCategoryMapper.selectByExample(questionCategoryExample);
        Map<Integer, String> categoryMap = new HashMap<Integer, String>();
        for (EduQuestionCategory eqc : questionCategories) {
            categoryMap.put(eqc.getId(), eqc.getName());
        }
        return categoryMap;
    }

    /**
     * 获取题库题型Map
     * @return
     */
    private Map<Integer, String> getQuestionTypeMap () {
        List<EduQuestionType> questionTypes = questionTypeMapper.selectByExample(new EduQuestionTypeExample());
        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        for (EduQuestionType tp : questionTypes) {
            typeMap.put(tp.getId(), tp.getName());
        }
        return typeMap;
    }
}