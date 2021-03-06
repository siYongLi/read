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
 *
 */
package net.ninemm.base.utils;

import com.jfinal.plugin.activerecord.Model;
import io.jboot.utils.StrUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

/**
 * @author Eric Huang 黄鑫 （ninemm@126.com）
 * @version V1.0
 * @Package net.ninemm.base.utils
 */
public class CommonsUtils {


    public static String generateCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9999 - 1000 + 1) + 1000);
    }

    public static void quietlyClose(AutoCloseable... autoCloseables) {
        for (AutoCloseable closeable : autoCloseables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }


    public static String maxLength(String content, int maxLength) {
        if (StrUtil.isBlank(content)) {
            return content;
        }

        if (maxLength <= 0) {
            throw new IllegalArgumentException("maxLength 必须大于 0 ");
        }

        return content.length() <= maxLength ? content :
                content.substring(0, maxLength);

    }

    public static String maxLength(String content, int maxLength, String suffix) {
        if (StrUtil.isBlank(suffix)) {
            return maxLength(content, maxLength);
        }

        if (StrUtil.isBlank(content)) {
            return content;
        }

        if (maxLength <= 0) {
            throw new IllegalArgumentException("maxLength 必须大于 0 ");
        }

        return content.length() <= maxLength ? content :
                content.substring(0, maxLength) + suffix;

    }

    public static String removeSuffix(String url) {

        int indexOf = url.indexOf(".");

        if (indexOf == -1) {
            return url;
        }

        return url.substring(0, indexOf);
    }

    /**
     * 防止 model 存储关于 xss 相关代码
     *
     * @param model
     */
    public static void escapeHtmlForAllAttrs(Model model, String... ignoreAttrs) {
        String[] attrNames = model._getAttrNames();
        for (String attr : attrNames) {

            if (ArrayUtils.contains(ignoreAttrs, attr)) {
                continue;
            }

            Object value = model.get(attr);

            if (value != null && value instanceof String) {
                model.set(attr, escapeHtml(value.toString()));
            }
        }
    }

    public static String escapeHtml(String content) {

        if (StrUtil.isBlank(content)) {
            return content;
        }

        /**
         "&lt;" represents the < sign.
         "&gt;" represents the > sign.
         "&amp;" represents the & sign.
         "&quot; represents the " mark.
         */

        return unEscapeHtml(content)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("'", "&#39;")
                .replace("\"", "&quot;");
    }

    public static String unEscapeHtml(String content) {

        if (StrUtil.isBlank(content)) {
            return content;
        }

        return content
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#39;", "'")
                .replace("&quot;", "\"")
                .replace("&amp;", "&");
    }

    public static String getValidateCode() {
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }

    public static void main(String[] args) {
        String script = "<script>alert(\"abc\");</script>";
        System.out.println(escapeHtml(script));
        System.out.println(escapeHtml(escapeHtml(script)));
        System.out.println(unEscapeHtml(escapeHtml(script)));
        System.out.println(generateCode());
    }
}
