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
 * 常量
 * @author Eric
 *
 */
public class Consts {

	/**
	 * Jwt 对应的userId
	 */
	public static final String JWT_USER_ID = "userId";
	public static final String JWT_DATA_AREA = "dataArea";

	public static final String JWT_DEPT_ID = "deptId";
	public static final String JWT_DEPT_DATA_AREA = "deptDataArea";

	public static final int PAGE_DEFAULT_SIZE = 10;
	public static final int PAGE_DEFAULT_NUMBER = 1;

	public static final String TOKEN_TAG = "_token";

	public static final String CAPTCHA_CODE = "code";

	public static final String COOKIE_USER_ID = "cookieUserId";

	public static final String WEB_NAME ="web_name";
	public static final String WEB_TITLE ="web_title";
	public static final String WEB_COPYRIGHT ="web_copyright";
	public static final String WEB_VERSION ="web_version";
	public static final String WEB_ICP_NUMBER ="web_icp_number";
	public static final String WEB_TEMPLATE_ID ="web_template_id";
	public static final String COPYRIGHT ="copyright";
	public static final String WEB_RECORD_NUMBER ="web_record_number";


	/** 树根节点默认ID */
	public static final Integer TREE_DEFAULT_ROOT_ID = 0;

	/** 平台类型 */
	public static final String PLATFORM_TYPE_VALUE = "sys-manage";

	/**
	 * 跨域访问时，过滤请求类型为 options 的方法
	 */
	public static final String FILTER_OPTIONS_METHOD = "options";

	/** 是否启用CDN */
	public static final String OPTION_CDN_ENABLE = "cdn_enable";
	/** CDN域名 */
	public static final String OPTION_CDN_DOMAIN = "cdn_domain";
	/** 网站域名 */
	public static final String OPTION_WEB_DOMAIN = "web_domain";

	public static final String OPTION_API_DOMAIN = "api_domain";

	public static final String USER_DEFAULT_PASSWORD = "123456";

	//短信
	public static final String SMS_APPID ="sms_appid";
	public static final String SMS_FREE_SIGN_NAME ="sms_free_sign_name";
	public static final String SMS_APPSECRET ="sms_appsecret";
	public static final String SMS_ENABLE ="sms_enable";
	public static final String SMS_PUSH_TEMPLATE_CODE ="sms_push_template_code";
	public static final String SMS_CHECKED_TEMPLATE_CODE ="sms_checked_template_code";
	public static final String SMS_TYPE ="sms_type";

	public static final String SMS_TYPE_ALIYUN ="aliyun";
	public static final String SMS_TYPE_QCLOUD ="qcloud";


	//邮箱
	public static final String EMAIL_ENABLE = "email_enable"; // 是否启用邮件发送功能
	public static final String EMAIL_SMTP = "email_smtp"; // 邮件服务器smtp
	public static final String EMAIL_ACCOUNT = "email_account"; //邮箱账号
	public static final String EMAIL_NAME = "email_name"; //邮箱账号
	public static final String EMAIL_PASSWORD = "email_password"; //邮箱密码
	public static final String EMAIL_SSL_ENABLE = "email_ssl_enable"; //是否启用ssl

	//七牛云的配置信息
	public static final String QINIU_AK = "qiniu_ak";
	public static final String QINIU_SK = "qiniu_sk";
	public static final String QINIU_BUCKET = "qiniu_bucket";
	public static final String QINIU_DOMAIN = "qiniu_domain";

	//高德地图的key
	public static final String GAODE_KEY = "gaode_key";

	//日志下载地址
	public static final String LOG_DOWNLOAD_PATH = "log_download_path";
	public static final String LOG_NAME = "logs.log";

	public static final String SQL_USERNAME = "sql_username";
	public static final String SQL_PASSWORD = "sql_password";

	public static final String UNDERTOWHOST ="undertowHost";
	public static final String UNDERTOWPORT ="undertowPort";
	public static final String UNDERTOWSERVER ="undertowPort";

	public static final int ES_MAX_RESULT_WINDOW = 1000000000;
	public static final int ES_MIN_SIZE = 0;

	/**
	 * 公共返回参数Key
	 *
	 * @author Eric.Huang
	 * @date 2018-06-28 22:58
	 **/
	public interface ResponseKeys {
		/**
		 * 状态位
		 */
		String STATUS = "status";

		/**
		 * 返回提示信息
		 */
		String MESSAGE = "message";

		/**
		 * 签名认证信息
		 */
		String SIGN = "sign";
	}

	/**
	 * 接口公共请求参数KEY
	 *
	 * @author Eric.Huang
	 * @date 2018-06-28 22:56
	 **/
	public interface RequestKeys {
		/**
		 * 时间戳
		 */
		public static final String TIMESTAMP = "timestamp";

		/**
		 * 客户端版本号
		 */
		public static final String VERSION = "version";

		/**
		 * 客户端版本标识
		 */
		public static final String PLATFORM = "platform";

		/**
		 * 签名认证信息
		 */
		public static final String SIGN = "sign";

		/**
		 * 登录令牌
		 */
		public static final String JWT = "Jwt";

		/**
		 * 请求主要数据域
		 */
		public static final String DATA = "data";
	}

	/**
	 * 平台
	 */
	public interface Platform {
		String ANDROID = "android";
		String IOS = "ios";
		String WECHAT = "wechat";
		String QYWECHAT = "qywechat";
	}
}
