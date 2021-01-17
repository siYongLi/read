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

package net.ninemm.base.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Rest返回结果
 *
 * @author Eric
 */
@ApiModel
public class RestResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(required = true)
	private int code;
	@ApiModelProperty(required = true)
    private String msg;
    private T data;

    public RestResult() {

    }

    public RestResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RestResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RestResult<Object> buildSuccess() {
        RestResult<Object> restResult = new RestResult<Object>();
        restResult.success();
        return restResult;
    }

    public static RestResult<Object> buildSuccess(Object t) {
        RestResult<Object> restResult = buildSuccess();
        restResult.setData(t);
        return restResult;
    }

    public static RestResult<Object> buildError() {
        RestResult<Object> restResult = new RestResult<Object>();
        restResult.error();
        return restResult;
    }

    public static RestResult<Object> buildError(String msg) {
        RestResult<Object> restResult = new RestResult<Object>();
        restResult.error(msg);
        return restResult;
    }

    public RestResult<T> success() {
        this.code = ResultCode.SUCCESS;
        this.msg = "操作成功";
        return this;
    }

    public RestResult<T> success(T t) {
        success();
        this.data = t;
        return this;
    }

    public RestResult<T> error() {
        this.code = ResultCode.ERROR;
        this.msg = "操作失败";
        return this;
    }

    public RestResult<T> error(String msg) {
        this.code = ResultCode.ERROR;
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
