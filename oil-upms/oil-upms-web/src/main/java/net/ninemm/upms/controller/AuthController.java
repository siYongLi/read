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
import io.jboot.Jboot;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.cors.EnableCORS;
import net.ninemm.base.utils.ScpClient;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

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


}
