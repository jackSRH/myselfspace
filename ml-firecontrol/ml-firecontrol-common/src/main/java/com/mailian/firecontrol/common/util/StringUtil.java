package com.mailian.firecontrol.common.util;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class StringUtil {
    public static String getMd5InputStream(InputStream in) {
        MessageDigest digest = null;
        byte[] buffer = new byte[1024];

        try {
            digest = MessageDigest.getInstance("MD5");
            while (true) {
                int len;
                if ((len = in.read(buffer, 0, 1024)) == -1) {
                    in.close();
                    break;
                }
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger var5 = new BigInteger(1, digest.digest());
        return String.format("%1$032x", new Object[]{var5});
    }
}
