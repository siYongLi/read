package net.ninemm.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import io.jboot.utils.StrUtil;
import net.ninemm.base.SystemOptions;
import net.ninemm.base.common.Consts;

import java.util.Arrays;
import java.util.List;

/**
 * 高德
 *
 * @author Eric.Huang
 * @date 2018-09-19 10:48
 **/

public class GaoDeUtils {

    private static final String GEO_CODER_URL = "http://restapi.amap.com/v3/geocode/regeo?location=${location}&output=${output}&key=${key}";
    private static final String IP_URL = "https://restapi.amap.com/v3/ip?ip=${ip}&output=json&key=${key}";

    //直辖市 :北京市、上海市、天津市、重庆市
    private static final List CITYLIST= Arrays.asList("北京市","上海市","天津市","重庆市");

    public static void main(String[] args) {//116.393058,39.959857
        //114.34573,30.52892
        //116.55438， 40.087616 北京
        //121.453745,31.255182 上海
        //113.166706,30.644923 天门
        //114.200417,22.262863 香港
        //113.55248,22.18791 澳门
        //74.451467,26.188711 国外
        System.out.println(getAddresJson("792626a8e422ea6fc7bc2ead87661b78", Double.parseDouble("116.55438"),Double.parseDouble("40.087616")));
        System.out.println(getAddresJson("792626a8e422ea6fc7bc2ead87661b78", Double.parseDouble("121.453745"),Double.parseDouble("31.255182")));
        System.out.println(getAddresJson("792626a8e422ea6fc7bc2ead87661b78", Double.parseDouble("113.166706"),Double.parseDouble("30.644923")));
        System.out.println(getAddresJson("792626a8e422ea6fc7bc2ead87661b78", Double.parseDouble("114.200417"),Double.parseDouble("22.262863")));
        System.out.println(getAddresJson("792626a8e422ea6fc7bc2ead87661b78", Double.parseDouble("113.55248"),Double.parseDouble("22.18791")));
        System.out.println(getAddresJson("792626a8e422ea6fc7bc2ead87661b78", Double.parseDouble("74.451467"),Double.parseDouble("26.188711")));

        //String[] addressByIp = findAddressByIp("27.22.92.20", "792626a8e422ea6fc7bc2ead87661b78");
        //System.out.println(addressByIp[0]);
    }

    public static String[] findAddressByIp(String ip,String key){
        String[] arr = new String[2];
        if (!StrUtil.isNotEmpty(key)) {
            key = SystemOptions.get(Consts.GAODE_KEY);
        }
        String url = IP_URL.replace("${ip}",ip).replace("${key}", key);
        try {
            String json = HttpUtils.get(url);
            JSONObject jsonObject = JSONObject.parseObject(json);
            String info = jsonObject.getString("info");
            if ("OK".equals(info)) {
                arr[0]=jsonObject.getString("province");
                arr[1]=jsonObject.getString("city");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static String getCity(String key, Double lng, Double lat) {

        JSONObject locationObj = getLocationInfo(key, lng, lat);
        if (locationObj == null) {
            return null;
        }

        JSONObject obj = locationObj.getJSONObject("regeocode")
                .getJSONObject("addressComponent");
        return obj.getString("city");
    }

    public static JSONObject getLocationInfo(String key, Double lng, Double lat) {
        String url = GEO_CODER_URL.replace("${location}", lng + "," + lat)
                .replace("${output}", "json").replace("${key}", key);
        try {
            String json = HttpUtils.get(url);
            JSONObject obj = JSONObject.parseObject(json);
            LogKit.info("\n"+"经纬度查询地址为：" + url);
            LogKit.info("\n"+"经纬度坐标返回结果为：" + obj.toString());
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getAddresJson(String key, Double lng, Double lat) {  //lat经度，lng纬度
        JSONObject addressJson = new JSONObject();
        JSONObject locationObj = getLocationInfo(key, lng, lat);
        if (locationObj == null) {
            return null;
        }

        JSONObject obj = locationObj.getJSONObject("regeocode");
        JSONObject addressComponent = obj.getJSONObject("addressComponent");
        String province = addressComponent.getString("province");
        if ("[]".equals(province)) {
            //国外地址
            return null;
        }
        String district = addressComponent.getString("district")==null?"":addressComponent.getString("district");
        String street = addressComponent.getString("street")==null?"":addressComponent.getString("street");
        String city = null;
        if (CITYLIST.contains(province)) {
            //直辖市需要把市字去掉
            city = province;
            province = province.replace("市", "");
        }else {
            //非直辖市 当city 为空是需要把地区值设置为市
            city = addressComponent.getString("city").equals("[]")?district:addressComponent.getString("city");
        }

        addressJson.put("province",province);
        addressJson.put("city",city);
        addressJson.put("district",district);

        StringBuilder address = new StringBuilder();
        if (!province.equals(city)) {
            address.append(province);
        }
        address.append(city).append(district).append(street);

        String township = addressComponent.getString("township");
        if (!"[]".equals(township)) {
            address.append(township);
        }
        JSONObject streetNumber = addressComponent.getJSONObject("streetNumber");
        if (streetNumber != null) {
            String strNum = streetNumber.getString("street");
            if (StrKit.notBlank(strNum)) {
                address.append(strNum);
            }

            String number = streetNumber.getString("number");
            if (StrKit.notBlank(number)) {
                address.append(number);
            }
        }

        addressJson.put("address",address);
        return addressJson;
    }

}
