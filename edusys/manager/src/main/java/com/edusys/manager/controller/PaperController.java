package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.dao.pojo.AnswerSheetItem;
import com.edu.common.dao.pojo.QuestionAdapter;
import com.edu.common.validator.LengthValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduPaperCategoryService;
import com.edusys.manager.service.EduPaperService;
import com.edusys.manager.service.EduQuestionService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Gary on 2017/5/7.
 * 试卷Controller
 */
@Controller
@Api(value = "试卷管理", description = "试卷管理")
@RequestMapping("/manage/paper")
public class PaperController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    private EduPaperService paperService;
    @Autowired
    private EduPaperCategoryService paperCategoryService;
    @Autowired
    private EduQuestionService questionService;

    @ApiOperation("试卷设置首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("edu:paper:read")
    public String index(){
        return "/manage/paper/index.jsp";
    }

    @ApiOperation("试卷列表管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("edu:paper:read")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       @RequestParam(required = false, defaultValue = "0", value = "status") int status,
                       String search, Integer typeId){
        EduPaperExample paperExample = new EduPaperExample();
        EduPaperExample.Criteria criteria = paperExample.createCriteria();
        paperExample.setOffset(offset);
        paperExample.setLimit(limit);
        paperExample.setOrderByClause("id DESC");

        if(status >0){
            criteria.andStatusEqualTo(true);
        }

        if(typeId!=null && typeId!=0){
            criteria.andCategoryIdEqualTo(typeId);
        }

        //排除考试自动组卷
        criteria.andPaperTypeNotEqualTo(3);

        // 模糊查询
        if (StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            paperExample.or(paperExample.createCriteria().andNameLike(search));
        }
        List<EduPaper> rows = paperService.selectByExample(paperExample);
        long total = paperService.countByExample(paperExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("试卷新增页面")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(){
        return "/manage/paper/create.jsp";
    }

    @ApiOperation("试卷新增处理操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduPaper paper){
        long time = System.currentTimeMillis();
        paper.setCreateTime(time);
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        paper.setCreator(username);

        //手工组卷
        if(paper.getPaperType() == 1){
            int count = paperService.insertSelective(paper);
            return new SysResult(SysResultConstant.SUCCESS, paper.getId());
        }

        List<Integer> idList = new ArrayList<Integer>();
        //处理题目分类
        Iterator<Integer> it = paper.getQuestionTypeRate().keySet().iterator();
        while(it.hasNext()){
            idList.add(it.next());
        }

        // 创建试卷
        try {
            paperService.createPaper(paper);
            return new SysResult(SysResultConstant.SUCCESS, paper.getId());
        } catch (Exception e) {
            return new SysResult(SysResultConstant.FAILED, e.getMessage());
        }

    }

    @ApiOperation("试卷编辑")
    @RequestMapping(value = "/updatePaper/{id}", method = RequestMethod.GET)
    public String updatePaper(ModelMap modelMap, @PathVariable("id") int id) {
        EduPaper paper = paperService.selectByPrimaryKey(id);
        modelMap.put("paper", paper);
        return "/manage/paper/update_paper.jsp";
    }

    @ApiOperation("试卷编辑操作")
    @RequestMapping(value = "/updatePaper/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePaper(@PathVariable("id") int id, EduPaper paper) {
        paper.setId(id);
        EduPaper oldPaper = paperService.selectByPrimaryKey(id);

        SysResult sysResult = null;
        if(oldPaper.getTotalPoint() == paper.getTotalPoint()){//未修改总分
            sysResult = new SysResult(SysResultConstant.SUCCESS, 0);
        }else{//总分修改需要完善试卷，并且将试卷状态变为未完成
            paper.setStatus(0);
            sysResult = new SysResult(SysResultConstant.SUCCESS, paper.getId());
        }
        paperService.updateByPrimaryKeySelective(paper);
        return sysResult;
    }

    @ApiOperation("试卷修改页面")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap, HttpServletRequest request){
        String strurl = "http://" + request.getServerName() // 服务器地址
                + ":" + request.getServerPort() + "/";
        EduPaper paper = paperService.selectByPrimaryKey(id);
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotBlank(paper.getContent())){
            Gson gson = new Gson();
            List<QuestionResult> questionResults = gson.fromJson(paper.getContent(), new TypeToken<List<QuestionResult>>(){}.getType());
            for(QuestionResult qr : questionResults){
                QuestionAdapter adapter = new QuestionAdapter(qr, strurl);
                sb.append(adapter.getStringFromXML());
            }
        }
        modelMap.put("paper", paper);
        modelMap.put("htmlStr", sb);
        return "/manage/paper/update.jsp";
    }

    @ApiOperation(value = "删除试卷")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = paperService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("修改题目分值页面")
    @RequestMapping(value = "/updateScore", method = RequestMethod.GET)
    public String updateScore(){
        return "/manage/paper/update_score.jsp";
    }

    @ApiOperation("修改试卷名称页面")
    @RequestMapping(value = "/updateName", method = RequestMethod.GET)
    public String updateName(){
        return "/manage/paper/update_name.jsp";
    }

    @ApiOperation("新增题目列表页面")
    @RequestMapping(value = "/questionList", method = RequestMethod.GET)
    public String questionList(){
        return "/manage/paper/question_list.jsp";
    }

    @ApiOperation("试卷批量添加题目")
    @RequestMapping(value = "/batchAddQuestion", method = RequestMethod.POST)
    @ResponseBody
    public Object batchAddQuestions(HttpServletRequest request, String idListStr){
        String strUrl = "http://" + request.getServerName() // 服务器地址
                + ":" + request.getServerPort() + "/";
        Type type = new TypeToken<List<Integer>>(){}.getType();
        Gson gson = new Gson();
        List<Integer> idList = gson.fromJson(idListStr, type);

        Set<Integer> set = new TreeSet<Integer>();
        for (int id : idList){
            set.add(id);
        }
        idList.clear();
        Iterator<Integer> it = set.iterator();
        while (it.hasNext()){
            idList.add(it.next());
        }
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andIdIn(idList);
        List<EduQuestion> questionList = questionService.selectByExample(questionExample);

        List<QuestionResult> qrList = new ArrayList<>();
        for(EduQuestion question : questionList){
            qrList.add(new QuestionResult(question.getId(), question.getContent(),question.getAnswer(),
                    question.getQuestionTypeId(),0,0));
        }

        for(QuestionResult qr : qrList){
            QuestionAdapter adapter = new QuestionAdapter(qr, strUrl);
            qr.setContent(adapter.getStringFromXML());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("rows", qrList);
        return result;
    }

    @ApiOperation("修改试卷保存操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, String questionPointMapStr, String paperName){
        Type mapType = new TypeToken<HashMap<Integer, Float>>(){}.getType();
        Gson gson = new Gson();
        HashMap<Integer, Float> questionPointMap = gson.fromJson(questionPointMapStr, mapType);
        EduPaper paper = paperService.selectByPrimaryKey(id);
        List<Integer> idList = new ArrayList<Integer>();
        Iterator<Integer> it = questionPointMap.keySet().iterator();
        float sum = 0;
        while(it.hasNext()){
            int key = it.next();
            idList.add(key);
        }
        EduQuestionExample questionExample = new EduQuestionExample();
        EduQuestionExample.Criteria criteria = questionExample.createCriteria();
        criteria.andIdIn(idList);
        List<EduQuestion> questionList = questionService.selectByExample(questionExample);

        List<QuestionResult> qrList = new ArrayList<>();
        for(EduQuestion question : questionList){
            qrList.add(new QuestionResult(question.getId(), question.getContent(),question.getAnswer(),
                    question.getQuestionTypeId(),0,0));
        }

        AnswerSheet as = new AnswerSheet();
        as.setExamPaperId(id);
        List<AnswerSheetItem> asList = new ArrayList<AnswerSheetItem>();
        for(QuestionResult qr : qrList){
            AnswerSheetItem item = new AnswerSheetItem();
            item.setAnswer(qr.getAnswer());
            item.setQuestionId(qr.getQuestionId());
            item.setPoint(questionPointMap.get(qr.getQuestionId()));
            item.setQuestionTypeId(qr.getQuestionTypeId());
            qr.setQuestionPoint(questionPointMap.get(qr.getQuestionId()));
            sum += questionPointMap.get(qr.getQuestionId());
            asList.add(item);
        }

        as.setPointMax(sum);
        as.setAnswerSheetItems(asList);
        as.setPointPass(paper.getPassPoint());
        String content = gson.toJson(qrList);
        String answerSheet = gson.toJson(as);

        paper.setAmount(qrList.size());
        paper.setAnswerSheet(answerSheet);
        paper.setId(id);
        paper.setContent(content);
        paper.setStatus(1);
        paper.setTotalPoint((int) sum);//总分

        paper.setName(paperName);

        int count = paperService.updateByPrimaryKeySelective(paper);

        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    /*试卷分类管理*/
    @ApiOperation(value = "试卷分类首页")
    @RequiresPermissions("edu:paper:category:read")
    @RequestMapping(value = "/category/index", method = RequestMethod.GET)
    public String category_index() {
        return "/manage/paper/category/index.jsp";
    }

    @ApiOperation(value = "试卷分类列表")
    @RequiresPermissions("edu:paper:category:read")
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    @ResponseBody
    public Object category_list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "pid") Integer pid,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            String search) {
        EduPaperCategoryExample paperCategoryExample = new EduPaperCategoryExample();
        EduPaperCategoryExample.Criteria criteria = paperCategoryExample.createCriteria();
        if(null != pid){
            criteria.andPidEqualTo(pid);
        }
        paperCategoryExample.setOffset(offset);
        paperCategoryExample.setLimit(limit);

        paperCategoryExample.setOrderByClause("orderby ASC");
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            paperCategoryExample.setOrderByClause(sort + " " + order);
        }
        // 模糊查询
        if (StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            paperCategoryExample.or(paperCategoryExample.createCriteria().andNameLike(search));
        }
        List<EduPaperCategory> rows = paperCategoryService.selectByExample(paperCategoryExample);
        List<EduPaperCategory> list = new ArrayList<>();
        if(rows!=null && rows.size()>0){
            for(EduPaperCategory paperCategory : rows){
                if(paperCategory.getPid()>0){
                    paperCategory.setParentName(paperCategoryService.selectByPrimaryKey(paperCategory.getPid()).getName());
                }
                list.add(paperCategory);
            }
        }
        long total = paperCategoryService.countByExample(paperCategoryExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增试卷分类")
    @RequestMapping(value = "/category/create", method = RequestMethod.GET)
    public String category_create(ModelMap modelMap) {
        EduPaperCategoryExample paperCategoryExample = new EduPaperCategoryExample();
        paperCategoryExample.createCriteria().andPidEqualTo(0);
        List<EduPaperCategory> paperCategories = paperCategoryService.selectByExample(paperCategoryExample);
        modelMap.put("paperCategories", paperCategories);
        return "/manage/paper/category/create.jsp";
    }

    @ApiOperation(value = "新增试卷分类操作")
    @ResponseBody
    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    public Object category_create(EduPaperCategory paperCategory) {
        ComplexResult result = FluentValidator.checkAll()
                .on(paperCategory.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        if(paperCategory.getLevel() == 2)
            paperCategory.setOrderby(paperCategory.getPid());
        int count = paperCategoryService.insertSelective(paperCategory);
        if(paperCategory.getLevel() == 1)
            paperCategory.setOrderby(paperCategory.getId());
        paperCategoryService.updateByPrimaryKeySelective(paperCategory);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除试卷分类")
    @RequestMapping(value = "/category/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object category_delete(@PathVariable("ids") String ids) {
        int count = paperCategoryService.deleteByPrimaryKeys(ids);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改试卷分类")
    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.GET)
    public String category_update(@PathVariable("id") int id, ModelMap modelMap) {
        EduPaperCategory paperCategory = paperCategoryService.selectByPrimaryKey(id);
        EduPaperCategoryExample paperCategoryExample = new EduPaperCategoryExample();
        paperCategoryExample.createCriteria().andPidEqualTo(0);
        List<EduPaperCategory> paperCategories = paperCategoryService.selectByExample(paperCategoryExample);
        modelMap.put("paperCategories", paperCategories);
        modelMap.put("paperCategory", paperCategory);
        return "/manage/paper/category/update.jsp";
    }

    @ApiOperation(value = "修改试卷分类")
    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object category_update(@PathVariable("id") int id, EduPaperCategory paperCategory) {
        ComplexResult result = FluentValidator.checkAll()
                .on(paperCategory.getName(), new LengthValidator(1, 20, "名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        paperCategory.setId(id);
        if(paperCategory.getPid() == null){
            paperCategory.setPid(0);
        }
        if(paperCategory.getLevel() == 1){
            paperCategory.setOrderby(id);
        }else{
            paperCategory.setOrderby(paperCategory.getPid());
        }
        int count = paperCategoryService.updateByPrimaryKey(paperCategory);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

}
