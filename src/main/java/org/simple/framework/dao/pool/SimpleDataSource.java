package org.simple.framework.dao.pool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author cuishifeng
 * @email cuishifeng0207@163.com
 * @date 2022/07/02
 */
public class SimpleDataSource {

    private DataSource dataSource;

    /**
     * 连接缓存 - 对应当前线程
     */
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    public SimpleDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获取连接
     *
     * @return
     */
    public Connection getConnection() {
        //从当前线程中取出一个连接
        Connection conn = tl.get();
        if (conn == null) {
            //从池中取出一个
            try {
                conn = getDataSource().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("get connection failure by druidHelper!");
            }
            //把conn对象放入到当前线程对象中
            tl.set(conn);
        }
        return conn;

    }

    /**
     * 释放连接
     *
     * @param conn
     * @throws Exception
     */
    public void release(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.getAutoCommit()) {
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
                tl.remove();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
