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

package org.simple.framework.dao.util;

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
 * @Title: ConnectionHelper
 * @ProjectName org.saladframework.dao.util
 * @date 2018-10-14
 */
public class ConnectionHelper {

    private static volatile ConnectionHelper connectionHelper = null;

    private static volatile DruidDataSource dataSource = null;

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
