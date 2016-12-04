package com.zzw.myheartclient;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.zzw.beans.Hearts;
import com.zzw.myimpl.Cmd;
import com.zzw.tools.LookupThread;
import com.zzw.tools.getNumberThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class LookupActivity extends Activity {
    private Button btnCheckRecent;//显示列表
    private TextView tvShowRecent;//
    private Button btnShowPie;//显示图表
    private PieChart mChart;
    private HashMap hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);
        btnCheckRecent = (Button) findViewById(R.id.btn_checkRecent);
        btnShowPie = (Button) findViewById(R.id.btn_showPie);
        tvShowRecent = (TextView) findViewById(R.id.tv_showRecent);
        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        tvShowRecent.setText("获取数据.......");
        btnCheckRecent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mChart.setVisibility(View.GONE);
                tvShowRecent.setVisibility(View.VISIBLE);
                psotJson();// 新方法
            }
        });
        btnShowPie.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tvShowRecent.setVisibility(View.GONE);
                mChart.setVisibility(View.VISIBLE);
                getNumber();//获取相应用户数量


            }
        });
    }

    private void getNumber() {
        getNumberThread lt = new getNumberThread(handler,this);
        new Thread(lt).start();
    }

    protected void psotJson() {
        // TODO Auto-generated method stub
        LookupThread lt = new LookupThread(handler, this);
        new Thread(lt).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Cmd.MSG_SEE:
                    /*System.out.println("获取到信息了.........................");
                    String getdata= (String) msg.obj;
                    hm = (HashMap) transStringToMap(getdata);

                    System.out.println("hmhnhnhnhhmhmkhmjfdslfjklsfskl");*/
                    System.out.println("handler");
                    String response = (String) msg.obj;
                    List<Hearts> lh = JSONArray.parseArray(response, Hearts.class);
                    String hrText = "查询结果：\r\n";
                    for (int i = 0; i < lh.size(); i++) {
                        Hearts hr = lh.get(i);
                        hrText += hr.getName() + "," + String.valueOf(hr.getHeart() + "," + hr.getStatus() + "," + hr.getDtime() + "," + hr.getMobile() + "\r\n");
                    }
                    tvShowRecent.setText(hrText);
                    break;
                case Cmd.GETCOUNT:
                    System.out.println("获取到信息了.........................");
                    String getdata= (String) msg.obj;
                    hm = (HashMap) transStringToMap(getdata);
                    PieData mPieData = getPieData(4, 100);
                    showChart(mChart, mPieData);
                    System.out.println("hmhnhnhnhhmhmkhmjfdslfjklsfskl");
                break;
                default:
                    break;
            }
        }
    };

    private void showChart(PieChart pieChart, PieData pieData) {
//        pieChart.setHoleColorTransparent(true);

        //设置中心圆半径
        pieChart.setHoleRadius(30f);  //半径
        pieChart.setTransparentCircleRadius(30f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription("心率状况");

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("近期心率状况");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(Legend.LegendForm.LINE);  //设置比例图的形状，默认是方形
//        mLegend.setXEntrySpace(0f);
//        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        System.out.println("getpiedata");
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

//        for (int i = 0; i < count; i++) {
//            xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
//        }
        xValues.add("缓慢");
        xValues.add("平常");
        xValues.add("运动");
        xValues.add("剧烈");

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
//
//        System.out.println(hm.toString()+":map");
//        System.out.println(hm.get("运动")+"运动");
//                    System.out.println(hm.toString()+"djfskjfkjvksmvksvm");
        Set setKey= hm.keySet();
        Iterator it = setKey.iterator();
//        float[] floats=new float[4];
        List<Float> list=new ArrayList<Float>();
        System.out.println("list");
        while(it.hasNext()){

            String key = (String)it.next();
            System.out.println("it");
            float d =(Float.parseFloat((String) hm.get(key))*10);
            list.add(d);
//            System.out.println(key+"-----"+d);
//                        dataset.setValue(key,d);
        }
        System.out.println("while end");
        float quarterly1 = list.get(0);
        float quarterly2 = list.get(1);
        float quarterly3 = list.get(2);
        float quarterly4 = list.get(3);

        yValues.add(new Entry(quarterly4, 0));//缓慢
        yValues.add(new Entry(quarterly3, 1));//平常
        yValues.add(new Entry(quarterly2, 2));//运动
        yValues.add(new Entry(quarterly1, 3));//剧烈

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图右上角*/);
        pieDataSet.setSliceSpace(2f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(245, 174, 95));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(250, 71, 95));
        colors.add(Color.rgb(247, 44, 15));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
    public static Map transStringToMap(String mapString){
        Map map = new HashMap();
        java.util.StringTokenizer items;
        for(StringTokenizer entrys = new StringTokenizer(mapString, ", "); entrys.hasMoreTokens();
            map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "=");
        return map;
    }


}
