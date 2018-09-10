package com.sparetime.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by muye on 17/8/29.
 */
public class DESedeUtil {

    private static final String  ALGORITHM = "DESede";

    public static String encrypt(String str, String key, String iv, String model) throws Exception{

        SecretKey secretKey = new SecretKeySpec(buildStream(key, 24), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM + model);
        IvParameterSpec ips = new IvParameterSpec(buildStream(iv, 8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ips);

        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes()));
    }

    public static String decrypt(String str, String key, String iv, String model) throws Exception{

        SecretKey secretKey = new SecretKeySpec(buildStream(key, 24), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM + model);
        IvParameterSpec ips = new IvParameterSpec(buildStream(iv, 8));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ips);

        return new String(cipher.doFinal(Base64.getDecoder().decode(str)), "UTF-8");
    }

    private static byte[] buildStream(String keyStr, int length) throws UnsupportedEncodingException{
        byte[] stream = new byte[length];
        byte[] temp = keyStr.getBytes("UTF-8");
        System.arraycopy(temp, 0, stream, 0, stream.length > temp.length ? temp.length : stream.length);
        return stream;
    }
}
