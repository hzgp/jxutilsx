package com.jxkj.utils;

/**
 * Desc:
 * Author:zhujb
 * Date:2020/6/17
 */
public interface IConfigLoader {
    String AES_KEY();
    String Cipher_Algorithm();

    String MeiZu_App_Id();

    String MeiZu_App_Key();

    String MiPush_App_Id();

    String MiPush_App_Key();

    String OppoPush_App_Key();

    String OppoPush_App_Secret();

    String ALi_App_Key();

    String ALi_App_Secret();
}
