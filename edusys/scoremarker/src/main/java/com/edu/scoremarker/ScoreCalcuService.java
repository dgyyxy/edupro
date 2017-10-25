package com.edu.scoremarker;

import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.dao.pojo.AnswerSheetItem;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by Gary on 2017/6/8.
 */
@Service
@Scope("prototype")
public class ScoreCalcuService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("answerSheetPostUri")
    private String answerSheetPostUri;

    @Autowired
    @Qualifier("examPaperGetUri")
    private String examPaperGetUri;

    @Autowired
    HashMap<String, EduPaper> Examapapers;

    private static final Logger LOGGER = Logger.getLogger(ScoreCalcuService.class);

    public void calcuScore(AnswerSheet as) {
        System.out.println("answerSheet is calculating..." + as);

        EduPaper examPaper = Examapapers.get(as.getExamPaperId());

        // TODO 计算得分 结果等数据..
        if (examPaper == null) {
            examPaper = this.getExamPaper(as.getExamPaperId());
            Examapapers.put(examPaper.getId() + "", examPaper);
        }
        Gson gson = new Gson();
        AnswerSheet target = gson.fromJson(examPaper.getAnswerSheet(),AnswerSheet.class);
        HashMap<Integer,AnswerSheetItem> answerMap = new HashMap<Integer,AnswerSheetItem>();
        for(AnswerSheetItem item : target.getAnswerSheetItems()){
            answerMap.put(item.getQuestionId(), item);
        }
        as.setPointPass(examPaper.getPassPoint());
        as.setPointMax(target.getPointMax());
        for(AnswerSheetItem item : as.getAnswerSheetItems()){
            if(item.getAnswer().equals(answerMap.get(item.getQuestionId()).getAnswer())){
                as.setPointRaw(as.getPointRaw() + answerMap.get(item.getQuestionId()).getPoint());
                item.setPoint(answerMap.get(item.getQuestionId()).getPoint());
                item.setRight(true);
            }
        }
        this.postAnswerSheet(answerSheetPostUri, as);
        System.out.println("answerSheet has been post to" + answerSheetPostUri);
    }

    private void postAnswerSheet(String uri, Object body) {
        try {
            restTemplate.postForLocation(uri, body);
        } catch (RestClientException e) {
            LOGGER.error("Received exception:", e);
        }
    }

    private EduPaper getExamPaper(int examaperId) {
        EduPaper examPaper = null;
        try {
            System.out.println("try to fetch exampaper");
            examPaper = restTemplate.getForObject(examPaperGetUri + "/" + examaperId, EduPaper.class);
        } catch (RestClientException e) {
            LOGGER.error("Get Exampaper exception:", e);
        }
        if(examPaper == null){
            LOGGER.error("Get Exampaper exception: The specical Examaper is not existed.");
            System.out.println("Get Exampaper exception: The specical Examaper is not existed.");
        }
        return examPaper;

    }
}
