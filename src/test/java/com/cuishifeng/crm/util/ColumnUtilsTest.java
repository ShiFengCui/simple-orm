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

import java.lang.reflect.Field;

import org.apache.ibatis.reflection.property.PropertyNamer;
import org.junit.Test;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.cuishifeng.crm.annotation.Column;
import com.cuishifeng.crm.service.User;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-13
 */
public class ColumnUtilsTest {

    @Test
    public void testColumnToString() throws NoSuchFieldException {
        LambdaMeta lambdaMeta = LambdaUtils.extract(User::getName);
        System.out.println(lambdaMeta.getImplMethodName());
        String property = PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName());
        System.out.println(PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName()));


        System.out.println(lambdaMeta.getInstantiatedClass());
        Class<?> instantiatedClass = lambdaMeta.getInstantiatedClass();
        Field declaredField = instantiatedClass.getDeclaredField(PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName()));
        Column fieldAnnotation = declaredField.getAnnotation(Column.class);
        System.out.println(fieldAnnotation.name());
        String lowerCase = property.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        System.out.println(lowerCase);
    }

    @Test
    public void testColumn() throws Exception {
        String column = ColumnUtils.columnToString(User::getName);
        System.out.println(column);
    }
}