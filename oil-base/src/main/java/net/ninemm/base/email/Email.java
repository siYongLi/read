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
package net.ninemm.base.email;

import com.jfinal.log.Log;
import net.ninemm.base.SystemOptions;
import net.ninemm.base.common.Consts;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.commons.email
 */

public class Email {

    private static final Log LOG = Log.getLog(Email.class);
    public static final String SUBJECT ="问卷调查";

    private String[] to = null;
    private String[] cc = null;
    private String subject = null;
    private String content = null;

    public static Email create() {
        return new Email();
    }

    public Email to(String... toEmails) {
        this.to = toEmails;
        return this;
    }

    public Email cc(String... ccEmails) {
        this.cc = ccEmails;
        return this;
    }

    public Email subject(String subject) {
        this.subject = subject;
        return this;
    }

    public Email content(String content) {
        this.content = content;
        return this;
    }

    public String[] getTo() {
        return to;
    }

    public String[] getCc() {
        return cc;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void send() {
        send(new SimpleEmailSender());
    }

    public void send(IEmailSender sender) {
        try {
            sender.send(this);
        } catch (Throwable ex) {
            LOG.error(ex.toString(), ex);
        }
    }

    public static void main(String[] args) {
        SystemOptions.set(Consts.EMAIL_SMTP,"smtp.163.com");
        SystemOptions.set(Consts.EMAIL_ACCOUNT,"15527128008@163.com");
        SystemOptions.set(Consts.EMAIL_NAME,"武汉珞究");
        SystemOptions.set(Consts.EMAIL_PASSWORD,"a4882633");
        SystemOptions.set(Consts.EMAIL_SSL_ENABLE,"true");
        SystemOptions.set(Consts.EMAIL_ENABLE,"true");
        //Email.create().subject("这是邮件标题").content("").to("1303435095@qq.com").send();
        StringBuffer sb = new StringBuffer();
        sb.append("尊敬的用户您好: <br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;您的意见对我们至关重要，希望您能够认真填写，点击下面的链接进入问卷页面 <br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;<a href='http://surveys.lisiyong.top/wxoauth/index?shortUrl=jaqQZZ3vBF&appIdKey=wx737b3fcddeb1273d'>点击此链接打开问卷</a>");

        Email.create().subject("这是邮件标题").content(sb.toString()).to("1303435095@qq.com").send();
    }
}

