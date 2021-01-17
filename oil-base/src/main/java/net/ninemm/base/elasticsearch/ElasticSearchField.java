package net.ninemm.base.elasticsearch;


/**
 * @author chen.xuebing
 * @date 2019/7/24 11:35
 */
public class ElasticSearchField {
	public static final String LOGIC_EQUALS = " = ";
	public static final String LOGIC_NOT_EQUALS = " != ";
	public static final String LOGIC_LIKE = " LIKE ";
	public static final String LOGIC_NOT_LIKE = " NOT LIKE ";
	public static final String LOGIC_IN = " IN ";
	public static final String LOGIC_NOT_IN = " NOT IN ";
	public static final String LOGIC_GT = " > ";
	public static final String LOGIC_GE = " >= ";
	public static final String LOGIC_LT = " < ";
	public static final String LOGIC_LE = " <= ";

	private String name;
	private Object value;
	private String logic = LOGIC_EQUALS;

	public ElasticSearchField(){

	}

	public static ElasticSearchField create(String name) {
		ElasticSearchField field = new ElasticSearchField();
		field.setName(name);
		return field;
	}

	public static ElasticSearchField create(String name, Object value) {
		ElasticSearchField field = new ElasticSearchField();
		field.setName(name);
		field.setValue(value);
		return field;
	}

	public static ElasticSearchField create(String name, Object value, String logic) {
		ElasticSearchField field = new ElasticSearchField();
		field.setName(name);
		field.setValue(value);
		field.setLogic(logic);
		return field;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {

		this.value = value;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public boolean isMustNeedValue() {
		return !" IS NULL ".equals(this.getLogic()) && !" IS NOT NULL ".equals(this.getLogic());
	}
}
