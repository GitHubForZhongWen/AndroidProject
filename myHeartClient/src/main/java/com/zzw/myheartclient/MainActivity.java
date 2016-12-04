package com.zzw.myheartclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzw.myimpl.Cmd;
import com.zzw.tools.ShowTools;
import com.zzw.tools.UpLoadService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.zzw.myheartclient.HeartDraw.calbeatlist;

public class MainActivity extends Activity implements SurfaceHolder.Callback, PreviewCallback {

    private static final int UPDATE_BUTTON = 2;
    //    private static final int BUTTON_SHOW = 3;
    private TextView showHeart;
    private Button btnStart;// 开始检测
    private Button btnStop;// 停止检测
    private Button btnCheck;// 查看心率历史
    private static Camera camera;
    private Timer timer = new Timer();// 新建定时器
    private TimerTask task;
    private HeartDraw heartDraw;
    private int[] hrs = new int[200];
    private static int hearts = 0;// 心跳强度
    private Button log_off;//注销登录


    public static int getHearts() {
        return hearts;
    }

    public static void setHearts(int hearts) {
        MainActivity.hearts = hearts;
    }

    protected Handler handler = new Handler() {

        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 1:
                    // 回调相机
                    camera.setOneShotPreviewCallback(MainActivity.this);
                    heartDraw.invalidate();
                    break;
                case Cmd.MSG_SUCCESS:
                    prompt(R.string.success);
                    break;
                case Cmd.MSG_FAIL:
                    prompt(R.string.fail);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("oncreate");
        showHeart = (TextView) findViewById(R.id.tv_showHeart);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnCheck = (Button) findViewById(R.id.btn_check);
        log_off = (Button) findViewById(R.id.btn_Log_off);

        final SharedPreferences loginData = getSharedPreferences("LoginData", MODE_PRIVATE);
        final boolean status = loginData.getBoolean("status", false);
        if (status) {
            log_off.setVisibility(View.VISIBLE);
        }

        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                camera.startPreview();// 相机数据预览
                Parameters param = camera.getParameters();// 获取相机参数
                param.setFlashMode(Parameters.FLASH_MODE_TORCH);// 相机参数中开启闪光灯
                camera.setParameters(param);// 设置相机参数
                // 使用定时器，定时发送相机消息
                startTimer();
            }
        });

        btnStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取摄像机当前参数
                Parameters param = camera.getParameters();
                // 关闭闪光灯
                param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                // 设置摄像机参数
                camera.setParameters(param);
                // 关闭定时器
                pauseTimer();

                int rate =calheartbeat();// //getHeartrate();//// getHeartRate();// 获得心率
