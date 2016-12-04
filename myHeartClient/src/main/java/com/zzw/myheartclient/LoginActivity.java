package com.zzw.myheartclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zzw.beans.UserBean;
import com.zzw.myimpl.Cmd;
import com.zzw.tools.ShowTools;
import com.zzw.tools.UpLoadService;

/**
 *
 */
public class LoginActivity extends Activity {

    private TextView registerLink;
    private Button btnLogin;
    private EditText userName;
    private EditText userPswd;
    private String name;
    private String pswd;
    private String s;


    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Cmd.LOG_SUCCESS) {
                ShowTools.showToast(LoginActivity.this,"登录成功");
                SharedPreferences.Editor loginData = getSharedPreferences("LoginData", MODE_PRIVATE).edit();
                loginData.putBoolean("status", true);
                loginData.putString("username", name);
                loginData.putString("userpswd", pswd);
                loginData.putString("userphone", s);
                loginData.commit();//提交
                finish();//关闭登录网页
            } else if (msg.what == Cmd.LOG_FAIL) {
                ShowTools.showToast(LoginActivity.this,"登录失败");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.username_edit);
        userPswd = (EditText) findViewById(R.id.password_edit);
        btnLogin = (Button) findViewById(R.id.signin_button);
        registerLink = (TextView) findViewById(R.id.register_link);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginVerify();
            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 登录验证，判断账号密码是否匹配
     */

    private void LoginVerify() {
        //获取登录页面用户名和密码
        name = userName.getText().toString().trim();
        pswd = userPswd.getText().toString().trim();

        /**
         * 获取bean对象
         */
        final UserBean userBean = new UserBean();
        userBean.setUserName(name);
        userBean.setUserPswd(pswd);
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    //s:返回登录的结果，登录成功或者失败
                    s =   UpLoadService.loginServide(userBean);
                    Message msg = new Message();
                    if (!s.isEmpty()||!s.equals("")) {
                        msg.what = Cmd.LOG_SUCCESS;

                    } else {
                        msg.what = Cmd.LOG_FAIL;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();

    }
}
