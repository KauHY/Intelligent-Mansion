package com.crlink.smartpark.core.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.crlink.smartpark.common.constants.SmsConstant;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class SendSmsUtil {

    private static String SMS_REGION_ID;
    private static String SMS_ACCESSKEY_ID;
    private static String SMS_SECRET;
    private static String aliyun_sms_product = "Dysmsapi";
    private static String aliyun_sms_domain = "dysmsapi.aliyuncs.com";

    @Value("${aliyun.sms.regionId}")
    public void setSmsRegionId(String regionId) {
        SMS_REGION_ID = regionId;
    }

    @Value("${aliyun.sms.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        SMS_ACCESSKEY_ID = accessKeyId;
    }

    @Value("${aliyun.sms.secret}")
    public void setSecret(String secret) {
        SMS_SECRET = secret;
    }


    /**
     * 阿里云发送短信通用方法
     *
     * @param signName      签名
     * @param templateCode  模板
     * @param phoneNumbers  手机号码集合
     * @param templateParam 参数集合
     */
    public static boolean sendSms(String signName, String templateCode, String phoneNumbers, String templateParam) {
        boolean isSendSuccess = false;
        try {
            IClientProfile profile = DefaultProfile.getProfile(SMS_REGION_ID, SMS_ACCESSKEY_ID, SMS_SECRET);
            DefaultProfile.addEndpoint(SMS_REGION_ID, SMS_REGION_ID, aliyun_sms_product, aliyun_sms_domain);
            IAcsClient client = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setPhoneNumbers(phoneNumbers);
            request.setTemplateParam(templateParam);

            SendSmsResponse response = client.getAcsResponse(request);
            if (null != response.getCode() && response.getCode().equals("OK")) {
                isSendSuccess = true;
            }
            System.out.println(new Gson().toJson(response));
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        } finally {
            return isSendSuccess;
        }
    }


    /**
     * 阿里云发送短信通用方法
     *
     * @param signName      签名
     * @param templateCode  模板
     * @param phoneNumbers  手机号码集合
     * @param templateParam 参数集合
     */
    public static boolean sendSms1(String signName, String templateCode, String phoneNumbers, String templateParam) {
        boolean isSendSuccess = false;
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "${ALIYUN_SMS_ACCESSKEYID}", "${ALIYUN_SMS_SECRET}");
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", aliyun_sms_product, aliyun_sms_domain);
            IAcsClient client = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setPhoneNumbers(phoneNumbers);
            request.setTemplateParam(templateParam);

            SendSmsResponse response = client.getAcsResponse(request);
            if (null != response.getCode() && response.getCode().equals("OK")) {
                isSendSuccess = true;
            }
            System.out.println(new Gson().toJson(response));
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        } finally {
            return isSendSuccess;
        }
    }


    public static void main(String[] args) {
//        String code = String.format("%06d", new Random().nextInt(999999));
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code", code);
//        sendSms1("淮安智慧谷", "SMS_243392928", "15279181580", JSONUtil.toJsonStr(jsonObject));

        // TODO: 密码生成即发送
        String pwd = MyStrUtil.genRandomNum();
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", "1232");
        jsonObject.set("telephone", "15279181580");
        sendSms1("淮安智慧谷", "SMS_245090841", "15279181580", JSONUtil.toJsonStr(jsonObject));
    }

}
