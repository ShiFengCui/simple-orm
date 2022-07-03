/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cuishifeng.crm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.cuishifeng.crm.util.StringChecker;

import com.google.common.base.Strings;

/**
 * 基础查询条件
 * @author cuishifeng
 * @Title: DbQuery
 * @ProjectName org.saladframework.dao
 * @date 2018-10-09
 */
public class DbQuery {
    /**
     * 查询条件的值
     */
    private List<Object> values = new ArrayList<Object>();
    /**
     * sql
     */
    private StringBuilder sql = new StringBuilder();

    public DbQuery select() throws Exception {
        return select("*");
    }

    public DbQuery select(String columns) throws Exception {
        String[] colAry = columns.split(",");
        for (int i = 0; i < colAry.length; i++) {
            StringChecker.checkColumn(colAry[i]);
        }

        sql.insert(0, " SELECT ");
        if (Strings.isNullOrEmpty(columns)) {
            sql.append(" * ");
        } else {
            sql.append(columns).append(' ');
        }

        return this;
    }

    public DbQuery from(String tableName) throws Exception {
        //表名合法性检查
        StringChecker.checkTableName(tableName);

        sql.append(" FROM ").append(tableName);

        return this;
    }

    public DbQuery as(String as) throws Exception {
        if (as != null && as.length() > 0) {
            //as合法性检查
            StringChecker.checkTableName(as);

            sql.append(" AS ").append(as);
        }

        return this;
    }

    public DbQuery leftJoin(String tableName) throws Exception {
        return join("LEFT", tableName);
    }

    public DbQuery rightJoin(String tableName) throws Exception {
        return join("RIGHT", tableName);
    }

    public DbQuery innerJoin(String tableName) throws Exception {
        return join("INNER", tableName);
    }

    public DbQuery fullJoin(String tableName) throws Exception {
        return join("OUTER", tableName);
    }

    private DbQuery join(String type, String tableName) throws Exception {
        StringChecker.checkTableName(tableName);

        sql.append(' ').append(type).append(" JOIN ").append(tableName);
        return this;
    }

    public DbQuery on(String onColumn1, String onColumn2) throws Exception {
        StringChecker.checkColumn(onColumn1);
        StringChecker.checkColumn(onColumn2);

        sql.append(" ON ").append(onColumn1).append(" = ").append(onColumn2);
        return this;
    }

    public DbQuery between(Object param1, Object param2) {
        sql.append(" BETWEEN ? AND ? ");
        values.add(param1);
        values.add(param2);
        return this;
    }

    public DbQuery where() {
        sql.append(" WHERE ");
        return this;
    }

    public DbQuery limit(int offset, int rows) {
        sql.append(" LIMIT ?,? ");
        values.add(offset);
        values.add(rows);
        return this;
    }

    public DbQuery column(String column) throws Exception {
        StringChecker.checkColumn(column);

        sql.append(" ");
        sql.append(column);
        sql.append(" ");
        return this;
    }

    public DbQuery equal(Object value) {
        sql.append(" =? ");
        values.add(value);
        return this;
    }

    public DbQuery isNull() {
        sql.append(" is null ");
        return this;
    }

    public DbQuery isNotNull() {
        sql.append(" is not null ");
        return this;
    }

    public DbQuery notEqual(Object value) {
        sql.append(" !=? ");
        values.add(value);
        return this;
    }

    public DbQuery greater(Object value) {
        sql.append(" >? ");
        values.add(value);
        return this;
    }

    public DbQuery greaterOrEqual(Object value) {
        sql.append(" >=? ");
        values.add(value);
        return this;
    }

    public DbQuery less(Object value) {
        sql.append(" <? ");
        values.add(value);
        return this;
    }

    public DbQuery lessOrEqual(Object value) {
        sql.append(" <=? ");
        values.add(value);
        return this;
    }

    public DbQuery in(Object... value) {
        // invalid case, create a null pointer exception.

        if (value.length == 1) {
            sql.append(" =? ");
            values.add(value[0]);
            return this;
        }

        sql.append(" IN (");
        for (int i = 0; i < value.length; i++) {
            if (i != 0) {
                sql.append(',');
            }
            sql.append('?');
            values.add(value[i]);
        }
        sql.append(')');
        return this;
    }

