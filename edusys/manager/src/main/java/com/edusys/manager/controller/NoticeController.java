package com.edusys.manager.controller;

import com.edu.common.base.BaseController;
import com.edu.common.dao.model.EduAdvert;
import com.edu.common.dao.model.EduAdvertExample;
import com.edu.common.dao.model.EduNotice;
import com.edu.common.dao.model.EduNoticeExample;
import com.edusys.manager.common.SysResult;
import com.edusys.manager.common.SysResultConstant;
import com.edusys.manager.service.EduNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Gary on 2017/8/17.
 * 通知消息管理
 *
 */
@Controller
@Api(value = "通知消息管理", description = "通知消息管理")
@RequestMapping("/manage/notice")
public class NoticeController extends BaseController {

    private static Logger _log = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private EduNoticeService noticeService;

    @ApiOperation("通知消息管理首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "/manage/notice/index.jsp";
    }

    @ApiOperation("通知消息管理列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
                       @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       String search){
        EduNoticeExample noticeExample = new EduNoticeExample();
        EduNoticeExample.Criteria criteria = noticeExample.createCriteria();
        noticeExample.setOffset(offset);
        noticeExample.setLimit(limit);
        noticeExample.setOrderByClause("id desc");
        //模糊查询
        if(StringUtils.isNotBlank(search)){
            search = "%"+search+"%";
            criteria.andContentLike(search);
        }

        List<EduNotice> rows = noticeService.selectByExample(noticeExample);
        long total = noticeService.countByExample(noticeExample);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    @ApiOperation("公告新增页面")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(){
        return "/manage/notice/create.jsp";
    }

    @ApiOperation("公告新增处理操作")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(EduNotice notice){
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        notice.setCreator(username);
        notice.setCreateTime(new Date());
        int count = noticeService.insertSelective(notice);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation("公告修改页面")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") int id, ModelMap map){
        EduNotice notice = noticeService.selectByPrimaryKey(id);
        map.put("notice", notice);
        return "/manage/notice/update.jsp";
    }

    @ApiOperation("公告修改处理操作")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable("id") int id, EduNotice notice){
        notice.setId(id);
        int count = noticeService.updateByPrimaryKeySelective(notice);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }

    @ApiOperation(value = "删除公告")
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
        EduNoticeExample noticeExample = new EduNoticeExample();
        EduNoticeExample.Criteria criteria = noticeExample.createCriteria();
        criteria.andIdIn(delIds);
        int count = noticeService.deleteByExample(noticeExample);
        return new SysResult(SysResultConstant.SUCCESS, count);
    }
}
