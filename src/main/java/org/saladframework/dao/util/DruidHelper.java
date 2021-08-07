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

package org.saladframework.dao.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * @author cuishifeng
 * @Title: DruidHelper
 * @ProjectName com.bj58.esc.spat.dao.connpool
 * @date 2018-11-19
 */
public class DruidHelper {

    private static volatile DruidHelper druidHelper = null;
    private static volatile DruidDataSource dataSource = null;

    private static final Log log = LogFactory.getLog(ConnectionHelper.class);

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
