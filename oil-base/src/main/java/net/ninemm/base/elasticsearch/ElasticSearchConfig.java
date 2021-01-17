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

package net.ninemm.base.elasticsearch;

import io.jboot.app.config.annotation.ConfigModel;

/**
 * ElasticSearch 配置文件
 *
 * @author Eric.Huang
 * @date 2018-09-04 15:34
 **/

@ConfigModel(prefix = "jboot.search.es")
public class ElasticSearchConfig {

    private String host;
    private Integer port = 9300;
    private Boolean sniff = true;
    private String clusterName = "elasticsearch";
    private String pingTimeout;
    private Boolean ignoreClusterName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(String pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public Boolean getSniff() {
        return sniff;
    }

    public void setSniff(Boolean sniff) {
        this.sniff = sniff;
    }

    public Boolean getIgnoreClusterName() {
        return ignoreClusterName;
    }

    public void setIgnoreClusterName(Boolean ignoreClusterName) {
        this.ignoreClusterName = ignoreClusterName;
    }
}
