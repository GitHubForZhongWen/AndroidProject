package com.zzw.myheartclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zzw.beans.UserBean;
import com.zzw.myimpl.Cmd;
import com.zzw.tools.ShowTools;
import com.zzw.tools.UpLoadService;
import com.zzw.tools.Verify;


public class RegisterActivity extends Activity {

    private Button btnRegister;
    private Button btnReturn;
    private EditText etUserName;
    private EditText etUserPswd;
    private EditText etUserRePswd;
    private EditText etUserMobile;

    protected Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == Cmd.REG_SUCCESS) {
//                System.out.println("准备调用prompt（）方法....");
                ShowTools.showToast(RegisterActivity.this,"注册成功");
            } else if (msg.what == Cmd.REG_FAIL) {
                ShowTools.showToast(RegisterActivity.this,"注册失败");
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUserName = (EditText) findViewById(R.id.et_uername);
        etUserPswd = (EditText) findViewById(R.id.et_userpswd);
        etUserRePswd = (EditText) findViewById(R.id.et_userrepswd);
        etUserMobile = (EditText) findViewById(R.id.et_usermobile);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnReturn = (Button) findViewById(R.id.btn_return);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始注册
                registerUser();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回到登录界面
                finish();
            }
        });
    }

    public void registerUser() {
        //获取注册信息

        String name = etUserName.getText().toString().trim();
        String pswd = etUserPswd.getText().toString().trim();
        String repswd = etUserRePswd.getText().toString().trim();
        String mobile = etUserMobile.getText().toString().trim();
        UserBean userBean = new UserBean(name, pswd, mobile);
        //判断输入的信息是否为空，如果为空返回true值，反之返回false
        boolean isEmpty = Verify.infoIsEmpty(name, pswd, repswd, mobile);
        if (isEmpty) {
            Toast.makeText(this, "输入的信息不能有空值", Toast.LENGTH_SHORT).show();
        }else{
            //判断两次输入的信息是否一致，若一致返回true，反之返回false
            if (!(pswd.equals(repswd))) {
                Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                //开始注册用户
                registerSubmit(userBean);
            }

        }
    }

    private void registerSubmit(final UserBean userBean) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String bl = UpLoadService.registerServide(userBean);
                    Message msg = new Message();
                    if (bl.equals("注册成功")) {
                        msg.what = Cmd.REG_SUCCESS;
                    } else {
                        msg.what= Cmd.REG_FAIL;
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
