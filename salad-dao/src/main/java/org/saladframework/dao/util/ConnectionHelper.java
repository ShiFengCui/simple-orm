package org.saladframework.dao.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author cuishifeng
 * @Title: ConnectionHelper
 * @ProjectName org.saladframework.dao.util
 * @date 2018-10-14
 */
public class ConnectionHelper {

    private static ConnectionHelper connectionHelper = null;

    private static DruidDataSource dataSource = null;

    private Log log = LogFactory.getLog(ConnectionHelper.class);

    private ConnectionHelper(String config) {
        try {
            InputStream is = new FileInputStream(config);
            Properties properties = new Properties();
            properties.load(is);
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            log.error("init druid failure", e);
        }
    }


    public static ConnectionHelper getInstance(String config) {
        if (connectionHelper == null) {
            synchronized (ConnectionHelper.class) {
                if (connectionHelper == null) {
                    return new ConnectionHelper(config);
                }
            }
        }
        return connectionHelper;
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new RuntimeException("data source is null");
        }
        return dataSource;
    }


    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
