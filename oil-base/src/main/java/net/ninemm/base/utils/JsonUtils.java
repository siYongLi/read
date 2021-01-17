package net.ninemm.base.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @description:
 * @author: lsy
 * @create: 2019-05-05 10:19
 **/
public class JsonUtils {

    public static boolean isjson(String string){
        try {
            JSONObject jsonStr= JSONObject.parseObject(string);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
}
