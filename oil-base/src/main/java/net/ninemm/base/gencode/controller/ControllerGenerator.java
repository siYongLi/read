/**
 * Copyright (c) 2016-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ninemm.base.gencode.controller;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControllerGenerator {
    private String modulePackage;
    private Kv data;
    private String controllerOutputDir;
    private String targetTemplate;
    private String targetOutputDirFile;
    List<TableMeta> tableMetaList;

    private Engine engine = Engine.create("forUI");

    public ControllerGenerator(String modelPackage, String controllerPackage, String outputDir, List<TableMeta> tableMetaList) {
        this.tableMetaList = tableMetaList;
        this.controllerOutputDir = outputDir;

        modulePackage = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        data = Kv.by("modulePackage", modulePackage);
        data.set("modelPackage", modelPackage);

        data.set("controllerPackage", controllerPackage);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        data.set("date", sdf.format(new Date()));
        engine.setSourceFactory(new ClassPathSourceFactory());

    }

    public void generate() {
        for (TableMeta tableMeta : tableMetaList) {
            data.set("tableMeta", tableMeta);
            String lowerCaseModelName = toLowerCaseFirstOne(tableMeta.modelName);
            data.set("lowerCaseModelName", lowerCaseModelName);

            targetTemplate = "services/ui_controller_template.jf";
            String targetOutputDir = controllerOutputDir;
            targetOutputDirFile = targetOutputDir + File.separator + tableMeta.modelName + "Controller" + ".java";

            String content = engine.getTemplate(targetTemplate).renderToString(data);

            File dir = new File(targetOutputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File targetFile = new File(targetOutputDirFile);
            if (targetFile.exists()) {
                continue;
            }
            try {
                FileWriter fw = new FileWriter(targetOutputDirFile);
                try {
                    fw.write(content);
                } finally {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    public String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }


}
