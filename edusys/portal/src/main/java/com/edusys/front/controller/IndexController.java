package com.edusys.front.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.validator.LengthValidator;
import com.edusys.front.common.SysResult;
import com.edusys.front.common.SysResultConstant;
import com.edusys.front.listener.MemoryData;
import com.edusys.front.listener.SessionListener;
import com.edusys.front.service.EduAdvertService;
import com.edusys.front.service.EduNoticeService;
import com.edusys.front.service.OrganizationService;
import com.edusys.front.service.StudentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gary on 2017/5/15.
 * 首页Controller
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController{

    private Logger _log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EduNoticeService noticeService;

    @Autowired
    private EduAdvertService advertService;

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map){
        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");
        int organId = 0;

        //广告轮询图片
        List<EduAdvert> advertList = advertService.selectByExample(new EduAdvertExample());

        //公告
        EduNoticeExample noticeExample = new EduNoticeExample();
        noticeExample.setOrderByClause("id desc");
        if(student!=null){
            organId = student.getOrganizationId2();
            noticeExample.or(noticeExample.createCriteria().andOrganIdEqualTo(0));
            noticeExample.or(noticeExample.createCriteria().andOrganIdEqualTo(organId));
        }else{
            noticeExample.createCriteria().andOrganIdEqualTo(0);
        }
        List<EduNotice> noticeList = noticeService.selectByExample(noticeExample);

        map.put("advertList", advertList);
        map.put("noticeList", noticeList);
        map.put("advertCount", advertList.size());

        return "/index.jsp";
    }


    /**
     * 登录操作
     * @return
     */
    @RequestMapping(value = "login/done", method = RequestMethod.POST)
    @ResponseBody
    public Object login_done(String account, String password, HttpServletRequest request){
        HttpSession session = request.getSession();

        ComplexResult result = FluentValidator.checkAll()
                .on(account, new LengthValidator(1, 20, "身份证号或者学生账号"))
                .on(password, new LengthValidator(1, 20, "密码"))
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }

        EduStudentExample ese = new EduStudentExample();
        //学号
        ese.or(ese.createCriteria().andStuNoEqualTo(account));
        //身份证号
        ese.or(ese.createCriteria().andCardNoEqualTo(account));

        List<EduStudent> students = studentService.selectByExample(ese);
        if(students!=null && students.size()>0){
            EduStudent student = students.get(0);
            String stuId = student.getStuId().toString();
            if(student!=null){
                if(student.getPassword().equals(password)){
                    //保存学员信息在session中
                    String sessionId = request.getRequestedSessionId();
                    session.setAttribute("user", student);
                    String userkey = "student-"+student.getStuId();
                    if(!MemoryData.getSessionIDMap().containsKey(userkey)){
                        MemoryData.getSessionIDMap().put(userkey, sessionId);
                    }else if(MemoryData.getSessionIDMap().containsKey(userkey)
                            && !StringUtils.equals(sessionId, MemoryData.getSessionIDMap().get(userkey))){
                        MemoryData.getSessionIDMap().remove(userkey);
                        MemoryData.getSessionIDMap().put(userkey, sessionId);
                    }
                    return new SysResult(SysResultConstant.SUCCESS, "success");
                }else{
                    return new SysResult(SysResultConstant.INVALID_PASSWORD, "fail");
                }
            }
        }

        return new SysResult(SysResultConstant.INVALID_USERNAME, "fail");
    }


    /**
     * 注册
     * @param student
     * @return
     */
    @RequestMapping(value = "/register/done", method = RequestMethod.POST)
    @ResponseBody
    public Object register(EduStudent student){
        ComplexResult result = FluentValidator.checkAll()
                .on(student.getStuName(), new LengthValidator(1, 20, "学员姓名"))
                .on(student.getCardNo(), new LengthValidator(1, 20, "学员身份证"))
                .on(student.getOrganizationName1(), new LengthValidator(1, 20, "一级机构"))
                .on(student.getOrganizationName2(), new LengthValidator(1, 20, "二级机构"))
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new SysResult(SysResultConstant.INVALID_LENGTH, result.getErrors());
        }
        EduStudentExample studentExample = new EduStudentExample();
        studentExample.createCriteria().andCardNoEqualTo(student.getCardNo());
        long count = studentService.countByExample(studentExample);
        if(count>0){
            return new SysResult(SysResultConstant.FAILED, "fail");
        }else{
            studentService.insertSelective(student);
            return new SysResult(SysResultConstant.INVALID_USERNAME, "success");
        }
    }

    /**
     * 组织机构列表
     * @return
     */
    @RequestMapping(value = "/organ/list", method = RequestMethod.GET)
    @ResponseBody
    public Object organizations(int pid){
        EduOrganizationExample eoe = new EduOrganizationExample();
        EduOrganizationExample.Criteria criteria = eoe.createCriteria();
        criteria.andParentIdEqualTo(pid);
        List<EduOrganization> list = organizationService.selectByExample(eoe);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        return result;
    }


    /**
     * 安全退出
     * @param request
     * @return
     */
    @RequestMapping(value = "/login/out", method = RequestMethod.GET)
    public String login_out(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            //EduStudent student = (EduStudent) session.getAttribute("user");
            //SessionListener.sessionContext.getSessionMap().remove(student.getStuId().toString());
            session.removeAttribute("user");
            MemoryData.removeAll();
        }
        response.sendRedirect("/index");
        return null;
    }


}
