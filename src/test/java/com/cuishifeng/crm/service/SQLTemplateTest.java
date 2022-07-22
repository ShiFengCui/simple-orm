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


import org.junit.Assert;
import org.junit.Test;

import com.cuishifeng.crm.StartCRUD;
import com.cuishifeng.crm.dao.DaoHelper;
import com.cuishifeng.crm.DataSourceTest;
import com.cuishifeng.crm.dao.SimpleDAOHelper;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-12
 */
public class SQLTemplateTest {


    @Test
    public void testGetTableName() {
        User user = new User();
        System.out.println(user.tableEntityClass);
        Assert.assertEquals("user", user.getTableName());
    }

    @Test
    public void testSaveOne() throws Exception {
        SimpleDAOHelper simpleDAOHelper = DaoHelper.createInstance(DataSourceTest.getDataSource());
        User user = new User();
        user.setName("hello2");
        Object result = user.saveOne(user);
        System.out.println(result);
    }

}