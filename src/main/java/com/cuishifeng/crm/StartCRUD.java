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

package com.cuishifeng.crm;

import static com.cuishifeng.crm.model.SQLConstants.COMMA;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.cuishifeng.crm.core.Select;
import com.cuishifeng.crm.util.ColumnUtils;
import com.cuishifeng.crm.util.GlobalConfigUtils;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-20
 */
public class StartCRUD {


    public static Select select() {
        return new Select(GlobalConfigUtils.instance().getDaoHelper(), null);
    }

    public static <T> Select select(SFunction<T, ?>... columns) {
        String columnStr = null;
        if (columns != null) {
            columnStr = Arrays.stream(columns)
                    .map(ColumnUtils::columnToString).collect(Collectors.joining(COMMA));
        }
        return new Select(GlobalConfigUtils.instance().getDaoHelper(), columnStr);
    }

    public static <T> Select select(String... columns) {
        String columnStr = null;
        if (columns != null) {
            columnStr = Arrays.stream(columns).collect(Collectors.joining(COMMA));
        }
        return new Select(GlobalConfigUtils.instance().getDaoHelper(), columnStr);
    }
}
