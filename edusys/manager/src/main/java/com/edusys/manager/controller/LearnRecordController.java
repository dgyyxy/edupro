package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.ExportStudyVo;
import com.edu.common.util.NumberUtils;
import com.edusys.manager.service.EduStuJobCourseService;
import com.edusys.manager.service.EduStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Gary on 2017/8/13.
 * 学习记录管理
 */
@Controller
@Api(value = "学习记录管理", description = "学习记录管理")
@RequestMapping("/manage/record")
public class LearnRecordController extends BaseController{

    @Autowired
    private EduStudentService studentService;

    @Autowired
    private EduStuJobCourseService jobCourseService;

    @ApiOperation(value = "学习记录首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/record/index.jsp";
    }

    @ApiOperation(value = "学习记录列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduStudentExample studentExample = new EduStudentExample();

        studentExample.setOffset(offset);
        studentExample.setLimit(limit);
        studentExample.setOrderByClause("es.stu_id DESC");
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "'%" + search + "%'";
            studentExample.or(studentExample.createCriteria().otherOperate("es.stu_name like "+search));
            studentExample.or(studentExample.createCriteria().otherOperate("es.card_no like "+search));
            studentExample.or(studentExample.createCriteria().otherOperate("es.organization_name2 like "+search));
        }
        List<EduStudent> rows = studentService.selectLearnRecordList(studentExample);


        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        EduStuJobCourseExample.Criteria criteria = stuJobCourseExample.createCriteria();

        criteria.andTotalCourseGreaterThan(0);
        List<EduStuJobCourse> stuJobCourseList = jobCourseService.selectByExample(stuJobCourseExample);

        Map<Integer, Integer[]> map = new HashMap<>();
        int studyCount = 0;
        int time = 0;
        for(EduStuJobCourse esjc : stuJobCourseList){
            if(map.size()>0 && map.containsKey(esjc.getStuId())){
                studyCount = map.get(esjc.getStuId())[0];
                time = map.get(esjc.getStuId())[1]+esjc.getTime();
            }else{
                studyCount = 0;
                time = esjc.getTime();
            }
            studyCount++;
            Integer[] objs = new Integer[2];
            objs[0] = studyCount;
            objs[1] = time;
            map.put(esjc.getStuId(), objs);
        }

        long total = studentService.countLearnRecord(studentExample);

        List<EduStudent> list = new ArrayList<>();

        if(rows!=null && rows.size()>0){
            for(EduStudent student : rows){
                int stuId = student.getStuId();
                int study = 0;
                int unstudy =0;
                int sumtime = 0;
                if(map!=null && map.size()>0){
                    if(map.get(stuId)!=null) {
                        study = map.get(stuId)[0];
                        unstudy = student.getJobCount() - study;
                        sumtime = map.get(stuId)[1];
                    }
                }

                student.setStudyCount(study);
                student.setUnStudyCount(unstudy);
                student.setSumTime(sumtime);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("学员对应学习任务记录")
    @RequestMapping(value = "/job-list/{stuId}", method = RequestMethod.GET)
    public String jobList(ModelMap map, @PathVariable("stuId") Integer stuId){
        map.put("stuId", stuId);
        return "/manage/record/jobList.jsp";
    }

    @ApiOperation("学员对应学习任务记录")
    @RequestMapping(value = "/job-record/{stuId}", method = RequestMethod.GET)
    @ResponseBody
    public Object jobRecord(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @PathVariable("stuId") Integer stuId, String search){
        List<EduStuJobCourse> rows = jobCourseService.selectJobsByStuIdPage(stuId,limit, offset, search);
        long total = jobCourseService.jobsCountByStuId(stuId, search);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("学员对应课件记录")
    @RequestMapping(value = "/course-list/{stuId}/{jobId}", method = RequestMethod.GET)
    public String courseList(ModelMap map, @PathVariable("stuId") Integer stuId, @PathVariable("jobId") Integer jobId){
        map.put("stuId", stuId);
        map.put("jobId", jobId);
        return "/manage/record/courseList.jsp";
    }

    @ApiOperation("学员对应课件记录")
    @RequestMapping(value = "/course-record/{stuId}/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    public Object courseRecord(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @PathVariable("stuId") Integer stuId, @PathVariable("jobId") Integer jobId, String search){
        List<EduStuJobCourse> rows = jobCourseService.selectCoursesByStuIdPage(stuId, jobId, limit, offset, search);
        long total = jobCourseService.courseCountByStuId(stuId, jobId, search);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "导出学习记录页面")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(){
        return "/manage/record/export.jsp";
    }

    @ApiOperation(value = "导出学习记录")
    @RequestMapping(value = "/exportdo", method = RequestMethod.GET)
    public String exportLearnRecord(HttpServletResponse response, int jobId, int organId, HttpServletRequest request) {
        response.setContentType("application/binary;charset=ISO8859_1");
        try {
            String fileNameStr = "学习记录";
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(fileNameStr.getBytes(), "ISO8859_1") + "-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            String[] titles = new String[]{"序号", "姓名", "证件号", "所属机构", "学习任务", "学习时长", "学习进度",  "已学课件"};
            List<ExportStudyVo> list = jobCourseService.exportLearnRecord(organId, jobId);
            jobCourseService.exportOperate(titles, outputStream, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
