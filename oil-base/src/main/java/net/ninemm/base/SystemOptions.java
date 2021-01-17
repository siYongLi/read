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
package net.ninemm.base;

import com.jfinal.log.Log;
import io.jboot.utils.StrUtil;
import net.ninemm.base.common.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric Huang 黄鑫 （ninemm@126.com）
 * @version V1.0
 * @Title: 系统常量
 * @Package cn.ninemm.commons
 */
public class SystemOptions {

    private static final Log LOG = Log.getLog(SystemOptions.class);
    private static OptionStore store = new OptionStore() {
        private final Map<String, String> cache = new ConcurrentHashMap<>();

        @Override
        public String get(String key) {
            return cache.get(key);
        }

        @Override
        public void put(String key, String value) {
            if (StrUtil.isBlank(value)) {
                remove(key);
            } else {
                cache.put(key, value);
            }
        }

        @Override
        public void remove(String key) {
            cache.remove(key);
        }
    };


    private static List<OptionChangeListener> LISTENERS = new ArrayList<>();

    public static void set(String key, String value) {
        if (StrUtil.isBlank(key)) {
            return;
        }


        String oldValue = store.get(key);
        if (Objects.equals(value, oldValue)) {
            return;
        }

        store.put(key, value);


        for (OptionChangeListener listener : LISTENERS) {
            try {
                listener.onChanged(key, value, oldValue);
            } catch (Throwable ex) {
                LOG.error(ex.toString(), ex);
            }
        }

    }


    public static String get(String key) {
        return store.get(key);
    }

    public static boolean getAsBool(String key) {
        return Boolean.parseBoolean(store.get(key));
    }

    public static boolean isTrueOrNull(String key){
        String data = get(key);
        return data == null || "true".equals(data);
    }

    public static int getAsInt(String key, int defaultValue) {
        String value = get(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            LOG.warn(ex.toString(), ex);
            return defaultValue;
        }
    }

    public static float getAsFloat(String key, float defaultValue) {
        String value = get(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(value);
        } catch (Exception ex) {
            LOG.warn(ex.toString(), ex);
            return defaultValue;
        }
    }

    public static void addListener(OptionChangeListener listener) {
        LISTENERS.add(listener);
    }

    public static void removeListener(OptionChangeListener listener) {
        LISTENERS.remove(listener);
    }


    public static String getCDNDomain() {
        boolean cdnEnable = getAsBool(Consts.OPTION_CDN_ENABLE);
        if (cdnEnable == false) {
            return null;
        }

        String cdnDomain = get(Consts.OPTION_CDN_DOMAIN);
        return StrUtil.isBlank(cdnDomain) ? null : cdnDomain;
    }

    public static String getResDomain() {
        String cdnDomain = getCDNDomain();
        return cdnDomain == null ? get(Consts.OPTION_WEB_DOMAIN) : cdnDomain;
    }

    public static interface OptionChangeListener {
        public void onChanged(String key, String newValue, String oldValue);
    }


    private static final String indexStyleKey = "index_style";

    private static String indexStyleValue = null;

    public static String getIndexStyle() {
        return indexStyleValue;
    }


    private static boolean fakeStaticEnable = false;
    private static String fakeStaticSuffix = "";

    public static String getAppUrlSuffix() {
        return fakeStaticEnable
                ? (StrUtil.isBlank(fakeStaticSuffix) ? "" : fakeStaticSuffix)
                : "";
    }

    public static OptionStore getStore() {
        return store;
    }

    public static void setStore(OptionStore store) {
        SystemOptions.store = store;
    }

    public static interface OptionStore {

        public String get(String key);

        public void put(String key, String value);

        public void remove(String key);

    }


}
