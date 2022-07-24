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

import com.google.common.collect.Lists;
import org.junit.Test;

import com.cuishifeng.crm.DataSourceTest;
import com.cuishifeng.crm.StartCRUD;
import com.cuishifeng.crm.dao.DaoHelper;
import com.cuishifeng.crm.dao.SimpleDAOHelper;
import com.cuishifeng.crm.service.User;

import java.util.stream.Stream;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-20
 */
public class SelectTest {

    @Test
    public void testGetsByIDList() {
    }

    @Test
    public void testGetOne() throws Exception {
        SimpleDAOHelper simpleDAOHelper = DaoHelper.createInstance(DataSourceTest.getDataSource());
        User user1 = StartCRUD.select(User::getName).getOne(User.class, 1);
        // StartCRUD.select().where().eq(true,User::getId,1)
        System.out.println(user1);
    }


    @Test
    public void test(){
        Stream.of(Lists.newArrayList(1,2,3)).filter(x -> x.equals(1));
        // CRUD.select().from().where().eq().one()
        // CRUD.selectOne(User.class , id);
    }
}