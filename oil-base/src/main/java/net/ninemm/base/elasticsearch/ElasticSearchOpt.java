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
import com.jfinal.kit.StrKit;
import net.ninemm.base.common.Consts;
import net.ninemm.base.utils.ListUtils;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch 索引增删改查工具类
 *
 * @author Eric.Huang
 * @date 2018-09-04 17:34
 **/

public class ElasticSearchOpt {

    private static final String TYPE_TEXT = "text";
    private static final String TYPE_KEYWORD = "keyword";
    private static final String TYPE_BYTE ="byte";
    private static final String TYPE_SHORT ="short";
    private static final String TYPE_FLOAT ="float";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_DATE = "date";
    private static final String TYPE_GEO = "geo_point";
    private static final String TYPE_IP = "ip";

    private static TransportClient getClient() {
        return ElasticSearchManager.me().getClient();
    }

    /**
     * 判断索引库是否存在
     *
     * @param index
     * @return
     */
    public static boolean isIndexExist(String index) {
        IndicesAdminClient client = getClient().admin().indices();
        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse indicesExistsResponse = client.exists(indicesExistsRequest).actionGet();
        return indicesExistsResponse.isExists();
    }

    /**
     * 创建索引库
     *
     * @param index
     * @return boolean
     */
    public static boolean createIndex(String index) {
        if (!isIndexExist(index)) {
            CreateIndexResponse createIndexResponse = getClient().admin().indices()
                .prepareCreate(index)
                .execute()
                .actionGet();
            LogKit.info("create index is success. index is =>>>" + index);
            return createIndexResponse.isAcknowledged();
        }
        return false;
    }

    public static boolean createIndex(String index, String aliasName) {
        if (isIndexExist(index)) {
            LogKit.error("index [ " + index + " ] is exist.");
            return false;
        }

        CreateIndexRequestBuilder createIndexRequestBuilder = getClient().admin().indices().prepareCreate(index);
        if (StrKit.notBlank(aliasName)) {
            Alias alias = new Alias(aliasName);
            createIndexRequestBuilder.addAlias(alias);
        }

        CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
        LogKit.info("create index is success. index =>>>" + index + ", alias =>>>" + aliasName);
        return createIndexResponse.isAcknowledged();
    }

    public static boolean addAlias(String index, String alias) {

        if (!isIndexExist(index)) {
            LogKit.error("index [ " + index + " ] is not exist, you can't add alias.");
            return false;
        }

        if (index.equals(alias)) {
            LogKit.error("index exists with the same name as the alias");
            return false;
        }

        IndicesAdminClient client = getClient().admin().indices();
        IndicesAliasesResponse indicesAliasesResponse = client.prepareAliases().addAlias(index, alias).execute().actionGet();
        return indicesAliasesResponse.isAcknowledged();
    }

    public static boolean removeAlias(String index, String alias) {

        if (!isIndexExist(index)) {
            LogKit.error("index [ " + index + " ] is not exist, you can't add alias.");
            return false;
        }

        if (!StrKit.notBlank(index, alias)) {
            LogKit.error("index and alias couldn't be null at same time");
            return false;
        }

        IndicesAdminClient client = getClient().admin().indices();
        IndicesAliasesResponse indicesAliasesResponse = client.prepareAliases().removeAlias(index, alias).execute().actionGet();
        return indicesAliasesResponse.isAcknowledged();
    }

