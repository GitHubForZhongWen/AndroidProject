    package com.zzw.tools;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.zzw.myheartclient.LookupActivity;
import com.zzw.myimpl.Cmd;

import java.io.InputStream;
import java.net.HttpURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.zzw.tools.LookupThread.readInputStream;
import static com.zzw.tools.UpLoadService.connectServer;

/**
 * Created by ZZW on 2016/11/21.
 */

public class getNumberThread implements Runnable {
    public static String result = "0";
    private Handler handler;
    private LookupActivity activity;

    public getNumberThread(Handler handler, LookupActivity activity) {
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    public void run() {
        SharedPreferences loginData = activity.getSharedPreferences("LoginData", MODE_PRIVATE);
        String uname = loginData.getString("username", "");
        System.out.println(uname+"这是uname");
        String param = "uname=" + uname;
        //登录信息的上传路径
        String path = Cmd.IP + "/MyWeb/QueryHeartServlet";
        String params = "uname=" + uname;
        HttpURLConnection conn = null;
        try {
            conn = connectServer(params, path);

            if (conn.getResponseCode() == 200) { //判断接收方响应码
                System.out.println("login ser");
                InputStream is = conn.getInputStream();
                byte[] rdata = readInputStream(is);
                is.close();
                result = new String(rdata, "UTF-8");
                System.out.println(result+"11111111111111111111111111");
                result=result.substring(1,result.lastIndexOf("}"));
                Message message=new Message();
                message.what=Cmd.GETCOUNT;
                message.obj=result;

                handler.sendMessage(message);
            }
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }
}
