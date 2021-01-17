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

package net.ninemm.base.plugin.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 解析Excel监听器
 *
 * @author Eric.Huang
 * @date 2018-12-23 11:15
 **/

public class ExcelListener<T extends BaseRowModel> extends AnalysisEventListener {

    private static final int DEFAULT_BATCH_NUM = 100;

    protected Integer batchNum;
    /** 用于暂时存储data， 可以通过实例获取该值 **/
    private List<Object> rowDatas = Lists.newArrayList();

    public ExcelListener() {
        this.batchNum = DEFAULT_BATCH_NUM;
    }

    public ExcelListener(Integer batchNum) {
        this.batchNum = batchNum;
    }

    /**
     * 通过 analysisContext 对象还可以获取当前 sheet， 当前行等数据
     * @param obj
     * @param analysisContext
     * @return void
     */
    @Override
    public void invoke(Object obj, AnalysisContext analysisContext) {
        rowDatas.add(obj);
        // 处理数据量过大，进行分批处理
        if (getRowDatas().size() == batchNum) {
            analysis();
            rowDatas = Lists.newArrayList();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        analysis();
        /** 解析结束销毁不用的资源 **/
        //rowDatas.clear();
    }

    public void analysis() {}

    public List<Object> getRowDatas() {
        return rowDatas;
    }

    public void setRowDatas(List<Object> rowDatas) {
        this.rowDatas = rowDatas;
    }

    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batchNum) {
        this.batchNum = batchNum;
    }
}
