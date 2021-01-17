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

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.utils.StrUtil;
import net.ninemm.base.common.Consts;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * ElasticSearch 搜索
 *
 * @author Eric.Huang
 * @date 2018-09-05 17:27
 **/

public class ElasticSearchSearch {

	private static TransportClient getClient() {
		return ElasticSearchManager.me().getClient();
	}

	/**
	 * 通过ID获取数据
	 *
	 * @param index  索引，类似数据库
	 * @param type   类型，类似表
	 * @param id     数据ID
	 * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
	 * @return
	 */
	public static Map<String, Object> searchDocById(String index, String type, String id, String fields) {
		GetRequestBuilder getRequestBuilder = getClient().prepareGet(index, type, id);
		if (!StrKit.isBlank(fields)) {
			getRequestBuilder.setFetchSource(fields.split(","), null);
		} else {
			getRequestBuilder.setFetchSource(true);
		}
		GetResponse getResponse = getRequestBuilder.execute().actionGet();
		return getResponse.getSource();
	}

	/**
	 * 根据索引名称与索引值，查询索引文档列表
	 *
	 * @param index
	 * @param type
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public static List<Map<String, Object>> getDocListByField(String index, String type, String fieldName, String fieldValue) {

		if (!StrKit.notBlank(index, type, fieldName, fieldValue)) {
			return null;
		}

		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index).setTypes(type);
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(fieldName, fieldValue);
		searchRequestBuilder.setQuery(termQueryBuilder);
		searchRequestBuilder.setSize(Consts.ES_MAX_RESULT_WINDOW);
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		SearchHit[] searchHists = searchResponse.getHits().getHits();
		List<Map<String, Object>> list = Lists.newArrayList();
		for (SearchHit hit : searchHists) {
			Map<String, Object> map = hit.getSourceAsMap();
			list.add(map);
		}
		return list;
	}

	public static List<Map<String, Object>> getDocListByManyFields(String index, String type,String[] includeFields, ElasticSearchFields fields, Map<String, String> sorts) {

		if (!StrKit.notBlank(index, type)) {
			return null;
		}

		if (fields == null || fields.size() == 0) {
			return null;
		}

		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index).setTypes(type);
		searchRequestBuilder.setFetchSource(includeFields, null);

		BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(fields);
		searchRequestBuilder.setQuery(boolQueryBuilder);

		if(sorts != null) {
			for (Map.Entry<String, String> entry : sorts.entrySet()) {
				searchRequestBuilder.addSort(entry.getKey(), (StrKit.isBlank(entry.getValue()) || SortOrder.ASC.toString().equals(entry.getValue())) ? SortOrder.ASC : SortOrder.DESC);
			}
		}

		searchRequestBuilder.setSize(Consts.ES_MAX_RESULT_WINDOW);
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		SearchHit[] searchHists = searchResponse.getHits().getHits();
		List<Map<String, Object>> list = Lists.newArrayList();
		for (SearchHit hit : searchHists) {
			Map<String, Object> map = hit.getSourceAsMap();
			list.add(map);
		}
		return list;
	}

	public static BoolQueryBuilder getBoolQueryBuilder(ElasticSearchFields fields) {
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		for (ElasticSearchField field : fields.getList()) {
			if(ElasticSearchField.LOGIC_EQUALS.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.termQuery(field.getName(), field.getValue()));

			}else if(ElasticSearchField.LOGIC_NOT_EQUALS.equals(field.getLogic())){
				boolQueryBuilder.mustNot(QueryBuilders.termQuery(field.getName(), field.getValue()));

			}else if(ElasticSearchField.LOGIC_LIKE.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.wildcardQuery(field.getName(), Convert.toStr(field.getValue())));

			}else if(ElasticSearchField.LOGIC_NOT_LIKE.equals(field.getLogic())){
				boolQueryBuilder.mustNot(QueryBuilders.wildcardQuery(field.getName(), Convert.toStr(field.getValue())));

			}else if(ElasticSearchField.LOGIC_IN.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.termsQuery(field.getName(), Convert.toStrArray(field.getValue())));

			}else if(ElasticSearchField.LOGIC_NOT_IN.equals(field.getLogic())){
				boolQueryBuilder.mustNot(QueryBuilders.termsQuery(field.getName(), Convert.toStrArray(field.getValue())));

			}else if(ElasticSearchField.LOGIC_GT.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field.getName()).from(field.getValue(), false));

			}else if(ElasticSearchField.LOGIC_GE.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field.getName()).from(field.getValue(), true));

			}else if(ElasticSearchField.LOGIC_LT.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field.getName()).to(field.getValue(), false));

			}else if(ElasticSearchField.LOGIC_LE.equals(field.getLogic())){
				boolQueryBuilder.must(QueryBuilders.rangeQuery(field.getName()).to(field.getValue(), true));

			}
		}
		return boolQueryBuilder;
	}

	public static Page<Map<String, Object>> pageTextAnswer(String index, String type, Integer pageNumber, Integer pageSize, JSONObject rawObject) {
		if (!StrKit.notBlank(index, type)) {
			return null;
		}
		List<Map<String,Object>> answerList = new ArrayList<Map<String,Object>>();

		String questionId = rawObject.getString("questionId");
		String surveyId = rawObject.getString("survey_id");
		Integer hasOther = rawObject.getInteger("hasOther");
		String answerKey = rawObject.getString("answerKey");
		String rowOptionId = rawObject.getString("rowOptionId");

		String[] includeField = new String[]{"option_value","end_time","survey_id","question_id","answer_sheet_id","other_value"};
		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index).setTypes(type).setFrom((pageNumber-1)*pageSize).setSize(pageSize);
		searchRequestBuilder.setFetchSource(includeField,null);

		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

		if (StrUtil.isNotEmpty(surveyId)) {
			boolQueryBuilder.must(QueryBuilders.termQuery("survey_id", surveyId));
		}
		if (StrUtil.isNotEmpty(questionId)) {
			boolQueryBuilder.must(QueryBuilders.termQuery("question_id", questionId));
		}
		if (StrUtil.isNotEmpty(answerKey)) {
			if (hasOther!=null && hasOther==1) {
				boolQueryBuilder.must(QueryBuilders.matchQuery("other_value", answerKey));
			}else{
				boolQueryBuilder.must(QueryBuilders.matchQuery("option_value", answerKey));
			}
		}
		if (StrUtil.isNotEmpty(rowOptionId)) {
			boolQueryBuilder.must(QueryBuilders.termQuery("row_option_id", rowOptionId));
		}
		if (hasOther!=null && hasOther==1) {
			boolQueryBuilder.must(QueryBuilders.termQuery("has_other", 1));
		}
		searchRequestBuilder.setQuery(boolQueryBuilder).addSort("end_time",SortOrder.ASC);

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		int totalHits =(int)searchResponse.getHits().getTotalHits();

		SearchHit[] searchHists = searchResponse.getHits().getHits();
		for (SearchHit fields : searchHists) {
			Map<String, Object> map = fields.getSourceAsMap();
			answerList.add(map);
		}
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		page.setTotalRow(totalHits);
		page.setList(answerList);
		page.setPageNumber(pageNumber);
		page.setPageSize(pageSize);
		page.setTotalPage(totalHits/pageSize+1);
		return page;
	}

	public static JSONObject pageAnswer(String index, String type, Integer pageNumber, Integer pageSize, Map map) {
		if (!StrKit.notBlank(index, type)) {
			return null;
		}
		JSONObject result = new JSONObject();
		List<String> answerList = new ArrayList<String>();
		pageNumber = (pageNumber-1)*pageSize;

		String[] includeField = new String[]{"column_value"};
		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index).setTypes(type).setFrom(pageNumber).setSize(pageSize);
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		searchRequestBuilder.setFetchSource(includeField,null);

		Set set = map.keySet();
		for (Object o : set) {
			String key = o.toString();
			boolQueryBuilder.must(QueryBuilders.termQuery(key, map.get(key)));
		}
        searchRequestBuilder.setQuery(boolQueryBuilder);

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		int totalHits =(int)searchResponse.getHits().getTotalHits();

		SearchHit[] searchHists = searchResponse.getHits().getHits();
		for (SearchHit fields : searchHists) {
			answerList.add(fields.getSourceAsMap().get("column_value").toString());
		}
		result.put("list",answerList);
		result.put("page",totalHits);
		return result;
	}

	public static JSONObject pageMajorAnswer(String index, String type, Integer pageNumber, Integer pageSize, String surveyId) {
		if (!StrKit.notBlank(index, type)) {
			return null;
		}
		JSONObject result = new JSONObject();
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();


		String[] includeField = new String[]{"id","ip","start_time","end_time","survey_id","address","total_score"};
		SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(index).setTypes(type).setFrom((pageNumber-1)*pageSize).setSize(pageSize);
		searchRequestBuilder.setFetchSource(includeField,null);
		searchRequestBuilder.setQuery(new BoolQueryBuilder().must(QueryBuilders.termQuery("survey_id", surveyId)));
		searchRequestBuilder.addSort("end_time", SortOrder.ASC);
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
		int totalHits =(int)searchResponse.getHits().getTotalHits();

		SearchHit[] searchHists = searchResponse.getHits().getHits();
		for (SearchHit fields : searchHists) {
			maps.add(fields.getSourceAsMap());
		}
		result.put("list",maps);
		result.put("page",totalHits);
		return result;
	}


}
