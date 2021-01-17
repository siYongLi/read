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

package net.ninemm.base.web.base;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jfinal.core.NotAction;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.jfinal.weixin.sdk.utils.IOUtils;
import io.jboot.web.controller.JbootController;
import net.ninemm.base.common.Consts;
import net.ninemm.base.common.RestResult;
import net.ninemm.base.utils.AttachmentUtils;
import org.apache.commons.collections4.MapUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * 控制器基类
 *
 * @author Eric
 */
public class BaseController extends JbootController {

	private static final Log LOG = Log.getLog(BaseController.class);

	private Map<String, Object> responseMap = new TreeMap<String, Object>();

	protected Integer getPageNumber() {
		return getParaToInt("page", 1);
	}

	protected Integer getPageSize() {
		return getParaToInt("limit", Consts.PAGE_DEFAULT_SIZE);
	}

	/**
	 * 获取请求参数信息
	 */
	@NotAction
	protected Map<String, Object> getRequestMap() {
		Map<String, Object> result = null;
		try {
			String message = IOUtils.toString(getRequest().getInputStream(), Charset.defaultCharset());
			LOG.info("收到请求参数：" + message);
			result = JSONObject.parseObject(message, Map.class);
		} catch (Exception e) {
			LOG.error("解析请求参数出错：", e);
		}
		return result;
	}

	@NotAction
	public HashMap<String, String> getUploadFilesURLMap(String rootFilePath) {
		List<UploadFile> fileList = null;
		if (isMultipartRequest()) {
			fileList = getFiles();
		}

		HashMap<String, String> filesMap = null;
		if (fileList != null) {
			filesMap = new HashMap<String, String>();
			for (UploadFile ufile : fileList) {
				String filePath = AttachmentUtils.moveFile(ufile, rootFilePath).replace("\\", "/");
				filesMap.put(ufile.getParameterName(), filePath);
			}
		}
		return filesMap;
	}
	
	@Override
	@NotAction
	public Map<String, String[]> getParaMap() {
		Map<String, String[]> params = super.getParaMap();
		if(MapUtils.isEmpty(params)) {
			return null;
		}
		
		for (Iterator<Entry<String, String[]>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
    		Entry<String, String[]> entry = iterator.next();
    		String[] arr = entry.getValue();
    		if(arr != null && arr.length > 0) {
    			for (int i = 0; i < arr.length; i++) {
    				arr[i] = StrKit.isBlank(arr[i]) ? null : arr[i].trim();
    			}
    		}
		}
		return params;
	}

	public Map<String, Object> getAllParaMap() {
		Map<String, Object> params = Maps.newHashMap();
		Enumeration<String> paraNames = super.getParaNames();

		while (paraNames.hasMoreElements()) {
			String paraName = (String) paraNames.nextElement();
			String[] paraValues = super.getParaValues(paraName);

			if (paraValues.length > 0) {
				String paraValue = paraValues[0];
				if (StrKit.notBlank(paraValue)) {
					params.put(paraName, paraValue);
				}
			}
		}

		return params;
	}

	@NotAction
	public void renderAjaxResult(String message, int code, Object data) {
		RestResult restResult = new RestResult();
		restResult.setMsg(message);
		restResult.setCode(code);
		restResult.setData(data);

		if (isIEBrowser()) {
			render(new JsonRender(restResult).forIE());
		} else {
			renderJson(restResult);
		}
	}

	@NotAction
	public void downloadExcel(List<List<Object>> datas, Sheet sheet, String fileName){
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel");
		OutputStream out = null;
		try {
			response.setHeader("content-disposition","attachment;filename="+java.net.URLEncoder.encode(fileName, "UTF-8"));
			out = response.getOutputStream();
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
			writer.write1(datas, sheet);
			writer.finish();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@NotAction
	public void downloadExcel(List<List<Object>> datas, Sheet sheet, Table table , String fileName){
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel");
		OutputStream out = null;
		try {
			response.setHeader("content-disposition","attachment;filename="+java.net.URLEncoder.encode(fileName, "UTF-8"));
			out = response.getOutputStream();
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
			writer.write1(datas, sheet,table);
			writer.finish();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在服务器上生成文件并返回文件的路径
	 * @param datas
	 * @param sheet
	 * @param fileName
	 */
	@NotAction
	public String writeExcel(List<List<Object>> datas, Sheet sheet,String fileName,String filePath){

		FileOutputStream out = null;
		try {
			File folder = new File(filePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			filePath = filePath+"\\"+fileName;
			File file = new File(filePath);
			out =new FileOutputStream(file);
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
			writer.write1(datas, sheet);
			writer.finish();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filePath;
	}
}
