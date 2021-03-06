package com.naseemapps.guess;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
	
	static MediaPlayer mMediaPlayer;
	static Context mContext;
	
	private SoundPlayer() {}
	
	public static void init(Context context) {
		mContext = context;
		mMediaPlayer = MediaPlayer.create(context,R.raw.button);
	}
	
	public static void playKeyboardClickCorrect() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.button_correct);
		mMediaPlayer.start();
	}
	
	public static void playKeyboardClickWrong() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.button_wrong);
		mMediaPlayer.start();
	}
	
	
	public static void playVictory() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.victory);
		mMediaPlayer.start();
	}
	
	
	public static void playGameover() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.gameover);
		mMediaPlayer.start();
	}
	
	
	public static void playOpenSound() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.open);
		mMediaPlayer.start();
	}
	
	public static void playOpenbeep10() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.beep10);
		mMediaPlayer.start();
	}
	
	public static void playMagic1() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.magic_chime);
		mMediaPlayer.start();
	}
	
	public static void playMagic2() {
		if (mContext == null)
			return;
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(mContext,R.raw.magic_chime_7);
		mMediaPlayer.start();
	}

}
