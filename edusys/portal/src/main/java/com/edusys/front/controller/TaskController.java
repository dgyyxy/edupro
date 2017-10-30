package com.edusys.front.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.util.NumberUtils;
import com.edu.common.util.Paginator;
import com.edusys.front.service.StuJobCourseService;
import com.edusys.front.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Gary on 2017/5/16.
 * 学习任务Controller
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private StuJobCourseService stuJobCourseService;

    /**
     * 学习任务列表
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "8", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       HttpServletRequest request, ModelMap modelMap){
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");

        int tag = 0;//标识： 0，所有学习任务 1，进行中学习任务 2，已结束的学习任务
        if(request.getParameter("tag")!=null){
            tag = Integer.parseInt(request.getParameter("tag"));
        }

        EduJobsExample eje = new EduJobsExample();
        EduJobsExample.Criteria criteria = eje.createCriteria();
        List<Integer> organIdList = new ArrayList<>();
        organIdList.add(student.getOrganizationId2());
        organIdList.add(0);
        criteria.andOrganizationIdIn(organIdList);

        long nowtime = new Date().getTime();

        if(tag == 1){//1，进行中学习任务
            criteria.andStartTimeLessThanOrEqualTo(nowtime);
            criteria.andEndTimeGreaterThanOrEqualTo(nowtime);
        }else if(tag == 2){//2，已结束的学习任务
            criteria.andEndTimeLessThan(nowtime);
        }

        eje.setLimit(limit);
        int offset =(page - 1)*limit;
        eje.setOffset(offset);
        eje.setOrderByClause("id desc");

        List<EduJobs> list = taskService.selectByExample(eje);
        long total = taskService.countByExample(eje);

        Paginator paginator = new Paginator(total, page, limit, request);
        String htmlstr = paginator.getHtml();



        // 获取已学习的任务数
        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria mycri = stuJobCourseExample.createCriteria();
        mycri.andStuIdEqualTo(student.getStuId());
        mycri.andTotalCourseGreaterThan(0);
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        int studyCount = 0;
        Map<Integer, String> map = new HashMap<>();
        int time = 0;
        Map<Integer, Integer> timeMap = new HashMap<>();//记录学习任务用时
        for(EduStuJobCourse esjc : stuJobCourseList){
            //已学课件等学习任务总课件数时，代表已学完
            if(esjc.getCourseNum() == esjc.getTotalCourse()){
                studyCount++;
            }
            String study = NumberUtils.getPercentStr(esjc.getCourseNum(), esjc.getTotalCourse());
            time +=esjc.getTime();
            timeMap.put(esjc.getJobId(), esjc.getTime());
            map.put(esjc.getJobId(), study);
        }
        //学习百分比
        modelMap.put("map", map);
        modelMap.put("timeMap", timeMap);
        modelMap.put("studyCount", studyCount+"");
        modelMap.put("unstudyCount", (total-studyCount)+"");
        modelMap.put("total", total);
        modelMap.put("list", list);
        modelMap.put("pageHtml", htmlstr);
        modelMap.put("tag", tag);
        return "/task-list.jsp";
    }
}
