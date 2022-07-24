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

import static com.cuishifeng.crm.model.SQLConstants.COMMA;
import static com.cuishifeng.crm.model.SQLConstants.PERCENT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.cuishifeng.crm.annotation.Table;
import com.cuishifeng.crm.util.ColumnUtils;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-13
 */
public class LambdaDbQuery<T> implements Query, SQL<LambdaDbQuery, SFunction<T, ?>> {

    private DbQuery dbQuery = new DbQuery();


    public LambdaDbQuery() {

    }

    public DbQuery getDbQuery() {
        return dbQuery;
    }

    @Override
    public String toSql() {
        return dbQuery.toSql();
    }

    @Override
    public boolean isEmpty() {
        return dbQuery.isEmpty();
    }

    @Override
    public List<Object> values() {
        return dbQuery.values();
    }

    @Override
    public LambdaDbQuery select() {
        try {
            dbQuery.select(" * ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery select(SFunction<T, ?>... column) {
        try {
            String columns = Arrays.stream(column)
                    .map(ColumnUtils::columnToString).collect(Collectors.joining(COMMA));
            dbQuery.select(columns);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery update(String tableName) {
        try {
            dbQuery.forUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery update(Class<?> tableClass) {
        Table tableClassAnnotation = tableClass.getAnnotation(Table.class);
        if (tableClassAnnotation != null) {
            update(tableClassAnnotation.name());
            return this;
        }
        update(ColumnUtils.convertLowerStr(tableClass.getSimpleName()));
        return this;
    }

    @Override
    public LambdaDbQuery from(String tableName) {
        try {
            dbQuery.from(tableName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery where() {
        try {
            dbQuery.where();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery eq(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).equal(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery ne(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).notEqual(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery gt(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).greater(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery ge(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).greaterOrEqual(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery lt(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).less(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery le(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).lessOrEqual(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery between(boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).between(val1, val2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery notBetween(boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).notBetween(val1, val2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery like(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).like(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery notLike(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                dbQuery.and().column(ColumnUtils.columnToString(column)).notLike(val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery likeLeft(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                String format = String.format("%s%s", PERCENT, val);
                dbQuery.and().column(ColumnUtils.columnToString(column)).like(format);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public LambdaDbQuery likeRight(boolean condition, SFunction<T, ?> column, Object val) {
        try {
            if (condition) {
                String format = String.format("%s%s", val, PERCENT);
                dbQuery.and().column(ColumnUtils.columnToString(column)).like(format);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }


}
