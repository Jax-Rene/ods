package com.wskj.dao;

import com.wskj.dao.handler.GroupMapper;
import com.wskj.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zhuangjy on 2015/7/14.
 */
@Component
public class GroupDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Group getGroup(Integer groupId){
        String sql = "select * from ods.group where group_id=?";
        Group group = jdbcTemplate.queryForObject(sql,new Object[]{groupId},new GroupMapper());
        return group;
    }

    public String getGroupName(Integer groupId){
        String  sql = "select group_name from ods.group where group_id = ?";
        System.out.println(sql + groupId);
        return jdbcTemplate.queryForObject(sql,new Object[]{groupId},String.class);
    }

    public Group getGroup(String groupName){
        String sql = "select * from ods.group where group_name = ?";
        try {
            Group group = jdbcTemplate.queryForObject(sql,
                    new Object[]{groupName}, new GroupMapper());
            return group;
        }catch (EmptyResultDataAccessException e){ //找不到组
            return null;
        }
    }


    public Integer getGroupId(String groupName){
        String sql = "select group_id from ods.group where group_name = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{groupName},Integer.class);
    }

    public Group createGroup(Integer groupBossId,String groupName){
        String sql = "insert into ods.group (group_name, group_boss_id) values (?,?)";
        jdbcTemplate.update(sql, groupName, groupBossId);
        System.out.println("创建小组成功,返回小组!");
        return getGroupByName(groupName);
    }

    public Group createGroup(Integer groupBossId, String groupName ,String groupIcon){
        String sql = "insert into ods.group (group_name, group_boss_id,group_icon) values (?,?,?)";
        jdbcTemplate.update(sql,groupName,groupBossId,groupIcon);
        System.out.println("创建小组(插入图片)成功!返回小组");
        return getGroupByName(groupName);
    }



    public void setNickName(Integer userId,Integer groupId,String nickName){ //设置组昵称
        String sql = "update ods.user_group set nick_name=? where user_id=? and group_id=?";
        jdbcTemplate.update(sql,nickName,userId,groupId);
        System.out.println("更改昵称为 - " + nickName);
        return;
    }

    public void joinGroup(Integer userId,Integer groupId,String nickName){ //加入小组
        String sql = "insert into ods.user_group(user_id,group_id,nick_name) values(?,?,?)";
        jdbcTemplate.update(sql,userId,groupId,nickName);
        System.out.println(userId + "加入小组:" + groupId +"成功！");
        return;
    }

    public List<Group> getMyGroup(Integer userId){//获取我创建的组
        String sql = "select * from ods.group where group_boss_id =?";
        List<Group> mygroup = jdbcTemplate.query(sql,new Object[]{userId},new GroupMapper());
        return mygroup;
    }

    public List<Group> getUnderGroup(Integer userId){ //获取我从属的组(所有组)
        String sql ="select group_id from ods.user_group where user_id=?";
        List<Integer> groupIds = jdbcTemplate.queryForList(sql,Integer.class,userId);
        List<Group> groups = new ArrayList<Group>();
        for(Integer t:groupIds)
            groups.add(getGroup(t));
        return groups;
    }


    public Group getGroupByName(String groupName){ //由组名获取组
        String sql = "select * from ods.group where group_name =?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{groupName}, new GroupMapper());
        }catch(EmptyResultDataAccessException e){ //找不到小组
            return null;
        }
    }


    //获取nickname
    public String getNickName(int userId , int groupId){
        String sql = "select nick_name from ods.user_group where user_id=? and group_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, groupId}, String.class);
    }


    //获取组长ID
    public Integer getBossId(int groupId){
        String sql = "select group_boss_id from ods.group where group_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{groupId},Integer.class);
    }

    //获取组内成员
    public List<Integer> getMemberIds(int groupId){
        String sql = "select user_id from ods.user_group where group_id=?";
        List<Integer> userIds = jdbcTemplate.queryForList(sql,Integer.class,groupId);
        return userIds;
    }
}
