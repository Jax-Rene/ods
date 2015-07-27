package com.wskj.web;


import com.wskj.dao.GroupJDBCTemplate;
import com.wskj.dao.MessageJDBCTemplate;
import com.wskj.dao.UserJDBCTemplate;
import com.wskj.model.Group;
import com.wskj.model.Message;
import com.wskj.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by youjm on 2015/7/17.
 */
@Controller
public class GroupController {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
    UserJDBCTemplate userJDBCTemplate = (UserJDBCTemplate) context.getBean("userJDBCTemplate");
    GroupJDBCTemplate groupJDBCTemplate = (GroupJDBCTemplate) context.getBean("groupJDBCTemplate");
    MessageJDBCTemplate messageJDBCTemplate = (MessageJDBCTemplate) context.getBean("messageJDBCTemplate");

    @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
    public String createGroup(ModelMap model, HttpServletRequest request,
                              HttpSession httpSession)
            throws Exception {
        //enctype="multipart/form-data"方式提交表单，获得参数的方法
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile icon = multipartRequest.getFile("newGroupIcon");
        /**获取文件的后缀**/
        String suffix = icon.getOriginalFilename().substring
                (icon.getOriginalFilename().lastIndexOf("."));
        String name = multipartRequest.getParameter("newGroupName");

        Group newGroup = groupJDBCTemplate.getGroup(name);
        if (newGroup != null) { //组名已经存在了
            model.addAttribute("newGroupError", "对不起，组名已经存在，请输入新的组名");
            return "groupindex";
        } else {
            //没有的话，就要通过session传过来的username，通过usertable查找到对应的id。
            //即获得登录用户对应的id
            User user = (User) httpSession.getAttribute("curUser");
            Integer userId = user.getId();
            //判断是否有没有上传头像，写一个上传文件的工具类，将它存在image-icon
            //如果没有上传图片，直接将group_name，group_boss_id写入数据库
            if (icon.isEmpty()) {
                System.out.println("这里没有上传图像");
                Group curGroup = groupJDBCTemplate.createGroup(userId, name);
            } else {
                //如果有上传头像，将头像存在服务器中，并重命名，然后将group_name，group_boss_id，group_icon写入数据库
                //限制上传的类型
                if (!(suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".gif"))) {
                    model.addAttribute("newGroupError", "请上传jpg、png、gif格式的图片");
                    return "index";
                } else {
                    //开始上传文件
                    /**得到图片保存目录的真实路径**/
                    String logoRealPathDir = request.getSession().getServletContext().getRealPath("/image/icon");
                    System.out.println("tomcat真实路径" + logoRealPathDir);
                    /**根据真实路径创建目录,如果不存在，创建它**/
                    File logoSaveFile = new File(logoRealPathDir);
                    if (!logoSaveFile.exists()) {
                        System.out.println("文件夹不存在");
                        logoSaveFile.mkdir();
                        System.out.println("创建文件夹成功!");
                    }
                    /**使用UUID生成随机文件名称**/
                    String logImageName = UUID.randomUUID().toString() + suffix;//构建文件名称
                    /**拼成完整的文件保存路径加文件**/
                    String fileName = logoRealPathDir + File.separator + logImageName;
                    File file = new File(fileName);
                    icon.transferTo(file);
                    System.out.println(fileName);
                    System.out.println(logImageName);
                    //文件上传结束，写入数据库
                    groupJDBCTemplate.createGroup(userId, name, logImageName);
                }
            }
            //创建组完将自己加入这个小组的关联默认nickname为用户名
            Group curGroup = groupJDBCTemplate.getGroup(name);
            groupJDBCTemplate.joinGroup(user.getId(), curGroup.getId(), user.getUserName());
            //消息通知 - 创建成功
            messageJDBCTemplate.createMessage(userId, "恭喜您创建小组<font color='red'>" + name + "</font>成功!", 0);
            //获取group并且加入request中
            model.addAttribute("group", curGroup);
            //标志为组长
            model.addAttribute("boss","true");
            //将自己加入List中
            List<User> users = new ArrayList<User>();
            List<String> nicknames  = new ArrayList<String>();
            users.add(user);
            nicknames.add(user.getUserName());
            model.addAttribute("members",users);
            model.addAttribute("nicknames",nicknames);
            return "groupindex";
        }
    }


    @RequestMapping(value = "/getGroupInfo" ,method = RequestMethod.GET)
    public String getGroupInfo(String groupId,HttpSession session,ModelMap model){
        User curUser = (User)session.getAttribute("curUser");
        int gId = Integer.parseInt(groupId);
        Group curGroup = groupJDBCTemplate.getGroup(gId);
        if(curGroup.getGroupBossId().equals(curUser.getId())){
            model.addAttribute("boss","true"); //是组长
        }
        model.addAttribute("group",curGroup);
        //获取组内成员
        List<Integer> userIds = groupJDBCTemplate.getMemberIds(gId);
        List<User> users = new ArrayList<User>();
        List<String> nickNames = new ArrayList<String>();
        for(Integer t:userIds){
            users.add(userJDBCTemplate.getUser(t));
            nickNames.add(groupJDBCTemplate.getNickName(t,gId));
        }

        model.addAttribute("nicknames",nickNames);
        model.addAttribute("members",users);
        return "groupindex";
    }


    @RequestMapping(value = "/changeNickName", method = RequestMethod.GET)
    @ResponseBody
    public boolean changeNickName(String newName, int groupId, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        try {
            groupJDBCTemplate.setNickName(curUser.getId(), groupId, newName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 邀请XX进入组
     * @param memberName 被邀请人用户名
     * @param groupId 邀请的小组
     * @param session
     * @return
     */
    @RequestMapping(value = "/inviteMember", method = RequestMethod.GET)
    @ResponseBody
    public boolean inviteMember(String memberName, int groupId, HttpSession session) {
        try {
            User curUser = (User) session.getAttribute("curUser");
            User targetUser = userJDBCTemplate.getUser(memberName);
            String groupName = groupJDBCTemplate.getGroupName(groupId);
            String nickName = groupJDBCTemplate.getNickName(curUser.getId(), groupId);
            String message = "邀请您进入小组<font color='red'>" + groupName + "</font>";
            messageJDBCTemplate.createMessage(targetUser.getId(), message, 1, groupId, nickName,curUser.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @RequestMapping(value = "/acceptJoin" ,method = RequestMethod.GET)
    @ResponseBody
    public boolean acceptJohn(HttpSession session,int groupId,String nickName,int messageId){
        try {
            User user = (User) session.getAttribute("curUser");
            groupJDBCTemplate.joinGroup(user.getId(),groupId,nickName);
            //设置消息结果
            messageJDBCTemplate.setResult(messageId,1);
            //告知组长，XXX加入了
            Group group = groupJDBCTemplate.getGroup(groupId);
            messageJDBCTemplate.createMessage(group.getGroupBossId(),nickName + "已经加入" + group.getGroupName() +"了",0);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/refuseJoin" ,method = RequestMethod.GET)
    @ResponseBody
    public boolean refuseJoin(HttpSession session,int groupId,String nickName,int messageId){
        try {
            User user = (User) session.getAttribute("curUser");
            groupJDBCTemplate.joinGroup(user.getId(),groupId,nickName);
            messageJDBCTemplate.setResult(messageId, -1);
            //告知邀请者，XX拒绝加入
            Group group = groupJDBCTemplate.getGroup(groupId);
            messageJDBCTemplate.createMessage(group.getGroupBossId(),user.getUserName() + "拒绝加入您的小组:" + group.getGroupName(),0);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 搜索小组,并且申请加入
     * @param groupName 小组名称
     * @param session
     * @param nickName 自己的小组昵称（用于验证)
     * @return
     */
    @RequestMapping(value = "/searchGroup" ,method = RequestMethod.GET)
    @ResponseBody
    public boolean searchGroup(String groupName,HttpSession session,String nickName) {
        Group group = (Group) groupJDBCTemplate.getGroupByName(groupName);
        User user = (User) session.getAttribute("curUser");
        if (group == null) //找不到该组
            return false;
        //将加入小组的请求发向组长
        messageJDBCTemplate.createMessage(group.getGroupBossId(), "请求加入您的小组:" + groupName, 2, group.getId(), nickName ,user.getId());
        return true;
    }

    /**
     * 同意让XX加入小组
     * @param messageId
     * @param session
     * @return
     */
    @RequestMapping(value = "/agreeJoin" ,method = RequestMethod.GET)
    @ResponseBody
    public boolean agreeJoin(int messageId,HttpSession session){
        try {
            //接受请求的是BOSS
            User Boss = (User) session.getAttribute("curUser");
            Message message = messageJDBCTemplate.getMessage(messageId);
            int userId = message.getMessageFromId(); // 获取请求加入小组的人的ID
            String nickName = message.getMessageFrom();
            int groupId = message.getMessageGroupId();
            String groupName = groupJDBCTemplate.getGroupName(groupId);
            groupJDBCTemplate.joinGroup(userId, groupId, nickName);
            //设置消息结果
            messageJDBCTemplate.setResult(messageId,1);
            //通知对方已经加入小组
            messageJDBCTemplate.createMessage(userId,"恭喜您已经加入小组:<font color='red'>" + groupName + "</font>",0);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/disagreeJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean disagreeJoin(int messageId){
        try {
            messageJDBCTemplate.setResult(messageId, -1);
            //告知申请者被拒绝
            Message message = messageJDBCTemplate.getMessage(messageId);
            int userId = message.getMessageFromId();
            int groupId = message.getMessageGroupId();
            System.out.println(groupId);
            String groupName = groupJDBCTemplate.getGroupName(groupId);
            messageJDBCTemplate.createMessage(userId, "很遗憾您已被拒绝加入组<font color='red'>" + groupName + "</font>", 0);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
