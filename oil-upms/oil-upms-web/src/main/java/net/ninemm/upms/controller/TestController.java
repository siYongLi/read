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
import cn.hutool.core.util.ZipUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.jfinal.kit.PathKit;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.cors.EnableCORS;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户登录认证
 *
 * @author Eric.Huang
 * @date 2018-12-26 21:46
 **/
@RequestMapping(value = "/",viewPath = "/html")
@EnableCORS
public class TestController extends BaseAppController {
    public void index() {
        render("index.html");
    }

    public void download() {
        String no = getPara("no");
        if(StringUtils.isEmpty(no)){
            renderJson("工号不正确");
            return;
        }


        File file = new File("D:\\dev\\read\\oil-upms\\oil-upms-web\\src\\main\\java\\net\\ninemm\\upms\\excel\\listener\\ExcelUserListener.java");
        renderFile(file);
    }
}
