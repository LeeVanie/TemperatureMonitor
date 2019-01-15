package com.zz.temperaturemonitor.utils;

import android.content.Context;

public class DisplayUtil {
	public static int getScreenWidth(Context mContext) {
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		return width;
	}

	public static int getScreenHeight(Context mContext) {
		int height = mContext.getResources().getDisplayMetrics().heightPixels;
		return height;
	}
}
