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

public class SmsMessage {

    private String mobile;
    private String sign; //阿里云和腾讯云需要签名
    private String template;
    private String code; //验证码


    public static SmsMessage create(String mobile, Object code, String template, String sign) {
        SmsMessage sms = new SmsMessage();
        sms.setCode(code.toString());
        sms.setSign(sign);
        sms.setMobile(mobile);
        sms.setTemplate(template);
        return sms;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 发送短信
     */
    public boolean send() {
        return SmsSenderFactory.createSender().send(this);
    }
}
