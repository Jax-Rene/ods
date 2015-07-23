package com.wskj.model;

/**
 * Created by zhuangjy on 2015/7/14.
 */
public class Group {
    private Integer id;
    private String groupName;
    private Integer groupBossId;
    private String groupIcon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupBossId() {
        return groupBossId;
    }

    public void setGroupBossId(Integer groupBossId) {
        this.groupBossId = groupBossId;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }
}
