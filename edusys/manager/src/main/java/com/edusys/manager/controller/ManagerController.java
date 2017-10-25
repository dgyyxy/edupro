package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduPermission;
import com.edu.common.dao.model.EduUser;
import com.edusys.manager.service.SysApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 后台Controller
 * Created by Gary on 2017/3/27.
 */
@Controller
@RequestMapping("/manage")
@Api(value = "后台管理", description = "后台管理")
public class ManagerController extends BaseController{

    private static Logger _log = LoggerFactory.getLogger(ManagerController.class);

    @Autowired
    private SysApiService apiService;

    @ApiOperation(value = "后台首页界面")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap){
        // 当前登录用户权限
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        EduUser user = apiService.selectUserByUsername(username);
        List<EduPermission> permissions = apiService.selectPermissionByUserId(user.getUserId());
        modelMap.put("permissions", permissions);
        modelMap.put("user", user);
        return "/manage/index.jsp";
    }

}
