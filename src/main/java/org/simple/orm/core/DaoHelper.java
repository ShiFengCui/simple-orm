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

package org.simple.orm.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simple.orm.core.statement.IStatementCreater;
import org.simple.orm.core.statement.MysqlPSCreater;
import org.simple.orm.core.util.ClassHelper;
import org.simple.orm.core.util.DruidHelper;

/**
 * @author cuishifeng
 * @Title: DaoHelper
 * @ProjectName org.saladframework.dao
 * @date 2018-10-09
 */
public abstract class DaoHelper {


    /**
     * log
     */
    protected static final Log logger = LogFactory.getLog(DaoHelper.class);

    protected IStatementCreater sdmtCreater;

    protected DruidHelper connHelper;

    /**
     * 默认查询超时时间
     */
    protected static final int QUERY_TIMEOUT = 2;

    /**
     * 默认添加/修改超时时间
     */
    protected static final int INSERT_TIMEOUT = 5;

    /**
     * 默认数据库配置文件路径(jar包所在目录 / bin所在目录)
     */
    private static final String DB_CONFIG_PATH = "db.properties";

    /**
     * 创建DAO实例
     *
     * @param configPath druid 的配置文件路径
     * @return DAO实例
     * @throws Exception
     */
    public static DaoHelper createInstance(String configPath) throws Exception {
        return createDAO(configPath);
    }

    public DruidHelper getConnHelper() {
        return connHelper;
    }

    /**
     * 创建DAO
     *
     * @param configPath
     * @return
     * @throws Exception
     */
    private static DaoHelper createDAO(String configPath) throws Exception {

        DruidHelper ch = DruidHelper.getInstance(configPath);
        IStatementCreater crater = new MysqlPSCreater();

        DaoHelper daoHelper = new SimpleDAOHelper();
        daoHelper.connHelper = ch;
        daoHelper.sdmtCreater = crater;
        logger.info("create DAOHelper success!");
        return daoHelper;
    }

    /**
     * 开启事务(默认级别TRANSACTION_READ_COMMITTED)
     *
     * @throws Exception
     */
    public void beginTransaction() throws Exception {
        beginTransaction(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * 开启事务
     *
     * @param level 事务级别
     * @throws Exception
     */
    public void beginTransaction(int level) throws Exception {
        Connection conn = connHelper.getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(level);
            } catch (Exception ex) {
                logger.error(ex);
            }
        } else {
            throw new Exception("conn is null when beginTransaction");
        }
    }

    /**
     * 提交事务
     *
     * @throws Exception
     */
    public void commitTransaction() throws Exception {
        try {
            connHelper.getConnection().commit();
        } catch (Exception e) {
            throw new Exception("conn is null when beginTransaction");
        }

    }

    /**
     * 回滚事务
     *
     * @throws Exception
     */
    public void rollbackTransaction() throws Exception {
        Connection conn = connHelper.getConnection();
        if (conn != null) {
            conn.rollback();
        } else {
            throw new Exception("conn is null when rollbackTransaction");
        }
    }

    /**
     * 结束事务
     *
     * @throws Exception
     */
    public void endTransaction() throws Exception {
        Connection conn = connHelper.getConnection();
        if (conn != null) {
            //恢复默认
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connHelper.release(conn);
        } else {
            throw new Exception("conn is null when endTransaction");
        }
    }


    /**
     * 插入一个实体，返回自增ID
     *
     * @param t
     * @return ID
     * @throws Exception
     */
    public abstract <I, T> I insert(T t) throws Exception;

    public abstract <I, T> I insert(T t, String tableName) throws Exception;

    /**
     * 批量插入实体，返回每一条记录的插入结果
     *
     * @param list
     * @return int[]
     * @throws Exception
     */
    public abstract <T> int[] batchInsert(List<T> list) throws Exception;

    /**
     * 跟据实体的ID,更新整个实体
     *
     * @param t
     * @return 更新成功记录数
     * @throws Exception
     */
    public abstract <T> int update(T t) throws Exception;

    /**
     * 跟据IDlist更新指定的字段
     *
     * @param cls
     * @param updateKV 待更新的字段
     * @param ids      ID list
     * @return
     * @throws Exception
     */
    public abstract <I, T> int updateByIDList(Class<T> cls, KeyValueParis updateKV, List<I> ids) throws Exception;

