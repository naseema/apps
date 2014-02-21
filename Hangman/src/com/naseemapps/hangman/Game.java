package com.naseemapps.hangman;

import java.util.ArrayList;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Game implements OnClickListener{
	
	String[] mCorrectLetters = new String[] {"ا","ب"};
	ArrayList<String> ClickedLetters = new ArrayList<String>();
	
	int mRemianLife = 6;
	Listener mGameListener = null;
	
	static Game mGame = null;
	static Context mContext;
	
	static Drawable mDrawableNormal;
	static Drawable mDrawableCorrect;
	static Drawable mDrawableWrong;
	
	static public void  intiGame(Context context){
		if (mGame == null) {
			mGame = new Game();
			mContext = context;
			mDrawableNormal = context.getResources().getDrawable(R.drawable.letter_key);
			mDrawableCorrect = context.getResources().getDrawable(R.drawable.btn_keyboard_key_light_normal_holo_correct);
			mDrawableWrong = context.getResources().getDrawable(R.drawable.btn_keyboard_key_light_normal_holo_error);
		} 
	} 
	
	static public Game getInstance() throws Exception {
		if (mGame == null) {
			throw new Exception("Game not intialzed yet");
//			mGame = new Game();
		}
		return mGame;
	}

	@Override
	public void onClick(View v) {
//		restartGame();
		if (v instanceof TextView) {
			TextView view = (TextView) v;
			if (correctrLetter(view.getText().toString())) {
				view.setBackgroundDrawable(mDrawableCorrect);
			} else {
				view.setBackgroundDrawable(mDrawableWrong);
				mRemianLife --;
			}
			view.setClickable(false);
			if (mRemianLife == 0) {
				Toast.makeText(mContext, "Game over", Toast.LENGTH_LONG).show();
				mGameListener.onFail();
			}
		}		
	}
	
	
	private boolean correctrLetter(String letter) {
		for (String s : mCorrectLetters) {
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
		mRemianLife = 6;
	}
	
	public void setListener(Listener listener) {
		mGameListener = listener;
	}
}
