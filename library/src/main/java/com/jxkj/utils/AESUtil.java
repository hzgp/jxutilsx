
/******************************************************************************
 * Copyright (c) 2006-2013 ZheJiang Electronic Port, Inc.
 * All rights reserved.
 * <p>
 * 项目名称：EPLINK
 * 版权说明：本软件属浙江电子口岸有限公司所有，在未获得浙江电子口岸有限公司正式授权
 * 情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 * 识产权保护的内容。
 *****************************************************************************/
package com.jxkj.utils;


import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 *
 * @author <a href="mailto:zengdan@zjport.gov.cn">zengdan</a>
 * @version $Id$
 * @since 1.0
 */
public class AESUtil {
    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法 / 工作模式 / 填充方式 Java 6支持PKCS5Padding填充方式 Bouncy
     * Castle支持PKCS7Padding填充方式
     */

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key 密钥
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {

        // 实例化AES密钥材料
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);

        return secretKey;
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static String decryptStr(String data) throws Exception {

        // 解密
        byte[] content = Base64.decode(data.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);

        // 解密
        byte[] key = Base64.decode(JXConfig.getInstance().getConfigLoader().AES_KEY().getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
        // 还原密钥
        Key k = toKey(key);

		/*
         * 实例化 使用PKCS7Padding填充方式，按如下方式实现 Cipher.getInstance(CIPHER_ALGORITHM,
		 * "BC");
		 */
        Cipher cipher = Cipher.getInstance(JXConfig.getInstance().getConfigLoader().Cipher_Algorithm());

        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);

        // 执行操作
        return new String(cipher.doFinal(content), StandardCharsets.UTF_8);
    }


    /**
     * 解密
     *
     * @param data 待解密数据
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static String decrypt(byte[] data) throws Exception {

        // 解密
        byte[] content = Base64.decode(new String(data).getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);

        // 解密
        byte[] key = Base64.decode(JXConfig.getInstance().getConfigLoader().AES_KEY().getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
        // 还原密钥
        Key k = toKey(key);

		/*
         * 实例化 使用PKCS7Padding填充方式，按如下方式实现 Cipher.getInstance(CIPHER_ALGORITHM,
		 * "BC");
		 */
        Cipher cipher = Cipher.getInstance(JXConfig.getInstance().getConfigLoader().Cipher_Algorithm());

        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);

        // 执行操作
        return new String(cipher.doFinal(content), StandardCharsets.UTF_8);
    }

    /**
     * 加密
     *
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static String encrypt(String str) throws Exception {

        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        byte[] key = Base64.decode(JXConfig.getInstance().getConfigLoader().AES_KEY().getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);

        // 还原密钥
        Key k = toKey(key);

		/*
         * 实例化 使用PKCS7Padding填充方式，按如下方式实现 Cipher.getInstance(CIPHER_ALGORITHM,
		 * "BC");
		 */
        Cipher cipher = Cipher.getInstance(JXConfig.getInstance().getConfigLoader().Cipher_Algorithm());

        // 初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);

        // 执行操作
        return new String(Base64.encode(cipher.doFinal(data),Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    /**
     * 生成密钥 <br>
     *
     * @return byte[] 二进制密钥
     * @throws Exception
     */
    public static byte[] initKey() throws Exception {

        // 实例化
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);

        // SecureRandom sr = null;
        // if (android.os.Build.VERSION.SDK_INT >= 17) {
        // sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        // } else {
        // sr = SecureRandom.getInstance("SHA1PRNG");
        // }
        // sr.setSeed(seed);

		/*
         * AES 要求密钥长度为 128位、192位或 256位
		 */
        kg.init(128);

        // 生成秘密密钥
        SecretKey secretKey = kg.generateKey();

        // 获得密钥的二进制编码形式
        return secretKey.getEncoded();
    }

}
