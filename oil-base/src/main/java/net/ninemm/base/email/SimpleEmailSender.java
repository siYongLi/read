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

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class SimpleEmailSender extends Authenticator implements IEmailSender {

    private static final Log logger = Log.getLog(SimpleEmailSender.class);

    private String host;
    private String name;
    private String nickName;
    private String password;
    private boolean useSSL = true;
    private boolean enable = false;

    public SimpleEmailSender() {
        this.host = SystemOptions.get(Consts.EMAIL_SMTP);
        this.name = SystemOptions.get(Consts.EMAIL_ACCOUNT);
        this.password = SystemOptions.get(Consts.EMAIL_PASSWORD);
        this.useSSL = SystemOptions.getAsBool(Consts.EMAIL_SSL_ENABLE);
        this.enable = SystemOptions.getAsBool(Consts.EMAIL_ENABLE);
        this.nickName = SystemOptions.get(Consts.EMAIL_NAME);
    }

    private Message createMessage() {


        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");

        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", "25");

        if (useSSL) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.port", "465");
        }

        // error:javax.mail.MessagingException: 501 Syntax: HELO hostname
        props.setProperty("mail.smtp.localhost", "127.0.0.1");

        Session session = Session.getInstance(props, this);
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(MimeUtility.encodeText(nickName) + "<" + name + ">"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(name, password);
    }

    private static Address[] toAddress(String... emails) {
        if (emails == null || emails.length == 0){
            return null;
        }

        Set<Address> addSet = new HashSet<Address>();
        for (String email : emails) {
            try {
                addSet.add(new InternetAddress(email));
            } catch (AddressException e) {
                continue;
            }
        }
        return addSet.toArray(new Address[0]);
    }

    @Override
    public void send(Email email) {
        if (enable == false) {
            //do nothing
            return;
        }

        Message message = createMessage();
        try {
            message.setSubject(email.getSubject());
            message.setContent(email.getContent(), "text/html;charset=utf-8");

            message.setRecipients(Message.RecipientType.TO, toAddress(email.getTo()));
            message.setRecipients(Message.RecipientType.CC, toAddress(email.getCc()));

            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("SimpleEmailSender send error", e);
        }

    }
}
