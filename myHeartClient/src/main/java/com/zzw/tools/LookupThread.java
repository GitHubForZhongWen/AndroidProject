package com.zzw.tools;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzw.myheartclient.LookupActivity;
import com.zzw.myimpl.Cmd;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.zzw.tools.UpLoadService.connectServer;

public class LookupThread implements Runnable {
    public static String result = "0";
    private Handler handler;
    private LookupActivity activity;

    public LookupThread(Handler handler, LookupActivity activity) {
        this.handler = handler;
        this.activity = activity;
    }

//    public LookupThread(Handler handler) {
//        super();
//        this.handler = handler;
//    }

    @Override
    public void run() {
        SharedPreferences loginData = activity.getSharedPreferences("LoginData", MODE_PRIVATE);
        String mobile = loginData.getString("userphone", "");
//        System.out.println(loginData+"wwwwwwwwwwwwwwwwwww");
        JSONObject jo = new JSONObject();
        jo.put("cmd", Cmd.LOOKUPHR);// 查看命令
        jo.put("mobile", mobile);// 手机号码可以更改
        // String path = Cmd.IP + "/MyWeb/UpLoadServlet";
        String param = Cmd.REQ + "=" + JSON.toJSONString(jo);
        byte[] data = param.getBytes();
        try {
//            URL url = new URL(Cmd.IP + "/MyWeb/LookUpServlet");
            String path = Cmd.IP + "/MyWeb/LookUpServlet";
            HttpURLConnection conn =connectServer(param,path);

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                byte[] rdata = readInputStream(is);
                is.close();
                result = new String(rdata, "UTF-8");
//				System.out.println("result:"+result);
                Message message = new Message();
                message.what = Cmd.MSG_SEE;//线程发的消息
                message.obj = result;// 将服务器返回的结果存放到Message中
                handler.sendMessage(message);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

}
