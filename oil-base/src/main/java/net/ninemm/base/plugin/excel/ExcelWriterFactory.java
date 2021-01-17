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

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * excel文件导出工厂类
 *
 * @author Eric.Huang
 * @date 2018-12-23 11:29
 **/

public class ExcelWriterFactory extends ExcelWriter {

    private int sheetNo = 1;
    private OutputStream outputStream;

    public ExcelWriterFactory(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public ExcelWriterFactory(OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead) {
        super(outputStream, typeEnum, needHead);
    }

    public ExcelWriterFactory write(List<? extends BaseRowModel> list, String sheetName, BaseRowModel obj) {
        this.sheetNo ++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, obj.getClass());
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (ExcelException ex) {
            ex.printStackTrace();
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
