package com.cybersource;

import com.cybersource.authsdk.core.Authorization;
import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.payloaddigest.PayloadDigest;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PocHttpConnection {
    private static Logger logger = LogManager.getLogger(PocHttpConnection.class);
    private MerchantConfig merchantConfig;
    private PayloadDigest digestPayLoad;
    private HttpURLConnection con;
    private String requestType;
    private String requestId;
    private String url;
    private String merchantId;
    private String digestMessageBody;
    private String requestHost;

    public PocHttpConnection(MerchantConfig merchantConfig) {
        this.merchantConfig = merchantConfig;
        this.digestPayLoad = new PayloadDigest(merchantConfig);
        this.requestType = merchantConfig.getRequestType();
        this.requestHost = merchantConfig.getRequestHost();
        this.merchantId = merchantConfig.getMerchantID();
        if (this.requestType.equalsIgnoreCase("GET")) {
            this.requestId = Utility.retrieveGetIDFromRequestTarget(merchantConfig.getRequestTarget());
            logger.info("GET_ID : {}", this.requestId);
        }

    }

    public void setHeaderData(String merchantId) {
        try {
            logger.info("{} : {}", "v-c-merchant-id", merchantId);
            this.con.setRequestProperty("v-c-merchant-id", merchantId);
        } catch (Exception var3) {
            logger.fatal("Unable to set merchantID");
            logger.error(var3);
        }

    }

    public void openConnection() {
        try {
            this.url = "https://" + this.merchantConfig.getRequestHost() + this.merchantConfig.getRequestTarget();
            URL urlObj = new URL(this.url);
            this.con = (HttpURLConnection)urlObj.openConnection();
        } catch (Exception var2) {
            logger.error("Unable to open/set URL :: ");
            logger.error(var2);
        }

    }

    public void setDigest() {
        this.con.setRequestProperty("Digest", this.digestPayLoad.getDigest());
        System.out.println("From Cybersource: " + this.digestPayLoad.getDigest());
        this.digestMessageBody = this.digestPayLoad.getMessageBody();
    }

    public void setContentType() {
        try {
            logger.info("{} : {}", "Content-Type", "application/json");
            this.con.setRequestProperty("Content-Type", "application/json");
        } catch (Exception var2) {
            logger.fatal("Unable to set content type");
            logger.error(var2);
        }

    }

    public void maskingPayload(String serverResponse) {
        logger.info("Response Message : {}", serverResponse);
    }

    public void endConnection() {
        this.con.disconnect();
        this.con = null;
    }

    public void setUserAgent() {
        try {
            logger.info("{} : {}", "User-Agent", "Mozilla/5.0");
            this.con.setRequestProperty("User-Agent", "Mozilla/5.0");
        } catch (Exception var2) {
            logger.fatal("Unable to set USER_AGENT");
            logger.error(var2);
        }

    }

    public void setSignature() {
        Authorization authorization = new Authorization();

        try {
            String signatureHeaderValue = authorization.getToken(this.merchantConfig);
            this.con.setRequestProperty("Signature", signatureHeaderValue);
        } catch (Exception var3) {
            logger.fatal("Unable to set Signature ");
            logger.error(var3);
        }

    }

    public void setRequestMethod(String method) {
        try {
            logger.info("{} : {}", "Request Type", method);
            this.con.setRequestMethod(method);
        } catch (Exception var3) {
            logger.fatal("Invalid Request Type :: ");
            logger.error(var3);
        }

    }

    public Response httpConnection() throws IOException {
        this.openConnection();
        this.setHeaderData(this.merchantId);
        this.con.setRequestProperty("Date", PropertiesUtil.getNewDate());
        this.con.setRequestProperty("Host", this.requestHost);
        if (this.requestType.equalsIgnoreCase("POST") || this.requestType.equalsIgnoreCase("PUT")) {
            this.setDigest();
        }

        this.setSignature();
        this.setRequestMethod(this.requestType);
        this.setUserAgent();
        this.setContentType();
        if (this.requestType.equalsIgnoreCase("POST") || this.requestType.equalsIgnoreCase("PUT")) {
            this.con.setDoOutput(true);
            this.con.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(this.con.getOutputStream());
            wr.write(this.digestPayLoad.getPayLoad().getBytes("UTF-8"));
            wr.flush();
            wr.close();
        }

        int responseCode = this.con.getResponseCode();
        Response responseObj = new Response();
        responseObj.setResponseCode(String.valueOf(responseCode));
        responseObj.setVcCorelationId(this.con.getHeaderField("v-c-correlation-id"));
        logger.info("{} : {}", "Date", PropertiesUtil.getNewDate());
        logger.info("{} : {}", "Host", this.requestHost);
        if (this.requestType.equalsIgnoreCase("POST")) {
            logger.info("{} : {}", "Digest", this.digestMessageBody);
        }

        logger.info("{} : {}", "Response Code", responseObj.getResponseCode());
        logger.info("{} : {}", "v-c-correlation-id", responseObj.getVcCorelationId());
        StringBuilder response = new StringBuilder();
        if (responseCode != 404) {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.con.getInputStream()));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        }

        this.endConnection();
        this.maskingPayload(response.toString());
        String resp = Masking.prepareMasking(response.toString());
        responseObj.setResponseMessage(resp);

        return responseObj;
    }
}
