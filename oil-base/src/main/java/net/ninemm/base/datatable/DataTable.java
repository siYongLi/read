package net.ninemm.base.datatable;

import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Vue, ElementUI 数据表格
 * @author eric
 */
public class DataTable<T> {

    private int code = 0;
    private String msg;
    private int total;
    private List<T> records;

    public DataTable() {}

    public DataTable(Page<T> page) {
        this.total = page.getTotalRow();
        this.records = page.getList();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
