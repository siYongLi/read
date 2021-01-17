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
package net.ninemm.base.utils;

import com.jfinal.core.JFinal;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Http 请求工具类
 *
 * @author Eric Huang
 * @date 2018-06-28 18:58
 **/
public class HttpUtils {

	private static final String TAG = "HttpUtils";

	/**
	 * 10秒
 	 */
	private static final int M_READ_TIME_OUT = 1000 * 10;

	/**
	 * 5秒
	 */
	private static final int M_CONNECT_TIME_OUT = 1000 * 5;

	/**
	 * 默认尝试访问次数
	 */
	private static final int M_RETRY = 2;

	private static final String STR_HTTPS = "https";
	private static final String STR_QUESTION_MARK = "?";
	private static final Character CHAR_QUESTION_MARK = '?';
	private static final Character CHAR_AND_SYMBOL = '&';

	private static final String CHAR_SET = JFinal.me().getConstants().getEncoding();

	public static String get(String url) throws Exception {
		return get(url, null);
	}

	public static String get(String url, Map<String, ? extends Object> params) throws Exception {
		return get(url, params, null);
	}

	public static String get(String url, Map<String, ? extends Object> params, Map<String, String> headers)
			throws Exception {
		if (url == null || url.trim().length() == 0) {
			throw new Exception(TAG + ": url is null or empty!");
		}

		if (params != null && params.size() > 0) {
			if (!url.contains(STR_QUESTION_MARK)) {
				url += "?";
			}

			if (url.charAt(url.length() - 1) != CHAR_QUESTION_MARK) {
				url += "&";
			}

			url += buildParams(params);
		}

		return tryToGet(url, headers);
	}

	public static String buildParams(Map<String, ? extends Object> params) throws UnsupportedEncodingException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				builder.append(entry.getKey().trim()).append("=")
					.append(URLEncoder.encode(entry.getValue().toString(), CHAR_SET)).append("&");
			}
		}

		if (builder.charAt(builder.length() - 1) == CHAR_AND_SYMBOL.charValue()) {
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}

	private static String tryToGet(String url, Map<String, String> headers) throws Exception {
		int tryTime = 0;
		Exception ex = null;
		while (tryTime < M_RETRY) {
			try {
				return doGet(url, headers);
			} catch (Exception e) {
				if (e != null) {
					ex = e;
				}
				tryTime++;
			}
		}
		if (ex != null) {
			throw ex;
		} else {
			throw new Exception("未知网络错误 ");
		}
	}

	private static String doGet(String strUrl, Map<String, String> headers) throws Exception {
		HttpURLConnection connection = null;
		InputStream stream = null;
		try {

			connection = getConnection(strUrl);
			configConnection(connection);
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			connection.setInstanceFollowRedirects(true);
			connection.connect();

			stream = connection.getInputStream();
			ByteArrayOutputStream obs = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for (int len = 0; (len = stream.read(buffer)) > 0;) {
				obs.write(buffer, 0, len);
			}
			obs.flush();
			obs.close();
			stream.close();

			return new String(obs.toByteArray());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}

	public static String post(String url) throws Exception {
		return post(url, null);
	}

	public static String post(String url, Map<String, ? extends Object> params) throws Exception {
		return post(url, params, null);
	}

	public static String post(String url, Map<String, ? extends Object> params, Map<String, String> headers)
			throws Exception {
		if (url == null || url.trim().length() == 0) {
			throw new Exception(TAG + ":url is null or empty!");
		}

		if (params != null && params.size() > 0) {
			return tryToPost(url, buildParams(params), headers);
		} else {
			return tryToPost(url, null, headers);
		}
	}

	public static String post(String url, String content, Map<String, String> headers) throws Exception {
		return tryToPost(url, content, headers);
	}

	private static String tryToPost(String url, String postContent, Map<String, String> headers) throws Exception {
		int tryTime = 0;
		Exception ex = null;
		while (tryTime < M_RETRY) {
			try {
				return doPost(url, postContent, headers);
			} catch (Exception e) {
				if (e != null) {
					ex = e;
				}
				tryTime++;
			}
		}
		if (ex != null) {
			throw ex;
		} else {
			throw new Exception("未知网络错误 ");
		}
	}

	private static String doPost(String strUrl, String postContent, Map<String, String> headers) throws Exception {
		HttpURLConnection connection = null;
		InputStream stream = null;
		try {
			connection = getConnection(strUrl);
			configConnection(connection);
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			if (null != postContent && !"".equals(postContent)) {
				DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
				dos.write(postContent.getBytes(CHAR_SET));
				dos.flush();
				dos.close();
			}
			stream = connection.getInputStream();
			ByteArrayOutputStream obs = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			for (int len = 0; (len = stream.read(buffer)) > 0;) {
				obs.write(buffer, 0, len);
			}
			obs.flush();
			obs.close();

			return new String(obs.toByteArray());

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (stream != null) {
				stream.close();
			}
		}

	}

	private static void configConnection(HttpURLConnection connection) {
		if (connection == null) {
			return;
		}
		connection.setReadTimeout(M_READ_TIME_OUT);
		connection.setConnectTimeout(M_CONNECT_TIME_OUT);

		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
	}

	private static HttpURLConnection getConnection(String strUrl) throws Exception {
		if (strUrl == null) {
			return null;
		}
		if (strUrl.toLowerCase().startsWith(STR_HTTPS)) {
			return getHttpsConnection(strUrl);
		} else {
			return getHttpConnection(strUrl);
		}
	}

	private static HttpURLConnection getHttpConnection(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return conn;
	}

	private static HttpsURLConnection getHttpsConnection(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setHostnameVerifier(hnv);
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		if (sslContext != null) {
			TrustManager[] tm = { xtm };
			sslContext.init(null, tm, null);
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			conn.setSSLSocketFactory(ssf);
		}

		return conn;
	}

	private static X509TrustManager xtm = new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private static HostnameVerifier hnv = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

}
