package com.wskj.tools;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *  java日期对象经过Jackson库转换成JSON日期格式化自定义类
 */
public class CustomDateSerializer extends JsonSerializer<Timestamp> {

    @Override
    public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(new Date(value.getTime()));
        jgen.writeString(formattedDate);
    }
}