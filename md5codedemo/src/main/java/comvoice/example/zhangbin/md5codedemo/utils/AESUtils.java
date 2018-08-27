package comvoice.example.zhangbin.md5codedemo.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private final static String HEX="0123456789abcdef";
    private static final String CBC_PKCS5_PADDING="AES/CBC/PKCS5Padding";//aes是加密方式，cbc是工作模式，pk是填充模式
    private static final String AES="AES";
    private static final String SHA1PRNG="SHA1PRNG";

    //生成对技术，可以当作动态的密钥，
    public static String generateKey(){
        try {
            SecureRandom localSecureRandom=SecureRandom.getInstance(SHA1PRNG);
            byte[]bytes=new byte[20];
            localSecureRandom.nextBytes(bytes);
            String str_key=toHex(bytes);
            return str_key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //二进制转字符
    public static String toHex(byte[]buf){
        if(buf==null)
            return "";
            StringBuffer result=new StringBuffer(2*buf.length);
            for(int i=0;i<buf.length;i++){
                appendHex(result,buf[i]);
            }

        return result.toString();
    }
    private static void appendHex(StringBuffer sb,byte b){
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    //对密钥进行处理
    private static byte[]getRawKey(byte[]seed){
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance(AES);
            SecureRandom sr=null;
            //在4.2以上的版本中，SecureRandom获取方式发生了改变
            if(android.os.Build.VERSION.SDK_INT >= 17){
                sr=SecureRandom.getInstance(SHA1PRNG,"Crypto");
            }else {
                sr=SecureRandom.getInstance(SHA1PRNG);
            }
            sr.setSeed(seed);
            keyGenerator.init(128,sr);//256字节或者128字节或192字节
            //AES中128位密钥版本有10个加密循环，192版本有12个加密循环，256版本有14个加密循环
            SecretKey secretKey=keyGenerator.generateKey();
            byte[]raw=secretKey.getEncoded();
            return raw;
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e("异常2",e.getMessage().toString());
            return null;
        }
    }

    //加密

    public static String encrypt(String key, String cleartext){
        if(TextUtils.isEmpty(cleartext)){
            return cleartext;
        }
        try {
            byte[]result=encrypt(key,cleartext.getBytes());
//            Log.e("异常",Base64.getEncoder().encodeToString(result));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(result);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e("异常1",e.getMessage().toString());
        }
        return null;
    }
    /*
     * 加密
     */
    private static byte[] encrypt(String key, byte[] clear){
        byte[] raw = getRawKey(key.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
             encrypted= cipher.doFinal(clear);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypted;
    }
    //解密过程
    //解密
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String key, String encrypted){
        if(TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try{
            byte[]enc= Base64.getDecoder().decode(encrypted);
            byte[]result=decrypt(key,enc);
            return new String(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //解密
    private static byte[]decrypt(String key,byte[]encrypted){
        byte[]raw=getRawKey(key.getBytes());
        SecretKeySpec secretKeySpec=new SecretKeySpec(raw,AES);
        try {
            Cipher cipher=Cipher.getInstance(CBC_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[]decrypted=cipher.doFinal(encrypted);
            return decrypted;
        }catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

}
