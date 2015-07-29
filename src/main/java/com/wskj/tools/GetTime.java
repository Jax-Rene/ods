package com.wskj.tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public  Timestamp convertToTimeStamp(String time) throws ParseException {
        System.out.println(time);
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

//    public  static  void main(String[] args) throws ParseException {
//        String t = "2015-03-23";
//        Timestamp res = convertToTimeStamp(t);
//        System.out.println(res);
//    }

}
