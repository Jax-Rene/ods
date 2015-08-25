package com.wskj.controller;

import com.wskj.domain.User;
import com.wskj.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by zhuangjy on 2015/7/15.
 */
@Controller
public class IndexController {
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/getGroupInformation", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getGroupInformation(HttpSession session) {
        User user = (User) session.getAttribute("curUser");
        return groupService.getIndexGroupInformation(user.getId());
    }
}
