package org.saladframework.dao.util;

/**
 * @author CuiShiFeng
 * @date
 */
public class OutSQL {

    private String sql;

    public OutSQL() {

    }

    public OutSQL(String sql) {
        this.sql = sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

}