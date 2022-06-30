package com.phoenix.read.common;

/**
 * 通用常量
 * @author yannis
 * @version 2020/8/1 16:50
 */
public class CommonConstants {

    public final static String SESSION = "session";
    public final static String APP_NAME = "sex-edu";
    public final static String SHADOW_TEST = "shadow-test";
    public final static String SEPARATOR = ",";
    public final static String CHAT_RECORD_COLLECTION_NAME = "chat_record";
    public final static String WX_SESSION_REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";

    //https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_5_1.shtml
    public final static String WX_PAY_REQUEST_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    public final static String CNY_CURRENCY = "CNY";
    public final static String SIGN_TYPE_RSA = "RSA";
    public final static String SIGN_TYPE_HMAC_SHA256 = "HMAC-SHA256";
    public final static String LANG_TYPE_ZH_CN = "zh_CN";

    public final static String COS_REGION = "ap-shanghai";
    public final static String COS_APP_ID = "1305159828";
    public final static String COS_SECRET_ID = "AKIDwgANJxKQGp78tNaVAYfW6QTFkyvb092e";
    public final static String COS_SECRET_KEY = "MTKuDF0UgbFcYCZgW5NO8bmhLiocARRA";
    public static final String COS_BUCKET_NAME = "read-1305159828";
}
