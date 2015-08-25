package com.wskj.dao.handler;

import com.wskj.domain.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhuangjy on 2015/7/14.
 */
public class GroupMapper implements RowMapper<Group> {
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(rs.getInt("group_id"));
        group.setGroupBossId(rs.getInt("group_boss_id"));
        group.setGroupName(rs.getString("group_name"));
        group.setGroupIcon(rs.getString("group_icon"));
        return group;
    }
}
