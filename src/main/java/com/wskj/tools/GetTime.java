package com.wskj.tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhuangjy on 2015/7/13.
 */
public class GetTime {
    public static Timestamp getOutTime(){
        long currentTime = System.currentTimeMillis() + 120000;
        Date time = new Date(currentTime);
        Timestamp ts = new Timestamp(time.getTime());
        return ts;
    }

    public  static Timestamp convertToTimeStamp(String time) throws ParseException {
        if(time == null || time.equals(""))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(time);
            Timestamp ts = new Timestamp(date.getTime());
            return ts;
        }catch (Exception e){
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            Timestamp ts = new Timestamp(date.getTime());
            return ts;
        }
    }

    private GetTime(){}

}
