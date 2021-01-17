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

package net.ninemm.base.oauth2.connector;

import com.alibaba.fastjson.JSONObject;
import net.ninemm.base.oauth2.AbstractOauthConnector;
import net.ninemm.base.oauth2.OauthUser;

/**
 * 微信登录
 *
 * @author Eric.Huang
 * @date 2018-06-28 18:04
 **/

public class WechatConnector extends AbstractOauthConnector {

    public WechatConnector(String name, String appId, String appSecret) {
        super(name, appId, appSecret);
    }

    /**
     *  DOC
     *  https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&id=open1419316505
     *
     *  public WechatConnector() {
     *       setClientId(OptionQuery.findValue("oauth2_wechat_appkey"));
     *       setClientSecret(OptionQuery.findValue("oauth2_wechat_appsecret"));
     *       setName("wechat");
     *   }
     * @author Eric Huang
     * @date 2018-06-28 18:26
     * @param
     * @return
     **/
    @Override
    public String createAuthorizeUrl(String state) {
        StringBuilder urlBuilder = new StringBuilder("https://open.weixin.qq.com/connect/qrconnect?");
        urlBuilder.append("response_type=code");
        urlBuilder.append("&scope=snsapi_login");
        urlBuilder.append("&appid=" + getAppId());
        urlBuilder.append("&redirect_uri=" + getRedirectUri());
        urlBuilder.append("&state=" + state);
        urlBuilder.append("#wechat_redirect");

        return urlBuilder.toString();
    }

    protected JSONObject getAccessToken(String code) {

        // https://api.weixin.qq.com/sns/oauth2/access_token?
        // appid=APPID
        // &secret=SECRET
        // &code=CODE
        // &grant_type=authorization_code

        StringBuilder urlBuilder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?");
        urlBuilder.append("grant_type=authorization_code");
        urlBuilder.append("&appid=" + getAppId());
        urlBuilder.append("&secret=" + getAppSecret());
        urlBuilder.append("&code=" + code);

        String url = urlBuilder.toString();

        String httpString = httpGet(url);

        /**
         * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
         * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE",
         * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL" }
         */

        return JSONObject.parseObject(httpString);
    }

    @Override
    protected OauthUser getOauthUser(String code) {
        JSONObject tokenJson = getAccessToken(code);
        String accessToken = tokenJson.getString("access_token");
        String openId = tokenJson.getString("openid");

        // https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID

        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
        String httpString = httpGet(url);

        /**
         * { "openid":"OPENID", "nickname":"NICKNAME", "sex":1,
         * "province":"PROVINCE", "city":"CITY", "country":"COUNTRY",
         * "headimgurl":
         * "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
         * "privilege":[ "PRIVILEGE1", "PRIVILEGE2" ], "unionid":
         * " o6_bmasdasdsad6_2sgVt7hMZOPfL" }
         */

        OauthUser user = new OauthUser();
        JSONObject json = JSONObject.parseObject(httpString);
        user.setAvatar(json.getString("headimgurl"));
        user.setNickname(json.getString("nickname"));

        user.setOpenId(openId);
        int sex = json.getIntValue("sex");
        user.setGender(sex == 1 ? "male" : "female");
        user.setSource(getName());

        return user;
    }

}
