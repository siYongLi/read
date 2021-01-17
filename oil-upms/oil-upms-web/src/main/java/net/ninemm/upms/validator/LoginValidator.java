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

package net.ninemm.upms.validator;

import com.jfinal.core.Controller;
import net.ninemm.base.web.base.BaseJsonValidator;

/**
 * 登录校验
 *
 * @author Eric.Huang
 * @date 2018-12-30 22:31
 * @package net.ninemm.upms.validator
 **/

public class LoginValidator extends BaseJsonValidator {
    @Override
    protected void validate(Controller controller) {
        validateCaptcha("code", "");
    }
}
