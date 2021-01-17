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

package net.ninemm.base.exception;

import io.jboot.exception.JbootException;

/**
 * 异常信息获取类
 *
 * @author Eric
 */
public class ExceptionLoader {

    public static String read(JbootException e) {
        String message = null;

        if (e.getClass() == BusinessException.class) {
            message = e.getMessage();
        } else if (e.getCause() != null && e.getCause().getClass() == BusinessException.class) {
            message = e.getCause().getMessage();
        } else {
            throw e;
        }

        return message;
    }
}
