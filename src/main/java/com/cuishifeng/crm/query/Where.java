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

package com.cuishifeng.crm.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-21
 */
public class Where<C> implements SQL<Where, SFunction> {

    private LambdaDbQuery lambdaDbQuery = new LambdaDbQuery();


    @Override
    public Where select() {
        return this;
    }

    @Override
    public Where select(SFunction... column) {
        return this;
    }

    @Override
    public Where update(String tableName) {
        return this;
    }

    @Override
    public Where update(Class<?> tableClass) {
        return this;
    }

    @Override
    public Where from(String tableName) {
        return this;
    }

    @Override
    public Where where() {
        return this;
    }

    @Override
    public Where eq(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.eq(condition, column, val);
        return this;
    }

    @Override
    public Where ne(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.ne(condition, column, val);
        return this;
    }

    @Override
    public Where gt(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.gt(condition, column, val);
        return this;
    }

    @Override
    public Where ge(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.ge(condition, column, val);
        return this;
    }

    @Override
    public Where lt(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.lt(condition, column, val);
        return this;
    }

    @Override
    public Where le(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.le(condition, column, val);
        return this;
    }

    @Override
    public Where between(boolean condition, SFunction column, Object val1, Object val2) {
        lambdaDbQuery.between(condition, column, val1, val2);
        return this;
    }

    @Override
    public Where notBetween(boolean condition, SFunction column, Object val1, Object val2) {
        lambdaDbQuery.notBetween(condition, column, val1, val2);
        return this;
    }

    @Override
    public Where like(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.like(condition, column, val);
        return this;
    }

    @Override
    public Where notLike(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.notLike(condition, column, val);
        return this;
    }

    @Override
    public Where likeLeft(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.likeLeft(condition, column, val);
        return this;
    }

    @Override
    public Where likeRight(boolean condition, SFunction column, Object val) {
        lambdaDbQuery.likeRight(condition, column, val);
        return this;
    }
}
