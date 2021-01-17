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

import com.jfinal.kit.LogKit;
import io.jboot.Jboot;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * ElasticSearch 工具类
 *
 * @author Eric.Huang
 * @date 2018-09-04 14:25
 **/

public class ElasticSearchManager {

    private TransportClient client;
    private static ElasticSearchManager me = new ElasticSearchManager();

    public static ElasticSearchManager me() {
        return me;
    }

    public TransportClient getClient() {
        if (this.client == null) {
            ElasticSearchConfig config = Jboot.config(ElasticSearchConfig.class);
            this.client = init(config);
        }
        return client;
    }

    private TransportClient config(ElasticSearchConfig config) {
        try {
            Settings settings = Settings.builder()
                // 设置集群名称
                .put("cluster.name", config.getClusterName())
                // 启动嗅探功能，自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                //.put("client.transport.sniff", config != null ? config.getSniff() : true)
                // 略集群名字验证, 打开后集群名字不对也能连接上
//                .put("client.transport.ignore_cluster_name", true)
//                .put("client.transport.nodes_sampler_interval", 5)
                // ping等待时间
                //.put("client.transport.ping_timeout", "5s")
                .build();

            TransportAddress transportAddress =
                new TransportAddress(InetAddress.getByName(config.getHost()), config.getPort());
            return new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
        } catch (Exception e) {
            LogKit.error("init ES client failure!");
        }
        return null;
    }

    /**
     * 获取ES客户端
     * @return
     */
    private TransportClient init(ElasticSearchConfig config) {
        try {
            Settings settings = Settings.builder()
                // 设置集群名称
                .put("cluster.name", config != null ? config.getClusterName() : "elasticsearch")
                // 启动嗅探功能，自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                //.put("client.transport.sniff", config != null ? config.getSniff() : true)
                // 略集群名字验证, 打开后集群名字不对也能连接上
//                .put("client.transport.ignore_cluster_name", true)
//                .put("client.transport.nodes_sampler_interval", 5)
                // ping等待时间
                //.put("client.transport.ping_timeout", "5s")
                .build();

            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(config != null ? config.getHost() : "192.168.0.188"), config.getPort());
            return new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
        } catch (Exception e) {
            LogKit.error("Init ElasticSearch TransportClient Failure!");
        }
        return null;
    }

    public void close() {
        if (getClient()!= null) {
            getClient().close();
        }
    }
}
