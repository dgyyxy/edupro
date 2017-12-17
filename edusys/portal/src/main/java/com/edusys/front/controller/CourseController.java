package com.edusys.front.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.util.Paginator;
import com.edusys.front.common.SysResult;
import com.edusys.front.common.SysResultConstant;
import com.edusys.front.service.CourseService;
import com.edusys.front.service.EduJobCoursewareService;
import com.edusys.front.service.StuJobCourseService;
import com.edusys.front.service.TaskService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/16.
 * 课件Controller
 */
@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private EduJobCoursewareService jobCoursewareService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private StuJobCourseService stuJobCourseService;

    /**
     * 学习--课件列表
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "8", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       @PathVariable("id") Integer jobId,String search,Integer status,
                       HttpServletRequest request, ModelMap modelMap){
        EduJobs jobs = taskService.selectByPrimaryKey(jobId);
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        List<Integer> ids = null;
        if(StringUtils.isNotBlank(search)){
            ids = courseService.getIdsByNameStr(search);
            if(ids==null || ids.size()==0){
                modelMap.put("job", jobs);
                modelMap.put("total", 0);
                modelMap.put("unstudy", 0);
                modelMap.put("pageHtml", "");
                return "/course-list.jsp";
            }
        }

        EduJobCoursewareExample ejce = new EduJobCoursewareExample();
        EduJobCoursewareExample.Criteria criteria = ejce.createCriteria();
        int offset =(page - 1)*limit;
        ejce.setOffset(offset);
        ejce.setLimit(limit);
        ejce.setOrderByClause("sort_num asc");

        criteria.andJobIdEqualTo(jobId);
        if(ids!=null && ids.size()>0){
            criteria.andCoursewareIdIn(ids);
        }
        List<EduJobCourseware> rows = jobCoursewareService.selectByExample(ejce);
        long total = jobCoursewareService.countByExample(ejce);

        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria crit = stuJobCourseExample.createCriteria();
        //获取课件是否被收藏和是否已学
        crit.andStuIdEqualTo(student.getStuId());
        crit.andTotalCourseEqualTo(0);
        crit.andJobIdEqualTo(jobs.getId());
        if(ids!=null && ids.size()>0) crit.andCourseIdIn(ids);
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);

        Map<Integer, String> mapStatus = new HashMap<>();
        Map<Integer, String> mapFavorite = new HashMap<>();
        int study = 0;
        int studyend = 0;
        for(EduStuJobCourse stuJobCourse : stuJobCourseList){
            if(stuJobCourse.getStatus()==1){
                study++;
            }
            if(stuJobCourse.getStatus()==2) studyend++;
            mapStatus.put(stuJobCourse.getCourseId(), stuJobCourse.getStatus()+"");
            mapFavorite.put(stuJobCourse.getCourseId(), stuJobCourse.getFavorite()+"");
        }

        Paginator paginator = new Paginator(total, page, limit, request);
        String htmlstr = paginator.getHtml();
        modelMap.put("mapStatus", mapStatus);
        modelMap.put("mapFavorite", mapFavorite);
        modelMap.put("list", rows);
        modelMap.put("total", total+"");
        modelMap.put("unstudy", (total-study-studyend)+"");
        modelMap.put("pageHtml", htmlstr);
        modelMap.put("job", jobs);
        modelMap.put("page", page);
        return "/course-list.jsp";
    }

    /**
     * 记录课件学习
     * @param stuId
     * @param jobId
     * @param courseId
     * @return
     */
    @RequestMapping(value = "/record/{stuId}/{jobId}/{courseId}")
    @ResponseBody
    public Object record_study(@PathVariable("stuId") int stuId, @PathVariable("jobId") int jobId,
                                @PathVariable("courseId") int courseId){
        EduJobs job = taskService.selectByPrimaryKey(jobId);
        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria criteria = stuJobCourseExample.createCriteria();
        criteria.andTotalCourseGreaterThan(0);
        criteria.andJobIdEqualTo(jobId);
        criteria.andStuIdEqualTo(stuId);
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        EduStuJobCourse stuJobCourse = new EduStuJobCourse();
        int count = 0;
        //查询学员学习课件表里面是否有学习任务记录
        if(stuJobCourseList!=null && stuJobCourseList.size()>0){
            stuJobCourse = stuJobCourseList.get(0);
            if(job.getCoursewareCount()!=stuJobCourse.getTotalCourse()) {
                //修改学员学习任务记录里面的课件总数
                stuJobCourse.setTotalCourse(job.getCoursewareCount());
                stuJobCourseService.updateByPrimaryKeySelective(stuJobCourse);
            }
        }else{
            stuJobCourse.setStuId(stuId);
            stuJobCourse.setJobId(jobId);
            stuJobCourse.setCourseId(courseId);
            stuJobCourse.setCourseNum(0);
            stuJobCourse.setTotalCourse(job.getCoursewareCount());
            stuJobCourseService.insertSelective(stuJobCourse);
        }

        stuJobCourseExample = new EduStuJobCourseExample();
        criteria = stuJobCourseExample.createCriteria();
        criteria.andTotalCourseEqualTo(0);
        criteria.andCourseIdEqualTo(courseId);
        criteria.andJobIdEqualTo(jobId);
        criteria.andStuIdEqualTo(stuId);
        stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        if(stuJobCourseList==null || stuJobCourseList.size()==0) {
            //新增学员课件学习记录
            EduStuJobCourse jobCourse = new EduStuJobCourse();
            jobCourse.setCourseId(courseId);
            jobCourse.setJobId(jobId);
            jobCourse.setStuId(stuId);
            jobCourse.setStatus(1);//进行中
            stuJobCourseService.insertSelective(jobCourse);
        }else{
            EduStuJobCourse jobCourse = stuJobCourseList.get(0);//已经存在
            count = jobCourse.getStatus();
        }

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    /**
     * 关闭学习窗口时
     * 记录课件学习用时
     * @param stuId
     * @param jobId
     * @param courseId
     * @param time  记录学习用时
     * @param status 记录课件开始学习状态（用于记录已学习完的课件重复学习）
     * @return
     */
    @RequestMapping(value = "/study/{stuId}/{jobId}/{courseId}")
    @ResponseBody
    public Object course_study(@PathVariable("stuId") int stuId, @PathVariable("jobId") int jobId,
                                @PathVariable("courseId") int courseId, Integer time, Integer status){
        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria criteria = stuJobCourseExample.createCriteria();
        criteria.andTotalCourseEqualTo(0);
        criteria.andCourseIdEqualTo(courseId);
        criteria.andJobIdEqualTo(jobId);
        criteria.andStuIdEqualTo(stuId);
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        EduStuJobCourse stuJobCourse1 = new EduStuJobCourse();
        if(stuJobCourseList!=null && stuJobCourseList.size()>0){
            stuJobCourse1 = stuJobCourseList.get(0);
            if(stuJobCourse1.getStatus() == 0) stuJobCourse1.setStatus(1);//进行中
            stuJobCourse1.setTime(stuJobCourse1.getTime() + time);
            stuJobCourseService.updateByPrimaryKeySelective(stuJobCourse1);
        }
        //统计该课件对应的任务学习总时长
        stuJobCourseExample = new EduStuJobCourseExample();
        criteria = stuJobCourseExample.createCriteria();
        criteria.andTotalCourseGreaterThan(0);
        criteria.andJobIdEqualTo(jobId);
        criteria.andStuIdEqualTo(stuId);
        stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        EduStuJobCourse stuJobCourse2 = new EduStuJobCourse();
        if(stuJobCourseList!=null && stuJobCourseList.size()>0){
            stuJobCourse2 = stuJobCourseList.get(0);
            stuJobCourse2.setTime(stuJobCourse2.getTime()+time);
            if(stuJobCourse1.getStatus() == 2 && status < 2){//课件已学完（排除已学完的课件重复学习）
                stuJobCourse2.setCourseNum(stuJobCourse2.getCourseNum()+1);
            }
            stuJobCourseService.updateByPrimaryKey(stuJobCourse2);
        }

        return new SysResult(SysResultConstant.SUCCESS, stuJobCourse1.getStatus());
    }

    /**
     * 记录课件学习完成
     * @param stuId
     * @param jobId
     * @param courseId
     * @return
     */
    @RequestMapping(value = "/study-end/{stuId}/{jobId}/{courseId}")
    @ResponseBody
    public Object course_study_end(@PathVariable("stuId") int stuId, @PathVariable("jobId") int jobId,
                               @PathVariable("courseId") int courseId){
        int count = 0;
        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria criteria = stuJobCourseExample.createCriteria();
        criteria.andTotalCourseEqualTo(0);
        criteria.andCourseIdEqualTo(courseId);
        criteria.andJobIdEqualTo(jobId);
        criteria.andStuIdEqualTo(stuId);
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        EduStuJobCourse stuJobCourse = new EduStuJobCourse();
        if(stuJobCourseList!=null && stuJobCourseList.size()>0){
            stuJobCourse = stuJobCourseList.get(0);
            stuJobCourse.setStatus(2);//已学完
            stuJobCourseService.updateByPrimaryKeySelective(stuJobCourse);
        }

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    /**
     * 处理收藏与取消收藏
     * @param jobId
     * @param courseId
     * @param favorite
     * @return
     */
    @RequestMapping("/favorite/{jobId}/{courseId}/{favorite}")
    @ResponseBody
    public Object favorite(@PathVariable("jobId") Integer jobId, @PathVariable("courseId") Integer courseId,
                            @PathVariable("favorite") Integer favorite, HttpServletRequest request){
        int count = 0;
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");

        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria criteria = stuJobCourseExample.createCriteria();
        criteria.andJobIdEqualTo(jobId);
        criteria.andStuIdEqualTo(student.getStuId());
        criteria.andTotalCourseEqualTo(0);
        criteria.andCourseIdEqualTo(courseId);
        List<EduStuJobCourse> stuJobCourseList = stuJobCourseService.selectByExample(stuJobCourseExample);
        if(stuJobCourseList!=null && stuJobCourseList.size()>0){
            EduStuJobCourse stuJobCourse = stuJobCourseList.get(0);
            stuJobCourse.setFavorite(favorite);
            stuJobCourseService.updateByPrimaryKeySelective(stuJobCourse);
        }else{
            EduStuJobCourse stuJobCourse = new EduStuJobCourse();
            stuJobCourse.setCourseNum(0);
            stuJobCourse.setTime(0);
            stuJobCourse.setStatus(0);
            stuJobCourse.setFavorite(favorite);
            stuJobCourse.setJobId(jobId);
            stuJobCourse.setTotalCourse(0);
            stuJobCourse.setStuId(student.getStuId());
            stuJobCourse.setCourseId(courseId);
            stuJobCourseService.insertSelective(stuJobCourse);
        }
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    /**
     * MP4播放
     * @param uri
     * @param modelMap
     * @return
     */
    @RequestMapping("/player")
    public String player(String uri, ModelMap modelMap){
        modelMap.put("uri", uri);
        uri = uri.replace("/","%2F");
        modelMap.put("flashUrl", uri);
        return "/mp4.jsp";
    }

    /**
     * 课件包播放页面
     * @param uri
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/course-player", method = RequestMethod.GET)
    public String course_player(String uri, ModelMap modelMap){
        modelMap.put("url", uri);
        return "/player.jsp";
    }

    /**
     * 处理课件学完
     * @return
     */
    @RequestMapping(value = "/course-end", method = RequestMethod.GET)
    public String player_course_end(){
        return "/course-end.jsp";
    }


}
