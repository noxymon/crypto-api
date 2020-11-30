package id.noxymon.miner.crawler.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacUtil {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String from(String value, String secret){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    secret.getBytes(DEFAULT_ENCODING),
                    HMAC_SHA512
            );

            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(keySpec);
            final byte[] bytes = mac.doFinal(value.getBytes(DEFAULT_ENCODING));

            StringBuilder hash = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
