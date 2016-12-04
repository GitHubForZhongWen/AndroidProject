package com.zzw.coolweather.util;

/**
 * Created by ZZW on 2016/12/3.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
