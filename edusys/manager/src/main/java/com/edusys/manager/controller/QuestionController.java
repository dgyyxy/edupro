package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.util.ExcelUtil;
import com.edu.common.validator.LengthValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduQuestionCategoryService;
import com.edusys.manager.service.EduQuestionService;
import com.edusys.manager.service.EduQuestionTypeService;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/6.
 * 题库Controller
 */
@Controller
@Api(value = "题库管理", description = "题库管理")
@RequestMapping("/manage/question")
public class QuestionController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private EduQuestionService questionService;
    @Autowired
    private EduQuestionCategoryService questionCategoryService;
    @Autowired
    private EduQuestionTypeService questionTypeService;

    @ApiOperation("题库首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("edu:question:read")
    public String index() {
        return "/manage/question/index.jsp";
    }


    @ApiOperation(value = "题库列表")
    @RequiresPermissions("edu:question:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search, int categoryId, int typeId, int difficulty, String idstr) {
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        questionExample.setOffset(offset);
        questionExample.setLimit(limit);
        questionExample.setOrderByClause("id DESC");
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            questionExample.setOrderByClause(sort + " " + order);
        }
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            criteria.andNameLike(search);
        }
        if (0 != categoryId)
            criteria.andQuestionCategoryIdEqualTo(categoryId);
        if (0 != typeId) {
            criteria.andQuestionTypeIdEqualTo(typeId);
        }
        if (0 != difficulty)
            criteria.andDifficultyEqualTo(difficulty);
        if (null != idstr) {
            String[] idarray = idstr.split("-");
            List<Integer> ids = new ArrayList<>();
            for (String str : idarray) {
                ids.add(Integer.parseInt(str));
            }
            criteria.andIdNotIn(ids);
        }

        /*//统计出错率和答题数
        Map<Integer, StatisticBean> map = questionService.statisticalQuestion();
        if (map != null && map.size() > 0) {
            List<EduQuestion> updateQuestions = new ArrayList<>();
            //批量更新出错率和答题数
            for (Integer key : map.keySet()) {
                StatisticBean statisticBean = map.get(key);
                int qsum = statisticBean.getSum();
                int errorcount = statisticBean.getErrorCount();
                EduQuestion question = new EduQuestion();
                question.setId(key);
                question.setQsum(qsum);
                Double errorRate = NumberUtils.formatDoublePercent((float) errorcount / (float) qsum * 100);
                question.setErrorRate(errorRate);
                updateQuestions.add(question);
            }
            //批量更新处理
            questionService.updateQuestionBatch(updateQuestions);
        }*/

        List<EduQuestion> rows = questionService.selectByExample(questionExample);
        long total = questionService.countByExample(questionExample);


        /*
        List<EduQuestion> list = new ArrayList<>();
        if(rows!=null && rows.size()>0){

            for(EduQuestion question : rows){
                StatisticBean statisticBean = map.get(question.getId());
                if(statisticBean!=null) {
                    question.setQsum(statisticBean.getSum());
                    question.setErrorCount(statisticBean.getErrorCount());
                }
                list.add(question);
            }
        }*/


        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("试题新增页面")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(ModelMap modelMap) {

        return "/manage/question/create.jsp";
    }

    @ApiOperation("试题新增处理操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduQuestion question) {
        long time = System.currentTimeMillis();
        question.setCreateTime(time);
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        question.setCreator(username);
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andNameEqualTo(question.getName());
        List<EduQuestion> list = questionService.selectByExample(questionExample);
        if(list.size()>0){
            return new SysResult(SysResultConstant.FAILED, "该题目已存在！");
        }
        int count = questionService.insertSelective(question);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("试题修改页面")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") Integer id, ModelMap modelMap) {
        EduQuestion question = questionService.selectByPrimaryKey(id);
        modelMap.put("question", question);
        return "/manage/question/update.jsp";
    }

    @ApiOperation("试题修改处理操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(EduQuestion question, @PathVariable("id") Integer id) {
        question.setId(id);
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        question.setCreator(username);
        int count = questionService.updateByPrimaryKeySelective(question);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("批量导入试题")
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String importQuestion() {
        return "/manage/question/import.jsp";
    }

    @ApiOperation(value = "批量导入试题")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Object importData(HttpServletRequest request, Integer questionCategoryId) {
        //取得上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("importFile");
        String resultMsg = "";
        List<EduStudent> studentList = new ArrayList<>();
        try {
            List<Map<String, String>> mapList = ExcelUtil.ExcelToList(file);
            Subject subject = SecurityUtils.getSubject();
            String username = (String) subject.getPrincipal();
            questionService.uploadQuestions(file, questionCategoryId, username);
        } catch (Exception e) {
            e.printStackTrace();
            return new SysResult(SysResultConstant.FAILED, e.getMessage());
        }
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }


    @ApiOperation(value = "删除试题")
    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = questionService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }


    /*试题类型*/
    @ApiOperation(value = "试题类型首页")
    @RequiresPermissions("edu:question:type:read")
    @RequestMapping(value = "/type/index", method = RequestMethod.GET)
    public String type_index() {
        return "/manage/question/type/index.jsp";
    }

    @ApiOperation(value = "试题类型列表")
    @RequiresPermissions("edu:question:type:read")
    @RequestMapping(value = "/type/list", method = RequestMethod.GET)
    @ResponseBody
    public Object type_list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduQuestionTypeExample questionTypeExample = new EduQuestionTypeExample();
        EduQuestionTypeExample.Criteria criteria = questionTypeExample.createCriteria();
        questionTypeExample.setOffset(offset);
        questionTypeExample.setLimit(limit);
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            questionTypeExample.setOrderByClause(sort + " " + order);
        }
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            questionTypeExample.or(questionTypeExample.createCriteria().andNameLike(search));
        }
        List<EduQuestionType> rows = questionTypeService.selectByExample(questionTypeExample);
        long total = questionTypeService.countByExample(questionTypeExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增试题类型")
    @RequestMapping(value = "/type/create", method = RequestMethod.GET)
    public String type_create() {
        return "/manage/question/type/create.jsp";
    }

    @ApiOperation(value = "新增试题类型操作")
    @ResponseBody
    @RequestMapping(value = "/type/create", method = RequestMethod.POST)
    public Object type_create(EduQuestionType questionType) {
        ComplexResult result = FluentValidator.checkAll()
                .on(questionType.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        int count = questionTypeService.insertSelective(questionType);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除试题类型")
    @RequestMapping(value = "/type/delete/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object type_delete(@PathVariable("ids") String ids) {
        int count = questionTypeService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改试题类型")
    @RequestMapping(value = "/type/update/{id}", method = RequestMethod.GET)
    public String type_update(@PathVariable("id") int id, ModelMap modelMap) {
        EduQuestionType questionType = questionTypeService.selectByPrimaryKey(id);
        modelMap.put("questionType", questionType);
        return "/manage/question/type/update.jsp";
    }

    @ApiOperation(value = "修改试题类型")
    @RequestMapping(value = "/type/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object type_update(@PathVariable("id") int id, EduQuestionType questionType) {
        ComplexResult result = FluentValidator.checkAll()
                .on(questionType.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        questionType.setId(id);
        int count = questionTypeService.updateByPrimaryKeySelective(questionType);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    /*分类*/
    @ApiOperation(value = "试题分类首页")
    @RequiresPermissions("edu:question:category:read")
    @RequestMapping(value = "/category/index", method = RequestMethod.GET)
    public String category_index() {
        return "/manage/question/category/index.jsp";
    }

    @ApiOperation(value = "试题分类列表")
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    @ResponseBody
    public Object category_list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "pid") Integer pid,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduQuestionCategoryExample questionCategoryExample = new EduQuestionCategoryExample();
        EduQuestionCategoryExample.Criteria criteria = questionCategoryExample.createCriteria();
        if (null != pid) {
            criteria.andPidEqualTo(pid);
        }
        questionCategoryExample.setOffset(offset);
        questionCategoryExample.setLimit(limit);
        questionCategoryExample.setOrderByClause("orderby ASC");
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            questionCategoryExample.setOrderByClause(sort + " " + order);
        }
        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            questionCategoryExample.or(questionCategoryExample.createCriteria().andNameLike(search));
        }
        List<EduQuestionCategory> rows = questionCategoryService.selectByExample(questionCategoryExample);
        List<EduQuestionCategory> list = new ArrayList<>();
        if (rows != null && rows.size() > 0) {
            for (EduQuestionCategory questionCategory : rows) {
                if (questionCategory.getPid() > 0) {
                    questionCategory.setParentName(questionCategoryService.selectByPrimaryKey(questionCategory.getPid()).getName());
                }
                list.add(questionCategory);
            }
        }
        long total = questionCategoryService.countByExample(questionCategoryExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增试题分类")
    @RequestMapping(value = "/category/create", method = RequestMethod.GET)
    public String category_create(ModelMap modelMap) {
        EduQuestionCategoryExample questionCategoryExample = new EduQuestionCategoryExample();
        questionCategoryExample.createCriteria().andPidEqualTo(0);
        List<EduQuestionCategory> questionCategories = questionCategoryService.selectByExample(questionCategoryExample);
        modelMap.put("questionCategories", questionCategories);
        return "/manage/question/category/create.jsp";
    }

    @ApiOperation(value = "新增试题分类操作")
    @ResponseBody
    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    public Object category_create(EduQuestionCategory questionCategory) {
        ComplexResult result = FluentValidator.checkAll()
                .on(questionCategory.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        if (questionCategory.getLevel() == 2)
            questionCategory.setOrderby(questionCategory.getPid());
        int count = questionCategoryService.insertSelective(questionCategory);
        if (questionCategory.getLevel() == 1)
            questionCategory.setOrderby(questionCategory.getId());
        questionCategoryService.updateByPrimaryKeySelective(questionCategory);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除试题分类")
    @RequestMapping(value = "/category/delete/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object category_delete(@PathVariable("ids") String ids) {
        int count = questionCategoryService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改试题分类")
    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.GET)
    public String category_update(@PathVariable("id") int id, ModelMap modelMap) {
        EduQuestionCategory questionCategory = questionCategoryService.selectByPrimaryKey(id);
        EduQuestionCategoryExample questionCategoryExample = new EduQuestionCategoryExample();
        questionCategoryExample.createCriteria().andPidEqualTo(0);
        List<EduQuestionCategory> questionCategories = questionCategoryService.selectByExample(questionCategoryExample);
        modelMap.put("questionCategories", questionCategories);
        modelMap.put("questionCategory", questionCategory);
        return "/manage/question/category/update.jsp";
    }

    @ApiOperation(value = "修改试题分类")
    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object category_update(@PathVariable("id") int id, EduQuestionCategory questionCategory) {
        ComplexResult result = FluentValidator.checkAll()
                .on(questionCategory.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        questionCategory.setId(id);
        int count = questionCategoryService.updateByPrimaryKeySelective(questionCategory);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }
}
