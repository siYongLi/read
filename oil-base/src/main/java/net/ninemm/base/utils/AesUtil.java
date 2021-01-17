package net.ninemm.base.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @description:
 * @author: lsy
 * @create: 2019-07-15 11:23
 **/
public class AesUtil {
    public static void main(String args[]) throws Exception {

        String content = "明文 123 abc";

        //加密
        String encrypted = encrypt(content, KEY, IV);
        //解密
        String decrypted = decrypt("VOcmiyVWoEiIjBkcJ5krCbl9/UX1tQM9V5cnxlnPh9AmJtewZsw6f8SFEpTCu7XWp6mJAhH0bM4Ad6W/X2FNImscH/MrzgDU2MbJxPp9upo=", KEY, IV);

        System.out.println("加密前：" + content);

        System.out.println("加密后：" + encrypted);

        System.out.println("解密后：" + decrypted);
    }

    private static String KEY = "abcdef0123456789"; // 长度必须是 16

    private static String IV = "abcdef0123456789";  // 长度必须是 16

    /**
     * 加密返回的数据转换成 String 类型
     * @param content 明文
     * @param key 秘钥
     * @param iv 初始化向量是16位长度的字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key, String iv) throws Exception {
        // 将返回的加密过的 byte[] 转换成Base64编码字符串  ！！！！很关键
        return base64ToString(AES_CBC_Encrypt(content.getBytes(), key.getBytes(), iv.getBytes()));
    }

    public static String encrypt(String content) throws Exception {
        // 将返回的加密过的 byte[] 转换成Base64编码字符串  ！！！！很关键
        return base64ToString(AES_CBC_Encrypt(content.getBytes(), KEY.getBytes(), IV.getBytes()));
    }

    /**
     * 将解密返回的数据转换成 String 类型
     * @param content Base64编码的密文
     * @param key 秘钥
     * @param iv 初始化向量是16位长度的字符串
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String key, String iv) throws Exception {
        // stringToBase64() 将 Base64编码的字符串转换成 byte[]  !!!与base64ToString(）配套使用
        return new String(AES_CBC_Decrypt(stringToBase64(content), key.getBytes(), iv.getBytes()));
    }

    private static byte[] AES_CBC_Encrypt(byte[] content, byte[] keyBytes, byte[] iv){
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:"+e.toString());
        }
        return null;
    }

    private static byte[] AES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] iv){
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.out.println("exception:"+e.toString());
        }
        return null;
    }

    /**
     * 字符串装换成 Base64
     */

    public static byte[] stringToBase64(String key) throws Exception {
        return Base64.decodeBase64(key.getBytes());
    }

    /**
     * Base64装换成字符串
     */
    public static String base64ToString(byte[] key) throws Exception {
        return new Base64().encodeToString(key);
    }
}
