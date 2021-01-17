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


import io.jboot.core.spi.JbootSpiLoader;
import io.jboot.utils.StrUtil;
import net.ninemm.base.SystemOptions;
import net.ninemm.base.common.Consts;

public class SmsSenderFactory {


    public static ISmsSender createSender() {

        boolean smsEnable = SystemOptions.getAsBool(Consts.SMS_ENABLE);

        if (smsEnable == false) {
            return new NonSmsSender();
        }

        String type = SystemOptions.get(Consts.SMS_TYPE);

        // 默认使用阿里云
        if (StrUtil.isBlank(type)) {
            return new AliyunSmsSender();
        }

        switch (type) {
            case Consts.SMS_TYPE_ALIYUN:
                return new AliyunSmsSender();
            case Consts.SMS_TYPE_QCLOUD:
                return new QCloudSmsSender();
        }

        //其他通过SPI扩展机制加载
        return JbootSpiLoader.load(ISmsSender.class, type);
    }

}
