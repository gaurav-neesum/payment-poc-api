package com.cybersource;





import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CyberSourceSecurity {
    /**
     * "access_key, profile_id, transaction_uuid, signed_field_names, unsigned_field_names,signed_date_time,
     * locale,transaction_type,reference_number,amount,currency"
     */

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SECRET_KEY = "f9de0e88eeb44067bcb54b3a9e8279b7bac95891731b45489ccd3ea5937e8a581d2e74bb30bb4108b4a469439d5d6fa50568fcd29ed240f88dd021a0e3d19712991a0a5bfa17493b915ea6c197e1e4f12b3b49c497964e80a27975295fcae8fcc88959eb34c043a49fc3c82fddf282f8a0994c006f754801992a973104a1b16f";
    private static final String ACCESS_KEY = "89215fd9f36135628f4e776a07429bab";
    private static final String PROFILE_ID = "461BB716-40C4-4EE9-9F8B-131C0B776A01";
    private static final String SIGNED_FIELD_NAMES = "access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency";
    private static final String LOCALE = "en";
    private static final String TRANSACTION_TYPE = "authorization";
    private static final String CURRENCY = "GBP";


    public static String sign(String data) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printBase64Binary(rawHmac).replace("\n", "");
    }

    /**
     * <input type="hidden" name="access_key" value="<REPLACE WITH ACCESS KEY>">
     * <input type="hidden" name="profile_id" value="<REPLACE WITH PROFILE ID>">
     * <input type="hidden" name="transaction_uuid" value="<%= UUID.randomUUID() %>">
     * <input type="hidden" name="signed_field_names"
     * value="access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency">
     * <input type="hidden" name="unsigned_field_names">
     * <input type="hidden" name="signed_date_time" value="<%= getUTCDateTime() %>">
     * <input type="hidden" name="locale" value="en">
     * <fieldset>
     * <legend>Payment Details</legend>
     *     <div id="paymentDetailsSection" class="section">
     *         <span>transaction_type:</span><input type="text" name="transaction_type" size="25"><br/>
     *         <span>reference_number:</span><input type="text" name="reference_number" size="25"><br/>
     *         <span>amount:</span><input type="text" name="amount" size="25"><br/>
     *         <span>currency:</span><input type="text" name="currency" size="25"><br/>
     *     </div>
     * </fieldset>
     *
     * @param params
     * @return
     */

    public static String buildDataToSign(Map<String, Object> params) {

        String signedFieldNamesStr = "access_key,profile_id,transaction_uuid,signed_field_names,unsigned_field_names,signed_date_time,locale,transaction_type,reference_number,amount,currency";

//        String[] signedFieldNames = String.valueOf(params.get("signed_field_names")).split(",");
        String[] signedFieldNames = signedFieldNamesStr.split(",");

        ArrayList<String> dataToSign = new ArrayList<String>();
        for (String signedFieldName : signedFieldNames) {
            dataToSign.add(signedFieldName + "=" + String.valueOf(params.get(signedFieldName)));
        }
        return commaSeparate(dataToSign);
    }


    public static String commaSeparate(ArrayList<String> dataToSign) {
        StringBuilder csv = new StringBuilder();
        for (Iterator<String> it = dataToSign.iterator(); it.hasNext(); ) {
            csv.append(it.next());
            if (it.hasNext()) {
                csv.append(",");
            }
        }
        return csv.toString();
    }

    private static String getUTCDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static List<KeyValueResponse> getAllKeyValueForPayment(double amount) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> params = populateParams(amount);

        List<KeyValueResponse> keyValueResponses = new ArrayList<>();
        String signature = sign(buildDataToSign(params));
        keyValueResponses.add(new KeyValueResponse().setKey("signature").setValue(signature));
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            KeyValueResponse keyValueResponse = new KeyValueResponse()
                    .setKey(entry.getKey())
                    .setValue(entry.getValue());
            keyValueResponses.add(keyValueResponse);
        }
        return keyValueResponses;

    }

    private static Map<String, Object> populateParams(double amount) {

        String TRANSACTION_UUID = UUID.randomUUID().toString();
        String SIGNED_DATE_TIME = getUTCDateTime();
        String REFERENCE = String.valueOf(new Date().getTime());
        String signedFieldNamesStr = "access_key, profile_id, transaction_uuid, signed_field_names, unsigned_field_names, " +
                "signed_date_time, locale, transaction_type, reference_number, amount, currency";

        Map<String, Object> params = new HashMap<>();
        params.put("access_key", ACCESS_KEY);
        params.put("profile_id", PROFILE_ID);
        params.put("transaction_uuid", TRANSACTION_UUID);
        params.put("signed_field_names", signedFieldNamesStr);
        params.put("unsigned_field_names", "");
        params.put("signed_date_time", SIGNED_DATE_TIME);
        params.put("locale", LOCALE);
        params.put("transaction_type", TRANSACTION_TYPE);
        params.put("reference_number", REFERENCE);
        params.put("amount", amount);
        params.put("currency", CURRENCY);

        return params;
    }
}

