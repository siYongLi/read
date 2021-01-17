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

package net.ninemm.base.oauth2;

import com.jfinal.core.Controller;

import java.util.UUID;

/**
 * Oauth2认证抽象类
 *
 * @author Eric.Huang
 * @date 2018-06-28 18:00
 **/

public abstract class AbstractOauth2Controller extends Controller {

    public void index() {
        String processerName = getPara();
        AbstractOauthConnector op = onConnectorGet(processerName);
        String state = UUID.randomUUID().toString().replace("-", "");

        String requestUrl = getRequest().getRequestURL().toString();
        String callBackUrl = requestUrl.replace("/" + processerName, "/callback/" + processerName);
        String url = op.getAuthorizeUrl(state, callBackUrl);

        setSessionAttr("oauth_state", state);
        redirect(url);
    }

    /**
     * 回调函数
     *
     * xxx/callback/wechat
     * xxx/callback/qq
     * xxx/callback/weibo
     *
     * @author Eric Huang
     * @date 2018-06-28 18:32
     * @param
     * @return
     **/
    public void callback() {
        String sessionState = getSessionAttr("oauth_state");
        String state = getPara("state");

        if (!sessionState.equals(state)) {
            onAuthorizeError("state not validate");
            return;
        }

        String code = getPara("code");
        if (null == code || "".equals(code.trim())) {
            onAuthorizeError("can't get code");
            return;
        }

        String processerName = getPara();
        AbstractOauthConnector op = onConnectorGet(processerName);

        OauthUser ouser = null;
        try {
            ouser = op.getUser(code);
        } catch (Throwable e) {
            onAuthorizeError("get oauth2 user exception:" + e.getMessage());
            return;
        }

        if (ouser == null) {
            onAuthorizeError("can't get user info!");
            return;
        }

        onAuthorizeSuccess(ouser);

    }

    public abstract void onAuthorizeSuccess(OauthUser oauthUser);

    public abstract void onAuthorizeError(String errorMessage);

    public abstract AbstractOauthConnector onConnectorGet(String processerName);
}
