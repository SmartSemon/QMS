package com.usc.app.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;


/**
 * @description RSA加密算法工具类
 * @author caochengde
 * @date 2019年7月23日
 */
public class RSAUtils {

	public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";


    /**
     * @description 生成密钥对
     * @author caochengde
     * @param keySize
     * @return
     */
    public static Dto createKeys(int keySize) {
    	// c 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg = null;
        try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
		}
        // c 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // c 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // c 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        // c 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());

        Dto keyPairMap = new MapDto();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }


    /**
     * @description 得到公钥
     * @author caochengde
     * @param publicKey	密钥字符串（经过base64编码）
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPublicKey(String publicKey) {
        // c 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = null;
        RSAPublicKey key = null;
		try {
			keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
	        key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

        return key;
    }

    /**
     * @description 得到私钥
     * @author caochengde
     * @param privateKey	密钥字符串（经过base64编码）
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) {
        // c 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = null;
        RSAPrivateKey key = null;
		try {
			keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
	        key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

        return key;
    }

    /**
     * @description 公钥加密
     * @author caochengde
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * @description 私钥解密
     * @author caochengde
     * @param data
     * @param privateKey
     * @return
     */
    public static String privateDecrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * @description 私钥加密
     * @author caochengde
     * @param data
     * @param privateKey
     * @return
     */
    public static String privateEncrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * @description 公钥解密
     * @author caochengde
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicDecrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }



}











