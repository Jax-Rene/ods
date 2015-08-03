package com.wskj.dao;

import com.wskj.model.Order;
import com.wskj.model.PersonOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhuangjy on 2015/7/27.
 */
public class TestDao {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        OrderDao orderDao = (OrderDao) ctx.getBean("orderDao");
        PersonOrder personOrder =new PersonOrder();
        countResult(1);

    }



    public static  Map<String, Integer> countResult(int orderId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<String> orginNames = new ArrayList<String>();
        orginNames.add("水煮肉片");
        orginNames.add("101 水煮肉片");
        orginNames.add("耗油牛肉");
        List<String> orderNames = new ArrayList<String>();
        /*
        获取所有套餐的完整名字
        例如 105 北京烤鸭
        输入105 应该获取 105 北京烤鸭
         */
        for(int i=0;i<orginNames.size();i++){
            String targetName = orginNames.get(i);
            boolean flag = true;
            for(int j=0;j<orderNames.size();j++){
                String curName = orderNames.get(j);
                if((targetName.indexOf(curName)!=-1) || (curName.indexOf(targetName) !=-1)){
                    if(targetName.length() > curName.length()) {
                        orderNames.remove(j);
                        orderNames.add(targetName);
                        flag = false;
                        break;
                    }
                }
            }
            if(flag)
                orderNames.add(targetName);
        }

        for (String t : orginNames) {
            String name = t;
            for(String n:orderNames){
                if(n.indexOf(name)!=-1){
                    map.put(n,map.get(n) + 1);
                }else
                    map.put(n,1);
            }
        }
        return map;
    }
}
