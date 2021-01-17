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

import io.jboot.Jboot;
import io.jboot.utils.StrUtil;
import net.ninemm.base.common.CacheKey;
import net.ninemm.base.plugin.shiro.ShiroCacheUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;

/**
 * 基于JWT（ JSON WEB TOKEN）的认证域
 *
 * @author Eric
 * @date 2018-06-28 22:05
 */
public abstract class AbstractJwtAuthorizingRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtAuthenticationToken;
    }

    @Override
    public void setCacheManager(CacheManager cacheManager) {
        super.setCacheManager(cacheManager);
        ShiroCacheUtils.setCacheManager(cacheManager);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) token;
        String userId = (String) jwtToken.getPrincipal();

        String uidCache = Jboot.getCache().get(CacheKey.CACHE_JWT_TOKEN, userId);
        if (StrUtil.isNotBlank(uidCache)) {
            /** token 已被加入黑名单 */
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(userId, jwtToken.getCredentials(), this.getName());
    }

}
