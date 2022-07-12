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

import com.cuishifeng.crm.annotation.Table;
import com.cuishifeng.crm.util.ClassHelper;
import com.cuishifeng.crm.util.GlobalConfigUtils;
import com.cuishifeng.crm.util.ReflectionKit;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-12
 */
public class TableDaoService<TableEntity extends TableDaoService> {

    protected Class<TableEntity> tableEntityClass = currentModelClass();


    public String getTableName() {
        Table table = tableEntityClass.getAnnotation(Table.class);
        return table.name();
    }

    protected Class<TableEntity> currentModelClass() {
        return (Class<TableEntity>) getClass();
    }

    public <I> I saveOne(TableEntity t) {
        try {
            return GlobalConfigUtils.instance().getDaoHelper().insert(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
