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

import com.jfinal.log.Log;
import net.ninemm.base.utils.HttpUtils;

import java.util.Map;

/**
 * 第三方认证的抽象类
 *
 * 第一步，构建跳转的URL，跳转后用户登录成功，返回到callback url，并带上code
 * 第二步，通过code，获取access token
 * 第三步，通过 access token 获取用户的open_id
 * 第四步，通过 open_id 获取用户信息
 *
 * @author Eric.Huang
 * @date 2018-06-28 17:53
 **/

public abstract class AbstractOauthConnector {

    private static final Log LOG = Log.getLog(AbstractOauthConnector.class);

    private String appId;
    private String appSecret;
    private String name;
    private String redirectUri;

    public AbstractOauthConnector(String appId, String appSecret, String name) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.name = name;
    }

    protected String httpGet(String url) {
        try {
            return HttpUtils.get(url);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    protected String httpPost(String url, Map<String, ? extends Object> params) {
        try {
            return HttpUtils.post(url, params);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;

    }

    public abstract String createAuthorizeUrl(String state);

    protected abstract OauthUser getOauthUser(String code);

    public OauthUser getUser(String code) {
        return getOauthUser(code);
    }

    public String getAuthorizeUrl(String state, String redirectUri) {
        this.redirectUri = redirectUri;
        return createAuthorizeUrl(state);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