    public static boolean exchangeIndex(String srcIndex, String destIndex, String alias) {
        IndicesAdminClient client = getClient().admin().indices();
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest(alias);
        AliasesExistResponse aliasesExistResponse = client.aliasesExist(getAliasesRequest).actionGet();
        if (!aliasesExistResponse.isExists()) {
            LogKit.error("index alias = " + alias + " is not exist.");
            return false;
        }

        IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(srcIndex, destIndex);
        IndicesExistsResponse indicesExistsResponse = client.exists(indicesExistsRequest).actionGet();
        if (!indicesExistsResponse.isExists()) {
            LogKit.error("index is not exist. srcIndex =>>> " + srcIndex + ", destIndex =>>>" + destIndex);
            return false;
        }

        // 原子操作,不用担心索引同步问题
        IndicesAliasesResponse indicesAliasesResponse = client.prepareAliases().removeAlias(srcIndex, alias)
            .addAlias(destIndex, alias).execute().actionGet();
        LogKit.info("exchange index is success! srcIndex is " + srcIndex + ", and destIndex is " + destIndex);
        return indicesAliasesResponse.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @author
     * @date  2018-09-18 17:03
     * @param index
     * @return boolean
     */
    public static boolean deleteIndex(String index) {

        if (!isIndexExist(index)) {
            LogKit.error("index [ " + index + " ] is not exist ");
            return false;
        }

        DeleteIndexResponse indexResponse = getClient().admin().indices().prepareDelete(index).execute().actionGet();
        return indexResponse.isAcknowledged();
    }




    /**
     * 批量新增索引每1000条更新一次
     *
     * @param index         索引库名
     * @param type          索引类型名
     * @param list          索引数据列表
     * @return boolean
     */
    public static boolean bulkAddDocs(String index, String type, List<Map<String, Object>> list) {
        if (ListUtils.isEmpty(list)) {
            return true;
        }
        TransportClient client = getClient();
        BulkRequestBuilder requestBuilder = client.prepareBulk();
        requestBuilder.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        list.stream().forEach(doc -> {
            String docId = doc.get("id") != null ? doc.get("id").toString() : StrKit.getRandomUUID();
            requestBuilder.add(client.prepareIndex(index, type).setId(docId).setSource(doc));
        });

        BulkResponse bulkResponse = requestBuilder.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            LogKit.info(bulkResponse.getItems().length + "条索引创建完成!");
        } else {
            Arrays.stream(bulkResponse.getItems()).forEach(item -> {
                LogKit.error(item.getFailureMessage());
            });
        }
        return !bulkResponse.hasFailures();
    }

    public static boolean bulkUpdateDocs(String index, String type, List<Map<String, Object>> list) {

        TransportClient client = getClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (Map doc : list) {
            bulkRequestBuilder.add(client.prepareUpdate(index, type, doc.get("id").toString()).setDoc(doc));
        }
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            LogKit.info(bulkResponse.getItems().length + "条数据索引创建失败!");
        } else {
            Arrays.stream(bulkResponse.getItems()).forEach(item -> {
                LogKit.error(item.getFailureMessage());
            });
        }

