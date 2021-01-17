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

package net.ninemm.base.gencode.model;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import io.jboot.Jboot;
import io.jboot.codegen.model.JbootBaseModelGenerator;
import io.jboot.codegen.model.JbootModelGenerator;
import net.ninemm.base.gencode.CodeGenHelpler;

import javax.sql.DataSource;
import java.util.List;

/**
 * model代码自动生成
 * @author Eric
 *
 */
public class AppModelGenerator {

    public static void doGenerate() {
        AppModelGeneratorConfig config = Jboot.config(AppModelGeneratorConfig.class);

        System.out.println(config.toString());

        if (StrKit.isBlank(config.getModelpackage())) {
            System.err.println("jboot.admin.model.ge.modelpackage 不可为空");
            System.exit(0);
        }

        String modelPackage = config.getModelpackage();
        String baseModelPackage = modelPackage + ".base";

        String project = Jboot.configValue("jboot.admin.model.ge.project");
        String path = Jboot.configValue("jboot.admin.model.ge.path");

        String modelPath = PathKit.getWebRootPath().substring(0,PathKit.getWebRootPath().indexOf(project)).replace("\\", "/");
        String modelDir = modelPath+  path + modelPackage.replace(".", "/");
        String baseModelDir = modelPath+  path + baseModelPackage.replace(".", "/");

        System.out.println("start generate...");
        System.out.println("generate dir:" + modelDir);

        DataSource dataSource = CodeGenHelpler.getDatasource();

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

        new JbootBaseModelGenerator(baseModelPackage, baseModelDir).generate(tableMetaList);
        new JbootModelGenerator(modelPackage, baseModelPackage, modelDir).generate(tableMetaList);

        System.out.println("entity generate finished !!!");

    }

}
