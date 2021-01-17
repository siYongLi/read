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
package net.ninemm.base.sms;

import com.jfinal.kit.Base64Kit;
import com.jfinal.log.Log;
import io.jboot.utils.HttpUtil;
import io.jboot.utils.StrUtil;
import net.ninemm.base.SystemOptions;
import net.ninemm.base.common.Consts;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

/**
 * 阿里云短信发送
 * api 接口文档 ：https://help.aliyun.com/document_detail/56189.html?spm=a2c4g.11186623.6.590.263891ebLwA3nl
 */
public class AliyunSmsSender implements ISmsSender {

    private static final Log log = Log.getLog(AliyunSmsSender.class);

    @Override
    public boolean send(SmsMessage sms) {

        String app_key = SystemOptions.get(Consts.SMS_APPID);
        String app_secret = SystemOptions.get(Consts.SMS_APPSECRET);


        Map<String, String> params = new HashMap<>();
        params.put("AccessKeyId", app_key);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        String timestamp = df.format(new Date());
        params.put("Timestamp", timestamp);
        params.put("Format", "JSON");
        params.put("SignatureMethod", "HMAC-SHA1");
        params.put("SignatureVersion", "1.0");
        params.put("SignatureNonce", StrUtil.uuid());


        params.put("Action", "SendSms");
        params.put("Version", "2017-05-25");
        params.put("RegionId", "cn-hangzhou");
        params.put("PhoneNumbers", sms.getMobile());
        params.put("SignName", sms.getSign());
        params.put("TemplateCode", sms.getTemplate());

        if (StrUtil.isNotBlank(sms.getCode())) {
            params.put("TemplateParam", "{\"code\":\"" + sms.getCode() + "\"}");
        }

        try {
            String url = doSignAndGetUrl(params, app_secret);
            String content = HttpUtil.httpGet(url);
            if (StrUtil.isNotBlank(content) && content.contains("\"Code\":\"OK\"")) {
                return true;
            } else {
                log.error("aliyun sms send error : " + content);
            }
        } catch (Exception e) {
            log.error("AliyunSmsSender exception", e);
        }

        return false;
    }

    public boolean send(SmsMessage sms, String appKey, String appSecret) {
        if (!StringUtils.isNotEmpty(appKey)) {
            appKey = SystemOptions.get(Consts.SMS_APPID);
        }
        if (!StringUtils.isNotEmpty(appSecret)) {
            appSecret = SystemOptions.get(Consts.SMS_APPSECRET);
        }

        Map<String, String> params = new HashMap<>();
        params.put("AccessKeyId", appKey);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        String timestamp = df.format(new Date());
        params.put("Timestamp", timestamp);
        params.put("Format", "JSON");
        params.put("SignatureMethod", "HMAC-SHA1");
        params.put("SignatureVersion", "1.0");
        params.put("SignatureNonce", StrUtil.uuid());


        params.put("Action", "SendSms");
        params.put("Version", "2017-05-25");
        params.put("RegionId", "cn-hangzhou");
        params.put("PhoneNumbers", sms.getMobile());
        params.put("SignName", sms.getSign());
        params.put("TemplateCode", sms.getTemplate());

        if (StrUtil.isNotBlank(sms.getCode())) {
            params.put("TemplateParam", "{\"code\":\"" + sms.getCode() + "\"}");
        }

        try {
            String url = doSignAndGetUrl(params, appSecret);
            String content = HttpUtil.httpGet(url);
            if (StrUtil.isNotBlank(content) && content.contains("\"Code\":\"OK\"")) {
                return true;
            } else {
                log.error("aliyun sms send error : " + content);
            }
        } catch (Exception e) {
            log.error("AliyunSmsSender exception", e);
        }

        return false;
    }

    public static String doSignAndGetUrl(Map<String, String> params, String secret) throws Exception {

        java.util.TreeMap<String, String> sortParas = new java.util.TreeMap<>();
        sortParas.putAll(params);


        java.util.Iterator<String> it = sortParas.keySet().iterator();
        StringBuilder sortQueryStringTmp = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(params.get(key).toString()));
        }
        String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append("GET").append("&");
        stringToSign.append(specialUrlEncode("/")).append("&");
        stringToSign.append(specialUrlEncode(sortedQueryString));
        String sign = sign(secret + "&", stringToSign.toString());


        String signature = specialUrlEncode(sign);
        return "http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp;
    }

    public static String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    public static String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return Base64Kit.encode(signData);
