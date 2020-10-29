package com.offcn.common.demo;

import com.offcn.common.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;

public class SmsDemo {
    public static void main(String[] args) {

        String host = " http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String appcode = "f44b9437185349ad89f5504b8e01f393";

        //准备请求头
        HashMap<String, String> handers = new HashMap<>();
        handers.put("Authorization", "APPCODE"+appcode);

        //请求参数
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", "15526852709");
        params.put("param", "code:8888");
        params.put("tpl_id", "TP1711063");

        try {
            HttpResponse httpResponse = HttpUtils.doPost(host, path, method, handers, params, "");
            System.out.println(httpResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
