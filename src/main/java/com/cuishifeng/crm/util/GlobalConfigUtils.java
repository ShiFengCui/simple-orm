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

package com.cuishifeng.crm.util;

import com.cuishifeng.crm.SimpleDAOHelper;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-12
 */
public class GlobalConfigUtils {

    private SimpleDAOHelper daoHelper;

    private static volatile GlobalConfigUtils globalConfigUtils;


    public GlobalConfigUtils(SimpleDAOHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    public static void init(SimpleDAOHelper simpleDAOHelper) {
        globalConfigUtils = new GlobalConfigUtils(simpleDAOHelper);
    }

    public static GlobalConfigUtils instance() {
        return globalConfigUtils;
    }

    public SimpleDAOHelper getDaoHelper() {
        return daoHelper;
    }
}
