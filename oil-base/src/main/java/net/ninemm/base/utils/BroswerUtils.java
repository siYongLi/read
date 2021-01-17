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
 *
 */

package net.ninemm.base.utils;

/**
 *
 * @author Eric.Huang
 * @date 2019-01-15 23:08
 * @package net.ninemm.base.utils
 **/

public class BroswerUtils {
    static String[] Agents = {"ucbrowser","firefox","quark","opera","baidubrowser","qihoobrowser","safari"};

    public static String getBroswerType(String userAgent) {
        if (userAgent==null) {
            return null;
        }
        String ua = userAgent.toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {
            return "wechat";
        }else if (ua.indexOf("windowswechat") > 0) {
            return "windowswechat";
        }else if (ua.indexOf("mqqbrowser") > 0) {
            return "qqbrowser";
        }else if (ua.indexOf("chrome") > 0) {
            return "chrome";
        }else if (ua.indexOf("msie") > 0) {
            return "ie";
        }else {
            for (String agent : Agents) {
                if (ua.indexOf(agent) >= 0) {
                    return agent;
                }
            }
        }
        return null;
    }

    public static String getBrand(String userAgent){
        if(userAgent.contains("iPhone")){
            return "iPhone";
        }else if(userAgent.contains("iPad")){
            return "iPad";
        }

        if(!userAgent.contains("Build/")){
            return "未知";
        }
        String build = userAgent.substring(0,userAgent.indexOf("Build/"));
        String brand = build.substring(build.lastIndexOf(";") + 1);
        return brand==null?"未知":brand;
    }

}
