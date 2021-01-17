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

package net.ninemm.base.common;

/**
 * 缓存目录 KEY
 *
 */
public class CacheKey {

	/** 基础数据 对应 data 表 keyValue缓存在cache的name */
	public static final String CACHE_KEYVALUE = "keyValue";

	/** 页面数据缓存 */
	public static final String CACHE_PAGE = "pageCache";

	/** 30分钟缓存 */
	public static final String CACHE_H1M30 = "h1m30";
	
	/** 导航目录缓存 */
	public static final String CACHE_MENU = "menuCache";
	
	/** shiro 授权缓存 */
	public static final String CACHE_SHIRO_AUTH = "shiro-authorizationCache";
	
	/** shiro session在线缓存 */
	public static final String CACHE_SHIRO_ACTIVESESSION = "shiro-active-session";

	/** shiro session踢出缓存 */
	public static final String CACHE_SHIRO_KICKOUTSESSION = "shiro-kickout-session";
	
	/** shiro 密码重试缓存 */
	public static final String CACHE_SHIRO_PASSWORDRETRY = "shiro-passwordRetryCache";
	
	/** shiro SESSION KEY 缓存 */
	public static final String CACHE_SHIRO_SESSION = "shiro-session";
	
	/** 验证码缓存 */
	public static final String CACHE_CAPTCHAR_SESSION = "captchar-cache";

	/** jwt_token */
	public static final String CACHE_JWT_TOKEN = "jwt_token";

	/** 账套 缓存名称 */
	public static final String CACHE_SELLER = "seller";

	/** 根节点(部门)数据域 缓存名称 */
	public static final String CACHE_ROOT_DATA_AREA = "root-data-area";

	/** 父级部门数据域  缓存名称*/
	public static final String CACHE_PARENT_DATA_AREA = "parent-dataarea";

	/** 登录用户的角色为管理者 */
	public static final String CACHE_MANAGER_ROLE = "manager";

	/** 登录用户的角色为业务员 */
	public static final String CACHE_USER_ROLE = "normal";

	/** 登录用户角色的 KEY */
    public static final String CACHE_KEY_ROLE = "role";

	/** 登录用户缓存 CacheName */
	public static final String CACHE_LOGINED_USER = "user";

}
