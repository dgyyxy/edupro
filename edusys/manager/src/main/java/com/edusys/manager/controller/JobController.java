package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.validator.NotNullValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/4/28.
 * 学习任务Controller
 */
@Controller
@Api(value = "学习任务管理", description = "学习任务管理")
@RequestMapping("/manage/job")
public class JobController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private EduJobsService jobsService;

    @Autowired
    private SysApiService apiService;

    @Autowired
    private EduCoursewareTypeService coursewareTypeService;

    @Autowired
    private EduJobCoursewareService jobCoursewareService;

    @Autowired
    private EduCoursewareService coursewareService;

    @Autowired
    private EduStuJobCourseService stuJobCourseService;

    @ApiOperation("学习任务管理首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("edu:job:read")
    public String index(){
        return "/manage/job/index.jsp";
    }

    @ApiOperation("学习任务列表管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("edu:job:read")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search){
        EduJobsExample jobsExample = new EduJobsExample();
        EduJobsExample.Criteria criteria = jobsExample.createCriteria();
        jobsExample.setOffset(offset);
        jobsExample.setLimit(limit);
        jobsExample.setOrderByClause("id desc");
        //模糊查询
        if(StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            jobsExample.or(jobsExample.createCriteria().andNameLike(search));
        }

        List<EduJobs> rows = jobsService.selectByExample(jobsExample);
        long total = jobsService.countByExample(jobsExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("新增学习任务")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @RequiresPermissions("edu:job:create")
    public String create(){
        return "/manage/job/create.jsp";
    }

    @ApiOperation("新增学习任务操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduJobs job){
        ComplexResult result = FluentValidator.checkAll()
                .on(job.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        EduUser user = apiService.selectUserByUsername(username);
        job.setTeacher(user.getUsername());
        job.setTeacherId(user.getUserId());
        job.setTime(0);
        job.setCoursewareCount(0);
        int count = jobsService.insertSelective(job);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("修改学习任务")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    @RequiresPermissions("edu:job:update")
    public String update(@PathVariable("id") int id, ModelMap modelMap){
        EduJobs jobs = jobsService.selectByPrimaryKey(id);
        modelMap.put("job", jobs);
        return "/manage/job/update.jsp";
    }

    @ApiOperation("修改学习任务操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduJobs job){
        ComplexResult result = FluentValidator.checkAll()
                .on(job.getName(), new NotNullValidator("名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        job.setId(id);
        int count = jobsService.updateByPrimaryKeySelective(job);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除学习任务")
    @RequiresPermissions("edu:job:delete")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = jobsService.deleteByPrimaryKeys(ids);

        String[] idArray = ids.split("-");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            idList.add(Integer.parseInt(idStr));
        }

        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        stuJobCourseExample.createCriteria().andJobIdIn(idList);
        stuJobCourseService.deleteByExample(stuJobCourseExample);

        EduJobCoursewareExample jobCoursewareExample = new EduJobCoursewareExample();
        jobCoursewareExample.createCriteria().andJobIdIn(idList);
        jobCoursewareService.deleteByExample(jobCoursewareExample);

        EduJobsExample jobsExample = new EduJobsExample();
        jobsExample.createCriteria().andIdIn(idList);
        jobsService.deleteByExample(jobsExample);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "学习任务对应的课件页面")
    @RequiresPermissions("edu:job:courseware")
    @RequestMapping(value = "/courseware/{id}", method = RequestMethod.GET)
    public String courseware(@PathVariable("id") int id, ModelMap modelMap){
        modelMap.put("jobId", id);
        return "/manage/job/courseware.jsp";
    }

    @ApiOperation(value = "学习任务对应的课件")
    @RequiresPermissions("edu:job:courseware")
    @RequestMapping(value = "/courseware/{ids}/{jobId}",method = RequestMethod.POST)
    @ResponseBody
    public Object courseware(@PathVariable("ids") String ids, @PathVariable("jobId") int jobId) {
        String[] idArray = ids.split("-");
        List<EduJobCourseware> jobCoursewareList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        int time = 0;
        int i = jobCoursewareService.maxByExample(jobId)+1;//获取当前最后一个排序编号
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            idList.add(Integer.parseInt(idStr));
            EduJobCourseware jobCourseware = new EduJobCourseware();
            jobCourseware.setCoursewareId(Integer.parseInt(idStr));
            jobCourseware.setJobId(jobId);
            jobCourseware.setSortNum(i);
            jobCoursewareList.add(jobCourseware);
            i++;
        }

        EduCoursewareExample coursewareExample = new EduCoursewareExample();
        coursewareExample.createCriteria().andIdIn(idList);
        long sum = coursewareService.sumTimeByExample(coursewareExample);
        int count = jobCoursewareService.jobCoursewareSave(jobCoursewareList);
        EduJobs jobs = jobsService.selectByPrimaryKey(jobId);
        jobs.setCoursewareCount(jobs.getCoursewareCount()+idList.size());
        jobs.setTime(jobs.getTime()+Integer.parseInt(sum+""));
        count = jobsService.updateByPrimaryKeySelective(jobs);

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("课件列表")
    @RequiresPermissions("edu:courseware:read")
    @RequestMapping(value = "/courseList/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "typeId") Integer typeId,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       @PathVariable("jobId") int jobId, String search, int tag){
        EduCoursewareExample coursewareExample = new EduCoursewareExample();
        EduCoursewareExample.Criteria criteria = coursewareExample.createCriteria();
        if(null != typeId && 0 != typeId){
            criteria.andCategoryIdEqualTo(typeId);
        }

        //获取该学习任务已有的课件ID列表
        EduJobCoursewareExample jobCoursewareExample = new EduJobCoursewareExample();
        jobCoursewareExample.createCriteria().andJobIdEqualTo(jobId);
        List<EduJobCourseware> jobCoursewareList = jobCoursewareService.selectByExample(jobCoursewareExample);
        if(jobCoursewareList!=null && jobCoursewareList.size()>0){
            List<Integer> ids = new ArrayList<Integer>();
            for(EduJobCourseware jobCourseware : jobCoursewareList){
                ids.add(jobCourseware.getCourseware().getId());
            }
            coursewareExample.setOrderByClause("id desc");
            if(tag == 1) {  //获取未分配课件列表
                criteria.andIdNotIn(ids);
            }else if(tag == 2){ //获取已分配课件列表
                criteria.andIdIn(ids);
            }
        }

        coursewareExample.setOffset(offset);
        coursewareExample.setLimit(limit);

        //模糊查询
        if(StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            criteria.andNameLike(search);
        }

        List<EduCourseware> rows = coursewareService.selectByExample(coursewareExample);
        long total = coursewareService.countByExample(coursewareExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "删除已分配课件")
    @RequestMapping(value = "/delAllot/{jobId}/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delAllot(@PathVariable("ids") String ids, @PathVariable("jobId") int jobId) {

        String[] idArray = ids.split("-");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            idList.add(Integer.parseInt(idStr));
        }

        int count = jobCoursewareService.deleteBatch(idList, jobId);

        //更新分配课件数
        EduJobs job = jobsService.selectByPrimaryKey(jobId);
        job.setCoursewareCount(job.getCoursewareCount()-idList.size());
        jobsService.updateByPrimaryKeySelective(job);

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("学习任务对应课件列表")
    @RequiresPermissions("edu:courseware:read")
    @RequestMapping(value = "/jobcourses/{jobId}", method = RequestMethod.GET)
    @ResponseBody
    public Object jobcourses(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "typeId") Integer typeId,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       @PathVariable("jobId") int jobId, String search){
        EduJobCoursewareExample ejce = new EduJobCoursewareExample();
        EduJobCoursewareExample.Criteria criteria = ejce.createCriteria();
        ejce.setOffset(offset);
        ejce.setLimit(limit);
        ejce.setOrderByClause("sort_num asc");

        criteria.andJobIdEqualTo(jobId);
        List<EduJobCourseware> rows = jobCoursewareService.selectByExample(ejce);
        long total = jobCoursewareService.countByExample(ejce);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "取消课件分配")
    @RequestMapping(value = "/cancel/{jobId}/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object cancel(@PathVariable("ids") String ids, @PathVariable("jobId") int jobId) {

        String[] idArray = ids.split("-");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            idList.add(Integer.parseInt(idStr));
        }

        EduJobCoursewareExample jobCoursewareExample = new EduJobCoursewareExample();
        jobCoursewareExample.createCriteria().andIdIn(idList);
        int count = jobCoursewareService.deleteByExample(jobCoursewareExample);

        //更新分配课件数
        EduJobs job = jobsService.selectByPrimaryKey(jobId);
        job.setCoursewareCount(job.getCoursewareCount()-idList.size());
        jobsService.updateByPrimaryKeySelective(job);

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("学习任务已分配课件页面")
    @RequiresPermissions("edu:job:courseware")
    @RequestMapping(value = "/jobcourse/{id}", method = RequestMethod.GET)
    public String jobcourse(@PathVariable("id") int id, ModelMap modelMap){
        modelMap.put("jobId", id);
        return "/manage/job/jobcourse.jsp";
    }

    @ApiOperation("调整排序")
    @RequestMapping(value = "/sort/{type}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object sort(@PathVariable("type") String type, @PathVariable("id") int id){
        EduJobCourseware jobCourseware = jobCoursewareService.selectByPrimaryKey(id);
        int jobId = jobCourseware.getJobId();
        int maxId = jobCoursewareService.maxByExample(jobId);
        int sortNum = jobCourseware.getSortNum();
        EduJobCourseware updateJobCourse = null;

        if(type.equals("up")){//向上调整
            if(sortNum == 1){
                return new SysResult(SysResultConstant.FAILED, "first");
            }
            jobCourseware.setSortNum(sortNum-1);
            updateJobCourse = jobCoursewareService.selectBySortNum(sortNum-1, jobId);
            if(updateJobCourse!=null){
                updateJobCourse.setSortNum(updateJobCourse.getSortNum()+1);
            }
        }else if(type.equals("down")){
            if(maxId == sortNum){
                return new SysResult(SysResultConstant.FAILED, "last");
            }
            jobCourseware.setSortNum(sortNum+1);
            updateJobCourse = jobCoursewareService.selectBySortNum(sortNum+1, jobId);
            if(updateJobCourse!=null){
                updateJobCourse.setSortNum(updateJobCourse.getSortNum()-1);
            }
        }

        jobCoursewareService.updateByPrimaryKeySelective(jobCourseware);
        jobCoursewareService.updateByPrimaryKeySelective(updateJobCourse);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }
}
