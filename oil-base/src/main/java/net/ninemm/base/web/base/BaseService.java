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

package net.ninemm.base.web.base;

import com.google.common.base.Splitter;
import io.jboot.db.model.JbootModel;
import io.jboot.service.JbootServiceBase;

import java.util.List;

/**
 * Service 基类
 *
 * @author Eric.Huang
 * @date 2018-12-18 23:20
 **/

public abstract class BaseService<M extends JbootModel<M>> extends JbootServiceBase<M> {

    @Override
    public boolean deleteById(Object id) {
        clearAllCache();
        return super.deleteById(id);
    }

    @Override
    public boolean delete(M model) {
        clearAllCache();
        return super.delete(model);
    }

    @Override
    public Object save(M model) {
        clearAllCache();
        return super.save(model);
    }

    @Override
    public Object saveOrUpdate(M model) {
        clearAllCache();
        return super.saveOrUpdate(model);
    }

    @Override
    public boolean update(M model) {
        clearAllCache();
        return super.update(model);
    }

    /**
     * 清除 model 缓存
     */
    protected abstract void clearAllCache();

    /**
     * 生成排序 SQL
     * @author Eric
     * @date  2018-12-29 00:32
     * @param orderByField     排序字段 如 create_date order_list
     * @param isAsc             是否按升序排序 false, true
     * @return java.lang.String
     */
    protected String orderBy(Object orderByField, Object isAsc) {

        String orderByFields = orderByField != null ? orderByField.toString() : "create_date";
        boolean isSort = false;
        if (isAsc != null) {
            isSort = Boolean.valueOf(isAsc.toString());
        }
        String sort = isSort ? "asc" : "desc";

        List<String> list = Splitter.on(" ").splitToList(orderByFields);
        StringBuilder sb = new StringBuilder();
        if (list.size() == 1) {
            sb.append(list.get(0)).append(" ").append(sort);
            return sb.toString();
        }

        for (String field : list) {
            sb.append(field).append(" ").append(sort).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
