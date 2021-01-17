/*
 * Copyright (c) 2015-2018, Eric Huang 黄鑫 (ninemm@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ninemm.base.utils;

import com.jfinal.kit.StrKit;
import io.jboot.Jboot;
import io.jboot.support.redis.JbootRedis;
import org.joda.time.DateTime;

/**
 * 生成唯一的单号工具类
 *
 * @author Eric.Huang
 * @date 2018-08-18 21:19
 **/

public class SystemSerialUtil {

    private JbootRedis jbootRedis = Jboot.getRedis();

    public String getSequence(String type, String sellerCode, String name){

        if (StrKit.notBlank(name)) {
            String format = DateTime.now().toString("yyyyMMdd");
            String key = name + format;
            String num = jbootRedis.incr(key).toString();
            jbootRedis.expire(key,86400);
            while(null != num && num.length()<5){
                num = "0"+num;
            }
            String result = key+num;
            return result;
        }
        return null;
    }


}
