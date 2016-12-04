package com.zzw.tools;

import com.zzw.beans.UserBean;
import com.zzw.myimpl.Cmd;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zzw.tools.LookupThread.readInputStream;

public class UpLoadService {
    /*public static boolean postRequest(String name,String call, int rate, String state) throws IOException {
		System.out.println("调用UpLoadService.postRequest(call ,rate,state)方法");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		System.out.println("当前时间:"+nowTime);
		String path = Cmd.IP + "/MyWeb/UpLoadServlet";
		System.out.println("IP路径："+path);
		String params = "name="+name +"&mobile=" + call + "&heart=" + rate + "&dtime=" + nowTime + "&status=" + state;
		byte[] data = params.getBytes();// 待发送字节流

		HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
		
//		if(conn==null){
//			System.out.println("conn为空");
//		}else System.out.println("------"+conn+"-----------");
		
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");// 调用post方法
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", "" + data.length);
		OutputStream out = conn.getOutputStream();
		out.write(data);
		out.flush();
		if (conn.getResponseCode() == 200) {
			System.out.println("返回true");
			return true;
		} else {
			System.out.println("返回false");
			return false;
		}
	}*/

    public static boolean postRequest(String name,String call, int rate, String state) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        String path = Cmd.IP + "/MyWeb/UpLoadServlet";
        //String path="http://172.16.55.121:8088/zlTest/UploadServlet";
//        System.out.println("name:"+name+"mobile:"+call+"rate"+rate+"statues:"+state);
        String params = "name="+name+"&mobile=" + call + "&heart=" + rate + "&dtime=" + now + "&status=" + state;
        HttpURLConnection result = connectServer(params, path);
        if ( result.getResponseCode() == 200) { //判断接收方响应码
            System.out.println("返回true值结果");
            return true;
        } else {
            System.out.println("返回false值结果");
            return false;
        }
    }

    /**
     * 用户注册连接服务器
     * @param userBean
     * @return
     * @throws Exception
     */

    public static String registerServide(UserBean userBean) throws Exception {
        String resu= "注册结果";
        //注册的上传路径
        String path = Cmd.IP + "/MyWeb/ClientRegisterServlet";
        String params = "username="+userBean.getUserName()+"&userphone=" + userBean.getUserPhone() + "&userpswd=" + userBean.getUserPswd()  ;
        HttpURLConnection conn = connectServer(params, path);
        if (conn.getResponseCode() == 200) { //判断接收方响应码
            InputStream is = conn.getInputStream();
            byte[] rdata = readInputStream(is);
            is.close();
            resu = new String(rdata, "UTF-8");
            return resu;
        } else {
            return null;
        }
    }

    public static String loginServide(UserBean userBean) throws Exception {

        String resu= "登录结果";
        //登录信息的上传路径
        String path = Cmd.IP + "/MyWeb/LoginSuccess";
        String params = "username="+userBean.getUserName()+"&password=" + userBean.getUserPswd()  ;
        HttpURLConnection conn = connectServer(params, path);

        if (conn.getResponseCode() == 200) { //判断接收方响应码
//            System.out.println("login ser");
            InputStream is = conn.getInputStream();
            byte[] rdata = readInputStream(is);
            is.close();
            resu = new String(rdata, "UTF-8");
            System.out.println(resu + "===============");
            return resu;
        } else {
            return null;
        }
    }

    /**
     * 连接服务器
     * @param params 所要向服务器发送的数据
     * @param path  发送服务器的路径
     * @return
     * @throws Exception
     */

    public static HttpURLConnection connectServer(String params,String path) throws Exception {
        byte[] data = params.getBytes();//待发送字节流
        System.out.println(path);
        URL url=new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000); //连接超时值
        conn.setRequestMethod("POST"); //HTTP的POST方法
        conn.setDoOutput(true);//允许对外传输数据
        conn.setDoInput(true);

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//文档格式
        conn.setRequestProperty("Content-Length", data.length + ""); //文档长度
        System.out.println("conn="+conn);
        OutputStream outStream = conn.getOutputStream(); //准备输出流
        outStream.write(data); //发送


        outStream.flush(); //清除输出流
        System.out.println("qqqqqqqqqqqqqqqqqqqqqconn="+conn);

        return conn;
    }

}
