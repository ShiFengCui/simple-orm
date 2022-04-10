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

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * @author cuishifeng
 * @Title: DruidUtil
 * @ProjectName org.saladframework.dao.util
 * @date 2018-10-08
 */
public class DruidUtil {

    private static final Log log = LogFactory.getLog(DruidUtil.class);

    private static DruidDataSource dataSource;

    static {
        try {
            InputStream is = DruidUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            Properties properties = new Properties();
            properties.load(is);
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            log.error("init druid failure", e);
        }
    }

    /**
     * 返回druid数据源
     *
     * @return
     */
    public static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        return null;
    }

    /**
     * 返回数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            log.error("return druid connection exception", e);
        }
        return null;
    }

}
