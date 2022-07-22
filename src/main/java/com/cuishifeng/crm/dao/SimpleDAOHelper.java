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

package com.cuishifeng.crm.dao;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cuishifeng.crm.model.KeyValue;
import com.cuishifeng.crm.model.KeyValueParis;
import com.cuishifeng.crm.query.Query;
import com.cuishifeng.crm.util.ClassHelper;
import com.cuishifeng.crm.util.GlobalConfigUtils;
import com.cuishifeng.crm.util.JdbcUtil;
import com.cuishifeng.crm.model.OutSQL;
import com.cuishifeng.crm.util.StringChecker;

/**
 * @author CuiShiFeng
 * @date
 */
public class SimpleDAOHelper extends DaoHelper {

    public SimpleDAOHelper() {
        GlobalConfigUtils.init(this);
    }

    @Override
    public <I, T> I insert(T t) throws Exception {
        Class<?> beanCls = t.getClass();
        String tableName = ClassHelper.getTableName(beanCls);
        return this.insert(t, tableName);
    }

    @Override
    public <I, T> I insert(T t, String tableName) throws Exception {
        Class<?> beanCls = t.getClass();

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createInsert(t, conn, sql, tableName);
            ps.setQueryTimeout(INSERT_TIMEOUT);
            ps.executeUpdate();

            List<Field> identityFields = ClassHelper.getIdentityFields(beanCls);
            List<Field> idFields = ClassHelper.getIdFields(beanCls);
            if (identityFields.size() == 1) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return (I) rs.getObject(1);
                }
            } else if (identityFields.size() == 0 && idFields != null && idFields.size() > 0) {
                Method m = ClassHelper.getGetterMethod(beanCls, (idFields.get(0)));
                return (I) m.invoke(t, new Object[]{});
            }
        } catch (Exception e) {
            logger.error("insert error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }

        return null;
    }

    @Override
    public <T> int[] batchInsert(List<T> list) throws Exception {
        if (list == null || list.size() == 0) {
            return null;
        }

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createBatchInsert(list, conn, sql);
            ps.setQueryTimeout(INSERT_TIMEOUT);
            return ps.executeBatch();
        } catch (Exception e) {
            logger.error("batchInsert error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <T> int update(T t) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createUpdateEntity(t, conn, sql);
            ps.setQueryTimeout(INSERT_TIMEOUT);
            return ps.executeUpdate();
        } catch (Exception e) {
            logger.error("update error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <I, T> int updateByIDList(Class<T> cls, KeyValueParis updateKV, List<I> ids) throws Exception {
        if (CollectionUtils.isEmpty(ids)) {
            return NumberUtils.INTEGER_ZERO;
        }
        if (updateKV == null || MapUtils.isEmpty(updateKV.getKvPairs())) {
            throw new Exception("the field to be updated cannot be empty");
        }
        StringChecker.checkColumn(cls, updateKV.getKvPairs());
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createUpdateByID(cls, conn, updateKV.getKvPairs(), sql, ids);
            ps.setQueryTimeout(INSERT_TIMEOUT);
            return ps.executeUpdate();
        } catch (Exception e) {
            logger.error("updateByIDList error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <I, T> int updateByID(Class<T> cls, KeyValueParis updateKV, I id) throws Exception {
        List<I> ids = new ArrayList<I>();
        ids.add(id);

        return updateByIDList(cls, updateKV, ids);
    }

    @Override
    public <T> int updateByQuery(Class<T> cls, KeyValueParis updateKV, Query query) throws Exception {
        if (updateKV.getKvPairs() == null || updateKV.getKvPairs().size() == 0) {
            throw new Exception("待更新的字段不能为空");
        }
        StringChecker.checkColumn(cls, updateKV.getKvPairs());

        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createUpdateByQuery(cls, conn, updateKV.getKvPairs(), query, sql);
            ps.setQueryTimeout(INSERT_TIMEOUT);
            return ps.executeUpdate();
        } catch (Exception e) {
            logger.error("updateByQuery error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <I, T> int deleteByIDList(Class<T> cls, List<I> ids) throws Exception {
        if (ids == null || ids.size() == 0) {
            return 0;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createDeleteByID(cls, conn, ids, sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);
            return ps.executeUpdate();
        } catch (Exception e) {
            logger.error("deleteByIDList error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <I, T> int deleteByID(Class<T> cls, I id) throws Exception {
        List<I> ids = new ArrayList<I>();
        ids.add(id);
        return deleteByIDList(cls, ids);
    }

    @Override
    public <T> int deleteByQuery(Class<T> cls, Query query) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createDeleteByQuery(cls, conn, query, sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);
            return ps.executeUpdate();
        } catch (Exception e) {
            logger.error("deleteByQuery error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <T, I> T getByID(Class<T> cls, I id) throws Exception {
        return getByID(cls, id, null);
    }

    @Override
    public <T, I> T getByID(Class<T> cls, I id, String columns) throws Exception {
        List<I> ids = new ArrayList<I>();
        ids.add(id);
        List<T> list = getsByIDList(cls, ids, columns);
        return (list == null | list.size() == 0) ? null : list.get(0);
    }

    @Override
    public <T, I> List<T> getsByIDList(Class<T> cls, List<I> ids) throws Exception {
        return getsByIDList(cls, ids, null);
    }

    @Override
    public <T, I> List<T> getsByIDList(Class<T> cls, List<I> ids, String columns) throws Exception {
        if (ids == null || ids.size() == 0) {
            return null;
        }

        if (columns != null && columns.length() > 0) {
            StringChecker.checkColumn(cls, columns);
        }

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createGetByID(cls, conn, ids, columns, sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();
            return populateData(rs, cls);
        } catch (SQLException e) {
            logger.error("getsByIDList error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <T> T getOneByQuery(Class<T> cls, Query query, String columns, String orderBy) throws Exception {
        List<T> list = getsByQuery(cls, query, 1, 1, columns, orderBy);
        return (list == null | list.size() == 0) ? null : list.get(0);
    }

    @Override
    public <T> List<T> getsByQuery(Class<T> cls, Query query, String columns, String orderBy) throws Exception {
        return getsByQuery(cls, query, -1, -1, columns, orderBy);
    }

    @Override
    public <T> List<T> getsByQuery(Class<T> cls, Query query, int page, int pageSize, String columns, String orderBy) throws Exception {
        if (columns != null && columns.length() > 0) {
            StringChecker.checkColumn(cls, columns);
        }

        StringChecker.checkOrderBy(orderBy);

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createGetByQuery(cls, conn, query, columns, page, pageSize, orderBy, sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();
            return populateData(rs, cls);
        } catch (SQLException e) {
            logger.error("getsByQuery error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public int count(Class<?> cls, Query query) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();

        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createGetCountByQuery(cls, conn, query, sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("count error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }

        return 0;
    }

    @Override
    public int count(Query fullSQL) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            conn = crudDataSource.getConnection();
            ps = conn.prepareStatement(fullSQL.toSql());

            List<Object> values = fullSQL.values();
            for (int i = 0; i < values.size(); i++) {
                ClassHelper.setPara(ps, values.get(i), i + 1);
            }

            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("count error sql:" + fullSQL.toSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }

        return 0;
    }

    @Override
    public <T> List<T> selectByQuery(Class<T> cls, Query querySQL) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        OutSQL sql = new OutSQL();
        try {
            conn = crudDataSource.getConnection();
            ps = iStatementCreater.createSelectByQuery(conn, querySQL, sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();
            return populateData(rs, cls);
        } catch (SQLException e) {
            logger.error("selectByQuery error sql:" + sql.getSql(), e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public <T> List<T> selectBySQL(Class<T> cls, String sql, Object... params) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = crudDataSource.getConnection();
            ps = conn.prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    ClassHelper.setPara(ps, params[i], i + 1);
                }
            }

            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();

            return populateData(rs, cls);
        } catch (SQLException e) {
            logger.error("selectByQuery error sql:" + sql, e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }


    @Override
    public Map<String, Object> getMap(String sql, Object... param) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = crudDataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    ClassHelper.setPara(ps, param[i], i + 1);
                }
            }

            rs = ps.executeQuery();
            Map<String, Object> map = new HashMap<String, Object>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            if (rs.next()) {
                for (int i = 0; i < columnsCount; i++) {
                    String colName = rsmd.getColumnLabel(i + 1);
                    map.put(colName, rs.getObject(colName));
                }
            }
            return map;
        } catch (SQLException e) {
            logger.error("getMap error sql:" + sql, e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public List<Map<String, Object>> getMaps(String sql, Object... param) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = crudDataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    ClassHelper.setPara(ps, param[i], i + 1);
                }
            }

            rs = ps.executeQuery();

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (int i = 0; i < columnsCount; i++) {
                    String colName = rsmd.getColumnLabel(i + 1);
                    map.put(colName, rs.getObject(colName));
                }
                list.add(map);
            }
            return list;
        } catch (SQLException e) {
            logger.error("getMaps error sql:" + sql, e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public List<List<KeyValue<String, Object>>> getKeyValues(String sql, Object... param) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = crudDataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(QUERY_TIMEOUT);

            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    ClassHelper.setPara(ps, param[i], i + 1);
                }
            }

            rs = ps.executeQuery();

            List<List<KeyValue<String, Object>>> list = new ArrayList<List<KeyValue<String, Object>>>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while (rs.next()) {
                List<KeyValue<String, Object>> row = new ArrayList<KeyValue<String, Object>>();
                for (int i = 0; i < columnsCount; i++) {
                    String colName = rsmd.getColumnLabel(i + 1);

                    KeyValue<String, Object> kv = new KeyValue<String, Object>();
                    kv.setKey(colName);
                    kv.setValue(rs.getObject(colName));

                    row.add(kv);
                }
                list.add(row);
            }
            return list;
        } catch (SQLException e) {
            logger.error("getMaps error sql:" + sql, e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }
    }

    @Override
    public int countBySQL(String sql, Object[] params) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            conn = crudDataSource.getConnection();
            ps = conn.prepareStatement(sql);

            if (params != null) {
                Object[] values = params;
                for (int i = 0; i < values.length; i++) {
                    ClassHelper.setPara(ps, values[i], i + 1);
                }
            }
            ps.setQueryTimeout(QUERY_TIMEOUT);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error("count error sql:" + sql, e);
            throw e;
        } finally {
            JdbcUtil.closeResultSet(rs);
            JdbcUtil.closeStatement(ps);
            crudDataSource.release(conn);
        }

        return 0;
    }
}
