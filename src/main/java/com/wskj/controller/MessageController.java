package com.wskj.controller;

import com.wskj.dao.MessageDao;
import com.wskj.domain.Group;
import com.wskj.domain.Message;
import com.wskj.domain.User;
import com.wskj.service.GroupService;
import com.wskj.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zhuangjy on 2015/7/17.
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/notifyMessage")
    @ResponseBody
    public int notifyMessage(HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        return messageService.getMessageUnReadNumber(curUser.getId());
    }

    @RequestMapping(value = "/getRencentMessage")
    @ResponseBody
    public List<Message> getRencentMessage(HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        return messageService.getRecentMessage(curUser.getId());
    }

    @RequestMapping(value = "/acceptJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean acceptJohn(HttpSession session, int groupId, String nickName, int messageId) {
        User user = (User) session.getAttribute("curUser");
        groupService.joinGroup(user.getId(), groupId, nickName);
        messageService.setMessageResult(messageId, 1);
        Group group = groupService.getGroupById(groupId);
        messageService.createMessage(group.getGroupBossId(), nickName + "已经加入" + group.getGroupName() + "了", 0);
        return true;
    }

    @RequestMapping(value = "/refuseJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean refuseJoin(HttpSession session, int groupId, int messageId) {
        User user = (User) session.getAttribute("curUser");
        messageService.setMessageResult(messageId, -1);
        Group group = groupService.getGroupById(groupId);
        messageService.createMessage(group.getGroupBossId(), user.getUserName() + "拒绝加入您的小组:" +
                group.getGroupName(), 0);
        return true;
    }

    /**
     * 同意让XX加入小组
     *
     * @param messageId
     * @param session
     * @return
     */
    @RequestMapping(value = "/agreeJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean agreeJoin(int messageId, HttpSession session) {
        //接受请求的是BOSS
        User Boss = (User) session.getAttribute("curUser");
        Message message = messageService.getMessage(messageId);
        // 获取请求加入小组的人的ID
        int userId = message.getMessageFromId();
        String nickName = message.getMessageFrom();
        int groupId = message.getMessageGroupId();
        String groupName = groupService.getGroupName(groupId);
        groupService.joinGroup(userId, groupId, nickName);
        //设置消息结果
        messageService.setMessageResult(messageId, 1);
        //通知对方已经加入小组
        messageService.createMessage(userId, "恭喜您已经加入小组:<font color='red'>" + groupName + "</font>", 0);
        return true;
    }


    @RequestMapping(value = "/disagreeJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean disagreeJoin(int messageId) {
        messageService.setMessageResult(messageId, -1);
        //告知申请者被拒绝
        Message message = messageService.getMessage(messageId);
        int userId = message.getMessageFromId();
        int groupId = message.getMessageGroupId();
        String groupName = groupService.getGroupName(groupId);
        messageService.createMessage(userId, "很遗憾您已被拒绝加入组<font color='red'>" + groupName + "</font>", 0);
        return true;
    }
}
