package com.wskj.tools;

import java.sql.Timestamp;
import java.util.Date;
import java.sql.*;

/**
 * Created by zhuangjy on 2015/7/13.
 */
public class GetTime {
    public Timestamp getOutTime(){
        long currentTime = System.currentTimeMillis() + 120000;
        Date time = new Date(currentTime);
        Timestamp ts = new Timestamp(time.getTime());
        return ts;
    }
}
