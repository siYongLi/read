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
import com.jfinal.kit.LogKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import io.jboot.support.jwt.JwtManager;
import net.ninemm.base.common.Consts;

/**
 * 缓存拦截器
 *
 * @author Eric.Huang
 * @date 2018-07-07 14:46
 **/

public class GlobalCacheInterceptor implements Interceptor {

    private static final String DEFAULT_SWAGGERUI_ACTION = "swaggerui";

    @Override
    public void intercept(Invocation inv) {

        String actionKey = inv.getActionKey();
        if (actionKey.contains(DEFAULT_SWAGGERUI_ACTION)) {
            inv.invoke();
            return;
        }

        Controller controller = (Controller) inv.getController();
        String userId = JwtManager.me().getPara(Consts.JWT_USER_ID);;
        //Object obj = Jboot.me().getCache().get(CacheKey.CACHE_SELLER, controller.getJwtPara(Consts.JWT_USER_ID));
        //String dataArea = Jboot.me().getCache().get(CacheKey.CACHE_DEALER_DATA_AREA, controller.getJwtPara(Consts.JWT_USER_ID));
        //String role = Jboot.me().getCache().get(CacheKey.CACHE_LOGINED_USER,controller.getJwtPara(Consts.JWT_USER_ID) + ":" + CacheKey.CACHE_KEY_ROLE);
        if (StrKit.isBlank(userId)) {
            LogKit.error("用户Id和数据域不能同时为空");
            controller.renderJson(Ret.fail("result","用户Id和数据域不能同时为空"));
            return;
        }

//        if (obj == null) {
//            LogKit.error("未选择账套，请重新登录!");
//            controller.renderJson(RestResult.buildError("未选择账套，请重新登录!"));
//            return ;
//        }

//        if (StrKit.isBlank(role)) {
//
//            RestResult restResult = new RestResult();
//            restResult.setMsg("缓存过期，请重新登录!");
//            restResult.setCode(ResultCode.TOKEN_ERROR);
//            controller.renderJson(restResult);
//            return ;
//        }

        inv.invoke();

    }

}
