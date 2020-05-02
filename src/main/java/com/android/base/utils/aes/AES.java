//package com.android.base.utils.aes;
//
//import android.os.Build;
//import androidx.annotation.RequiresApi;
//import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
//
//import com.orhanobut.logger.Logger;
//
//import javax.crypto.*;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//import java.util.Base64;
//
//public class AES {
//    private final String KEY_GENERATION_ALG = "PBEWITHSHAANDTWOFISH-CBC";
//    // private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
//    private final int HASH_ITERATIONS = 10000;
//    private final int KEY_LENGTH = 128;
//    private char[] humanPassphrase = { 'P', 'e', 'r', ' ', 'v', 'a', 'l', 'l',
//            'u', 'm', ' ', 'd', 'u', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a',
//            'n', 't' };// per vallum duces labant
//    private byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD,
//            0xE, 0xF }; // must save this for next time we want the key
//
//    private PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt,
//            HASH_ITERATIONS, KEY_LENGTH);
//
//    private final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";// AES/CBC/PKCS7Padding
//
//    private SecretKeyFactory keyfactory = null;
//    private SecretKey sk = null;
//    private SecretKeySpec skforAES = null;
//    private static String ivParameter = "36610d6de6960b65";// 密钥默认偏移，可更改
//    // private byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8,
//    // 0xC,
//    // 0xD, 91 };
//    private byte[] iv = ivParameter.getBytes();
//    private IvParameterSpec IV;
//    String sKey = "36610d6de6960b65";// key必须为16位，可更改为自己的key
//
//    public AES() {
//        try {
//            keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
//            sk = keyfactory.generateSecret(myKeyspec);
//        } catch (NoSuchAlgorithmException nsae) {
//            Logger.e("AESdemo",
//                    "no key factory support for PBEWITHSHAANDTWOFISH-CBC");
//        } catch (InvalidKeySpecException ikse) {
//            Logger.e("AESdemo", "invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
//        }
//
//        // This is our secret key. We could just save this to a file instead of
//        // regenerating it
//        // each time it is needed. But that file cannot be on the device (too
//        // insecure). It could
//        // be secure if we kept it on a server accessible through https.
//
//        // byte[] skAsByteArray = sk.getEncoded();
//        byte[] skAsByteArray;
//        try {
//            skAsByteArray = sKey.getBytes("ASCII");
//            skforAES = new SecretKeySpec(skAsByteArray, "AES");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        IV = new IvParameterSpec(iv);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public String encrypt(byte[] plaintext) {
//        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
//        String base64_ciphertext = Base64.getEncoder().encodeToString(ciphertext);
//        return base64_ciphertext;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public String encrypt1(byte[] plaintext) {
//        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
////        byte[] base64_ciphertext = Base64.getEncoder().encode(ciphertext);
//        return ByteUtils.toHexString(ciphertext);
//    }
//
//
//    public String decrypt(String ciphertext_base64) {
//        byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
//        String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV,
//                s));
//        return decrypted;
//    }
//
//    // Use this method if you want to add the padding manually
//    // AES deals with messages in blocks of 16 bytes.
//    // This method looks at the length of the message, and adds bytes at the end
//    // so that the entire message is a multiple of 16 bytes.
//    // the padding is a series of bytes, each set to the total bytes added (a
//    // number in range 1..16).
//    private byte[] addPadding(byte[] plain) {
//        byte plainpad[] = null;
//        int shortage = 16 - (plain.length % 16);
//        // if already an exact multiple of 16, need to add another block of 16
//        // bytes
//        if (shortage == 0)
//            shortage = 16;
//        // reallocate array bigger to be exact multiple, adding shortage bits.
//        plainpad = new byte[plain.length + shortage];
//        for (int i = 0; i < plain.length; i++) {
//            plainpad[i] = plain[i];
//        }
//        for (int i = plain.length; i < plain.length + shortage; i++) {
//            plainpad[i] = (byte) shortage;
//        }
//        return plainpad;
//    }
//
//    // Use this method if you want to remove the padding manually
//    // This method removes the padding bytes
//    private byte[] dropPadding(byte[] plainpad) {
//        byte plain[] = null;
//        int drop = plainpad[plainpad.length - 1]; // last byte gives number of
//        // bytes to drop
//
//        // reallocate array smaller, dropping the pad bytes.
//        plain = new byte[plainpad.length - drop];
//        for (int i = 0; i < plain.length; i++) {
//            plain[i] = plainpad[i];
//            plainpad[i] = 0; // don't keep a copy of the decrypt
//        }
//        return plain;
//    }
//
//    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
//                           byte[] msg) {
//        try {
//            Cipher c = Cipher.getInstance(cmp);
//            c.init(Cipher.ENCRYPT_MODE, sk, IV);
//            return c.doFinal(msg);
//        } catch (NoSuchAlgorithmException nsae) {
//            Logger.e("AESdemo", "no cipher getinstance support for " + cmp);
//        } catch (NoSuchPaddingException nspe) {
//            Logger.e("AESdemo", "no cipher getinstance support for padding " + cmp);
//        } catch (InvalidKeyException e) {
//            Logger.e("AESdemo", "invalid key exception");
//        } catch (InvalidAlgorithmParameterException e) {
//            Logger.e("AESdemo", "invalid algorithm parameter exception");
//        } catch (IllegalBlockSizeException e) {
//            Logger.e("AESdemo", "illegal block size exception");
//        } catch (BadPaddingException e) {
//            Logger.e("AESdemo", "bad padding exception");
//        }
//        return null;
//    }
//
//    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
//                           byte[] ciphertext) {
//        try {
//            Cipher c = Cipher.getInstance(cmp);
//            c.init(Cipher.DECRYPT_MODE, sk, IV);
//            return c.doFinal(ciphertext);
//        } catch (NoSuchAlgorithmException nsae) {
//            Logger.e("AESdemo", "no cipher getinstance support for " + cmp);
//        } catch (NoSuchPaddingException nspe) {
//            Logger.e("AESdemo", "no cipher getinstance support for padding " + cmp);
//        } catch (InvalidKeyException e) {
//            Logger.e("AESdemo", "invalid key exception");
//        } catch (InvalidAlgorithmParameterException e) {
//            Logger.e("AESdemo", "invalid algorithm parameter exception");
//        } catch (IllegalBlockSizeException e) {
//            Logger.e("AESdemo", "illegal block size exception");
//        } catch (BadPaddingException e) {
//            Logger.e("AESdemo", "bad padding exception");
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//
//
//
//
//
//}