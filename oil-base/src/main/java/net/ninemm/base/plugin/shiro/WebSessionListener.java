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

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * session 监听
 * @author Eric
 *
 */
public class WebSessionListener extends SessionListenerAdapter {

	private final static Logger logger = LoggerFactory.getLogger(WebSessionListener.class);

	@Override
	public void onExpiration(Session session) {
		logger.info("onExpiration session {}" + session);
		super.onExpiration(session);
	}

    @Override
    public void onStart(Session session) {
        logger.info("onStart session {}" + session);
        super.onStart(session);
    }

    @Override
    public void onStop(Session session) {
        logger.info("onStop session {}" + session);
        super.onStop(session);
    }
}
