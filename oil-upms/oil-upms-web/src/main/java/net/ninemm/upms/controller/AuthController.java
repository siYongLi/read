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

package net.ninemm.upms.controller;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPInputStream;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.jboot.Jboot;
import io.jboot.utils.FileUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.cors.EnableCORS;
import net.ninemm.base.interceptor.AesInterceptor;
import net.ninemm.base.interceptor.GlobalCacheInterceptor;
import net.ninemm.base.interceptor.NotNullPara;
import net.ninemm.base.utils.AttachmentUtils;
import net.ninemm.base.utils.ScpClient;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户登录认证
 *
 * @author Eric.Huang
 * @date 2018-12-26 21:46
 **/
@RequestMapping(value = "/")
@EnableCORS
public class AuthController extends BaseAppController {


    public void index() {

        Connection connection = new Connection("192.168.200.3",22);

        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword("root","lsy123..");
            if(isAuthenticated){
                SCPClient scpClient = connection.createSCPClient();
                SCPInputStream sis = scpClient.get("/home/zax/app/eslogs/elk_audit.json");


                HttpServletResponse response = getResponse();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                response.setHeader("content-disposition","attachment;filename="+java.net.URLEncoder.encode("111.txt", "UTF-8"));


                ServletOutputStream outputStream = response.getOutputStream();
                byte[] b = new byte[4096];
                int i;
                while ((i = sis.read(b)) != -1){
                    outputStream.write(b,0, i);
                }
                outputStream.flush();
                sis.close();
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    public void getFil(){
        //导出excel数据
        List<List<Object>> datas = new ArrayList<List<Object>>();
        datas.add(Arrays.asList("lsy","8008"));
        datas.add(Arrays.asList("lsy","8003"));
        datas.add(Arrays.asList("lsy","8004"));
        //设置表头信息
        Sheet sheet = new Sheet(1,0);
        sheet.setSheetName("学生成绩");

        List<List<String>> heads = new ArrayList<List<String>>();
        heads.add(Arrays.asList("姓名"));
        heads.add(Arrays.asList("学号"));
        sheet.setHead(heads);

        makeFile(datas, sheet, "8008", "1.xlsx");
        File zip = ZipUtil.zip(getZipFolder("8008"));
        renderFile(zip);
    }

    private String getZipFolder(String workNo) {
        return new StringBuilder(PathKit.getWebRootPath())
                .append(File.separator).append("excel")
                .append(File.separator).append(new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .append(File.separator).append(workNo).toString();

    }

    private File makeFile(List<List<Object>> datas, Sheet sheet,String workNo,String FileName) {
        File userFile = getUserFile(workNo, FileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(userFile);
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            writer.write1(datas, sheet);
            writer.finish();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userFile;
    }

    private File getUserFile(String workNo,String FileName) {
        String folder = new StringBuilder(PathKit.getWebRootPath())
                .append(File.separator).append("excel")
                .append(File.separator).append(new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .append(File.separator)  .append(workNo).toString();
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }

        return  new File(file,FileName);
    }


}
