package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.*;
import com.edu.common.util.PropertiesFileUtil;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduAdvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Gary on 2017/8/17.
 * 广告轮询图管理
 */
@Controller
@Api(value = "广告轮询图管理", description = "广告轮询图管理")
@RequestMapping("/manage/advert")
public class AdvertController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(AdvertController.class);

    @Autowired
    private EduAdvertService advertService;

    @ApiOperation("广告轮询图管理首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/manage/advert/index.jsp";
    }

    @ApiOperation("广告轮询图列表管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search){
        EduAdvertExample advertExample = new EduAdvertExample();
        EduAdvertExample.Criteria criteria = advertExample.createCriteria();
        advertExample.setOffset(offset);
        advertExample.setLimit(limit);
        advertExample.setOrderByClause("id desc");
        //模糊查询
        if(StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            criteria.andImgurlLike(search);
        }

        List<EduAdvert> rows = advertService.selectByExample(advertExample);
        long total = advertService.countByExample(advertExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("新增图片")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(){
        return "/manage/advert/create.jsp";
    }

    @ApiOperation("新增图片处理操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(HttpServletRequest request, EduAdvert advert){
        //取得上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        CommonsMultipartFile img = (CommonsMultipartFile)multipartRequest.getFile("img");

        PropertiesFileUtil propertiesFileUtil = PropertiesFileUtil.getInstance("config");
        String realPath = propertiesFileUtil.get("indexDir");
        System.out.println(realPath);
        if(img.getSize() > 0){
            String imgName = "img" + new Date().getTime() + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf("."), img.getOriginalFilename().length());
            ;
            try {
                FileUtils.copyInputStreamToFile(img.getInputStream(), new File(realPath + "/img/", imgName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            advert.setImgurl("/index/img/" + imgName);
        }
        int count = advertService.insertSelective(advert);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("修改图片页面")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap map){

        EduAdvert advert = advertService.selectByPrimaryKey(id);
        map.put("advert", advert);
        return "/manage/advert/update.jsp";
    }

    @ApiOperation("修改处理操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduAdvert advert, HttpServletRequest request){
        advert.setId(id);

        //取得上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        CommonsMultipartFile img = (CommonsMultipartFile)multipartRequest.getFile("img");

        PropertiesFileUtil propertiesFileUtil = PropertiesFileUtil.getInstance("config");
        String realPath = propertiesFileUtil.get("indexDir");

        if(img.getSize() > 0){
            String imgName = "img" + new Date().getTime() + img.getOriginalFilename().substring(img.getOriginalFilename().indexOf("."), img.getOriginalFilename().length());
            ;
            try {
                FileUtils.copyInputStreamToFile(img.getInputStream(), new File(realPath + "/img/", imgName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            advert.setImgurl("/index/img/" + imgName);
        }

        int count = advertService.updateByPrimaryKeySelective(advert);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除图片")
    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("ids") String ids) {
        String[] idArray = ids.split("-");
        List<Integer> delIds = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            delIds.add(Integer.parseInt(idStr));
        }
        EduAdvertExample advertExample = new EduAdvertExample();
        EduAdvertExample.Criteria criteria = advertExample.createCriteria();
        criteria.andIdIn(delIds);
        int count = advertService.deleteByExample(advertExample);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }
}