    public DbQuery notIn(Object... value) {
        // empty param is invalid by logic, just let the invoker get an invalid sql.

        if (value.length == 1) {
            sql.append(" =? ");
            values.add(value[0]);
            return this;
        }

        sql.append(" NOT IN (");
        for (int i = 0; i < value.length; i++) {
            if (i != 0) {
                sql.append(',');
            }
            sql.append('?');
            values.add(value[i]);
        }
        sql.append(')');
        return this;
    }

    public <T> DbQuery inCollection(Collection<T> value) {
        // empty param is invalid by logic, just let the invoker get an invalid sql.

        Iterator<T> iterator = value.iterator();
        if (value.size() == 1) {
            sql.append(" =? ");
            values.add(iterator.next());
            return this;
        }
        sql.append(" IN (");
        for (int i = 0; i < value.size(); i++) {
            if (i != 0) {
                sql.append(',');
            }
            sql.append('?');
            values.add(iterator.next());
        }
        sql.append(')');
        return this;
    }

    public <T> DbQuery inList(List<T> value) {
        if (value.size() == 1) {
            sql.append(" =? ");
            values.add(value.get(0));
            return this;
        }
        sql.append(" IN (");
        for (int i = 0; i < value.size(); i++) {
            if (i != 0) {
                sql.append(',');
            }
            sql.append('?');
            values.add(value.get(i));
        }
        sql.append(')');
        return this;
    }

    public <T> DbQuery notInList(List<T> value) {
        if (value.size() == 1) {
            sql.append(" =? ");
            values.add(value.get(0));
            return this;
        }
        sql.append("NOT IN (");
        for (int i = 0; i < value.size(); i++) {
            if (i != 0) {
                sql.append(',');
            }
            sql.append('?');
            values.add(value.get(i));
        }

        sql.append(')');
        return this;
    }

    public DbQuery like(Object value) {
        sql.append(" LIKE ? ");
        values.add(value);
        return this;
    }

    public DbQuery and() {
        if (sql.length() > 0 && !sql.toString().trim().endsWith("WHERE")) {
            sql.append(" AND ");
        }
        return this;
    }

    public DbQuery or() {
        if (sql.length() > 0 && !values.isEmpty()) {
            sql.append(" OR ");
        }
        return this;
    }

    public DbQuery mathAnd(long value, long equal) {
        sql.append(" & ? = ?");
        values.add(value);
        values.add(equal);
        return this;
    }

    public DbQuery mathOr(long value, long equal) {
        sql.append(" | ? = ?");
        values.add(value);
        values.add(equal);
        return this;
    }

    public DbQuery count(String column) throws Exception {
        sql.append(" COUNT(");
        sql.append(column);
        sql.append(") ");
        return this;
    }

    public DbQuery distinct(String column) throws Exception {
        StringChecker.checkColumn(column);

        sql.append("SELECT DISTINCT ");
        sql.append(column);
        return this;
    }

    public DbQuery distinct() throws Exception {
        sql.append("SELECT DISTINCT ");
        return this;
    }

    public DbQuery leftBracket() {
        sql.append(" ( ");
        return this;
    }

    public DbQuery rightBracket() {
        sql.append(" ) ");
        return this;
    }

    public DbQuery orderBy(String orderBy) throws Exception {
        if (orderBy == null || orderBy.length() == 0) {
            return this;
        }

        StringChecker.checkOrderBy(orderBy);

        if (sql.toString().trim().endsWith("WHERE")) {
            sql.append(" 1=1 ");
        }
        sql.append(" ORDER BY ").append(orderBy);
        return this;
    }

    public DbQuery groupBy(String groupby) {
        if (sql.toString().trim().endsWith("WHERE")) {
            sql.append(" 1=1 ");
        }
        sql.append(" GROUP BY ").append(groupby);
        return this;
    }

    public DbQuery forUpdate() {
        sql.append(" FOR UPDATE ");
        return this;
    }


    public List<Object> values() {
        return this.values;
    }

    public String toSql() {
        if (sql.toString().trim().endsWith("WHERE")) {
            return sql.toString().replace("WHERE", "");
        }
        return sql.toString();
    }

    public boolean isEmpty() {
        return sql.length() == 0 ? true : false;
    }

}
