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

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.render.JsonRender;
import com.jfinal.render.NullRender;
import com.jfinal.render.RedirectRender;
import io.jboot.utils.StrUtil;
import io.jboot.web.render.JbootJsonRender;
import net.ninemm.base.utils.AesUtil;

/**
 * 数据加密
 * 返回json数据的时候会做加密
 */
public class AesInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        inv.invoke();
        Controller target = inv.getTarget();
        String notAes = target.getPara("notAes");
        if (StrUtil.isNotEmpty(notAes)) {
            return;
        }
        try {
            if (target.getRender() instanceof JbootJsonRender) {
                JbootJsonRender render = (JbootJsonRender) target.getRender();
                String jsonText = render.getJsonText();
                if (jsonText != null) {
                    jsonText = AesUtil.encrypt(jsonText.toString());
                    target.renderJson(jsonText.replaceAll("\r\n", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
