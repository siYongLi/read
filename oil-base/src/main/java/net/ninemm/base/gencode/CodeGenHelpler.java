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
 */

package net.ninemm.base.gencode;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import io.jboot.utils.StrUtil;

import java.util.List;
import java.util.Set;

/**
 * 代码自动生成帮助类
 *
 * @author Eric.Huang
 * @date 2018-06-28 14:32
 **/

public class CodeGenHelpler extends io.jboot.codegen.CodeGenHelpler {
    /**
     * 生成指定表的Model
     *
     * @author Eric Huang
     * @date 2018-06-28 14:36
     * @param [list, excludeTables]
     * @return void
     **/
    public static void includeTables(List<TableMeta> list, String includeTables) {

        if (StrUtil.isNotBlank(includeTables)) {
            List<TableMeta> newTableMetaList = Lists.newArrayList();
            Set<String> includeTableSet = StrUtil.splitToSet(includeTables.toLowerCase(), ",");
            for (TableMeta tableMeta : list) {
                if (includeTableSet.contains(tableMeta.name.toLowerCase())) {
                    System.out.println("include table : " + tableMeta.name);
                    newTableMetaList.add(tableMeta);
                }
            }
            list.clear();
            list.addAll(newTableMetaList);
        }
    }
}