//        return new sun.misc.BASE64Encoder().encode(signData);
    }


    public static void main(String[] args) {
        //String mobile, String url,String templete,String sign,String key,String secret
        Boolean aBoolean = AliyunSmsSender.sendSurvey("15527128008", "test", "SMS_180048683", "营销与创新研究中心", "LTAIJ5GGEuBDDB7e", "KVOVkHFCG7essZXOQ1bdGtVyBIxasn");
        System.out.println(aBoolean);
        /*String app_key = "LTAIJ5GGEuBDDB7e";
        String app_secret = "KVOVkHFCG7essZXOQ1bdGtVyBIxasn";

        SystemOptions.set(Consts.SMS_APPID, app_key);
        SystemOptions.set(Consts.SMS_APPSECRET, app_secret);
        String url ="123456";
        SmsMessage sms = new SmsMessage();
        sms.setMobile("15527128008");
        sms.setTemplate("SMS_166777160");
        sms.setSign("武汉珞究");
        sms.setCode(url);

        boolean sendOk = new AliyunSmsSender().send(sms);

        System.out.println(sendOk);
        System.out.println("===============finished!===================");*/
    }

    public static Boolean sendValidateCode(String mobile ,String code) {
        SmsMessage sms = new SmsMessage();
        sms.setMobile(mobile);
        sms.setTemplate(SystemOptions.get(Consts.SMS_CHECKED_TEMPLATE_CODE));
        sms.setCode(code);
        sms.setSign(SystemOptions.get(Consts.SMS_FREE_SIGN_NAME));
        return new AliyunSmsSender().send(sms);
    }

    public static Boolean sendSurvey(String mobile, String url) {
        SmsMessage sms = new SmsMessage();
        sms.setMobile(mobile);
        sms.setTemplate(SystemOptions.get(Consts.SMS_PUSH_TEMPLATE_CODE));
        sms.setCode(url);
        sms.setSign(SystemOptions.get(Consts.SMS_FREE_SIGN_NAME));
        return new AliyunSmsSender().send(sms);
    }

    public static Boolean sendSurvey(String mobile, String url,String templete,String sign,String key,String secret) {
        if (!StrUtil.isNotEmpty(templete)) {
            templete = SystemOptions.get(Consts.SMS_PUSH_TEMPLATE_CODE);
        }
        if (!StrUtil.isNotEmpty(sign)) {
            sign = SystemOptions.get(Consts.SMS_FREE_SIGN_NAME);
        }
        SmsMessage sms = new SmsMessage();
        sms.setMobile(mobile);
        sms.setTemplate(templete);
        sms.setCode(url);
        sms.setSign(sign);
        return new AliyunSmsSender().send(sms,key,secret);
    }

    public String doSend(SmsMessage sms, String app_key, String app_secret) {
        Map<String, String> params = new HashMap<>();
        params.put("AccessKeyId", app_key);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        String timestamp = df.format(new Date());
        params.put("Timestamp", timestamp);
        params.put("Format", "JSON");
        params.put("SignatureMethod", "HMAC-SHA1");
        params.put("SignatureVersion", "1.0");
        params.put("SignatureNonce", StrUtil.uuid());


        params.put("Action", "SendSms");
        params.put("Version", "2017-05-25");
        params.put("RegionId", "cn-hangzhou");
        params.put("PhoneNumbers", sms.getMobile());
        params.put("SignName", sms.getSign());
        params.put("TemplateCode", sms.getTemplate());

        if (StrUtil.isNotBlank(sms.getCode())) {
            params.put("TemplateParam", "{\"code\":\"" + sms.getCode() + "\"}");
        }
        String url = null;
        try {
            url = doSignAndGetUrl(params, app_secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = HttpUtil.httpGet(url);
        return content;
    }
}
