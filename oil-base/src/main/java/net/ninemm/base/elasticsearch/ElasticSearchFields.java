package net.ninemm.base.elasticsearch;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chen.xuebing
 * @date 2019/7/24 11:46
 */
public class ElasticSearchFields {

	private List<ElasticSearchField> fields;

	public ElasticSearchFields() {
		this.fields = new LinkedList();
	}

	public static ElasticSearchFields create() {
		return new ElasticSearchFields();
	}

	public static ElasticSearchFields create(ElasticSearchField field) {
		ElasticSearchFields that = new ElasticSearchFields();
		that.add(field);
		return that;
	}

	public static ElasticSearchFields create(List<ElasticSearchField> fields) {
		ElasticSearchFields that = new ElasticSearchFields();
		that.fields = fields;
		return that;
	}

	public static ElasticSearchFields create(String name, Object value) {
		return create().eq(name, value);
	}

	public void add(ElasticSearchField field) {
		if (!field.isMustNeedValue() || field.getValue() != null) {
			this.fields.add(field);
		}
	}

	public void addAll(ElasticSearchFields fields) {
		List<ElasticSearchField> list = fields.getList();
		for (ElasticSearchField field : list) {
			if (!field.isMustNeedValue() || field.getValue() != null) {
				this.fields.add(field);
			}
		}
	}

	public ElasticSearchFields add(String name, Object value) {
		return this.eq(name, value);
	}

	public ElasticSearchFields eq(String name, Object value) {
		this.add(ElasticSearchField.create(name, value));
		return this;
	}

	public ElasticSearchFields ne(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_NOT_EQUALS));
		return this;
	}

	public ElasticSearchFields like(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_LIKE));
		return this;
	}

	public ElasticSearchFields nLike(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_NOT_LIKE));
		return this;
	}

	public ElasticSearchFields in(String name, Object... arrays) {
		this.add(ElasticSearchField.create(name, arrays, ElasticSearchField.LOGIC_IN));
		return this;
	}

	public ElasticSearchFields notIn(String name, Object... arrays) {
		this.add(ElasticSearchField.create(name, arrays, ElasticSearchField.LOGIC_NOT_IN));
		return this;
	}

	public ElasticSearchFields gt(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_GT));
		return this;
	}

	public ElasticSearchFields ge(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_GE));
		return this;
	}

	public ElasticSearchFields lt(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_LT));
		return this;
	}

	public ElasticSearchFields le(String name, Object value) {
		this.add(ElasticSearchField.create(name, value, ElasticSearchField.LOGIC_LE));
		return this;
	}

	public boolean isEmpty() {
		return this.fields == null || this.fields.isEmpty();
	}

	public int size() {
		return this.fields.size();
	}

	public List<ElasticSearchField> getList() {
		return this.fields;
	}
}
