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

package net.ninemm.base.plugin.shiro;

import io.jboot.Jboot;
import net.ninemm.base.common.CacheKey;
import net.ninemm.base.exception.BusinessException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 密码重试认证
 * @author Eric
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	/** 允许密码重试最大次数 */
	private int allowRetryCount = 100;
	/** 账户将被锁定的时间 */
	private int lockedSeconds = 3600;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		MuitiLoginToken toToken = (MuitiLoginToken) token;
		
		String username = (String) toToken.getPrincipal();
		AtomicInteger atomicInteger = Jboot.getCache().get(CacheKey.CACHE_SHIRO_PASSWORDRETRY, username);

		if (atomicInteger == null) {
			atomicInteger = new AtomicInteger(0);
		} else {
			atomicInteger.incrementAndGet();
		}
		Jboot.getCache().put(CacheKey.CACHE_SHIRO_PASSWORDRETRY, username, atomicInteger, lockedSeconds);

		if (atomicInteger.get() > allowRetryCount) {
			throw new ExcessiveAttemptsException();
		}

		boolean matches = false;
		
		if (toToken.getLoginType().equals(MuitiLoginToken.USERPASSWORD_MODE)) {
			matches = super.doCredentialsMatch(toToken, info);
		} else if (toToken.getLoginType().equals(MuitiLoginToken.TOKEN_MODE)) {
			SimpleCredentialsMatcher simpleMatcher = new SimpleCredentialsMatcher();
			matches = simpleMatcher.doCredentialsMatch(toToken, info);
		} else {
			throw new BusinessException("not support login type :" + toToken.getLoginType());
		}
		
		if (matches) {
			Jboot.getCache().remove(CacheKey.CACHE_SHIRO_PASSWORDRETRY, username);
		}
		
		return matches;
	}

	public int getAllowRetryCount() {
		return allowRetryCount;
	}

	public void setAllowRetryCount(int allowRetryCount) {
		this.allowRetryCount = allowRetryCount;
	}

	public int getLockedSeconds() {
		return lockedSeconds;
	}

	public void setLockedSeconds(int lockedSeconds) {
		this.lockedSeconds = lockedSeconds;
	}
}
