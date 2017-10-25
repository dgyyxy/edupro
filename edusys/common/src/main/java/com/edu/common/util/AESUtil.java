package com.edu.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密工具类
 * Created by Gary on 2017/3/24.
 */
public class AESUtil {

    private static final String encodeRules ="gary";

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据encodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     * @param content
     * @return
     */
    public static String AESEncode(String content){
        try {
            //1.构造密钥生成器，指定为AES算法，不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据encodeRules规则初始化密钥生成器
            //生成一个128位的随机源，根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获取原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES生成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrpty_mode)或者解密(Decrpty_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内空的字节数组(这里设置为UTF-8)不然内容中如果有中文和英文混合中文就会有解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的字节数组转化为字符串
            //这里用Base64Encoder中会找不到包
            //解决方法
            //在项目的Build path中先移除JRE System Library, 再添加JRE Syste Library,重新编译后就一切正常
            String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
            return AES_encode;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * 解密过程
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数值
     * 3.将加密内容解密
     */
    public static String AESDecode(String content) {
        try {
            //1.构造密钥生成器，指定为AES算法，不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据encodeRules规则初始化密钥生成器
            //生成一个128位的随机源，根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获取原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES生成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrpty_mode)或者解密(Decrpty_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解密成字节数组
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);

            //解密
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String destring(){
        try{
            String sKey = "U94320KcSoC094IQ";
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args){
        String str = "aiks@root!";
        str = "AES/CBC/PKCS5Padding";
        String encryString = AESEncode(str);
        System.out.println(encryString);
        String decryString = AESDecode(encryString);
        System.out.println(decryString);
    }
}
