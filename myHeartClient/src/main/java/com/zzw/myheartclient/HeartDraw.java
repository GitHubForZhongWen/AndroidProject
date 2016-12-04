package com.zzw.myheartclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HeartDraw extends View {
	private int RECT_W = 800;
	private int RECT_H = 400;
	private int RECT_LEFT = 10;// 定义矩形左上角X坐标
	private int RECT_TOP = 20;// 定义左上角Y坐标
	private List<Point> plist;
	private final MainActivity activity;
	public static List<Integer> calbeatlist;

	public HeartDraw(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		activity = (MainActivity) context;
		plist = new ArrayList<Point>();
		calbeatlist=new ArrayList<Integer>();
	}

	// 绘制表格
	private void drawTable(Canvas canvas) {
		// 画矩形框
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		// 定义矩形(两个点,左上角一个点，右下角一个点)
		Rect rect = new Rect(RECT_LEFT, RECT_TOP, RECT_LEFT + RECT_W, RECT_TOP + RECT_H);
		canvas.drawRect(rect, paint);

		// 画虚线条
		Path path = new Path();
		DashPathEffect effect = new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(false);
		paint.setPathEffect(effect);
		for (int i = 0; i < 10; i++) {
			path.moveTo(RECT_LEFT, RECT_TOP + RECT_H / 10 * i);
			path.lineTo(RECT_LEFT + RECT_W, RECT_TOP + RECT_H / 10 * i);
			canvas.drawPath(path, paint);
		}
	}

	// 绘制曲线图
	private void drawCurve(Canvas canvas) {

		int X_STEP = 20;// x轴步长，每隔20一个点
		int DOTS = RECT_W / X_STEP;// 曲线点数
		// plist = new ArrayList<Point>();// 曲线点集

		// 生成曲线
		int beat = activity.getHearts();
		System.out.println("---------------------:"+String.valueOf(beat));
		if (30 > beat)
			beat = 0;
		calbeatlist.add(beat);
		Point pt = new Point(RECT_LEFT, RECT_H - beat);
		plist.add(pt);
		if (plist.size() > DOTS)
			plist.remove(0);

		for (int i = 0; i < plist.size() - 1; i++) {
			plist.get(i).x += X_STEP;
		}

		// 绘制
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);// 设置线条颜色
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);// 去锯齿
		if (plist.size() >= 2) {
			for (int i = 0; i < plist.size() - 1; i++) {
				canvas.drawLine(plist.get(i).x, plist.get(i).y, plist.get(i + 1).x, plist.get(i + 1).y, paint);
			}
		}
	}

	// 更新绘图

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawTable(canvas);
		drawCurve(canvas);
	}

}
