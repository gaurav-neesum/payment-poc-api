package com.cybersource.test;

import com.cybersource.CybersourceConstants;

import java.util.Properties;

public class Config {

    public  static Properties getMerchantDetails(){
        Properties props = new Properties();

        // HTTP_Signature = http_signature and JWT = jwt
        props.setProperty("authenticationType", "http_signature");
        props.setProperty("merchantID", "novacroft_sandbox");
        props.setProperty("runEnvironment", "apitest.cybersource.com");
//        props.setProperty("requestJsonPath", "src/main/resources/request.json");


        // HTTP Parameters
        props.setProperty("merchantKeyId", CybersourceConstants.restApiKey);
        props.setProperty("merchantsecretKey", CybersourceConstants.restApiSharedSecret);


        return props;
    }
}
