package net.ninemm.base.utils;

import io.jboot.utils.StrUtil;

import java.util.Random;

/**
 * @description:
 * @author: lsy
 * @create: 2019-03-28 15:46
 **/
public class StrUtils extends StrUtil {
    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String getRandomNum() {
        int length=10;
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 输入几率返回布尔值 例如中将几率,返回true表示中奖了
     * @param chance 取值0-100
     * @return
     */
    public static Boolean getSendChance(Integer chance) {
        Boolean res =false;
        Random random = new Random();
        int nextInt = random.nextInt(100)+1;
        if(nextInt<=chance){
            res=true;
        }
        return res;
    }

    /**
     * 每多少人有一人中奖
     * @param peopleNum
     * @return
     */
    public static Boolean getChanceByPeopleNum(Integer peopleNum) {
        Boolean res =false;
        Random random = new Random();
        int nextInt = random.nextInt(peopleNum)+1;
        if (nextInt==1) {
            res = true;
        }
        return res;
    }

    /**
     * @param num
     * @return
     * 获取0-num-1范围内的随机数
     */
    public static Integer getRandom(int num) {
        Random random = new Random();
        int nextInt = random.nextInt(num);
        return nextInt;
    }

}
