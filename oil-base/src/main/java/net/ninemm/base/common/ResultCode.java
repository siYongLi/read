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
 * 返回结果常量定义
 * @author Eric
 *
 */
public class ResultCode {

    /** 成功 */
    public final static int SUCCESS = 0;

    /** 执行异常 */
    public final static int ERROR = 1;

    /** FALLBACK异常 */
    public final static int FALLBACK_ERROR = 2;

    /** 参数异常 */
    public final static int PARAM_ERROR = 201;

    /** 解密失败 */
    public final static int DECODE_ERROR = 301;

    /** 请求超时 */
    public final static int OVERTIME_ERROR = 401;

    /** 签名错误 */
    public final static int SIGN_ERROR = 402;

    /** 验证码错误 */
    public final static int MSG_CODE_ERROR = 403;

    /** 信息已存在错误 */
    public final static int EXIST_ERROR = 404;

    /** Token错误 */
    public final static int TOKEN_ERROR = 405;

    /** 密钥不匹配错误 */
    public final static int SECRET_ERROR = 406;

    /** 请求错误 */
    public final static int REQUEST_ERROR = 407;

    /** 服务器异常 */
    public final static int SYSTEM_ERROR = 501;

    /** 其他错误 */
    public final static int OTHER_ERROR = 999;
}