        return !bulkResponse.hasFailures();
    }

    /**
     * 添加文档索引
     *
     * @param document       文档信息
     * @param index     索引，类似数据库
     * @param type      类型，类似表
     * @param id        文档ID
     * @return boolean
     */
    public static String insert(String index, String type, String id, Map<String, Object> document) {

        IndexResponse indexResponse = getClient().prepareIndex(index, type, id).setSource(document).get();
        return indexResponse.getId();
    }

    /**
     * Document 存在则更新，不存在则添加文档
     *
     * @param index
     * @param type
     * @param id
     * @param document
     * @return boolean
     */
    public static boolean upsert(String index, String type, String id, Map<String, Object> document) {

        if (document == null || !StrKit.notBlank(index, type, id)) {
            return false;
        }
        Map<String, Object> src = ElasticSearchSearch.searchDocById(index, type, id, null);
        if (src == null) {
            src = document;
        }
        IndexRequest indexRequest = new IndexRequest(index, type, id).source(src);
        UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(document).upsert(indexRequest);
        UpdateResponse updateResponse = getClient().update(updateRequest).actionGet();
        updateResponse.setForcedRefresh(true);

        return updateResponse.forcedRefresh();
    }

    /**
     * 通过ID 更新数据
     *
     * @param doc 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return boolean
     */
    public static void updateDocById(String index, String type, String id, Map<String, Object> doc) {

        if (!StrKit.notBlank(index, type, id) || doc == null) {
            return ;
        }

        UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(doc);
        getClient().update(updateRequest).actionGet();
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     * @return boolean 是否成功
     */
    public static boolean deleteById(String index, String type, String id) {
        DeleteResponse response = getClient().prepareDelete(index, type, id).execute().actionGet();
        response.getResult();
        return true;
    }

    /**
     * 批量删除doc
     * @param index
     * @param type
     * @param list
     * @return boolean 是否成功
     */
    public static boolean bulkDeleteDocs(String index, String type, List<Map<String, Object>> list) {
        BulkRequestBuilder bulkRequest = getClient().prepareBulk();
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        list.stream().forEach(map -> {
            DeleteRequest deleteRequest = new DeleteRequest(index, type, map.get("id").toString());
            bulkRequest.add(deleteRequest);
        });

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if (!bulkResponse.hasFailures()) {
            LogKit.info(bulkResponse.getItems().length + "条索引创建完成!");
        } else {
            Arrays.stream(bulkResponse.getItems()).forEach(item -> {
                LogKit.error(item.getFailureMessage());
            });
        }

        return !bulkResponse.hasFailures();
    }

    public static void bulkDeleteByQuery(String index, String type, Map<String, Object> param) {

        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        param.forEach((key, value) -> {
            queryBuilder.must(QueryBuilders.termQuery(key, value));
        });

        BulkByScrollResponse response =
            DeleteByQueryAction.INSTANCE.newRequestBuilder(getClient())
            .filter(queryBuilder)
            .source(index)
            .get();

        long deleted = response.getDeleted();
        LogKit.info(deleted + "条数据删除索引完成!");
    }

    public static void bulkUpdateByQuery(String index, String type, String sellerCustomerId, Map<String, Object> param) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.must(QueryBuilders.termQuery("sellerCustomerId", sellerCustomerId));
        UpdateByQueryRequestBuilder requestBuild = UpdateByQueryAction.INSTANCE.newRequestBuilder(getClient())
            .filter(queryBuilder)
            .source(index)
            .abortOnVersionConflict(false);

        param.forEach((key, value) -> {
            requestBuild.script(new Script("ctx._source." + key + "='" + value + "'"));
        });
        BulkByScrollResponse response = requestBuild.get();

        List<BulkItemResponse.Failure> list = response.getBulkFailures();
    }

    public static String[] getAllIndex() {
        ClusterAdminClient clusterAdminClient = getClient().admin().cluster();
        ClusterStateResponse clusterStateResponse =  clusterAdminClient.prepareState().execute().actionGet();
        String[] indexs = clusterStateResponse.getState().getMetaData().getConcreteAllIndices();
        return indexs;
    }

    public static boolean createIndexAsMapping(String index, String type) {
        if (isIndexExist(index)) {
            LogKit.error("index is exist, you can't create mapping index.");
            return false;
        }

        CreateIndexRequestBuilder createIndexRequestBuilder = getClient().admin().indices().prepareCreate(index);
        try {
            XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
             .startObject()
             .startObject("properties")



             .endObject().endObject();

            createIndexRequestBuilder.addMapping(type, mapBuilder);

            Settings esSetting = Settings.builder()
             .put("index.max_result_window", Consts.ES_MAX_RESULT_WINDOW)
             .build();

            createIndexRequestBuilder.setSettings(esSetting);

            CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
            LogKit.info("------create mapping index success------");
            return createIndexResponse.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createMatrixIndexAsMapping(String index, String type) {
        if (isIndexExist(index)) {
            LogKit.error("index is exist, you can't create mapping index.");
            return false;
        }

        CreateIndexRequestBuilder createIndexRequestBuilder = getClient().admin().indices().prepareCreate(index);
        try {
            XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
             .startObject()
             .startObject("properties")
             .startObject("id").field("type", TYPE_KEYWORD).endObject();

            createIndexRequestBuilder.addMapping(type, mapBuilder);

            Settings esSetting = Settings.builder()
                                         .put("index.max_result_window", Consts.ES_MAX_RESULT_WINDOW)
                                         .build();

            createIndexRequestBuilder.setSettings(esSetting);

            CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
            LogKit.info("------create mapping index success------");
            return createIndexResponse.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean createAnswerSheetMapping(String index, String type) {
        if (isIndexExist(index)) {
            LogKit.error("index is exist, you can't create mapping index.");
            return false;
        }

        CreateIndexRequestBuilder createIndexRequestBuilder = getClient().admin().indices().prepareCreate(index);
        try {
            XContentBuilder mapBuilder = XContentFactory.jsonBuilder()
                 .startObject()
                 .startObject("properties")
                 .startObject("id").field("type", TYPE_KEYWORD).endObject()

                 .endObject().endObject();

            createIndexRequestBuilder.addMapping(type, mapBuilder);

            Settings esSetting = Settings.builder()
             .put("index.max_result_window", Consts.ES_MAX_RESULT_WINDOW)
             .build();

            createIndexRequestBuilder.setSettings(esSetting);

            CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
            LogKit.info("------create mapping index success------");
            return createIndexResponse.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
