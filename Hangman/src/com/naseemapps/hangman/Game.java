package com.naseemapps.hangman;

import java.io.IOException;
import java.util.ArrayList;

import com.naseemapps.hangman.R.string;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game implements OnClickListener {
	
	static final int DEFAULT_REMAIN_VAL = 8;
	
	static final String BUNDLE_USER_SOL = "BUNDLE_USER_SOL";
	static final String BUNDLE_USER_REMAIN_LIFE = "BUNDLE_USER_REMAIN_LIFE";

//	String[] mCorrectLetters = new String[] { "ا", "ب" };
	static ArrayList<String> mClickedLetters = new ArrayList<String>();
	
	static ArrayList<String> mSolutionLetters = new ArrayList<String>();

	int mRemianLife = DEFAULT_REMAIN_VAL;
	Listener mGameListener = null;

	static Game mGame = null;
	static Context mContext;

	static Drawable mDrawableNormal;
	static Drawable mDrawableCorrect;
	static Drawable mDrawableWrong;
	static TextView mUserSolTv;
	static ImageView mImageStatus;


	static public void intiGame(Context context) {
		if (mGame == null) {
			mGame = new Game();
			mContext = context;
			mDrawableNormal = context.getResources().getDrawable(
					R.drawable.letter_key);
			mDrawableCorrect = context.getResources().getDrawable(
					R.drawable.btn_keyboard_key_light_normal_holo_correct);
			mDrawableWrong = context.getResources().getDrawable(
					R.drawable.btn_keyboard_key_light_normal_holo_error);
		}
	}

	static public Game getInstance() throws Exception{
		if (mGame == null) {
			throw new Exception("Game not intialzed yet");
			// mGame = new Game();
		}
		return mGame;
	}

	@Override
	public void onClick(View v) {
		// restartGame();
		if (v instanceof TextView) {
			TextView view = (TextView) v;
			mClickedLetters.add(view.getText().toString());
			if (correctrLetter(view.getText().toString())) {
//				mGameListener.onSuccess();TODO
				
//				playButtonClickCorrect();
				SoundPlayer.playKeyboardClickCorrect();
				view.setBackgroundDrawable(mDrawableCorrect);
				showLetter(view.getText().toString());
				String sol = mUserSolTv.getText().toString();
				if (!sol.contains("_")) {
					mUserSolTv.setText(sol.replace(" ", ""));
					mGameListener.onSuccess();
				}
			} else {
//				mGameListener.onFail();TODO

//				playButtonClickWrong();
				SoundPlayer.playKeyboardClickWrong();
				view.setBackgroundDrawable(mDrawableWrong);
				setUserRemianLife(mRemianLife-1);
			}
			view.setClickable(false);
			if (mRemianLife == 0) {
//				Toast.makeText(mContext, "Game over", Toast.LENGTH_LONG).show();
				mGameListener.onFail();
			}
		}
	}

	private boolean correctrLetter(String letter) {
		for (String s : mSolutionLetters) {
			if (s.equals(letter)) {
				return true;
			}
		}
		return false;
	}

	public interface Listener {
		abstract public void onSuccess();

		abstract public void onFail();
	}

	public void restartGame() {
		for (View v : ClickableLetter.gameKeyboard) {
			v.setClickable(true);
			v.setBackgroundDrawable(mDrawableNormal);
		}
		setUserRemianLife(DEFAULT_REMAIN_VAL);
		mClickedLetters.clear();
		mSolutionLetters.clear();

	}
	
	

	public void setListener(Listener listener) {
		mGameListener = listener;
	}

	public void setSolutionView(TextView userSolTv, ImageView iv) {
		mUserSolTv = userSolTv;
		mImageStatus = iv;
	}

	
	public void setCorrectSolution(String sol) {
		mSolutionLetters.clear();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < sol.length(); i++) {
			sb.append("_ ");
			mSolutionLetters.add(sol.charAt(i) + "");
		}
		
		mUserSolTv.setText(sb);
	}
	
	private void showLetter(String letter) {
		String [] sol = mUserSolTv.getText().toString().trim().split(" ");
		for (int i = 0; i < mSolutionLetters.size(); i++) {
			if (mSolutionLetters.get(i).equals(letter)) {
				sol[i] = letter;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (String s : sol) {
			sb.append(s + " ");
		}
		mUserSolTv.setText(sb.toString().trim());
	}
	
	public void saveInstance(Bundle outState) {
//		outState.putString(BUNDLE_USER_SOL, mUserSolTv.getText().toString());
		outState.putInt(BUNDLE_USER_REMAIN_LIFE, mRemianLife);
	}
	
	public void restoreInstance(Bundle savedInstanceState) {
//		mUserSolTv.setText(savedInstanceState.getString(BUNDLE_USER_SOL));

		setUserRemianLife(savedInstanceState.getInt(BUNDLE_USER_REMAIN_LIFE, DEFAULT_REMAIN_VAL));

		for (TextView tv : ClickableLetter.gameKeyboard) {
			for (String let : mClickedLetters) {
				if (tv.getText().toString().equals(let)) {
					tv.setClickable(false);
					if (mUserSolTv.getText().toString().contains(let)) {
						tv.setBackgroundDrawable(mDrawableCorrect);
					} else {
						tv.setBackgroundDrawable(mDrawableWrong);
					}
				}
				
			}
		}
		
		String sol = mUserSolTv.getText().toString();
		if (!sol.contains("_")) {
			mUserSolTv.setText(sol.replace(" ", ""));
			mGameListener.onSuccess();
			return;
		}
		if (mRemianLife == 0) {
			mGameListener.onFail();
			return;
		}
	}
	
	private void setUserRemianLife(int r) {
		mRemianLife = r;
		switch (r) {
		case 0:
			mImageStatus.setImageResource(R.drawable.h7);
			break;
		case 1:
			mImageStatus.setImageResource(R.drawable.h7);
			break;
		case 2:
			mImageStatus.setImageResource(R.drawable.h6);
			break;
		case 3:
			mImageStatus.setImageResource(R.drawable.h5);
			break;
		case 4:
			mImageStatus.setImageResource(R.drawable.h4);
			break;
		case 5:
			mImageStatus.setImageResource(R.drawable.h3);
			break;
		case 6:
			mImageStatus.setImageResource(R.drawable.h2);
			break;
		case 7:
			mImageStatus.setImageResource(R.drawable.h1);
			break;
		case 8:
			mImageStatus.setImageResource(R.drawable.h0);
			break;
		default:
			mImageStatus.setImageResource(R.drawable.h0);
			break;
		}
	}
	
	public void helpMe() {
		String sol = mUserSolTv.getText().toString();
		if (!sol.contains("_")) {
			return;
		}		
		String [] solArr = mUserSolTv.getText().toString().trim().split(" ");
		for (int i = 0; i < solArr.length; i++) {
			if (solArr[i].equals("_")) {
				for (TextView tv : ClickableLetter.gameKeyboard) {
						if (tv.getText().toString().equals(mSolutionLetters.get(i))) {
							onClick(tv);
							return;
						}
				}
				//ERROR
				mGameListener.onFail();
			}
		}
		
		/*sol = mUserSolTv.getText().toString();
		if (!sol.contains("_")) {
			mUserSolTv.setText(sol.replace(" ", ""));
			mGameListener.onSuccess();
		}*/
	}
}
