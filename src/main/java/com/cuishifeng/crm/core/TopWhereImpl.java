package com.cuishifeng.crm.core;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.cuishifeng.crm.query.LambdaDbQuery;
import com.cuishifeng.crm.query.Query;

import java.util.List;

/**
 * @author cuishifeng <cuishifeng0207@163.com>
 * Created on 2022-07-23
 */
public class TopWhereImpl implements TopWhere, Query {

    private LambdaDbQuery lambdaDbQuery = new LambdaDbQuery();

    @Override
    public <T> TopWhere eq(boolean condition, SFunction<T, ?> column, Object val) {
         lambdaDbQuery.eq(condition,column,val);
         return this;
    }

    @Override
    public <T> TopWhere eq(boolean condition, String column, Object val) {
        try {
            if (condition){
                lambdaDbQuery.getDbQuery().and().column(column).equal(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    @Override
    public String toSql() {
        return lambdaDbQuery.toSql();
    }

    @Override
    public boolean isEmpty() {
        return lambdaDbQuery.isEmpty();
    }

    @Override
    public List<Object> values() {
        return lambdaDbQuery.values();
    }
}
