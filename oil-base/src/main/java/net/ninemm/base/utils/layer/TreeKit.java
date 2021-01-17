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

package net.ninemm.base.utils.layer;

import com.jfinal.kit.StrKit;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析树形数据工具类
 *
 * @author Eric.Huang
 * @date 2018-12-14 22:10
 **/

public class TreeKit {

    /**
     * 包含所有树节点
     * @param list
     * @return
     */
    public static <T extends TreeEntity> List<T> toTree(List<T> list) {
        return toTree(null, list, false);
    }

    public static <T extends TreeEntity> List<T> toTree(String rootId, List<T> list) {
        return toTree(rootId, list, false);
    }

    /**
     * 根据rootId查找对应的树
     * @param rootId
     * @param list
     * @return
     */
    public static <T extends TreeEntity> List<T> toTree(String rootId, List<T> list, boolean isIncludeRoot) {

        List<T> treeList = new ArrayList<>();

        //获取顶层元素集合
        String parentId;
        for (T entity : list) {
            parentId = entity.getParentId();
            if (rootId == null && StrKit.isBlank(parentId)) {
                treeList.add(entity);
                break;
            }

            if (isIncludeRoot) {
                if (entity.getId().equals(rootId)) {
                    treeList.add(entity);
                }
            } else {
                if (parentId.equals(rootId)) {
                    treeList.add(entity);
                }
            }
        }

        //获取每个顶层元素的子数据集合
        for (T entity : treeList) {
            entity.setChildren(getSubList(entity.getId(), list));
        }

        return treeList;
    }

    /**
     * 获取子数据集合
     * @param id
     * @param list
     * @return
     * @date 2017年5月29日
     */
    private static <T extends TreeEntity> List<T> getSubList(String id, List<T> list) {
        List<T> childList = new ArrayList<>();
        String parentId;

        //子集的直接子对象
        for (T entity : list) {
            parentId = entity.getParentId();
            if (id.equals(parentId)) {
                childList.add(entity);
            }
        }

        //子集的间接子对象
        for (T entity : childList) {
            entity.setChildren(getSubList(entity.getId(), list));
        }

        //递归退出条件
        if (childList.size() == 0) {
            return null;
        }

        return childList;
    }
}
