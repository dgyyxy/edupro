package com.edusys.manager.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduStuJobCourseExample;
import com.edu.common.dao.model.EduStudent;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edu.common.dao.model.EduStudentExample;
import com.edu.common.util.ExcelUtil;
import com.edu.common.validator.ExcelValidator;
import com.edu.common.validator.LengthValidator;
import com.edu.common.validator.NotNullValidator;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduOrganizationService;
import com.edusys.manager.service.EduStuJobCourseService;
import com.edusys.manager.service.EduStudentExamService;
import com.edusys.manager.service.EduStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Gary on 2017/4/12.
 * 学员信息Controller
 */
@SuppressWarnings("ALL")
@Controller
@Api(value = "学员信息管理", description = "学员信息管理")
@RequestMapping("/manage/student")
public class StudentController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private EduStudentService studentService;

    @Autowired
    private EduOrganizationService organizationService;

    @Autowired
    private EduStuJobCourseService eduStuJobCourseService;

    @Autowired
    private EduStudentExamService eduStudentExamService;

    @ApiOperation(value = "学员信息首页")
    @RequiresPermissions("edu:student:read")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/manage/student/index.jsp";
    }

    @ApiOperation(value = "学员列表")
    @RequiresPermissions("sys:student:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order,
            Integer typeId,Integer typePid,
            String search) {
        EduStudentExample studentExample = new EduStudentExample();

        studentExample.setOffset(offset);
        studentExample.setLimit(limit);
        studentExample.setOrderByClause("stu_id DESC");

        if(typeId!=null && typeId!=0){
            if(typePid == 0)
                studentExample.createCriteria().andOrganizationId1EqualTo(typeId);
            else
                studentExample.createCriteria().andOrganizationId2EqualTo(typeId);
        }

        // 模糊查询
        if (StringUtils.isNotBlank(search)) {
            search = "%" + search + "%";
            studentExample.or(studentExample.createCriteria().andStuNameLike(search));
            studentExample.or(studentExample.createCriteria().andStuNoLike(search));
            studentExample.or(studentExample.createCriteria().andCardNoLike(search));
            studentExample.or(studentExample.createCriteria().andPhoneLike(search));
            studentExample.or(studentExample.createCriteria().andOrganizationName2Like(search));
        }

        List<EduStudent> rows = studentService.selectByExample(studentExample);
        long total = studentService.countByExample(studentExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation(value = "新增学员信息")
    @RequiresPermissions("edu:student:create")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "/manage/student/create.jsp";
    }

    @ApiOperation(value = "新增学员信息")
    @RequiresPermissions("edu:student:create")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object create(EduStudent student) {
        ComplexResult result = FluentValidator.checkAll()
                .on(student.getStuName(), new LengthValidator(1, 20, "姓名"))
                .on(student.getStuNo(), new NotNullValidator("学号"))
                .on(student.getPassword(), new NotNullValidator("密码"))
                .on(student.getOrganizationName2(), new NotNullValidator("所属机构"))
                .on(student.getCardNo(), new NotNullValidator("身份证号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.FAILED, result.getErrors());
        }
        EduStudentExample studentExample = new EduStudentExample();
        studentExample.or(studentExample.createCriteria().andCardNoEqualTo(student.getCardNo()));
        studentExample.or(studentExample.createCriteria().andStuNoEqualTo(student.getStuNo()));
        List<EduStudent> students = studentService.selectByExample(studentExample);
        if (students.size() > 0) {
            return new SysResult(SysResultConstant.FAILED, "证件号已存在！不允许重复使用！");
        }
        int count = studentService.insertSelective(student);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "修改学员信息")
    @RequiresPermissions("edu:student:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap modelMap) {
        EduStudent student = studentService.selectByPrimaryKey(id);
        modelMap.put("student", student);
        return "/manage/student/update.jsp";
    }

    @ApiOperation(value = "修改用户")
    @RequiresPermissions("sys:user:update")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduStudent student) {
        ComplexResult result = FluentValidator.checkAll()
                .on(student.getStuName(), new LengthValidator(1, 20, "姓名"))
                .on(student.getOrganizationName2(), new NotNullValidator("所属机构"))
                .on(student.getCardNo(), new NotNullValidator("身份证号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.FAILED, result.getErrors());
        }
        EduStudent eduStudent = studentService.selectByPrimaryKey(id);
        if(eduStudent!=null){
            if (!eduStudent.getCardNo().equals(student.getCardNo())) {
                EduStudentExample studentExample = new EduStudentExample();

                if (!eduStudent.getCardNo().equals(student.getCardNo()))
                    studentExample.or(studentExample.createCriteria().andCardNoEqualTo(student.getCardNo()));
//                if (!eduStudent.getStuNo().equals(student.getStuNo()))
//                    studentExample.or(studentExample.createCriteria().andStuNoEqualTo(student.getStuNo()));
                List<EduStudent> students = studentService.selectByExample(studentExample);
                if (students.size() > 0) {
                    return new SysResult(SysResultConstant.FAILED, "该学员学号或身份证号重复使用！");
                }
            }
        }

        student.setStuId(id);
        student.setPassword(eduStudent.getPassword());
        int count = studentService.updateByPrimaryKey(student);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除学员信息")
    @RequiresPermissions("edu:student:delete")
    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        int count = studentService.deleteByPrimaryKeys(ids);

        String[] idArray = ids.split("-");
        List<Integer> stuIds = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            stuIds.add(Integer.parseInt(idStr));

        }
        EduStudentExample studentExample = new EduStudentExample();
        studentExample.createCriteria().andStuIdIn(stuIds);

        EduStuJobCourseExample stuJobCourseExample = new EduStuJobCourseExample();
        stuJobCourseExample.createCriteria().andStuIdIn(stuIds);

        EduStudentExamExample studentExamExample = new EduStudentExamExample();
        studentExamExample.createCriteria().andStuIdIn(stuIds);

        try {
            eduStudentExamService.deleteByExample(studentExamExample);
            eduStuJobCourseService.deleteByExample(stuJobCourseExample);
            studentService.deleteByExample(studentExample);
        }catch (Exception ex){
            return new SysResult(SysResultConstant.FAILED, 0);
        }
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "重置学员信息密码")
    @RequiresPermissions("edu:student:resetPassword")
    @RequestMapping(value = "/resetPassword/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object resetPassowrd(@PathVariable("ids") String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            EduStudent student = studentService.selectByPrimaryKey(Integer.parseInt(idStr));
            student.setPassword(student.getCardNo().substring(student.getCardNo().length() - 4));
            studentService.updateByPrimaryKey(student);
        }
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "导入学员信息")
    @RequiresPermissions("edu:student:import")
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String importData() {
        return "/manage/student/import.jsp";
    }

    @ApiOperation(value = "导入学员信息")
    @RequiresPermissions("edu:student:import")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Object importData(HttpServletRequest request, Integer organizationId1,
                             String organizationName1, Integer organizationId2, String organizationName2) {
        //取得上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("importFile");
        ComplexResult result = FluentValidator.checkAll()
                .on(organizationName2, new NotNullValidator("所属机构"))
                .on(file, new ExcelValidator())
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.FAILED, result.getErrors());
        }
        String resultMsg = "";
        List<EduStudent> studentList = new ArrayList<>();

        //获取所有学员证件号列表
        List<String> cardnoList = studentService.selectCardNos();
        try {
            List<Map<String, String>> mapList = ExcelUtil.ExcelToList(file);
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, String> map = mapList.get(i);
                EduStudent student = new EduStudent();
                String stuName = map.get("姓名(必填)");
                student.setStuName(stuName);
                String stuNo = map.get("学号");
                student.setStuNo(stuNo);
                String phone = map.get("手机");
                student.setPhone(phone);
                String cardNo = map.get("身份证号(必填)");
                student.setCardNo(cardNo);
                //判断导入的学员证件号是否已经存在
                if(cardnoList.contains(cardNo)){
                    continue;
                }
                if (StringUtils.isNotBlank(cardNo)) {
                    if (cardNo.length() > 4) {
                        student.setPassword(cardNo.substring(cardNo.length() - 4));
                    }
                }
                student.setOrganizationId1(organizationId1);
                student.setOrganizationId2(organizationId2);
                student.setOrganizationName1(organizationName1);
                student.setOrganizationName2(organizationName2);
                studentList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = studentService.insertBatch(studentList);
        return new SysResult(SysResultConstant.SUCCESS, "success");
    }

    @ApiOperation(value = "导出学员信息")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletResponse response, String ids, int isAll) {
        response.setContentType("application/binary;charset=ISO8859_1");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = new String(("学员列表").getBytes(), "ISO8859_1") + new Date().getTime();
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
            List<EduStudent> students = new ArrayList<>();
            EduStudentExample ese = new EduStudentExample();
            ese.setOrderByClause("stu_id DESC");
            if (isAll == 0) {//只选择已勾选的学员导出
                if (StringUtils.isNotBlank(ids)) {
                    List<Integer> idList = new ArrayList<>();
                    String[] idArray = ids.split("-");
                    for (String idstr : idArray) {
                        idList.add(Integer.parseInt(idstr));
                    }
                    ese.createCriteria().andStuIdIn(idList);
                }
            }
            students = studentService.selectByExample(ese);
            String[] titles = new String[]{"学号","姓名","身份证号","手机号","所属机构"};
            studentService.exportExcel(titles, outputStream, students);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


