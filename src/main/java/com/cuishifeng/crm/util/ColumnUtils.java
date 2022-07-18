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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.cuishifeng.crm.annotation.Column;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-13
 */
public class ColumnUtils {

    private static Map<String, String> columnNameCache = new ConcurrentHashMap<>(32);


    public static <T> String columnToString(SFunction<T, ?> column) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(column);
        String cacheName = getCacheName(lambdaMeta);
        String columnName = columnNameCache.get(cacheName);
        if (StringUtils.isNotBlank(columnName)) {
            return columnName;
        }
        Column fieldAnnotation = lambdaMeta.getInstantiatedClass().getAnnotation(Column.class);
        if (fieldAnnotation != null && StringUtils.isNotBlank(fieldAnnotation.name())) {
            columnNameCache.put(cacheName, fieldAnnotation.name());
            return fieldAnnotation.name();
        }
        String property = PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName());
        String lowerCaseName = convertLowerStr(property);
        columnNameCache.put(cacheName, lowerCaseName);
        return lowerCaseName;
    }

    public static String convertLowerStr(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private static String getCacheName(LambdaMeta lambdaMeta) {
        return String.format("class:%s-method:%s",
                lambdaMeta.getInstantiatedClass().getSimpleName(), lambdaMeta.getImplMethodName());
    }
}
