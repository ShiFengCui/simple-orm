package org.saladframework.dao.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author CuiShiFeng
 * @date
 */
public class JdbcUtil {

    protected static final Log logger = LogFactory.getLog(JdbcUtil.class);

    /**
     * close a connection
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException ex) {
            logger.debug("Could not close JDBC Connection", ex);
        } catch (Throwable ex) {
            logger.debug("Unexpected exception on closing JDBC Connection", ex);
        }
    }

    /**
     * close a statement
     *
     * @param stmt
     */
    public static void closeStatement(Statement stmt) {
        if (stmt == null) {
            return;
        }

        try {
            stmt.close();
        } catch (SQLException ex) {
            logger.trace("Could not close JDBC Statement", ex);
        } catch (Throwable ex) {
            logger.trace("Unexpected exception on closing JDBC Statement", ex);
        }
    }

    /**
     * close a ResultSet
     *
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs == null) {
            return;
        }
        try {
            rs.close();
        } catch (SQLException ex) {
            logger.trace("Could not close JDBC ResultSet", ex);
        } catch (Throwable ex) {
            logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
        }
    }
}
