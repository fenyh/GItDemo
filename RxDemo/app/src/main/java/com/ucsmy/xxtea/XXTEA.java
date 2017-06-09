/**********************************************************\
 |                                                          |
 | XXTEA.java                                               |
 |                                                          |
 | XXTEA encryption algorithm library for Java.             |
 |                                                          |
 | Encryption Algorithm Authors:                            |
 |      David J. Wheeler                                    |
 |      Roger M. Needham                                    |
 |                                                          |
 | Code Authors: Ma Bingyao <mabingyao@gmail.com>           |
 | LastModified: Mar 10, 2015                               |
 |                                                          |
 \**********************************************************/

package com.ucsmy.xxtea;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

public final class XXTEA {

    private static final int DELTA = 0x9E3779B9;

    private static int MX(int sum, int y, int z, int p, int e, int[] k) {
        return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
    }

    private XXTEA() {
    }

    public static final byte[] encrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(
                encrypt(toIntArray(data, true), toIntArray(fixKey(key), false)), false);
    }

    public static final byte[] encrypt(String data, byte[] key) {
        try {
            return encrypt(data.getBytes("UTF-8"), key);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static final byte[] encrypt(byte[] data, String key) {
        try {
            return encrypt(data, key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static final byte[] encrypt(String data, String key) {
        try {
            return encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     *
     * @param context
     * @param rawFilePath
     * @param encryptFileFilePath 加密的文件存放完整地址路径
     * @param key
     * @return
     */
    public static final String encryptFile(Context context, File rawFilePath,String encryptFileFilePath, String key) {
        try {
            if (!rawFilePath.exists()) {
                //文件存在
                new Throwable("File is empty");
            }
            if (!rawFilePath.canRead()) {
                new Throwable("File can not Read");
            }
            FileInputStream fin = new FileInputStream(rawFilePath);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            //将文件流做加密处理
            byte[] fileEncrypt = encrypt(buffer, key);
            //缓存文件流
            String filepath  ;
            if(!TextUtils.isEmpty(encryptFileFilePath)){
                filepath = encryptFileFilePath + File.separator + rawFilePath.getName();
            }else{
                filepath =  context.getFilesDir().getAbsolutePath() +File.separator + rawFilePath.getName();
            }
            save2datafile(filepath, fileEncrypt);
            fin .close();
            return filepath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        /**
         * @param context
         * @param rawFilePath 需要加密的文件地址
         * @param key
         * @return 密文件的路径，位于data/data/packageName/files 中
         */
    public static final String encryptFile(Context context, File rawFilePath, String key) {
        return encryptFile(context,rawFilePath,null,key);
    }

    private static void save2datafile(String filepath, byte[] encryptFile) throws Exception{
            File file = new File(filepath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(encryptFile);
            fout.flush();
            fout.close();
    }

    /**
     * 默认存储路径位置，只需传文件名称即可
     * @param context
     * @param fileName 位于data/data/packageName/files中的密文名称
     * @param key
     * @return
     */
    public static final byte[] decryptFile(String fileName, String key) {
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            fin.close();
            return decrypt(buffer, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static final String encryptToBase64String(byte[] data, byte[] key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encode(bytes);
    }

    public static final String encryptToBase64String(String data, byte[] key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encode(bytes);
    }

    public static final String encryptToBase64String(byte[] data, String key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encode(bytes);
    }

    public static final String encryptToBase64String(String data, String key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encode(bytes);
    }

    public static final byte[] decrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(
                decrypt(toIntArray(data, false), toIntArray(fixKey(key), false)), true);
    }

    public static final byte[] decrypt(byte[] data, String key) {
        try {
            return decrypt(data, key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static final byte[] decryptBase64String(String data, byte[] key) {
        return decrypt(Base64.decode(data), key);
    }

    public static final byte[] decryptBase64String(String data, String key) {
        return decrypt(Base64.decode(data), key);
    }

    public static final String decryptToString(byte[] data, byte[] key) {
        try {
            byte[] bytes = decrypt(data, key);
            if (bytes == null) return null;
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static final String decryptToString(byte[] data, String key) {
        try {
            byte[] bytes = decrypt(data, key);
            if (bytes == null) return null;
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static final String decryptBase64StringToString(String data, byte[] key) {
        try {
            byte[] bytes = decrypt(Base64.decode(data), key);
            if (bytes == null) return null;
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static final String decryptBase64StringToString(String data, String key) {
        try {
            byte[] bytes = decrypt(Base64.decode(data), key);
            if (bytes == null) return null;
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    private static int[] encrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        int p, q = 6 + 52 / (n + 1);
        int z = v[n], y, sum = 0, e;

        while (q-- > 0) {
            sum = sum + DELTA;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v[p + 1];
                z = v[p] += MX(sum, y, z, p, e, k);
            }
            y = v[0];
            z = v[n] += MX(sum, y, z, p, e, k);
        }
        return v;
    }

    private static int[] decrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        int p, q = 6 + 52 / (n + 1);
        int z, y = v[0], sum = q * DELTA, e;

        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= MX(sum, y, z, p, e, k);
            }
            z = v[n];
            y = v[0] -= MX(sum, y, z, p, e, k);
            sum = sum - DELTA;
        }
        return v;
    }

    private static byte[] fixKey(byte[] key) {
        if (key.length == 16) return key;
        byte[] fixedkey = new byte[16];
        if (key.length < 16) {
            System.arraycopy(key, 0, fixedkey, 0, key.length);
        } else {
            System.arraycopy(key, 0, fixedkey, 0, 16);
        }
        return fixedkey;
    }

    private static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (((data.length & 3) == 0)
                ? (data.length >>> 2)
                : ((data.length >>> 2) + 1));
        int[] result;

        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for (int i = 0; i < n; ++i) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    private static byte[] toByteArray(int[] data, boolean includeLength) {
        int n = data.length << 2;

        if (includeLength) {
            int m = data[data.length - 1];
            n -= 4;
            if ((m < n - 3) || (m > n)) {
                return null;
            }
            n = m;
        }
        byte[] result = new byte[n];

        for (int i = 0; i < n; ++i) {
            result[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3));
        }
        return result;
    }
}
