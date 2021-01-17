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

/**
 * 暂未实现
 */
public class AliyunEmailSender implements IEmailSender {
    private static final Log logger = Log.getLog(AliyunEmailSender.class);

    /**
     * 文档：
     * https://help.aliyun.com/document_detail/directmail/api-reference/sendmail
     * -related/SingleSendMail.html?spm=5176.docdirectmail/api-reference/
     * sendmail-related/BatchSendMail.6.118.Qd9yth
     */
    @Override
    public void send(Email email) {

    }
}
