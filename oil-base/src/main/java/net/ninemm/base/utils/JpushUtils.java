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

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.jfinal.kit.LogKit;

/**
 * JPush 消息推送工具类
 *
 * @author Eric.Huang
 * @date 2018-07-19 23:24
 **/

public class JpushUtils {

    private static final String APP_KEY = "a63c0fa2984102cba91f8cbd";
    private static final String MASTER_SECRET = "0b88921c5217c51e59e663f6";

    private static PushPayload buildPushObject(String alias) {
        return PushPayload.newBuilder()
            .setPlatform(Platform.all())
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.alert("您有新的订单请尽快处理!"))
            .build();
    }

    private static PushPayload buildPushObject(Platform platform, String alias) {
        return PushPayload.newBuilder()
            .setPlatform(platform)
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.alert("您有新的订单请尽快处理!"))
            .build();
    }

    /**
     * JPush推送消息
     * @author Eric Huang
     * @date 2018-07-20 15:40
     * @param alias
     * @return void
     **/
    public static void send(String alias) {

        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setMaxRetryTimes(5);
        clientConfig.setConnectionTimeout(10 * 1000);
        clientConfig.setSSLVersion("TLSv1.1");

        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, clientConfig);
        PushPayload pushPayload = buildPushObject(Platform.android(), alias);
        try {
            PushResult result = jpushClient.sendPush(pushPayload);
            LogKit.info(result.toString());
            jpushClient.close();
        } catch (APIConnectionException e) {
            e.printStackTrace();
            LogKit.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            e.printStackTrace();
            LogKit.error("Should review the error, and fix the request", e);
        }
    }

}
