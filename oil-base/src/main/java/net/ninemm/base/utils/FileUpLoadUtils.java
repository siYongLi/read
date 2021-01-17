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

import cn.hutool.core.codec.Base64;
import com.jfinal.kit.PathKit;
import io.jboot.utils.StrUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUpLoadUtils {
    public static String saveFile(String base64,String sufferFix) {
        String uuid = StrUtil.uuid();
        FileOutputStream fos=null;
        BufferedOutputStream bos=null;
        try {
            String filePath = PathKit.getRootClassPath()+"/webapp/uploadfiles";
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(sufferFix==null?(filePath+"/"+uuid):(filePath+"/"+uuid+sufferFix));
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            byte[] decode = Base64.decode(base64);
            fos = new FileOutputStream(file, true);
            bos = new BufferedOutputStream(fos);
            bos.write(decode);
            bos.flush();
            bos.close();
            fos.close();
            return sufferFix==null?("/uploadfiles/"+uuid):("/uploadfiles/"+uuid+sufferFix);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (bos!=null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
