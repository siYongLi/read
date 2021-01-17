package net.ninemm.base.plugin.plugins;

import com.google.gson.Gson;
import com.jfinal.kit.Base64Kit;
import com.jfinal.kit.LogKit;
import com.jfinal.upload.UploadFile;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.jboot.utils.StrUtil;
import net.ninemm.base.SystemOptions;
import net.ninemm.base.common.Consts;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class QiniuKit {

	static QiniuConfig qConfig;
	
	static void init(QiniuConfig config) {
		qConfig = config;
	}

	public static String uploadFile(UploadFile file) {
		if (file!=null) {
			String bucket = SystemOptions.get(Consts.QINIU_BUCKET);
			String fileName = file.getFileName();
			String localPath = file.getUploadPath()+"/"+fileName;
			String sufferFix = null;
			if(fileName.contains(".")){
				sufferFix = fileName.substring(fileName.lastIndexOf("."));
			}

			String savefileName = StrUtil.uuid();
			if(sufferFix!=null){
				savefileName = savefileName+sufferFix;
			}
			DefaultPutRet res = QiniuKit.put(bucket, savefileName, localPath);
			if (res != null){
				return SystemOptions.get(Consts.QINIU_DOMAIN)+"/"+res.key;
			}
		}
		return null;
	}


	public static String uploadFile(String base64, String sufferFix) {
		if (StrUtil.isNotEmpty(base64)) {
            String bucket = SystemOptions.get(Consts.QINIU_BUCKET);
			byte[] b = Base64Kit.decode(base64);
			String fileName = StrUtil.getRandomUUID();
			if(StrUtil.isNotEmpty(sufferFix)){
				fileName=fileName+sufferFix;
			}
			DefaultPutRet res = QiniuKit.put(bucket, fileName, b);
			if (res != null){
                return SystemOptions.get(Consts.QINIU_DOMAIN)+"/"+res.key;
			}
		}
		return null;
	}

	public static String uploadTxt(String text) {
		if (StrUtil.isNotEmpty(text)) {
			String bucket = SystemOptions.get(Consts.QINIU_BUCKET);
			byte[] b = text.getBytes();
			String fileName = StrUtil.getRandomUUID()+".txt";
			DefaultPutRet res = QiniuKit.put(bucket, fileName, b);
			if (res != null){
				return SystemOptions.get(Consts.QINIU_DOMAIN)+"/"+res.key;
			}
		}
		return null;
	}

	public static String uploadQrCodePng(byte[] b) {
		if (b.length>0) {
			String bucket = SystemOptions.get(Consts.QINIU_BUCKET);
			String fileName = StrUtil.getRandomUUID()+".png";
			DefaultPutRet res = QiniuKit.put(bucket, fileName, b);
			if (res != null){
				return SystemOptions.get(Consts.QINIU_DOMAIN)+"/"+res.key;
			}
		}
		return null;
	}

	public static DefaultPutRet put(String bucket, String key, String localFilePath) {
		Configuration cfg = new Configuration(Zone.zone0());
		UploadManager uploadManager = new UploadManager(cfg);
		String token = qConfig.getToken(bucket);
		
		try {
			Response response = uploadManager.put(localFilePath, key, token);
			//解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    return putRet;
		} catch (QiniuException e) {
			e.printStackTrace();
			Response r = e.response;
			try {
				LogKit.error(r.bodyString());
			} catch (QiniuException e1) {
				e1.printStackTrace();
				return null;
			}
			return null;
		}
	}
	
	public static DefaultPutRet put(String bucket, String key, byte[] uploadBytes) {
		
		Configuration cfg = new Configuration(Zone.zone0());
		UploadManager uploadManager = new UploadManager(cfg);
		String token = qConfig.getToken(bucket);
		
		try {
			Response response = uploadManager.put(uploadBytes, key, token);
			//解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    return putRet;
		} catch (QiniuException e) {
			e.printStackTrace();
			Response r = e.response;
			try {
				LogKit.error(r.bodyString());
			} catch (QiniuException e1) {
				e1.printStackTrace();
				return null;
			}
			return null;
		}
	}
	
	public static DefaultPutRet put(String bucket, String key, ByteArrayInputStream byteInputStream) {
		
		Configuration cfg = new Configuration(Zone.zone0());
		UploadManager uploadManager = new UploadManager(cfg);
		String token = qConfig.getToken(bucket);
		
		try {
			Response response = uploadManager.put(byteInputStream, key, token, null, null);
			//解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    return putRet;
		} catch (QiniuException e) {
			e.printStackTrace();
			Response r = e.response;
			try {
				LogKit.error(r.bodyString());
			} catch (QiniuException e1) {
				e1.printStackTrace();
				return null;
			}
			return null;
		}
	}

	public static String readTxt(String url)  throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try(InputStream inputStream =new URL(url).openStream()){
			IOUtils.copy(inputStream, byteArrayOutputStream);
		}
		return byteArrayOutputStream.toString();
	}

	public static ByteArrayOutputStream readBaosByUrlPath(String urlPath) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try(InputStream inputStream =new URL(urlPath).openStream()){
			IOUtils.copy(inputStream, byteArrayOutputStream);
		}
		return byteArrayOutputStream;
	}


	public String getPublicUrl(String domain, String bucket, String filename) {
		String publicUrl = String.format("%s/%s/%s", domain, bucket, filename);
		Auth auth = qConfig.getAuth();
		long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
		String url = auth.privateDownloadUrl(publicUrl, expireInSeconds);
		return url;
	}
	
	public static Response delete(String bucket, String key) {
		Configuration cfg = new Configuration(Zone.zone0());
		Auth auth = qConfig.getAuth();
		BucketManager bucketManager = new BucketManager(auth, cfg);
		try {
			Response res = bucketManager.delete(bucket, key);
			return res;
		} catch (QiniuException e) {
			return e.response;
		}
	}
	
}
