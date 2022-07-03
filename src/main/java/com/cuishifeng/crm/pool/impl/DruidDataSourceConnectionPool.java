package com.cuishifeng.crm.pool.impl;

import java.util.Map;

import javax.sql.DataSource;

import com.cuishifeng.crm.pool.DataSourceConnectionPool;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * @author simple <cuishifeng0207@163.com>
 * Created on 2022-04-10
 */
public class DruidDataSourceConnectionPool extends DataSourceConnectionPool {

    public DruidDataSourceConnectionPool(Map properties) {
        super(properties);
    }

    @Override
    public DataSource createDataSource(Map properties) throws Exception {
       return DruidDataSourceFactory.createDataSource(properties);
    }
}