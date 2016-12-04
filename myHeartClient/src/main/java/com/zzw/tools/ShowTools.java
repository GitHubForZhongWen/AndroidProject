package com.zzw.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ZZW on 2016/11/20.
 */

public class ShowTools {

    public static void showToast(Context con, String message) {
        // TODO Auto-generated method stub
        Toast.makeText(con, message, Toast.LENGTH_LONG).show();
    }
}
