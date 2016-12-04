package com.zzw.myheartclient;

/**
 * 这是一个计算心率算法类
 */

public class Heartrate {
	public static int rgbAvg(byte[] yuv420sp, int width, int height) {
		int avg = 0;
		if (yuv420sp == null)
			return 0;
		final int size = width * height;
		for (int j = 0, py = 0; j < height; j++) {
			int puv = size + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, py++) {
				int y, r, g, b;
				y = (0xFF & yuv420sp[py]) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {// 偶数列像素，相当于i%2==0
					v = (0xFF & yuv420sp[puv++]) - 128;
					u = (0xFF & yuv420sp[puv++]) - 128;
				}
				r = (int) (y + 1.140 * v);
				g = (int) (y - 0.394 * u - 0.581 * v);
				b = (int) (u + 2.203 * v);
				if (r > 170) {
					avg += (int) (0.3 * r + 0.59 * g + 0.11 * b);// 求和
				}
			}
		}
		return avg / size;
	}
}
