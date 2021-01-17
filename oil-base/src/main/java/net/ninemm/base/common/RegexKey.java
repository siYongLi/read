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

import java.util.regex.Pattern;

/**
 * 正则校验常量类
 * @author Eric
 *
 */
public class RegexKey {

    public final static String MOBILE = "^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$";

    public final static String EMAIL = "\\w+@(\\w+.)+[a-z]{2,3}";

    public static boolean isMobile(String mobile) {
        return Pattern.matches(RegexKey.MOBILE, mobile);
    }
    public static boolean isEmail(String email) {
        return Pattern.matches(RegexKey.EMAIL, email);
    }
}
