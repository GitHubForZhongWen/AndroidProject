package com.zzw.tools;

import android.text.TextUtils;

/**
 * Created by ZZW on 2016/11/19.
 */

public class Verify {
    /**
     * 密码是否一致
     * @param psw1
     * @param psd2
     * @return
     */
    public static boolean psdIsIdentical(String psw1,String psd2){
        boolean b= false;
        if (psw1.equals(psd2)) {
            b=true;
        }
        return b;
    }

    /**
     * 判断文本输入框中的数据是否为空
     * @param name 用户名
     * @param psd1 密码
     * @param psd2 确认密码
     * @param mobile 手机号码
     */

    public static boolean infoIsEmpty(String name,String psd1,String psd2,String mobile) {
        boolean b= false;
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(psd1) || TextUtils.isEmpty(psd2) || TextUtils.isEmpty(mobile)) {
            b=true;
        }
        return b;
    }
}
