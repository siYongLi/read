package net.ninemm.base.utils;

import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.QrcodeApi;
import com.jfinal.weixin.sdk.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lsy
 * @create: 2019-03-28 15:46
 **/
public class WxQrCodeUtils extends QrcodeApi {
    /**
     * 创建临时二维码
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）。
     * @param senceStr 场景值,字符串
     * @return ApiResult 二维码信息
     */
    public static ApiResult createStrTemporary(int expireSeconds, String senceStr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("expire_seconds", expireSeconds);
        params.put("action_name", "QR_STR_SCENE");

        Map<String, Object> actionInfo = new HashMap<String, Object>();
        Map<String, Object> scene = new HashMap<String, Object>();
        scene.put("scene_str", senceStr);

        actionInfo.put("scene", scene);
        params.put("action_info", actionInfo);
        return create(JsonUtils.toJson(params));
    }
}
