package org.saladframework.dao.statement;


import org.saladframework.dao.DbQuery;
import org.saladframework.dao.util.OutSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * @author CuiShiFeng
 * @date
 */
public interface IStatementCreater {

    public <I> PreparedStatement createDeleteByID(Class<?> clazz, Connection conn, List<I> ids, OutSQL sql) throws Exception;

    public PreparedStatement createDeleteByQuery(Class<?> clazz, Connection conn, DbQuery query, OutSQL sql) throws Exception;


    public <I> PreparedStatement createGetByID(Class<?> clazz, Connection conn, List<I> ids, String columns, OutSQL sql) throws Exception;

    public PreparedStatement createGetByPage(Class<?> clazz, Connection conn, Map<String, Object> condition, String columns, int page, int pageSize, String orderBy, OutSQL sql) throws Exception;

    public PreparedStatement createGetByQuery(Class<?> clazz, Connection conn, DbQuery query, String columns, int page, int pageSize, String orderBy, OutSQL sql) throws Exception;


    public PreparedStatement createGetCountByQuery(Class<?> clazz, Connection conn, DbQuery query, OutSQL sql) throws Exception;


    public <I> PreparedStatement createUpdateByID(Class<?> clazz, Connection conn, Map<String, Object> updateStatement, OutSQL sql, List<I> ids) throws Exception;

    public PreparedStatement createUpdateByQuery(Class<?> clazz, Connection conn, Map<String, Object> kv, DbQuery query, OutSQL sql) throws Exception;

    public <T> PreparedStatement createUpdateEntity(T bean, Connection conn, OutSQL sql) throws Exception;


    public <T> PreparedStatement createInsert(T bean, Connection conn, OutSQL sql) throws Exception;

    public <T> PreparedStatement createInsert(T bean, Connection conn, OutSQL sql, String tableName) throws Exception;

    public <T> PreparedStatement createBatchInsert(List<T> beans, Connection conn, OutSQL sql) throws Exception;


    public PreparedStatement createSelectByQuery(Connection conn, DbQuery querySQL, OutSQL sql) throws Exception;


}