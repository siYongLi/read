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

package net.ninemm.base.gencode.model;

import io.jboot.Jboot;
import io.jboot.app.config.annotation.ConfigModel;

/**
 * model代码生成配置
 * @author Eric
 *
 */
@ConfigModel(prefix="jboot.admin.model.ge")
public class AppModelGeneratorConfig {

    /**
     * entity 包名
     */
    private String modelpackage;

    /**
     * 移除的表前缀
     */
    private String removedtablenameprefixes;

    /**
     * 不包含表
     */
    private String excludedtable;

    /**
     * 包含表
     */
    private String includedtable;

    /**
     * 不包含表前缀
     */
    private String excludedtableprefixes;

    public String getModelpackage() {
        return modelpackage;
    }

    public void setModelpackage(String modelpackage) {
        this.modelpackage = modelpackage;
    }

    public String getRemovedtablenameprefixes() {
        return removedtablenameprefixes;
    }

    public void setRemovedtablenameprefixes(String removedtablenameprefixes) {
        this.removedtablenameprefixes = removedtablenameprefixes;
    }

    public String getExcludedtable() {
        return excludedtable;
    }

    public void setExcludedtable(String excludedtable) {
        this.excludedtable = excludedtable;
    }

    public String getIncludedtable() {
        if (includedtable==null) {
            includedtable= Jboot.configValue("includedtable");
        }
        return includedtable;
    }

    public void setIncludedtable(String includedtable) {
        this.includedtable = includedtable;
    }

    public String getExcludedtableprefixes() {
        return excludedtableprefixes;
    }

    public void setExcludedtableprefixes(String excludedtableprefixes) {
        this.excludedtableprefixes = excludedtableprefixes;
    }

    @Override
    public String toString() {
        return "AppGeneratorConfig{" +
            " modelpackage='" + modelpackage + '\'' +
            ", removedtablenameprefixes='" + removedtablenameprefixes + '\'' +
            ", includedtable='" + includedtable + '\'' +
            ", excludedtable='" + excludedtable + '\'' +
            ", excludedtableprefixes='" + excludedtableprefixes + '\'' +
            '}';
    }
}
