package com.wskj.dao;

import com.wskj.dao.handler.GroupMapper;
import com.wskj.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuangjy on 2015/7/14.
 */
@Component
public class GroupDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Group getGroup(Integer groupId) {
        String sql = "select * from ods.group where group_id=?";
        Group group = jdbcTemplate.queryForObject(sql, new Object[]{groupId}, new GroupMapper());
        return group;
    }

    public String getGroupName(Integer groupId) {
        String sql = "select group_name from ods.group where group_id = ?";
        System.out.println(sql + groupId);
        return jdbcTemplate.queryForObject(sql, new Object[]{groupId}, String.class);
    }

    public Integer getGroupId(String groupName) {
        String sql = "select group_id from ods.group where group_name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{groupName}, Integer.class);
    }

    public Group createGroup(Integer groupBossId, String groupName) {
        String sql = "insert into ods.group (group_name, group_boss_id) values (?,?)";
        jdbcTemplate.update(sql, groupName, groupBossId);
        System.out.println("创建小组成功,返回小组!");
        return getGroupByName(groupName);
    }

    public Group createGroup(Integer groupBossId, String groupName, String groupIcon) {
        String sql = "insert into ods.group (group_name, group_boss_id,group_icon) values (?,?,?)";
        jdbcTemplate.update(sql, groupName, groupBossId, groupIcon);
        return getGroupByName(groupName);
    }


    public void setNickName(Integer userId, Integer groupId, String nickName) { //设置组昵称
        String sql = "update ods.user_group set nick_name=? where user_id=? and group_id=?";
        jdbcTemplate.update(sql, nickName, userId, groupId);
    }


    public void joinGroup(Integer userId, Integer groupId, String nickName) { //加入小组
        String sql = "insert into ods.user_group(user_id,group_id,nick_name) values(?,?,?)";
        jdbcTemplate.update(sql, userId, groupId, nickName);
    }

    public List<Group> getMyGroup(Integer userId) {//获取我创建的组
        String sql = "select * from ods.group where group_boss_id =?";
        List<Group> mygroup = jdbcTemplate.query(sql, new Object[]{userId}, new GroupMapper());
        return mygroup;
    }

    public List<Group> getUnderGroup(Integer userId) { //获取我从属的组(所有组)
        String sql = "select group_id from ods.user_group where user_id=?";
        List<Integer> groupIds = jdbcTemplate.queryForList(sql, Integer.class, userId);
        List<Group> groups = new ArrayList<Group>();
        for (Integer t : groupIds)
            groups.add(getGroup(t));
        return groups;
    }

    public List<Group> getAllGroup() {
        String sql = "select * from ods.group";
        return jdbcTemplate.query(sql, new Object[]{}, new GroupMapper());
    }


    //由组名获取组
    public Group getGroupByName(String groupName) {
        String sql = "select * from ods.group where group_name =?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{groupName}, new GroupMapper());
        } catch (EmptyResultDataAccessException e) { //找不到小组
            return null;
        }
    }


    //获取nickname
    public String getNickName(int userId, int groupId) {
        String sql = "select nick_name from ods.user_group where user_id=? and group_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, groupId}, String.class);
    }

    //获取小组中所有的昵称
    public List<String> getNickNames(int groupId) {
        String sql = "select nick_name from ods.user_group where group_id=?";
        return jdbcTemplate.queryForList(sql, String.class, new Object[]{groupId});
    }

    //判断该昵称在这组中是否已经存在
    public boolean judgeNickNameExist(String nickName, int groupId) {
        List<Integer> userId = getMemberIds(groupId);
        List<String> nickNames = getNickNames(groupId);
        if (nickNames.indexOf(nickName) != -1) {
            return true;
        }
        return false;
    }


    //获取组长ID
    public Integer getBossId(int groupId) {
        String sql = "select group_boss_id from ods.group where group_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{groupId}, Integer.class);
    }

    //获取组内成员
    public List<Integer> getMemberIds(int groupId) {
        String sql = "select user_id from ods.user_group where group_id=?";
        List<Integer> userIds = jdbcTemplate.queryForList(sql, Integer.class, groupId);
        return userIds;
    }

    public Map<String , String> getMemberIdAndName(int groupId) {
        String sql = "select user_id,nick_name from ods.user_group where group_id = ?";
        return jdbcTemplate.query(sql, new Object[]{groupId}, new ResultSetExtractor<Map<String, String>>() {
            public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, String> map = new HashMap<String, String>();
                while (rs.next()) {
                    map.put(rs.getInt("user_id") + "", rs.getString("nick_name"));
                }
                return map;
            }
        });
    }

    public void exitGroup(int userId,int groupId){
        String sql = "delete from ods.user_group where user_id = ? and group_id = ?";
        jdbcTemplate.update(sql,userId,groupId);
    }

    public void deleteGroup(int groupId){
        String sql = "delete from ods.group where group_id = ?";
        jdbcTemplate.update(sql,groupId);
        sql = "delete from ods.user_group where group_id = ?";
        jdbcTemplate.update(sql,groupId);
    }
}
