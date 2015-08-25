package com.wskj.controller;

import com.wskj.domain.Group;
import com.wskj.domain.User;
import com.wskj.service.GroupService;
import com.wskj.service.MessageService;
import com.wskj.service.UserService;
import com.wskj.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhuangjy on 2015/7/17.
 */
@Controller
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
    public String createGroup(ModelMap model, HttpServletRequest request, HttpSession session,
                              int targetX, int targetY, int targetW, int targetH, String currentPic)
            throws Exception {
        String newGroupName = request.getParameter("newGroupName");
        Group newGroup = groupService.getGroupByName("newGroupName");
        User user = (User) session.getAttribute("curUser");
        Group curGroup = groupService.createGroup(user.getId(), user.getUserName(), newGroupName, currentPic,
                request, targetX, targetY, targetW, targetH);
        model.addAttribute("group", curGroup);
        model.addAttribute("boss", "true");
        model.addAttribute("nickName", user.getUserName());
        return "group_index";
    }


    @RequestMapping(value = "/getGroupInfo", method = RequestMethod.GET)
    public String getGroupInfo(int groupId, HttpSession session, ModelMap model) {
        User curUser = (User) session.getAttribute("curUser");
        int authentic = groupService.authenticate(curUser.getId(), groupId);
        Group group = groupService.getGroupById(groupId);
        switch (authentic) {
            case 0:
                model.addAttribute("boss", "true");
                break;
            case 1:
                model.addAttribute("member", "true");
                break;
            default:
                break;
        }
        //获取昵称
        if (authentic == 0 || authentic == 1) {
            String nickName = groupService.getNickName(curUser.getId(), groupId);
            model.addAttribute("nickName", nickName);
        }
        model.addAttribute("group", group);
//        //获取组内成员
//        List<Integer> userIds = groupDao.getMemberIds(gId);
//        List<User> users = new ArrayList<User>();
//        List<String> nickNames = new ArrayList<String>();
//        for (Integer t : userIds) {
//            users.add(userDao.getUser(t));
//            nickNames.add(groupDao.getNickName(t, gId));
//        }
        return "group_index";
    }


    @RequestMapping(value = "/changeNickName", method = RequestMethod.GET)
    @ResponseBody
    public boolean changeNickName(String newName, int groupId, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        return groupService.changeNickName(newName, groupId, curUser.getId());
    }

    /**
     * 邀请XX进入组
     *
     * @param memberName 被邀请人用户名
     * @param groupId    邀请的小组
     * @param session
     * @return 2 - 不存在的用户 1 - 已经在组内 0 - 成功
     */
    @RequestMapping(value = "/inviteMember", method = RequestMethod.GET)
    @ResponseBody
    public Integer inviteMember(String memberName, int groupId, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        User targetUser = userService.getUser(memberName);
        //找不到用户
        if (targetUser == null)
            return 2;
        //先检查对方是否已经是组员
        if (groupService.isInGroup(groupId, targetUser.getId()))
            return 1;
        String groupName = groupService.getGroupName(groupId);
        String nickName = groupService.getNickName(curUser.getId(), groupId);
        String message = "邀请您进入小组<font color='red'>" + groupName + "</font>";
        messageService.createMessage(targetUser.getId(), message, 1, groupId, nickName, curUser.getId());
        return 0;
    }


    /**
     * 判断搜索的小组信息,如果找到了跳转到搜索页面
     *
     * @param groupName 小组名称
     * @param session
     * @return 搜索的页面
     */
    @RequestMapping(value = "/searchGroup", method = RequestMethod.GET)
    @ResponseBody
    public boolean searchGroup(String groupName, HttpSession session, String nickName) {
        if (StringUtils.isBlank(groupName))
            return false;
        Group group = groupService.getGroupByName(groupName);
        if (group == null)
            return false;
        return true;
    }


    @RequestMapping(value = "/addGroup", method = RequestMethod.GET)
    @ResponseBody
    public boolean addGroup(int groupId, String nickName, HttpSession session) {
        //检查昵称是否已经存在
        if (!groupService.judgeNickNameExist(nickName, groupId)) {
            User user = (User) session.getAttribute("curUser");
            Group group = groupService.getGroupById(groupId);
            groupService.joinGroup(user.getId(), groupId, nickName);
            //将加入小组的请求发向组长
            messageService.createMessage(group.getGroupBossId(), "请求加入您的小组:" + group.getGroupName(), 2, group.getId(),
                    nickName, user.getId());
            return true;
        } else
            return false;
    }


    @RequestMapping(value = "/restoreTempPic", method = RequestMethod.POST)
    @ResponseBody
    public String restoreTempPic(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile icon = multipartRequest.getFile("newGroupIcon");
        String suffix = icon.getOriginalFilename().substring(icon.getOriginalFilename().lastIndexOf("."));
        if (!(suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".gif")))
            return null;
        //得到图片保存目录的真实路径
        String logoRealPathDir = request.getSession().getServletContext().getRealPath("/img/icon");
        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdir();
        }
        String logImageName = UUID.randomUUID().toString() + suffix;//构建文件名称
        //拼成完整的文件保存路径加文件
        String fileName = logoRealPathDir + File.separator + logImageName;
        File file = new File(fileName);
        //缩放图片
        icon.transferTo(file);
        ImageUtil.zoomImage(file);
        return logImageName;
    }

    @RequestMapping(value = "gotoCreateGroup", method = RequestMethod.GET)
    public String gotoCreateGroup() {
        return "create_group";
    }

    @RequestMapping(value = "/getMemberIdAndName", method = RequestMethod.GET)
    @ResponseBody
    public Map<Integer, String> getMemberIdAndName(int groupId) {
        return groupService.getMemberIdAndName(groupId);
    }
}
