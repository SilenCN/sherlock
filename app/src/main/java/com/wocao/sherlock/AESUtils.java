package com.wocao.sherlock;

import android.annotation.SuppressLint;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 新宇 on 2015/12/3.
 */
public class AESUtils {
    public static String encrypt(String seed, String cleartext) {

        byte[] rawKey = getRawKey(seed.getBytes());

        byte[] result = encrypt(rawKey, cleartext.getBytes());

        return toHex(result);

    }

    public static String decrypt(String seed, String encrypted) {

        byte[] rawKey = getRawKey(seed.getBytes());

        byte[] enc = toByte(encrypted);

        byte[] result = new byte[0];
        try {
            result = decrypt(rawKey, enc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(result);

    }

    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) {

        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        sr.setSeed(seed);

        kgen.init(128, sr); // 192 and 256 bits may not be available

        SecretKey skey = kgen.generateKey();

        byte[] raw = skey.getEncoded();

        return raw;

    }

    private static byte[] encrypt(byte[] raw, byte[] clear) {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] encrypted = new byte[0];
        try {
            encrypted = cipher.doFinal(clear);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encrypted;

    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] decrypted = new byte[0];
        try {
            decrypted = cipher.doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return decrypted;

    }

    private static byte[] toByte(String hexString) {

        int len = hexString.length() / 2;

        byte[] result = new byte[len];

        for (int i = 0; i < len; i++)

            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();

        return result;

    }

    private static String toHex(byte[] buf) {

        if (buf == null)

            return "";

        StringBuffer result = new StringBuffer(2 * buf.length);

        for (int i = 0; i < buf.length; i++) {

            appendHex(result, buf[i]);

        }

        return result.toString();

    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {

        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));

    }

}
