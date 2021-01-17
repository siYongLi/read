package net.ninemm.base.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lsy
 * @create: 2019-07-15 11:23
 **/
public class QrCodeUtil {
    public static byte[] getQrCodeUrl(String publishUrl) {
        Map<EncodeHintType, Object> hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        ByteArrayOutputStream out = null;
        try {
            QRCodeWriter writer =  new QRCodeWriter();
            out = new ByteArrayOutputStream();
            BitMatrix bitMatrix = writer.encode(publishUrl, BarcodeFormat.QR_CODE, 300, 300, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            byte[] bytes = out.toByteArray();
            out.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out!=null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
