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

package com.cuishifeng.crm.core;

import java.util.List;

import com.cuishifeng.crm.dao.SimpleDAOHelper;
import com.cuishifeng.crm.query.Where;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-20
 */
public class Select {

    private SimpleDAOHelper simpleDAOHelper;
    private String columns;


    public <T> Select(SimpleDAOHelper simpleDAOHelper, String columns) {
        this.simpleDAOHelper = simpleDAOHelper;
        this.columns = columns;
    }

    public <T, I> List<T> getBatchIds(Class<T> cls, List<I> ids) throws Exception {
        return simpleDAOHelper.getsByIDList(cls, ids, columns);
    }

    public <T, I> T getOne(Class<T> cls, I id) throws Exception {
        return simpleDAOHelper.getByID(cls, id, columns);
    }

    public Where where() {
        return new Where();
    }

}
