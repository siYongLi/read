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

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import io.jboot.utils.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Excel解析工具类
 *
 * @author Eric.Huang
 * @date 2018-12-23 11:40
 **/

public class ExcelKit {

    /**
     * 读取 Excel （多个 sheet）
     * @author Eric
     * @date  2018-12-23 12:11
     * @param [file, rowModel]
     * @return java.util.List<java.lang.Object>
     */
    public static List<Object> readExcel(File file, ExcelListener excelListener, BaseRowModel rowModel) {

        ExcelReader reader = getReader(file, excelListener);
        if (reader == null) {
            return null;
        }

        for (Sheet sheet : reader.getSheets()) {
            if (rowModel != null) {
                sheet.setClazz(rowModel.getClass());
            }
            reader.read(sheet);
        }
        return excelListener.getRowDatas();
    }

    /**
     * 读取 Excel 中的某个 sheet
     * @author Eric
     * @param file  excel文件
     * @param excelListener
     * @param rowModel  实体映射类
     * @param sheetNo   sheet 的序号，从1开始
     * @return java.util.List<java.lang.Object>
     */
    public static List<Object> readExcel(File file, ExcelListener excelListener, BaseRowModel rowModel, int sheetNo) {
        return readExcel(file, excelListener, rowModel, sheetNo, 1);
    }

    /**
     * 读取 Excel 中的某个 sheet
     * @author Eric
     * @param file  excel文件
     * @param excelListener
     * @param rowModel  实体映射类
     * @param sheetNo   sheet 的序号，从1开始
     * @param headLineNum   表头行数，默认为1
     * @return java.util.List<java.lang.Object>
     */
    public static List<Object> readExcel(File file, ExcelListener excelListener, BaseRowModel rowModel,
        int sheetNo, int headLineNum) {

        ExcelReader reader = getReader(file, excelListener);
        if (reader == null) {
            return null;
        }

        reader.read(new Sheet(sheetNo, headLineNum, rowModel.getClass()));
        return excelListener.getRowDatas();
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param rowModel  映射实体类，Excel 模型
     */
    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list,
        String fileName, String sheetName, BaseRowModel rowModel) {
        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLS);
        Sheet sheet = new Sheet(1, 0, rowModel.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * 导出 Excel ：多个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param rowModel    映射实体类，Excel 模型
     */
    public static ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response, List<? extends BaseRowModel> list,
        String fileName, String sheetName, BaseRowModel rowModel) {

        ExcelWriterFactory writer = new ExcelWriterFactory(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, rowModel.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        return writer;
    }

    /**
     * 导出文件时为Writer生成 OutputStream
     * @author  
     * @date  2018-12-23 12:22
     * @param [fileName, response]
     * @return java.io.OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        String filePath = fileName + ".xls";
        File dbfFile = new File(filePath);

        try {
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                dbfFile.createNewFile();
            }
            fileName = new String(filePath.getBytes(), "ISO-8859-1");
            response.addHeader("Content-Disposition", "filename=" + fileName);
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ExcelException("创建文件失败!");
        }
    }

    /**
     * 返回 ExcelReader
     * @author  Eric
     * @date  2018-12-23 12:01
     * @param file
     * @param excelListener
     * @return com.alibaba.excel.ExcelReader
     */
    private static ExcelReader getReader(File file, ExcelListener excelListener) {

        String suffixName = FileUtil.getSuffix(file.getName());
        if (suffixName == null || !fileSuffix.contains(suffixName)) {
            throw new ExcelException("文件格式错误");
        }

        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            return EasyExcelFactory.getReader(inputStream, excelListener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static List<String> fileSuffix = Lists.newArrayList();
    static {
        fileSuffix.add(".xls");
        fileSuffix.add(".xlsx");
    }
}
