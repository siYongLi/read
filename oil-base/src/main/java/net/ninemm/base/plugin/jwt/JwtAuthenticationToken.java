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

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Jwt令牌信息
 *
 * @author Eric
 */
public class JwtAuthenticationToken implements AuthenticationToken {

	private static final long serialVersionUID = 1L;

	/** 用户id */
    private String userId;
    /** token */
    private String token;
	
	public JwtAuthenticationToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
