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

package com.cuishifeng.crm.statement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import com.cuishifeng.crm.DbQuery;
import com.cuishifeng.crm.util.OutSQL;

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