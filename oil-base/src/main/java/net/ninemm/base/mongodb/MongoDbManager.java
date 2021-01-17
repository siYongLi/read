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

package net.ninemm.base.mongodb;

import com.cybermkd.mongo.kit.MongoKit;
import com.cybermkd.mongo.plugin.MongoPlugin;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.mongodb.MongoClient;
import io.jboot.Jboot;

/**
 * ElasticSearch 工具类
 *
 * @author Eric.Huang
 * @date 2018-09-04 14:25
 **/

public class MongoDbManager {

    private static MongoDbManager me = new MongoDbManager();

    public static MongoDbManager me() {
        return me;
    }

    public  void init() {
        try {
            MongoDbConfig config = Jboot.config(MongoDbConfig.class);
            MongoPlugin mongoPlugin=new MongoPlugin();
            mongoPlugin.add(config.getHost(),config.getPort());
            mongoPlugin.setDatabase(config.getDatabase());
            if(StrKit.notBlank(config.getUser(), config.getPassword())){
                mongoPlugin.auth(config.getUser(), config.getPassword());
            }

            MongoClient client = mongoPlugin.getMongoClient();
            MongoKit.INSTANCE.init(client, mongoPlugin.getDatabase());
//            client.close();

        } catch (Exception e) {
            LogKit.error("Init mongoDB Failure!");
        }
    }


}
