/**
 * Copyright (c) 2015-2016, Eric 黄鑫 (ninemm@126.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ninemm.base.utils;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import io.jboot.utils.StrUtil;

import java.math.BigInteger;

/**
 *
 * @author Eric
 * @date  2018-06-27 16:52
 */

@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class CookieUtils {

	private final static String COOKIE_SEPARATOR = "#TC#";
	private final static int LEN = 4;

	public static void put(Controller ctr, String key, String value) {
		put(ctr, key, value, 60 * 60 * 24 * 7);
	}

	public static void put(Controller ctr, String key, BigInteger value) {
		put(ctr, key, value.toString());
	}
	
	public static void put(Controller ctr, String key, long value) {
		put(ctr, key, value+"");
	}

	public static void put(Controller ctr, String key, String value, int maxAgeInSeconds) {
		String encryptKey = PropKit.get("encrypt_key");
		String saveTime = System.currentTimeMillis() + "";
		String encryptValue = encrypt(encryptKey, saveTime, maxAgeInSeconds + "", value);

		String cookieValue = encryptValue + COOKIE_SEPARATOR + saveTime + COOKIE_SEPARATOR + maxAgeInSeconds
				+ COOKIE_SEPARATOR + value;

		ctr.setCookie(key, cookieValue, maxAgeInSeconds);

	}

	private static String encrypt(String encryptKey, String saveTime, String maxAgeInSeconds, String value) {
		return HashKit.md5(encryptKey + saveTime + maxAgeInSeconds + value);
	}

	public static void remove(Controller ctr, String key) {
		ctr.removeCookie(key);
	}

	public static String get(Controller ctr, String key) {

		String encryptKey = PropKit.get("encrypt_key");
		String cookieValue = ctr.getCookie(key);

		return getFromCookieInfo(encryptKey, cookieValue);
	}

	public static String getFromCookieInfo(String encryptKey, String cookieValue) {
		if (StrUtil.isNotBlank(cookieValue)) {
			String[] cookieStrings = cookieValue.split(COOKIE_SEPARATOR);
			if (null != cookieStrings && LEN == cookieStrings.length) {
				String encryptValue = cookieStrings[0];
				String saveTime = cookieStrings[1];
				String maxAgeInSeconds = cookieStrings[2];
				String value = cookieStrings[3];

				String encrypt = encrypt(encryptKey, saveTime, maxAgeInSeconds, value);

				/**
                 * 保证 cookie 不被人为修改
                 */
				if (encryptValue != null && encryptValue.equals(encrypt)) {
					long stime = Long.parseLong(saveTime);
					long maxtime = Long.parseLong(maxAgeInSeconds) * 1000;

					/**
                     *查看是否过时
                     */
					if ((stime + maxtime) - System.currentTimeMillis() > 0) {
						return value;
					}
				}
			}
		}
		return null;
	}

	public static Long getLong(Controller ctr, String key) {
		String value = get(ctr, key);
		return null == value ? null : Long.parseLong(value);
	}

	public static long getLong(Controller ctr, String key, long defalut) {
		String value = get(ctr, key);
		return null == value ? defalut : Long.parseLong(value);
	}

	public static Integer getInt(Controller ctr, String key) {
		String value = get(ctr, key);
		return null == value ? null : Integer.parseInt(value);
	}

	public static int getLong(Controller ctr, String key, int defalut) {
		String value = get(ctr, key);
		return null == value ? defalut : Integer.parseInt(value);
	}

}
