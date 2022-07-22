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

package com.cuishifeng.crm.service;

import java.util.List;

import com.cuishifeng.crm.query.DbQuery;
import com.cuishifeng.crm.model.KeyValueParis;
import com.cuishifeng.crm.dao.SimpleDAOHelper;
import com.cuishifeng.crm.annotation.Table;
import com.cuishifeng.crm.util.GlobalConfigUtils;
import com.cuishifeng.crm.util.ReflectionKit;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-12
 */
public class SQLTemplate<TableEntity extends SQLTemplate> {

    protected Class<TableEntity> tableEntityClass = currentModelClass();
    private SimpleDAOHelper daoHelper = GlobalConfigUtils.instance().getDaoHelper();

    public String getTableName() {
        Table table = tableEntityClass.getAnnotation(Table.class);
        return table.name();
    }

    protected Class<TableEntity> currentModelClass() {
        return (Class<TableEntity>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    public <I> I saveOne(TableEntity entity) {
        try {
            return daoHelper.insert(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveList(List<TableEntity> list) {
        try {
            int[] ints = daoHelper.batchInsert(list);
            return ints.length == list.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int update(TableEntity entity) {
        try {
            return daoHelper.update(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <I> int updateByIDList(KeyValueParis updateKV, List<I> ids) {
        try {
            return daoHelper.updateByIDList(tableEntityClass, updateKV, ids);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <I> int updateByID(KeyValueParis updateKV, I id) {
        try {
            return daoHelper.updateByID(tableEntityClass, updateKV, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int updateByQuery(KeyValueParis updateKV, DbQuery query) {
        try {
            return daoHelper.updateByQuery(tableEntityClass, updateKV, query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <I> int deleteByID(I id) {
        try {
            return daoHelper.deleteByID(tableEntityClass, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <I> int deleteByQuery(DbQuery query) {
        try {
            return daoHelper.deleteByQuery(tableEntityClass, query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <I> TableEntity getByID(I id) {
        try {
            return daoHelper.getByID(tableEntityClass, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