//                System.out.println(rate+"rate++++++++++++++");
                if (status) {
                    upLoadHr(rate);// 上传心率
                }
            }
        });


        heartDraw = new HeartDraw(this);
        heartDraw.invalidate();
        LinearLayout layoutShowView = (LinearLayout) findViewById(R.id.LL_draw);
        layoutShowView.addView(heartDraw);
        btnCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 判断用户是否已经登录，如果已经登录就直接跳到查询界面
                SharedPreferences loginData = getSharedPreferences("LoginData", MODE_PRIVATE);
                boolean loginStatues = loginData.getBoolean("status", false);
                System.out.println(loginStatues + "=====");
                if (loginStatues) {
                    Intent intent = new Intent(MainActivity.this, LookupActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("您还没有登录");
                    dialog.setMessage("是否登录？");
//                    dialog.setCancelable(false);//设置返回键不能操作
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // TODO  返回上个界面，不用做任何事情
                        }
                    });
                    dialog.show();
                }
            }
        });

        log_off.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status) {
                    loginData.edit().clear().commit();//清除用户信息
                    ShowTools.showToast(MainActivity.this, "注销成功");
                    log_off.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 获取心率
     * @param
     */
    //测心率算法
   /* private int getHeartrate(){
        int heartrate=0;
        ArrayList<Integer> peak = new ArrayList<Integer>();
        ArrayList<Integer> valley = new ArrayList<Integer>();
        boolean up = true;
        boolean down = false;
        boolean trend = up;
        int max = hrs[0];
        int min = 0;
        for( int i = 0; i < hrs.length;i++ ){

            if(trend){
                if(hrs[i]>=max){
                    max = hrs[i];
                }else{
                    trend = down;
                    min = hrs[i];
                    peak.add(i-1);
                    continue;
                }
            }else{
                if(hrs[i]<=min){
                    min = hrs[i];
                }else{
                    trend = up;
                    max = hrs[i];
                    valley.add(i-1);
                    continue;
                }
            }

        }
        int avg=0,sum = 0;
        for(int i=0;i<peak.size()-1;i++){
//            Log.i("峰值",""+peak.get(i)+hrs[peak.get(i)]);
            sum +=(peak.get(i+1)-peak.get(i));
        }
        avg = 60000/((sum/(peak.size()-1))*150);
        heartrate = avg;
        return heartrate;

    }*/
    /**
     * calbeatlist为检测到的点
     *
     *
     * */
    public static int calheartbeat(){
//        int max=0;
//        int max1=0;
        int i;
        List<Integer> max=new ArrayList<Integer>();
        while(true){
            for (i=1;i<calbeatlist.size()-1;i++){
                if (calbeatlist.get(i)==0)
                    continue;
                if (calbeatlist.get(i-1)<calbeatlist.get(i)&&calbeatlist.get(i)>=calbeatlist.get(i+1)){
                    if (calbeatlist.get(i)==calbeatlist.get(i+1)){
                        for (int j=i+2;j<calbeatlist.size()-1;j++){
                            if (calbeatlist.get(i)>calbeatlist.get(j)){
                                max.add(i);//=i;
                                i=j;
                            }else if (calbeatlist.get(i)<calbeatlist.get(j)){
                                i=j;
                            }

                        }
                    }else {
                        max.add(i);//=i;
                    }
                }
            }
            break;
        }

        System.out.println(calbeatlist.toString());
        System.out.println(calbeatlist.size()+"++++++++++++++++++++++++++++");
        System.out.println("max1:"+max.get(max.size()-1)+"max:"+max.get(0)+"---------------------");
        int index=0;
        for (i=0;i<max.size();i++){
            if((max.get(i+1)-max.get(0))>=10){
                index=i;
                break;
            }
        }
        int time=(max.get(max.size()-1)-max.get(0))*240;
        //int alltime=calbeatlist.size()*125;
        int rate=(int)(((double)time/1000)*20);//(int)(((double)(1000/(time*3)))*60);
        System.out.println(rate);
        return rate;
    }


    protected void prompt(int message) {
        // TODO Auto-generated method stub
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    protected void upLoadHr(final int rate) {
        System.out.println("进入uploadHr方法...............");
        // TODO Auto-generated method stub
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                final SharedPreferences loginData = getSharedPreferences("LoginData", MODE_PRIVATE);
                String status="";
                if (50 < rate && rate < 70) {
                    status = "缓慢";//参数暂时定死
                } else if (70 < rate && rate < 80) {
                    status = "平静";//参数暂时定死
                } else if (80 < rate && rate < 90) {
                    status = "运动";//参数暂时定死
                } else if (rate > 90) {
                    status = "剧烈";//参数暂时定死
                }
                String name = loginData.getString("username", "");
                ;
                String moblie = loginData.getString("userphone", "");//参数暂时是定死的

                try {
                    System.out.println("进入uploadHr.run（）方法...............");
                    boolean res = UpLoadService.postRequest(name, moblie, rate, status);
                    Message msg = new Message();
                    if (res == true) {
                        msg.what = Cmd.MSG_SUCCESS;
                    } else {
                        msg.what = Cmd.MSG_FAIL;
                    }
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }//
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }

    protected int getHeartRate() {
        return 0;
        // TODO Auto-generated method stub

    }

    protected void startTimer() {
        // TODO Auto-generated method stub
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Message msg = new Message();
                    msg.what = 1;// 设置相机消息
                    handler.sendMessage(msg);// 发送消息
                }
            };
        }

        if (timer != null && task != null) {
            timer.schedule(task, 500, 200);// 定时器从500ms开始，每隔200ms执行一次
        }

    }

    // 关闭定时器
    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();// 結束定時
            timer = null;
        }
        if (task != null) {
            task.cancel();// 結束任務定時
            task = null;
        }
    }

    /**
     * 与用户交互的方法
     */

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        camera = Camera.open();// 开启开启相机

        SharedPreferences loginData = getSharedPreferences("LoginData", MODE_PRIVATE);
        boolean loginStatues = loginData.getBoolean("status", false);
        if (loginStatues) {
            log_off.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer();// 暂停相机
        camera.setPreviewCallback(null);// 停止相机回调
        camera.stopPreview();// 停止相机预览
        camera.release();// 断开相机
        camera = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        SharedPreferences loginData = getSharedPreferences("LoginData", MODE_PRIVATE);
        String usrName = loginData.getString("username", "");
        if (data == null)
            throw new NullPointerException();
        Camera.Size size = camera.getParameters().getPreviewSize();
        if (size == null)
            throw new NullPointerException();
        hearts = Heartrate.rgbAvg(data.clone(), size.width, size.height);
        if (40 < hearts && hearts < 160) {
            setHearts(hearts);
            showHeart.setText(usrName + "心跳：" + hearts + "次/分");// 显示信号强度
        } else {
            setHearts(0);
            showHeart.setText(usrName + "请将手指防盗摄像头上");
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
}
