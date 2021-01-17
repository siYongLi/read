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

package net.ninemm.base.plugin.jwt;

import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import io.jboot.support.jwt.JwtManager;
import io.jboot.support.shiro.JbootShiroInvokeListener;
import io.jboot.support.shiro.processer.AuthorizeResult;
import io.jboot.utils.StrUtil;
import net.ninemm.base.common.Consts;
import net.ninemm.base.common.RestResult;
import net.ninemm.base.common.ResultCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

/**
 * jwt shiro listener
 *
 * @author Eric Huang
 * @date 2018-06-29 16:52
 **/
public class JwtShiroInvokeListener implements JbootShiroInvokeListener {

   private final static Log LOG = Log.getLog(JwtShiroInvokeListener.class);

   @Override
   public void onInvokeBefore(Invocation inv) {
       Controller controller = (Controller) inv.getController();
       String jwtToken = controller.getHeader(JwtManager.me().getHttpHeaderName());

       if (StrUtil.isBlank(jwtToken)) {
           inv.invoke();
           return;
       }

       String userId = JwtManager.me().getPara(Consts.JWT_USER_ID);
       AuthenticationToken token = new JwtAuthenticationToken(userId, jwtToken);

       try {
           Subject subject = SecurityUtils.getSubject();
           subject.login(token);
       } catch (Exception e) {
           LOG.error(e.getMessage());
       }
   }

   @Override
   public void onInvokeAfter(Invocation inv, AuthorizeResult result) {
       if (result == null || result.isOk()) {
           inv.invoke();
           return;
       }

       int errorCode = result.getErrorCode();
       switch (errorCode) {
           case AuthorizeResult.ERROR_CODE_UNAUTHENTICATED:
               doProcessUnauthenticated(inv.getController());
               break;
           case AuthorizeResult.ERROR_CODE_UNAUTHORIZATION:
               doProcessuUnauthorization(inv.getController());
           break;
           default:
               doProcessuDefault(inv.getController());
       }
   }

   /**
    * 其他处理
    * @param controller
    */
   private void doProcessuDefault(Controller controller) {
       controller.renderJson(RestResult.buildError("404"));
   }

   /**
    * 没有认证信息处理
    * @param controller
    */
   private void doProcessUnauthenticated(Controller controller) {
       RestResult restResult = new RestResult();
       restResult.setCode(ResultCode.OVERTIME_ERROR);
       restResult.setMsg("Token过期，请重新登录!");
       controller.renderJson(restResult);
   }

   /**
    * 无授权信息处理
    * @param controller
    */
   private void doProcessuUnauthorization(Controller controller) {
       RestResult restResult = new RestResult();
       restResult.setCode(ResultCode.MSG_CODE_ERROR);
       restResult.setMsg("没有权限，请联系管理员!");
       controller.renderJson(restResult);
   }
}
