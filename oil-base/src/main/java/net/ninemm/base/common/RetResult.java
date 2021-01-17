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

import java.io.Serializable;

/**
 * rpc 返回结果
 *
 * @author eric
 */
public class RetResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 成功失败 */
    private boolean success = true;
    /** 返回信息 */
    private T data;

    /** 错误信息 */
    private ErrorResult error;

    public static <T> RetResult<T> buildSuccess() {
        return buildSuccess(null);
    }

    public static <T> RetResult<T> buildSuccess(T t) {
        RetResult<T> rpcResult = new RetResult<T>();
        rpcResult.success(t);
        return rpcResult;
    }

    public static <T> RetResult<T> buildError() {
        return buildError(ErrorResult.NORMAL_ERROR, "操作失败");
    }

    public static <T> RetResult<T> buildValidatorError(String msg) {
        return buildError(ErrorResult.VALIDATOR_ERROR, msg);
    }

    public static <T> RetResult<T> buildBusinessError(String msg) {
        return buildError(ErrorResult.BUSINESS_ERROR, msg);
    }

    public static <T> RetResult<T> buildSystemError(String msg) {
        return buildError(ErrorResult.SYSTEM_ERROR, msg);
    }

    public static <T> RetResult<T> buildError(Integer code, String msg) {
        RetResult<T> rpcResult = new RetResult<T>();
        rpcResult.error(code, msg);
        return rpcResult;
    }

    private void success(T t) {
        this.success = true;
        this.data = t;
        this.error = null;
    }

    private void error(Integer code, String msg) {
        this.success = false;
        this.data = null;
        this.error = new ErrorResult(code, msg);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorResult getError() {
        return error;
    }

    public void setError(ErrorResult error) {
        this.error = error;
    }
}
