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

import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义 MetaBuilder，处理表前缀跳过生成
 * @author Eric
 *
 */
public class AppMetaBuilder extends MetaBuilder {
    /**
     * 需要跳过生成的表前缀
     */
    private String[] skipPre = null;

    public AppMetaBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected boolean isSkipTable(String tableName) {
        for (String skip : skipPre) {
            if (tableName.startsWith(skip)) {
                return true;
            }
        }
        return false;
    }

    public void setSkipPre(String... skipPre) {
        this.skipPre = skipPre;
    }

    @Override
    protected ResultSet getTablesResultSet() throws SQLException {
        String schemaPattern = dialect instanceof OracleDialect ? dbMeta.getUserName() : null;
        return dbMeta.getTables(conn.getCatalog(), schemaPattern, null, new String[]{"TABLE", "VIEW"});
    }

    @Override
    protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
        if (tableMeta.name.toLowerCase().endsWith("_view")) {
            tableMeta.primaryKey = "id";
        } else {
            super.buildPrimaryKey(tableMeta);
        }
    }
}