    /**
     * 跟据ID更新指定的字段
     *
     * @param cls
     * @param updateKV
     * @param id
     * @return
     * @throws Exception
     */
    public abstract <I, T> int updateByID(Class<T> cls, KeyValueParis updateKV, I id) throws Exception;

    /**
     * 根据条件更新指定的字段
     *
     * @param cls
     * @param updateKV
     * @param query
     * @return
     * @throws Exception
     */
    public abstract <T> int updateByQuery(Class<T> cls, KeyValueParis updateKV, DbQuery query) throws Exception;

    /**
     * 跟据ID删除记录
     *
     * @param cls
     * @param ids
     * @return
     * @throws Exception
     */
    public abstract <I, T> int deleteByIDList(Class<T> cls, List<I> ids) throws Exception;

    /**
     * 跟据ID删除记录
     *
     * @param cls
     * @param id
     * @return
     * @throws Exception
     */
    public abstract <I, T> int deleteByID(Class<T> cls, I id) throws Exception;

    /**
     * 根据条件删除记录
     *
     * @param cls
     * @param query
     * @return
     * @throws Exception
     */
    public abstract <T> int deleteByQuery(Class<T> cls, DbQuery query) throws Exception;

    /**
     * 跟据ID获取记录
     *
     * @param cls
     * @param id
     * @return
     * @throws Exception
     */
    public abstract <T, I> T getByID(Class<T> cls, I id) throws Exception;

    /**
     * 跟据ID获取记录
     *
     * @param cls
     * @param id
     * @param columns 要获取的列，为空带表*，例如：id,title
     * @return
     * @throws Exception
     */
    public abstract <T, I> T getByID(Class<T> cls, I id, String columns) throws Exception;

    /**
     * 跟据ID获取记录
     *
     * @param cls
     * @param ids
     * @return
     * @throws Exception
     */
    public abstract <T, I> List<T> getsByIDList(Class<T> cls, List<I> ids) throws Exception;

    /**
     * 跟据ID获取记录
     *
     * @param cls
     * @param ids
     * @param columns 要获取的列，为空带表*，例如：id,title
     * @return
     * @throws Exception
     */
    public abstract <T, I> List<T> getsByIDList(Class<T> cls,
                                                List<I> ids,
                                                String columns) throws Exception;

    /**
     * 根据条件获取一条记录
     *
     * @param cls
     * @param query   查询条件
     * @param columns 要获取的列，为空带表*，例如：id,title
     * @param orderBy 排序条件，为空带表默认排序，例如：id DESC
     * @return
     * @throws Exception
     */
    public abstract <T> T getOneByQuery(Class<T> cls,
                                        DbQuery query,
                                        String columns,
                                        String orderBy) throws Exception;

    /**
     * 获取指定条件的所有记录
     *
     * @param cls
     * @param query   查询条件
     * @param columns 要获取的列，为空带表*，例如：id,title
     * @param orderBy 排序条件，为空带表默认排序，例如：id DESC
     * @return
     * @throws Exception
     */
    public abstract <T> List<T> getsByQuery(Class<T> cls,
                                            DbQuery query,
                                            String columns,
                                            String orderBy) throws Exception;

    /**
     * 分页获取指定条件的记录
     *
     * @param cls
     * @param query    查询条件
     * @param page     页码
     * @param pageSize 每页显示的记录数
     * @param columns  要获取的列，为空带表*，例如：id,title
     * @param orderBy  排序条件，为空带表默认排序，例如：id DESC
     * @return
     * @throws Exception
     */
    public abstract <T> List<T> getsByQuery(Class<T> cls,
                                            DbQuery query,
                                            int page,
                                            int pageSize,
                                            String columns,
                                            String orderBy) throws Exception;

