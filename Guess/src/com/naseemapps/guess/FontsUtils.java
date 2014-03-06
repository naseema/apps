package com.naseemapps.guess;

import android.content.Context;
import android.graphics.Typeface;

public class FontsUtils {

	static Typeface mFontRobotoBold;
	static Typeface mFontRobotoItalic;
	static Typeface mFontRobotoBoldItalic;
	static Typeface mFontRobotoLight;
	static Typeface mFontRobotoLightItalic;
	static Typeface mFontRobotoRegular;
	static Typeface mFontRobotoThin;
	static Typeface mFontRobotoThinItalic;
	
	
	static Typeface mFontRobotoCondensedBold;
	static Typeface mFontRobotoCondensedBoldItalic;
	static Typeface mFontRobotoCondensedItalic;
	static Typeface mFontRobotoCondensedRegular;
	
	
	public static Typeface getFontRobotoBold(Context context) {
		if (mFontRobotoBold == null)
			mFontRobotoBold = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
		return mFontRobotoBold;
	}
	public static Typeface getFontRobotoBoldItalic(Context context) {
		if (mFontRobotoBoldItalic == null)
			mFontRobotoBoldItalic = Typeface.createFromAsset(context.getAssets(), "Roboto-BoldItalic.ttf");
		return mFontRobotoBoldItalic;
	}
	public static Typeface getFontRobotoLightItalic(Context context) {
		if (mFontRobotoLightItalic == null)
			mFontRobotoLightItalic = Typeface.createFromAsset(context.getAssets(), "Roboto-LightItalic.ttf");
		return mFontRobotoLightItalic;
	}
	public static Typeface getFontRobotoLight(Context context) {
		if (mFontRobotoLight == null)
			mFontRobotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
		return mFontRobotoLight;
	}
	public static Typeface getFontRobotoRegular(Context context) {
		if (mFontRobotoRegular == null)
			mFontRobotoRegular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
		return mFontRobotoRegular;
	}
	public static Typeface getFontRobotoThin(Context context) {
		if (mFontRobotoThin == null)
			mFontRobotoThin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
		return mFontRobotoThin;
	}
	public static Typeface getFontRobotoThinItalic(Context context) {
		if (mFontRobotoThinItalic == null)
			mFontRobotoThinItalic = Typeface.createFromAsset(context.getAssets(), "Roboto-ThinItalic.ttf");
		return mFontRobotoThinItalic;
	}
	
	
	public static Typeface getFontRobotoCondensedBold(Context context) {
		if (mFontRobotoCondensedBold == null)
			mFontRobotoCondensedBold = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
		return mFontRobotoCondensedBold;
	}
	public static Typeface getFontRobotoCondensedBoldItalic(Context context) {
		if (mFontRobotoCondensedBoldItalic == null)
			mFontRobotoCondensedBoldItalic = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-BoldItalic.ttf");
		return mFontRobotoCondensedBoldItalic;
	}
	public static Typeface getFontRobotoCondensedItalic(Context context) {
		if (mFontRobotoCondensedItalic == null)
			mFontRobotoCondensedItalic = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Italic.ttf");
		return mFontRobotoCondensedItalic;
	}
	public static Typeface getFontRobotoCondensedRegular(Context context) {
		if (mFontRobotoCondensedRegular== null)
			mFontRobotoCondensedRegular = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf");
		return mFontRobotoCondensedRegular;
	}
	
	

}
