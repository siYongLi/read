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
import io.jboot.utils.StrUtil;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 *
 * @author Eric.Huang
 * @date 2018-07-11 16:27
 **/

public class DateUtils {

    public static final String DEFAULT_DATE_FORMA = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 锁对象 */
    private static final Object LOCKOBJ = new Object();

    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 功能描述：当天的起始时间
     * 输入参数：
     * @return
     * 返回类型：Date
     * 创建人：eric
     * 日期：2016年4月13日
     */
    public static String getStartOfDay(String date) {
        DateTime dateTime = DateTime.now();

        if (StrKit.notBlank(date)) {
            dateTime = DateTime.parse(date);
        }

        dateTime = dateTime.withTimeAtStartOfDay();
        return dateTime.toString(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 功能描述：当天的结束时间
     * 输入参数：
     * @return
     * 返回类型：Date
     * 创建人：eric
     * 日期：2016年4月13日
     */
    public static String getEndOfDay(String date) {
        DateTime dateTime = DateTime.now();

        if (StrKit.notBlank(date)) {
            dateTime = DateTime.parse(date);
        }

        dateTime = dateTime.millisOfDay().withMaximumValue();
        return dateTime.toString(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (LOCKOBJ) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }


    public static String format(String date, String pattern) {
        if (!StrUtil.isNotEmpty(date)) {
            return null;
        }
        Date parse = null;
        try {
            parse = getSdf(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return getSdf(pattern).format(parse);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    public static Date parseWithDefault(String dateStr, String pattern,Date defaultDate){
        if (StrKit.isBlank(dateStr)) {
            return null;
        }
        try {
            return  getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            return defaultDate;
        }
    }

    public static void main(String[] args) {
        /*System.out.println(format("2019-05-17 17:32:19.0","yyyy-MM-dd HH:ss:mm"));
        try {
            System.out.println(parse("2019-08-20 16:47:16","yyyy-MM-dd HH:ss:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        System.out.println(formatSpendTime(58L));;
        System.out.println(formatSpendTime(60L));;
        System.out.println(formatSpendTime(62L));;
        System.out.println(formatSpendTime(3712L));;
        System.out.println(formatSpendTime(89400L));;

    }

    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }

    public static String formatSpendTime(Long spendTime) {
        if (spendTime==null) {
            return "0秒";
        }
        if(spendTime>=0 && spendTime<=60){
            return spendTime+"秒";
        }else if(spendTime>60 && spendTime<=3600){
            return spendTime/60+"分"+spendTime%60+"秒";
        }else if(spendTime>3600 && spendTime<=86400){
            return spendTime/3600+"小时"+spendTime%3600/60+"分"+spendTime%60+"秒";
        }else{
            return spendTime/86400+"天"+spendTime%86400/3600+"小时"+spendTime%3600/60+"分";
        }
    }
}
