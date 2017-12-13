package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduQuestionMapper;
import com.edu.common.dao.mapper.EduStudentExamMapper;
import com.edu.common.dao.model.EduQuestion;
import com.edu.common.dao.model.EduQuestionExample;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.dao.pojo.AnswerSheetItem;
import com.edu.common.dao.pojo.QuestionContent;
import com.edu.common.dao.pojo.StatisticBean;
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

    @Autowired
    EduStudentExamMapper studentExamMapper;

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
            throw new RuntimeException("导入表格的第" + index + "行信息有重复，请检查！");
        }
    }

    @Override
    public List<String> selectQuestionNameList(Integer qcId) {
        return eduQuestionMapper.selectQuestionNameList(qcId);
    }

    /**
     * 统计出错率，答题数
     * @return
     */
    @Override
    public Map<Integer, StatisticBean> statisticalQuestion() {
        List<String> answerSheetStrList = studentExamMapper.statisticalQuestion();

        Gson gson = new Gson();
        List<AnswerSheetItem> statisticItems = new ArrayList<>();
        if(answerSheetStrList!=null && answerSheetStrList.size()>0){
            for(String str : answerSheetStrList){
                AnswerSheet answerSheet = gson.fromJson(str, AnswerSheet.class);
                List<AnswerSheetItem> answerSheetItems = answerSheet.getAnswerSheetItems();
                for(AnswerSheetItem item : answerSheetItems){
                    statisticItems.add(item);
                }
            }
        }
        return statisticalQuestionOperate(statisticItems);
    }


    /**
     * 统计出错率和答题数
     * @param statisticItems
     * @return
     */
    private Map<Integer, StatisticBean> statisticalQuestionOperate(List<AnswerSheetItem> statisticItems){
        Map<Integer, StatisticBean> questionMap = new HashMap<>();

        for(AnswerSheetItem item : statisticItems){
            int questionId = item.getQuestionId();
            StatisticBean statisticBean = new StatisticBean(questionId);
            if(questionMap.containsKey(questionId)){
                statisticBean = questionMap.get(questionId);
                statisticBean.setSum(statisticBean.getSum()+1);
                statisticBean.setErrorCount(statisticBean.getErrorCount()+(item.isRight()?0:1));
            }else{
                statisticBean.setSum(1);
                statisticBean.setErrorCount(item.isRight()?0:1);
            }
            questionMap.put(questionId, statisticBean);
        }
        return questionMap;
    }

    public static void main(String[] args) {
        EduQuestionServiceImpl impl = new EduQuestionServiceImpl();
        List<String> answerSheetStrList = new ArrayList<>();
        answerSheetStrList.add("{\"examHistroyId\":1,\"examId\":26,\"examPaperId\":46,\"duration\":0,\"answerSheetItems\":[{\"point\":10.0,\"questionTypeId\":1,\"answer\":\"B\",\"questionId\":81,\"right\":true},{\"point\":10.0,\"questionTypeId\":1,\"answer\":\"C\",\"questionId\":82,\"right\":true},{\"point\":0.0,\"questionTypeId\":2,\"answer\":\" B D\",\"questionId\":91,\"right\":false},{\"point\":0.0,\"questionTypeId\":2,\"answer\":\" A C D\",\"questionId\":87,\"right\":false},{\"point\":10.0,\"questionTypeId\":3,\"answer\":\"F\",\"questionId\":76,\"right\":true},{\"point\":10.0,\"questionTypeId\":3,\"answer\":\"F\",\"questionId\":75,\"right\":true},{\"point\":10.0,\"questionTypeId\":3,\"answer\":\"F\",\"questionId\":79,\"right\":true},{\"point\":10.0,\"questionTypeId\":3,\"answer\":\"T\",\"questionId\":78,\"right\":true},{\"point\":10.0,\"questionTypeId\":4,\"answer\":\"孝\",\"questionId\":92,\"right\":true},{\"point\":10.0,\"questionTypeId\":4,\"answer\":\"仓颉\",\"questionId\":93,\"right\":true}],\"pointMax\":100.0,\"pointRaw\":80.0,\"startTime\":\"Sep 14, 2017 3:09:57 PM\",\"pointPass\":60.0}");
        answerSheetStrList.add("{\"examHistroyId\":12,\"examId\":27,\"examPaperId\":47,\"duration\":0,\"answerSheetItems\":[{\"point\":10.0,\"questionTypeId\":1,\"answer\":\"A\",\"questionId\":56,\"right\":true},{\"point\":10.0,\"questionTypeId\":1,\"answer\":\"D\",\"questionId\":58,\"right\":true},{\"point\":10.0,\"questionTypeId\":1,\"answer\":\"D\",\"questionId\":59,\"right\":true},{\"point\":10.0,\"questionTypeId\":1,\"answer\":\"D\",\"questionId\":60,\"right\":true},{\"point\":0.0,\"questionTypeId\":2,\"answer\":\" A B C\",\"questionId\":61,\"right\":false},{\"point\":0.0,\"questionTypeId\":2,\"answer\":\" A B\",\"questionId\":66,\"right\":false},{\"point\":10.0,\"questionTypeId\":3,\"answer\":\"F\",\"questionId\":76,\"right\":true},{\"point\":0.0,\"questionTypeId\":3,\"answer\":\"F\",\"questionId\":78,\"right\":false},{\"point\":0.0,\"questionTypeId\":4,\"answer\":\"平安夜\",\"questionId\":72,\"right\":false},{\"point\":10.0,\"questionTypeId\":4,\"answer\":\"北京\",\"questionId\":73,\"right\":true}],\"pointMax\":100.0,\"pointRaw\":60.0,\"startTime\":\"Sep 14, 2017 2:49:43 PM\",\"pointPass\":60.0}");
        Gson gson = new Gson();
        List<AnswerSheetItem> statisticItems = new ArrayList<>();
        if(answerSheetStrList!=null && answerSheetStrList.size()>0){
            for(String str : answerSheetStrList){
                AnswerSheet answerSheet = gson.fromJson(str, AnswerSheet.class);
                List<AnswerSheetItem> answerSheetItems = answerSheet.getAnswerSheetItems();
                for(AnswerSheetItem item : answerSheetItems){
                    statisticItems.add(item);
                }
            }
        }

        Map<Integer, StatisticBean> map = impl.statisticalQuestionOperate(statisticItems);
        System.out.println(map.size());
        for(int key : map.keySet()){
            System.out.println(key+"==="+map.get(key).toString());
        }
    }
}