package com.edusys.front.controller;

import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduNotice;
import com.edu.common.dao.model.EduNoticeExample;
import com.edu.common.dao.model.EduStudent;
import com.edu.common.util.Paginator;
import com.edusys.front.service.EduNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Gary on 2017/9/7.
 * 公告列表
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {

    private static Logger _log = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private EduNoticeService noticeService;

    /**
     * 公告列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "1", value = "page") int page,
                       @RequestParam(required = false, defaultValue = "6", value = "limit") int limit,
                       @RequestParam(required = false, value = "sort") String sort,
                       @RequestParam(required = false, value = "order") String order,
                       HttpServletRequest request, ModelMap modelMap){
        Integer offset = (page-1)*limit;

        HttpSession session = request.getSession();
        EduStudent student = (EduStudent) session.getAttribute("user");

        EduNoticeExample noticeExample = new EduNoticeExample();

        noticeExample.setOrderByClause("id desc");
        noticeExample.setLimit(limit);
        noticeExample.setOffset(offset);
        int organId = 0;
        if(student!=null){
            organId = student.getOrganizationId2();
            noticeExample.or(noticeExample.createCriteria().andOrganIdEqualTo(0));
            noticeExample.or(noticeExample.createCriteria().andOrganIdEqualTo(organId));
        }else{
            noticeExample.createCriteria().andOrganIdEqualTo(0);
        }

        List<EduNotice> list = noticeService.selectByExample(noticeExample);
        long total = noticeService.countByExample(noticeExample);

        Paginator paginator = new Paginator(total, page, limit, request);
        String htmlstr = paginator.getHtml();
        modelMap.put("list", list);
        modelMap.put("pageHtml", htmlstr);
        modelMap.put("total", total);
        return "/notice-list.jsp";
    }
}
