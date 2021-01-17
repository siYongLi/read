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
import com.jfinal.kit.Ret;
import io.jboot.utils.ArrayUtil;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.JbootController;

import java.lang.reflect.Method;

/**
 * 非空参数拦截器
 * @author Eric
 *
 */
public class NotNullParaInterceptor implements Interceptor {

    /** 异常页面 */
    private String exceptionView = "/exception.html";

    public NotNullParaInterceptor(String exceptionView) {
        this.exceptionView = exceptionView;
    }

    @Override
    public void intercept(Invocation inv) {
        Method method = inv.getMethod();

        NotNullPara notNullPara = method.getAnnotation(NotNullPara.class);
        if (notNullPara == null) {
            inv.invoke();
            return;
        }

        String[] paraKeys = notNullPara.value();
        if (ArrayUtil.isNullOrEmpty(paraKeys)) {
            inv.invoke();
            return;
        }

        for (String param : paraKeys) {
            String value = inv.getController().getPara(param);
            if (value == null || value.trim().length() == 0) {
                //renderError(inv, param, notNullPara.errorRedirect());
                renderJsonError(inv, param, notNullPara.errorRedirect());
                return;
            }
        }

        inv.invoke();
    }

    private void renderJsonError(Invocation inv, String param, String errorRedirect) {
        Controller controller = inv.getController();
        if (controller instanceof JbootController) {
            JbootController c = (JbootController) controller;
            c.renderJson(Ret.fail("result","参数["+param+"]不可为空"));
            return;
        }
    }


    private void renderError(Invocation inv, String param, String errorRedirect) {
        if (StrUtil.isNotBlank(errorRedirect)) {
            inv.getController().redirect(errorRedirect);
            return;
        }

        Controller controller = inv.getController();
        if (controller instanceof JbootController) {
            JbootController c = (JbootController) controller;
            if (c.isAjaxRequest()) {
                c.renderJson(Ret.fail("result","参数["+param+"]不可为空"));
                return;
            }
        }
        controller.setAttr(BusinessExceptionInterceptor.MESSAGE_TAG, "参数["+param+"]不可为空").render(exceptionView);
    }
}
