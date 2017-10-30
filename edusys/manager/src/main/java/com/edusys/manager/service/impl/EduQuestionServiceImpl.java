package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduQuestionMapper;
import com.edu.common.dao.model.EduQuestion;
import com.edu.common.dao.model.EduQuestionExample;
import com.edu.common.dao.pojo.QuestionContent;
import com.edu.common.util.ExcelUtil;
import com.edusys.manager.service.EduQuestionService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;

/**
* EduQuestionService实现
* Created by Gary on 2017/5/6.
*/
@Service
@Transactional
@BaseService
public class EduQuestionServiceImpl extends BaseServiceImpl<EduQuestionMapper, EduQuestion, EduQuestionExample> implements EduQuestionService {

    private static Logger _log = LoggerFactory.getLogger(EduQuestionServiceImpl.class);

    @Autowired
    EduQuestionMapper eduQuestionMapper;

    @Override
    public void uploadQuestions(CommonsMultipartFile file, int categoryId, String username) {
        int index = 2;
        //获取该题目分类下面所有题目名称列表
        List<String> qnameList = eduQuestionMapper.selectQuestionNameList(categoryId);

        try{
            List<EduQuestion> questions = new ArrayList<>();
            List<Map<String, String>> questionMapList = ExcelUtil.ExcelToList(file);
            for (Map<String, String> map : questionMapList) {
                EduQuestion question = new EduQuestion();
                String namestr = map.get("题目").length() > 30 ? map.get("题目").substring(0, 30) + "..." : map.get("题目");
                if(StringUtils.isBlank(namestr)){
                    throw new RuntimeException("题目不能为空！");
                }
                question.setName(namestr);
                if(qnameList.contains(namestr)){
                    throw new RuntimeException("该分类下题目已重复！");
                }
                if(StringUtils.isBlank(map.get("类型"))){
                    throw new RuntimeException("题目类型不能为空！");
                }
                if(map.get("类型").equals("单选题")
                        || map.get("类型").equals("单项选择题")){
                    question.setQuestionTypeId(1);
                }else if (map.get("类型").equals("多选题")
                        || map.get("类型").equals("多项选择题")){
                    question.setQuestionTypeId(2);
                }else if (map.get("类型").equals("判断题")
                        || map.get("类型").equals("是非题")
                        || map.get("类型").equals("是非")){
                    question.setQuestionTypeId(3);
                }else if (map.get("类型").equals("填空题")){
                    question.setQuestionTypeId(4);
                }
                if(StringUtils.isBlank(map.get("答案"))){
                    throw new RuntimeException("题目答案不能为空！");
                }
                question.setAnswer(map.get("答案"));

                if(question.getQuestionTypeId() == 3){
                    if (map.get("答案").equals("对") || map.get("答案").equals("正确"))
                        question.setAnswer("T");
                    if (map.get("答案").equals("错") || map.get("答案").equals("错误"))
                        question.setAnswer("F");
                }
                question.setQuestionCategoryId(categoryId);
                QuestionContent qc = new QuestionContent();
                Iterator<String> it = map.keySet().iterator();
                List<String> keyStr = new ArrayList<String>();
                while (it.hasNext()) {
                    String key = it.next();
                    if (key.contains("选项"))
                        keyStr.add(key.replace("选项", ""));
                }
                Collections.sort(keyStr);
                LinkedHashMap<String, String> choiceList = new LinkedHashMap<String, String>();
                for (int i = 0; i < keyStr.size(); i++) {
                    if (!map.get("选项" + keyStr.get(i)).trim().equals(""))
                        choiceList.put(keyStr.get(i), map.get("选项" + keyStr.get(i)));
                }
                if (question.getQuestionTypeId() == 1 || question.getQuestionTypeId() == 2)
                    qc.setChoiceList(choiceList);
                qc.setTitle(map.get("题目"));

                if(map.get("难度") != null){
                    question.setDifficulty(Integer.parseInt(map.get("难度")));
                }
                Gson gson = new Gson();
                String content = gson.toJson(qc);
                question.setContent(content);
                question.setCreator(username);

                long time = System.currentTimeMillis();
                question.setCreateTime(time);
                questions.add(question);
                index++;
            }

            //批量新增试题
            eduQuestionMapper.insertBatch(questions);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("第" + index + "行有错误，请检查！" + e.getMessage());
        }
    }

    @Override
    public List<String> selectQuestionNameList(Integer qcId) {
        return eduQuestionMapper.selectQuestionNameList(qcId);
    }
}