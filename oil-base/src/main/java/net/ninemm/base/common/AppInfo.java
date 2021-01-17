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

import io.jboot.app.config.annotation.ConfigModel;

/**
 * 应用信息
 * @author eric
 */
@ConfigModel(prefix = "jboot.admin.app")
public class AppInfo {

    private String name;
    private String org;
    private String type;
    private String orgWebsite;
    private String resourceHost;
    private String copyRight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgWebsite() {
        return orgWebsite;
    }

    public void setOrgWebsite(String orgWebsite) {
        this.orgWebsite = orgWebsite;
    }

    public String getResourceHost() {
        return resourceHost;
    }

    public void setResourceHost(String resourceHost) {
        this.resourceHost = resourceHost;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    @Override
    public String toString() {
        return "AppInfo {" +
            "name='" + name + '\'' +
            ", org='" + org + '\'' +
            ", type='" + type + '\'' +
            ", orgWebsite='" + orgWebsite + '\'' +
            ", resourceHost='" + resourceHost + '\'' +
            ", copyRight='" + copyRight + '\'' +
            '}';
    }
}
