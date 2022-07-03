package com.cuishifeng.crm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author simple <cuishifeng0207@163.com>
 * Created on 2022-04-10
 */
public abstract class DataSourceConnectionPool implements ConnectionPool {

    private static final Log log = LogFactory.getLog(DataSourceConnectionPool.class);

    /**
     * 连接缓存 - 对应当前线程
     */
    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();

    private static volatile DataSource dataSource;

    private Map properties;

    public DataSourceConnectionPool(Map properties) {
        this.properties = properties;
    }

    /**
     * create data source
     * @param properties
     * @throws Exception
     * @return DataSource
     */
    public abstract DataSource createDataSource(Map properties) throws Exception;

    /**
     * get data source
     * @return
     */
    private DataSource getDataSource() throws Exception {
        if (dataSource == null) {
            synchronized (DataSourceConnectionPool.class) {
                if (dataSource == null) {
                    dataSource = createDataSource(properties);
                    return dataSource;
                }
            }
        }
        return dataSource;
    }

    @Override
    public Connection getConnection() {
        //从当前线程中取出一个连接
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (conn == null) {
            //从池中取出一个
            try {
                conn = getDataSource().getConnection();
            } catch (Exception e) {
                throw new RuntimeException("get connection error", e);
            }
            //把conn对象放入到当前线程对象中
            CONNECTION_THREAD_LOCAL.set(conn);
        }
        return conn;
    }

    @Override
    public void release(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                if (!conn.getAutoCommit()) {
                    return;
                }
                conn.close();
                CONNECTION_THREAD_LOCAL.remove();
            } catch (SQLException e) {
                log.error("sql exception ", e);
                throw e;
            }
        }
    }

}
