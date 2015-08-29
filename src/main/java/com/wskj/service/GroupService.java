package com.wskj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wskj.dao.GroupDao;
import com.wskj.dao.MessageDao;
import com.wskj.domain.Group;
import com.wskj.util.ImageRunnable;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhuangjy on 2015/8/25.
 */
@Service
public class GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private MessageDao messageDao;

    public Group getGroupByName(String groupName) {
        return groupDao.getGroupByName(groupName);
    }

    public Group getGroupById(int groupId) {
        return groupDao.getGroup(groupId);
    }

    public String getGroupName(int groupId) {
        return groupDao.getGroupName(groupId);
    }

    public Group createGroup(int userId, String userName, String groupName, String currentPic, HttpServletRequest request,
                             Integer targetX, Integer targetY, Integer targetW, Integer targetH) {
        Group group = null;
        //没有上传图片生成随机图片
        if (StringUtils.isBlank(currentPic)) {
            Random random = new Random(System.currentTimeMillis());
            final int randomMax = 10;
            int randomNumber = random.nextInt(randomMax);
            String groupIcon = randomNumber + ".jpg";
            group = groupDao.createGroup(userId, groupName, groupIcon);
        } else {
            //获取真实路径
            String logoRealPathDir = request.getSession().getServletContext().getRealPath("/img/icon");
            File logoSaveFile = new File(logoRealPathDir);
            if (!logoSaveFile.exists()) {
                logoSaveFile.mkdir();
            }
            String fileName = logoRealPathDir + File.separator + currentPic;
            File file = new File(fileName);
            new Thread(new ImageRunnable(file,targetX,targetY,targetW,targetH)).start();
            group = groupDao.createGroup(userId, groupName, currentPic);
        }
        int groupId = groupDao.getGroupId(groupName);
        group.setId(groupId);
        //将创建者加入小组
        joinGroup(userId, groupId, userName);
        messageDao.createMessage(userId, "恭喜您创建小组<font color='red'>" + groupName + "</font>成功!", 0);
        return group;
    }

    /**
     * 认真身份
     *
     * @param userId
     * @param groupId
     * @return 0 - boss 1 - member 2 - others
     */
    public int authenticate(int userId, int groupId) {
        Group curGroup = groupDao.getGroup(groupId);
        if (curGroup.getGroupBossId().equals(userId)) {
            return 0;
        } else if (isInGroup(groupId, userId)) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean isInGroup(int groupId, int userId) {
        return (groupDao.getMemberIds(groupId).indexOf(userId) != -1);
    }

    public String getNickName(int userId, int groupId) {
        return groupDao.getNickName(userId, groupId);
    }

    public boolean changeNickName(String newName, int groupId, int userId) {
        if (!groupDao.judgeNickNameExist(newName, groupId)) {
            groupDao.setNickName(userId, groupId, newName);
            return true;
        } else
            return false;
    }

    public void joinGroup(int userId, int groupId, String userName) {
        groupDao.joinGroup(userId, groupId, userName);
    }

    public boolean judgeNickNameExist(String nickName, int groupId) {
        return groupDao.judgeNickNameExist(nickName, groupId);
    }


    public Map<String, Object> getIndexGroupInformation(int userId) {
        List<Group> myGroup = groupDao.getMyGroup(userId);
        List<Group> allGroup = groupDao.getAllGroup();
        Map<String, Object> map = Maps.newHashMap();
        List<String> myGroupBossName = Lists.newArrayList();
        List<String> allGroupBossName = Lists.newArrayList();
        for (Group group : myGroup)
            myGroupBossName.add(groupDao.getNickName(group.getGroupBossId(), group.getId()));
        for (Group group : allGroup)
            allGroupBossName.add(groupDao.getNickName(group.getGroupBossId(), group.getId()));
        map.put("myBossName", myGroupBossName);
        map.put("allBossName", allGroupBossName);
        map.put("myGroup", myGroup);
        map.put("allGroup", allGroup);
        return map;
    }


    public Map<String, String> getMemberIdAndName(int groupId) {
        return groupDao.getMemberIdAndName(groupId);
    }

    public boolean exitGroup(int groupId,int userId){
        String nickName = groupDao.getNickName(userId,groupId);
        groupDao.exitGroup(userId,groupId);
        //通知组长
        Group group = groupDao.getGroup(groupId);
        messageDao.createMessage(group.getGroupBossId(),nickName + "已经离开了" + group.getGroupName() , 0);
        messageDao.createMessage(userId,"您已经离开了" + group.getGroupName() , 0);
        return true;
    }

    public void deleteGroup(int groupId,int userId){
        List<Integer> membersId = groupDao.getMemberIds(groupId);
        String nickName = groupDao.getNickName(userId,groupId);
        String groupName = groupDao.getGroupName(groupId);
        groupDao.deleteGroup(groupId);
        String content = nickName + "已经解散了" + groupName;
        for(Integer id:membersId){
            if(id!=userId)
                messageDao.createMessage(id,content,0);
        }
        //组长的消息和别人不一样
        messageDao.createMessage(userId,"您已经离开了" + groupName,0);
    }


}
