//package com.android.base.utils.aes;
//import android.os.Build;
//import androidx.annotation.RequiresApi;
//import com.orhanobut.logger.Logger;
//import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//
//public class AESUtils {
//
//    //IV长度应该为16，请跟服务端保持一致
//    private static final String iv = "36610d6de6960b65";
//
//    //AES/CBC/PKCS5Padding默认对应PHP则为：AES-128-CBC
//    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
//
//    private static final String AES = "AES";//AES 加密
//    private static final String key = "36610d6de6960b65";// key必须为16位，可更改为自己的key
//
//    /**
//     *
//     * @param key 这个key长度应该为16位，另外不要用KeyGenerator进行强化，否则无法跨平台
//     * @param cleartext
//     * @return
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String encrypt(String cleartext){
//        try {
//
//            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
//            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), AES);
//            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
//            byte[] encrypted = cipher.doFinal(cleartext.getBytes());
//
//            //base64编码一下  这里是ok的 因为服务端无意加了2次base64
////            return Base64Encoder.encode(encrypted);
//           String result= Base64.getEncoder().encodeToString(encrypted);
//            return Base64.getEncoder().encodeToString(result.getBytes());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String decrypt( String encrypted){
//        try
//        {
//
//            byte[] encrypted1 = Base64Decoder.decodeToBytes(encrypted);
//            String str16=ByteUtils.toHexString(encrypted1);
//            String str16result= hexStringToString(str16);
//            byte[] test=Base64Decoder.decodeToBytes(str16result);
//
//
//            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
//            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), AES);
//            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//
//
//
//            byte[] original = cipher.doFinal(test);
//
//            //转换为字符串
//            return new String(original);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//
//
//
//    /**
//     * 16进制转换成为string类型字符串
//     * @param s
//     * @return
//     */
//    public static String hexStringToString(String s) {
//        if (s == null || s.equals("")) {
//            return null;
//        }
//        s = s.replace(" ", "");
//        byte[] baKeyword = new byte[s.length() / 2];
//        for (int i = 0; i < baKeyword.length; i++) {
//            try {
//                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            s = new String(baKeyword, "UTF-8");
//            new String();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return s;
//    }
//
//
//}
