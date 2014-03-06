package com.naseemapps.guess;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {
	
	static Toast toast;
	
	public static void showToast(Context context, String msg) {
		initToast(context);
		toast.setText(msg);
		toast.show();
	}
	
	public static void showToast(Context context, int msgId) {
		initToast(context);
		toast.setText(msgId);
		toast.show();
	}
	
	public static void showToast(Context context, int msgId, int duration) {
		initToast(context);
		toast.setText(msgId);
		toast.setDuration(duration);
		toast.show();
	}
	
	public static void showToast(Context context, String msg, int duration) {
		initToast(context);
		toast.setText(msg);
		toast.setDuration(duration);
		toast.show();
	}

	private static void initToast(Context context) {
		if ( toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
	}
}
