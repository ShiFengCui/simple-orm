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

import java.io.Serializable;

/**
 * @author cuishifeng <cuishifeng@kuaishou.com>
 * Created on 2022-07-13
 */
public interface Query<Children, R> extends Serializable {

    Children select();

    Children select(R... column);

    default Children eq(R column, Object val) {
        return eq(true, column, val);
    }

    Children eq(boolean condition, R column, Object val);

    default Children ne(R column, Object val) {
        return ne(true, column, val);
    }

    Children ne(boolean condition, R column, Object val);

    default Children gt(R column, Object val) {
        return gt(true, column, val);
    }

    Children gt(boolean condition, R column, Object val);

    default Children ge(R column, Object val) {
        return ge(true, column, val);
    }

    Children ge(boolean condition, R column, Object val);

    default Children lt(R column, Object val) {
        return lt(true, column, val);
    }

    Children lt(boolean condition, R column, Object val);

    default Children le(R column, Object val) {
        return le(true, column, val);
    }

    Children le(boolean condition, R column, Object val);

    default Children between(R column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    Children between(boolean condition, R column, Object val1, Object val2);

    default Children notBetween(R column, Object val1, Object val2) {
        return notBetween(true, column, val1, val2);
    }

    Children notBetween(boolean condition, R column, Object val1, Object val2);

    default Children like(R column, Object val) {
        return like(true, column, val);
    }

    Children like(boolean condition, R column, Object val);

    default Children notLike(R column, Object val) {
        return notLike(true, column, val);
    }

    Children notLike(boolean condition, R column, Object val);

    default Children likeLeft(R column, Object val) {
        return likeLeft(true, column, val);
    }

    Children likeLeft(boolean condition, R column, Object val);

    default Children likeRight(R column, Object val) {
        return likeRight(true, column, val);
    }

    Children likeRight(boolean condition, R column, Object val);

}
