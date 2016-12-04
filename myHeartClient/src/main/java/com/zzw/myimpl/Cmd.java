package com.zzw.myimpl;

public interface Cmd {
	//相应参数
	public String IP = "http://192.168.155.1:8080";/*"http://172.18.25.43:8080";*//*"http://172.16.55.126:8080";*//*"http://localhost:8080";*/
	public String mobile = "15211091103";
	public String REQ="myReq";
	// 消息
	public int MSG_SEE = 201;// 查看服务器响应消息
	public int MSG_OPEN = 101;// 相机消息
	public int MSG_SUCCESS = 102;// 上传心率成功消息
	public int MSG_FAIL = 103;// 上传心率失败消息
	public int REG_SUCCESS=104;//注册上传成功消息
	public int REG_FAIL=105;//注册上传失败消息
	public int LOG_SUCCESS=106;//登录成功消息
	public int LOG_FAIL=107;//登录失败消息

	public int GETCOUNT=108;//获得数据命令
	public String LOOKUPHR = "lookUpHr";// 查看手机号码的心率
	public String LOOKUPALLHR = "lookupAllHr";// 查看所有心率
	public String UPLOADHR = "uploadHr";// 上传心率
	public String REGISGERINFO = "registerInfo";//查看注册返回信息
}
