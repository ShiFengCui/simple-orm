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
 * @Title: DruidHelper
 * @ProjectName com.bj58.esc.spat.dao.connpool
 * @date 2018-11-19
 */
public class DruidHelper {

    private static DruidHelper druidHelper = null;
    private static DruidDataSource dataSource = null;

    private Log log = LogFactory.getLog(ConnectionHelper.class);

    /**
     * 连接缓存 - 对应当前线程
     */
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();


    /**
     * 初始化druid线程池
     *
     * @param config
     */
    private DruidHelper(String config) {
        try {
            InputStream is = new FileInputStream(config);
            Properties properties = new Properties();
            properties.load(is);
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            log.error("init druid failure", e);
        }
    }

    /**
     * 获取单例的连接帮助 DruidHelper
     *
     * @param config
     * @return
     */
    public static DruidHelper getInstance(String config) {
        if (druidHelper == null) {
            synchronized (DruidHelper.class) {
                if (druidHelper == null) {
                    return new DruidHelper(config);
                }
            }
        }
        return druidHelper;
    }

    private DataSource getDataSource() {
        if (dataSource == null) {
            throw new RuntimeException("data source is null");
        }
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
                e.printStackTrace();
            }
        }
    }


}
