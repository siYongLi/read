/*
 * Copyright (c) 2015-2018, Eric Huang 黄鑫 (ninemm@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.ninemm.base.utils;

import java.util.List;

public class ListUtils<T> {
    public static<T> Boolean isEmpty(List<T> list) {
        if (list!=null && list.size()>0) {
            return false;
        }
        return true;
    }
    public static<T> Boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static String parseListTOSqlPar(List<String> contactList) {
        if (isEmpty(contactList)) {
            return null;
        }
        StringBuilder ids = new StringBuilder();
        for (String id : contactList) {
            ids.append("'").append(id).append("',");
        }
        return ids.substring(0,ids.length()-1);
    }
}