    /**
     * 自定义查询
     * 例：
     * DbQuery query = new DbQuery();
     * query.select()
     * .from("tbl_order")
     * .where()
     * .column("id").equal(123)
     * .and().column("add_time").greater("2016-04-07 00:00:00")
     * .orderBy("id DESC")
     * .limit(100, 50);
     * <p>
     * 生成的SQL为：SELECT *  FROM tbl_order WHERE  id  =?  AND  add_time  >?  ORDER BY id DESC LIMIT ?,?
     * <p>
     * <p>
     * DbQuery query = new DbQuery();
     * query.select("order.id,user.userid")
     * .from("tbl_order", "order")
     * .innerJoin("tbl_user", "user", "order.userid", "user.userid")
     * .where()
     * .column("user.id").equal(123)
     * .and().column("order.add_time").greater("2016-04-07 00:00:00")
     * .orderBy("order.id DESC")
     * .limit(100, 50);
     * <p>
     * 生成的SQL为： SELECT order.id,user.userid  FROM tbl_order AS order INNER JOIN tbl_user AS user ON order.userid = user.userid WHERE  user.id  =?  AND  order.add_time  >?  ORDER BY order.id DESC LIMIT ?,?
     *
     * @param cls
     * @param querySQL
     * @return
     * @throws Exception
     */
    public abstract <T> List<T> selectByQuery(Class<T> cls, DbQuery querySQL) throws Exception;

    /**
     * 根据条件查询count
     *
     * @param cls
     * @param query
     * @return
     * @throws Exception
     */
    public abstract int count(Class<?> cls, DbQuery query) throws Exception;

    /**
     * 自定义完整的SQL语句查询count
     *
     * @param fullSQL 完整的SQL语句
     * @return
     * @throws Exception
     */
    public abstract int count(DbQuery fullSQL) throws Exception;

    /**
     * 按sql语句查询
     *
     * @param cls
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public abstract <T> List<T> selectBySQL(Class<T> cls, String sql, Object... params) throws Exception;

    /**
     * 通过完整SQL获得COUNT数量
     *
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public abstract int countBySQL(String sql, Object[] params) throws Exception;

    /**
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public abstract Map<String, Object> getMap(String sql, Object... param) throws Exception;

    /**
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public abstract List<Map<String, Object>> getMaps(String sql, Object... param) throws Exception;

    /**
     * @param sql
     * @param param
     * @return
     * @throws Exception
     */
    public abstract List<List<KeyValue<String, Object>>> getKeyValues(String sql, Object... param) throws Exception;


    protected <T> List<T> populateData(ResultSet rs, Class<T> clazz) throws Exception {
        List<T> dataList = new ArrayList<T>();
        List<Field> fieldList = ClassHelper.getAllFields(clazz);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        List<String> columnNameList = new ArrayList<String>();
        for (int i = 0; i < columnsCount; i++) {
            columnNameList.add(rsmd.getColumnLabel(i + 1).toLowerCase());
        }

        while (rs.next()) {
            T bean = clazz.newInstance();
            for (Field f : fieldList) {
                String columnName = ClassHelper.getDBCloumnName(clazz, f).toLowerCase();
                if (columnNameList.contains(columnName)) {
                    Object columnValueObj = null;
                    Class<?> filedCls = f.getType();

                    if (filedCls == int.class || filedCls == Integer.class) {
                        columnValueObj = rs.getInt(columnName);
                    } else if (filedCls == String.class) {
                        columnValueObj = rs.getString(columnName);
                    } else if (filedCls == boolean.class || filedCls == Boolean.class) {
                        columnValueObj = rs.getBoolean(columnName);
                    } else if (filedCls == byte.class || filedCls == Byte.class) {
                        columnValueObj = rs.getByte(columnName);
                    } else if (filedCls == short.class || filedCls == Short.class) {
                        columnValueObj = rs.getShort(columnName);
                    } else if (filedCls == long.class || filedCls == Long.class) {
                        columnValueObj = rs.getLong(columnName);
                    } else if (filedCls == float.class || filedCls == Float.class) {
                        columnValueObj = rs.getFloat(columnName);
                    } else if (filedCls == double.class || filedCls == Double.class) {
                        columnValueObj = rs.getDouble(columnName);
                    } else if (filedCls == BigDecimal.class) {
                        columnValueObj = rs.getBigDecimal(columnName);
                    } else {
                        columnValueObj = rs.getObject(columnName);
                    }

                    if (columnValueObj != null) {
                        Method setterMethod = ClassHelper.getSetterMethod(clazz, f);
                        setterMethod.invoke(bean, new Object[]{columnValueObj});
                    }
                }
            }
            dataList.add(bean);
        }
        return dataList;
    }
}
