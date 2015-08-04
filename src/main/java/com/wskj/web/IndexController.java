package com.wskj.web;

import com.wskj.dao.GroupDao;
import com.wskj.model.Group;
import com.wskj.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by zhuangjy on 2015/7/15.
 */
@Controller(value = "/index")
public class IndexController{
    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
    GroupDao groupDao = (GroupDao) ctx.getBean("groupDao");


//    @RequestMapping(value = "/getMyGroup", method = RequestMethod.GET)
//    @ResponseBody
//    public List<Group> getMyGroup(ModelMap model,HttpSession session){
//        User user = (User)session.getAttribute("curUser");
//        //获取所在组以及
//        List<Group> myGroup = groupDao.getMyGroup(user.getId());
//        if(myGroup.isEmpty()){//没有小组
//            model.addAttribute("noMyGroup","您还未创建过任何小组");
//            return null;
//        }else
//        return myGroup;
//    }
//
//    @RequestMapping(value ="/getAllGroup",method = RequestMethod.GET)
//    @ResponseBody
//    public List<Group> getAllGroup(ModelMap model ,HttpSession session){
//        List<Group> allGroup = groupDao.getAllGroup();
//        //没有小组
//        if(allGroup.isEmpty()){
//            model.addAttribute("noGroup","暂无小组");
//            return null;
//        }else
//            return allGroup;
//    }

    @RequestMapping(value = "/getGroupInformation",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getGroupInformation(HttpSession session){
        User user = (User)session.getAttribute("curUser");
        List<Group> myGroup = groupDao.getMyGroup(user.getId());
        List<Group> allGroup = groupDao.getAllGroup();
        Map<String,Object> map = new HashMap<String, Object>();
        List<String> myGroupBossName = new ArrayList<String>();
        List<String> allGroupBossName = new ArrayList<String>();
        for(Group group:myGroup)
            myGroupBossName.add(groupDao.getNickName(group.getGroupBossId(),group.getId()));
        for(Group group:allGroup)
            allGroupBossName.add(groupDao.getNickName(group.getGroupBossId(),group.getId()));
        map.put("myBossName",myGroupBossName);
        map.put("allBossName",allGroupBossName);
        map.put("myGroup",myGroup);
        map.put("allGroup",allGroup);
        return map;
    }
}
