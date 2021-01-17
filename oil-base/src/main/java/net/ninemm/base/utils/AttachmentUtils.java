/**
 * Copyright (c) 2015-2016, Eric Huang 黄鑫 (ninemm@qq.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ninemm.base.utils;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;
import io.jboot.utils.FileUtil;
import io.jboot.utils.StrUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AttachmentUtils {

	private static final Log LOG = Log.getLog(AttachmentUtils.class);

	public static String moveFile(UploadFile uploadFile) {
		if (uploadFile == null) {
			return null;
		}

		File file = uploadFile.getFile();
		if (!file.exists()) {
			return null;
		}

		File newfile = newAttachemnetFile(FileUtil.getSuffix(file.getName()));

		if (!newfile.getParentFile().exists()) {
			newfile.getParentFile().mkdirs();
		}

		try {
			cn.hutool.core.io.FileUtil.move(file, newfile, true);
		} catch (Exception e) {
			LOG.error(e.toString(), e);
		}

		String attachmentRoot = PathKit.getWebRootPath();
		return FileUtil.removePrefix(newfile.getAbsolutePath(), attachmentRoot);
	}

	public static File newAttachemnetFile(String suffix) {

		String attachmentRoot = PathKit.getWebRootPath();
		String uuid = UUID.randomUUID().toString().replace("-", "");

		StringBuilder newFileName = new StringBuilder(attachmentRoot)
			.append(File.separator).append("attachment")
			.append(File.separator).append(new SimpleDateFormat("yyyyMMdd").format(new Date()))
			.append(File.separator).append(uuid)
			.append(suffix);

		return new File(newFileName.toString());
	}

	public static File file(String path) {
		String attachmentRoot = PathKit.getWebRootPath();
		return new File(attachmentRoot, path);
	}

	/**
	 * @param uploadFile
	 * @return new file relative path
	 */
	public static String moveFile(UploadFile uploadFile, String rootFilePath) {
		if (uploadFile == null) {
			return null;
		}

		File file = uploadFile.getFile();
		if (!file.exists()) {
			return null;
		}

		String webRoot = PathKit.getWebRootPath();
		String uuid = UUID.randomUUID().toString().replace("-", "");

		StringBuilder newFileName = new StringBuilder(webRoot)
				.append(File.separator).append("attachment")
				.append(File.separator).append(new SimpleDateFormat("yyyyMMdd").format(new Date()))
				.append(File.separator).append(uuid)
				.append(FileUtil.getSuffix(file.getName()));

		File newfile = new File(newFileName.toString());

		if (!newfile.getParentFile().exists()) {
			newfile.getParentFile().mkdirs();
		}

		file.renameTo(newfile);

		return FileUtil.removePrefix(newfile.getAbsolutePath(), webRoot);
	}

	static List<String> imageSuffix = new ArrayList<String>();

	static {
		imageSuffix.add(".jpg");
		imageSuffix.add(".jpeg");
		imageSuffix.add(".png");
		imageSuffix.add(".bmp");
		imageSuffix.add(".gif");
		imageSuffix.add(".xls");
		imageSuffix.add(".xlsx");
	}

	public static boolean isImage(String path) {
		String sufffix = FileUtil.getSuffix(path);
		if (StrUtil.isNotBlank(sufffix)) {
			return imageSuffix.contains(sufffix.toLowerCase());
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(FileUtil.getSuffix("xxx.jpg"));
	}

}
