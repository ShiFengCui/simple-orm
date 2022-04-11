package org.simple.orm.core.pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * connection pool.
 * @author simple <cuishifeng0207@163.com>
 * Created on 2022-04-10
 */
public interface ConnectionPool {

    /**
     * get connection
     * @return
     */
    Connection getConnection();

    /**
     * release connection
     * @param conn
     * @throws SQLException
     */
    void release(Connection conn) throws SQLException;
}
