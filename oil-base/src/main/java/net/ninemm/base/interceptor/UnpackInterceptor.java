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

package net.ninemm.base.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.weixin.sdk.utils.IOUtils;
import io.jboot.utils.StrUtil;
import net.ninemm.base.common.Consts;
import net.ninemm.base.common.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口参数解析拦截器
 *
 * @author Eric.Huang
 * @date 2018-06-29 00:10
 **/

public class UnpackInterceptor implements Interceptor {

    private static final Logger _LOG = LoggerFactory.getLogger(UnpackInterceptor.class);

    @Override
    public void intercept(Invocation in) {

        long begin = System.currentTimeMillis();
        String actionKey = in.getActionKey();
        try {
            Controller controller = in.getController();
            String message = IOUtils.toString(controller.getRequest().getInputStream(), Charset.defaultCharset());
            _LOG.info("接受到消息(未解包)：{}", message);
            Map<String, Object> param = JSONObject.parseObject(message, Map.class);

            String version = (String) param.get(Consts.RequestKeys.VERSION);
            String platform = (String) param.get(Consts.RequestKeys.PLATFORM);
            if(StrUtil.isBlank(version) || StrUtil.isBlank(platform)){
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(Consts.ResponseKeys.STATUS, ResultCode.PARAM_ERROR);
                result.put(Consts.ResponseKeys.MESSAGE, "参数异常！");
                controller.renderJson(result);
                return;
            }

            /*String data = (String) param.get("data");
            if(actionKey.startsWith("/member")){
                //登录后的密钥解密
                String token = (String) param.get(Consts.RequestKeys.TOKEN);
                System.err.println(token);
            }else{
                //登录前的密钥解密
                AppKey key = AppKeyService.service.selectKey(version, platform);
                String publicKey = key.getStr("public_key");
                byte[] decodeData = RSAKit.decryptByPublicKey(data.getBytes(Constant.Charset.UTF_8), publicKey);
                String result = new String(decodeData, Constant.Charset.UTF_8);
                param.put("data", result);
                controller.setAttr("param", param);
            }*/
        } catch (Exception e) {
            _LOG.error("解包失败：", e);
        }
        in.invoke();
        long end = System.currentTimeMillis();
        _LOG.info("服务器处理耗时：{}毫秒...", end - begin);

    }

}
