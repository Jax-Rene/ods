package com.wskj.web;


import com.wskj.dao.GroupDao;
import com.wskj.dao.MessageDao;
import com.wskj.dao.UserDao;
import com.wskj.model.Group;
import com.wskj.model.Message;
import com.wskj.model.User;
import com.wskj.tools.ImageUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Random;
import java.util.UUID;

/**
 * Created by zhuangjy on 2015/7/17.
 */
@Controller
public class GroupController {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
    UserDao userDao = (UserDao) ctx.getBean("userDao");
    GroupDao groupDao = (GroupDao) ctx.getBean("groupDao");
    MessageDao messageDao = (MessageDao) ctx.getBean("messageDao");

    @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
    public String createGroup(ModelMap model, HttpServletRequest request,
                              HttpSession httpSession)
            throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile icon = multipartRequest.getFile("newGroupIcon");
        /**获取文件的后缀**/
        String name = multipartRequest.getParameter("newGroupName");

        Group newGroup = groupDao.getGroup(name);
        if (newGroup != null) { //组名已经存在了
            System.out.println("组名存在");
            model.addAttribute("newGroupError", "对不起，组名已经存在，请输入新的组名");
            return "index";
        } else {
            //没有的话，就要通过session传过来的username，通过usertable查找到对应的id。
            //即获得登录用户对应的id
            User user = (User) httpSession.getAttribute("curUser");
            Integer userId = user.getId();
            //判断是否有没有上传头像，写一个上传文件的工具类，将它存在image-icon
            //如果没有上传图片，直接将group_name，group_boss_id写入数据库
            if (icon.isEmpty()) {
                Random random = new Random(System.currentTimeMillis());
                final int randomMax = 11;
                int randomNumber = random.nextInt(randomMax);
                String groupIcon = randomNumber + ".jpg";
                Group curGroup = groupDao.createGroup(userId, name, groupIcon);
            } else {
                /**获取文件的后缀**/
                String suffix = icon.getOriginalFilename().substring
                        (icon.getOriginalFilename().lastIndexOf("."));
                //如果有上传头像，将头像存在服务器中，并重命名，然后将group_name，group_boss_id，group_icon写入数据库
                //限制上传的类型
                if (!(suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".gif"))) {
                    model.addAttribute("newGroupError", "请上传jpg、png、gif格式的图片");
                    return "index";
                } else {
                    //开始上传文件
                    /**得到图片保存目录的真实路径**/
                    String logoRealPathDir = request.getSession().getServletContext().getRealPath("/img/icon");
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
                    //文件上传结束，写入数据库
                    groupDao.createGroup(userId, name, logImageName);
                }
            }
            //创建组完将自己加入这个小组的关联默认nickname为用户名
            Group curGroup = groupDao.getGroup(name);
            groupDao.joinGroup(user.getId(), curGroup.getId(), user.getUserName());
            //消息通知 - 创建成功
            messageDao.createMessage(userId, "恭喜您创建小组<font color='red'>" + name + "</font>成功!", 0);
            //获取group并且加入request中
            model.addAttribute("group", curGroup);
            //标志为组长
            model.addAttribute("boss", "true");
            model.addAttribute("nickName", user.getUserName());
            return "groupindex";
        }
    }


    @RequestMapping(value = "/getGroupInfo", method = RequestMethod.GET)
    public String getGroupInfo(String groupId, HttpSession session, ModelMap model) {
        User curUser = (User) session.getAttribute("curUser");
        int gId = Integer.parseInt(groupId);
        Group curGroup = groupDao.getGroup(gId);
        if (curGroup.getGroupBossId().equals(curUser.getId())) {
            model.addAttribute("boss", "true"); //是组长
        } else { //不是组长判断是不是组员
            if (groupDao.getMemberIds(gId).indexOf(curUser.getId()) != -1)
                model.addAttribute("member", "true");
        }
        //获取昵称
        try {
            String nickName = groupDao.getNickName(curUser.getId(), gId);
            model.addAttribute("nickName", nickName);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        model.addAttribute("group", curGroup);
//        //获取组内成员
//        List<Integer> userIds = groupDao.getMemberIds(gId);
//        List<User> users = new ArrayList<User>();
//        List<String> nickNames = new ArrayList<String>();
//        for (Integer t : userIds) {
//            users.add(userDao.getUser(t));
//            nickNames.add(groupDao.getNickName(t, gId));
//        }
        return "groupindex";
    }


    @RequestMapping(value = "/changeNickName", method = RequestMethod.GET)
    @ResponseBody
    public boolean changeNickName(String newName, int groupId, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        if (groupDao.judgeNickNameExist(newName, groupId)) { //判断是否重名
            groupDao.setNickName(curUser.getId(), groupId, newName);
            return true;
        } else
            return false;
    }

    /**
     * 邀请XX进入组
     *
     * @param memberName 被邀请人用户名
     * @param groupId    邀请的小组
     * @param session
     * @return
     */
    @RequestMapping(value = "/inviteMember", method = RequestMethod.GET)
    @ResponseBody
    public Integer inviteMember(String memberName, int groupId, HttpSession session) {
        User curUser = (User) session.getAttribute("curUser");
        User targetUser = userDao.getUser(memberName);
        if (targetUser == null)
            return 2; //找不到用户
        //先检查对方是否已经是组员
        if (groupDao.getMemberIds(groupId).indexOf(targetUser.getId()) != -1) {
            return 1; //目标用户已经在小组中 抛出1
        }
        String groupName = groupDao.getGroupName(groupId);
        String nickName = groupDao.getNickName(curUser.getId(), groupId);
        String message = "邀请您进入小组<font color='red'>" + groupName + "</font>";
        messageDao.createMessage(targetUser.getId(), message, 1, groupId, nickName, curUser.getId());
        return 0; //成功返回0
    }


    @RequestMapping(value = "/acceptJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean acceptJohn(HttpSession session, int groupId, String nickName, int messageId) {
        try {
            User user = (User) session.getAttribute("curUser");
            groupDao.joinGroup(user.getId(), groupId, nickName);
            //设置消息结果
            messageDao.setResult(messageId, 1);
            //告知组长，XXX加入了
            Group group = groupDao.getGroup(groupId);
            messageDao.createMessage(group.getGroupBossId(), nickName + "已经加入" + group.getGroupName() + "了", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/refuseJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean refuseJoin(HttpSession session, int groupId, String nickName, int messageId) {
        try {
            User user = (User) session.getAttribute("curUser");
            groupDao.joinGroup(user.getId(), groupId, nickName);
            messageDao.setResult(messageId, -1);
            //告知邀请者，XX拒绝加入
            Group group = groupDao.getGroup(groupId);
            messageDao.createMessage(group.getGroupBossId(), user.getUserName() + "拒绝加入您的小组:" + group.getGroupName(), 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 搜索小组,并且申请加入
     *
     * @param groupName 小组名称
     * @param session
     * @param nickName  自己的小组昵称（用于验证)
     * @return
     */
    @RequestMapping(value = "/searchGroup", method = RequestMethod.GET)
    @ResponseBody
    public int searchGroup(String groupName, HttpSession session, String nickName) {
        //检查自己是否已经属于该小组了
        Group group = (Group) groupDao.getGroupByName(groupName);
        User user = (User) session.getAttribute("curUser");
        if (group == null) //找不到该组
            return 1;
        if (groupDao.getMemberIds(group.getId()).indexOf(user.getId()) != -1) {
            return 0; //已经在组中
        }
        //将加入小组的请求发向组长
        messageDao.createMessage(group.getGroupBossId(), "请求加入您的小组:" + groupName, 2, group.getId(), nickName, user.getId());
        return 2;
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
        try {
            //接受请求的是BOSS
            User Boss = (User) session.getAttribute("curUser");
            Message message = messageDao.getMessage(messageId);
            int userId = message.getMessageFromId(); // 获取请求加入小组的人的ID
            String nickName = message.getMessageFrom();
            int groupId = message.getMessageGroupId();
            String groupName = groupDao.getGroupName(groupId);
            groupDao.joinGroup(userId, groupId, nickName);
            //设置消息结果
            messageDao.setResult(messageId, 1);
            //通知对方已经加入小组
            messageDao.createMessage(userId, "恭喜您已经加入小组:<font color='red'>" + groupName + "</font>", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/disagreeJoin", method = RequestMethod.GET)
    @ResponseBody
    public boolean disagreeJoin(int messageId) {
        try {
            messageDao.setResult(messageId, -1);
            //告知申请者被拒绝
            Message message = messageDao.getMessage(messageId);
            int userId = message.getMessageFromId();
            int groupId = message.getMessageGroupId();
            System.out.println(groupId);
            String groupName = groupDao.getGroupName(groupId);
            messageDao.createMessage(userId, "很遗憾您已被拒绝加入组<font color='red'>" + groupName + "</font>", 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "/addGroup", method = RequestMethod.GET)
    @ResponseBody
    public boolean addGroup(int groupId, String nickName, HttpSession session) {
        //检查昵称是否已经存在
        if (groupDao.judgeNickNameExist(nickName, groupId)) {
            User user = (User) session.getAttribute("curUser");
            Group group = groupDao.getGroup(groupId);
            groupDao.joinGroup(user.getId(), groupId, nickName);
            //将加入小组的请求发向组长
            messageDao.createMessage(group.getGroupBossId(), "请求加入您的小组:" + group.getGroupName(), 2, group.getId(),
                    nickName, user.getId());
            return true;
        } else
            return false;
    }


    @RequestMapping(value = "/restoreTempPic", method = RequestMethod.POST)
    @ResponseBody
    public String restoreTempPic(HttpServletRequest request) throws Exception {
        System.out.println("da");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile icon = multipartRequest.getFile("newGroupIcon");
        String suffix = icon.getOriginalFilename().substring(icon.getOriginalFilename().lastIndexOf("."));
        if (!(suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".gif")))
            return null;
        //得到图片保存目录的真实路径
        String logoRealPathDir = request.getSession().getServletContext().getRealPath("/img/temp");
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
}
