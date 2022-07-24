package com.cuishifeng.crm.core;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * @author cuishifeng <cuishifeng0207@163.com>
 * Created on 2022-07-13
 */
public interface TopWhere {

    default <T> TopWhere eq(SFunction<T, ?> column, Object val) {
        return eq(true, column, val);
    }

    <T> TopWhere eq(boolean condition, SFunction<T, ?> column, Object val);

    default <T> TopWhere eq(String column, Object val) {
        return eq(true, column, val);
    }

    <T> TopWhere eq(boolean condition, String column, Object val);

    default TopWhereImpl instance(){
        return new TopWhereImpl();
    }

}
