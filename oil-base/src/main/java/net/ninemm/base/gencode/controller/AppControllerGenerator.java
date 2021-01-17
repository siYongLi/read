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
 */

package net.ninemm.base.gencode.controller;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import io.jboot.Jboot;
import net.ninemm.base.gencode.CodeGenHelpler;
import net.ninemm.base.gencode.model.AppMetaBuilder;

import javax.sql.DataSource;
import java.util.List;

/**
 * Controller代码自动生成
 *
 * @author lsy
 */
public class AppControllerGenerator {

    public static void doGenerate() {
        AppControllerGeneratorConfig config = Jboot.config(AppControllerGeneratorConfig.class);
        String modelPackage = config.getModelpackage();
        String controllerPackage = config.getControllerpackage();

        String modelPath = PathKit.getWebRootPath().substring(0, PathKit.getWebRootPath().indexOf(config.getProject())).replace("\\", "/");
        String outputDir = modelPath + config.getPath() +  controllerPackage.replace(".", "/");

        DataSource dataSource = net.ninemm.base.gencode.CodeGenHelpler.getDatasource();
        AppMetaBuilder metaBuilder = new AppMetaBuilder(dataSource);
        if (StrKit.notBlank(config.getRemovedtablenameprefixes())) {
            metaBuilder.setRemovedTableNamePrefixes(config.getRemovedtablenameprefixes().split(","));
        }

        if (StrKit.notBlank(config.getExcludedtableprefixes())) {
            metaBuilder.setSkipPre(config.getExcludedtableprefixes().split(","));
        }
        List<TableMeta> tableMetaList = metaBuilder.build();
        if (StrKit.notBlank(config.getIncludedtable())) {
            CodeGenHelpler.includeTables(tableMetaList, config.getIncludedtable());
        } else {
            CodeGenHelpler.excludeTables(tableMetaList, config.getExcludedtable());
        }

        new ControllerGenerator(modelPackage, controllerPackage, outputDir, tableMetaList).generate();
    }
}
