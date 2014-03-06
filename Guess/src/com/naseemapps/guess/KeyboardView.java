package com.naseemapps.guess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import android.R.array;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KeyboardView implements OnClickListener{

	ArrayList<TextView> mKeys;
	View mHelpAddLetterBt;
	View mHelpRemoveLetterBt;
	View mClearBtn;
	LinearLayout mCurrentSol;
	Activity mContext;
	Random mRandom;
	int mUserOptionSize;
	HashMap<TextView, TextView> mMapBetweenKeyAndSol;
	
	GameListener mGameListener;
	
	
	OnClickListener mKeyboardClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SoundPlayer.playKeyboardClickCorrect();

			if (v instanceof TextView) {
				TextView tvB = (TextView) v;
				for (int i = 0; i < mCurrentSol.getChildCount(); i++) {
					TextView tv = (TextView) mCurrentSol.getChildAt(i);
					if (tv.getText().equals("")) {
						tv.setText(tvB.getText());
						mMapBetweenKeyAndSol.put(tv, tvB);
						tvB.setVisibility(View.INVISIBLE);
						for (int j = 0; j < mCurrentSol.getChildCount(); j++) {
							tv = (TextView) mCurrentSol.getChildAt(j);
							if (tv.getText().equals("")) {
								return;
							}
						}
						checkSol();
						return;
					}
				}
			}
			SoundPlayer.playOpenbeep10();
		}
	};
	
	OnClickListener mSolutionClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SoundPlayer.playKeyboardClickCorrect();

			setUserInputError(false);

			if (v instanceof TextView) {
				TextView tv = (TextView) v;
				if (tv.getText().equals("")) {
					return;
				}
				tv.setText("");
				if (mMapBetweenKeyAndSol.containsKey(v)) {
					mMapBetweenKeyAndSol.get(v).setVisibility(View.VISIBLE);
					mMapBetweenKeyAndSol.remove(v);
				}
//				tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_keyboard_key_light_pressed_holo));
			}
		}
	};
	
	LinearLayout.LayoutParams mLp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	
	char[] mSolution;
	
	
	public KeyboardView (Activity context, GameListener listener) {
		mContext = context;
		mGameListener = listener;
		mKeys = new ArrayList<TextView>();
		mRandom = new Random();
		mLp.weight=1;
		mLp.width = 0;
		mMapBetweenKeyAndSol = new HashMap<TextView, TextView>();
		
		mCurrentSol = (LinearLayout) mContext.findViewById(R.id.current_user_sol_tv);
		mHelpAddLetterBt = mContext.findViewById(R.id.help_add_letter);
		mHelpRemoveLetterBt = mContext.findViewById(R.id.help_remove_letter);
		mClearBtn = mContext.findViewById(R.id.clear_del_btn);
		
		mClearBtn.setOnClickListener(this);
		mHelpAddLetterBt.setOnClickListener(this);
		mHelpRemoveLetterBt.setOnClickListener(this);
		
		mKeys.add((TextView) mContext.findViewById(R.id.Button01));
		mKeys.add((TextView) mContext.findViewById(R.id.Button02));
		mKeys.add((TextView) mContext.findViewById(R.id.Button03));
		mKeys.add((TextView) mContext.findViewById(R.id.Button04));
		mKeys.add((TextView) mContext.findViewById(R.id.Button05));
		mKeys.add((TextView) mContext.findViewById(R.id.Button06));
		mKeys.add((TextView) mContext.findViewById(R.id.Button07));
		mKeys.add((TextView) mContext.findViewById(R.id.Button08));
		mKeys.add((TextView) mContext.findViewById(R.id.Button09));
		mKeys.add((TextView) mContext.findViewById(R.id.Button10));
		mKeys.add((TextView) mContext.findViewById(R.id.Button11));
		mKeys.add((TextView) mContext.findViewById(R.id.Button12));
		for (View v : mKeys) {
			v.setOnClickListener(mKeyboardClick);
		}
	}
	
	
	public void initKeyboard(String solution) {
		if (solution == null) {
			return;
		}
		
		solution = solution.trim().toLowerCase();
		mSolution = solution.toCharArray();
		
		
		ArrayList<Character> letters = new ArrayList<Character>(); 
		mCurrentSol.removeAllViews();
		for (char c : mSolution) {
			letters.add(c);
			mCurrentSol.addView(createSolLetterBtnView(),mLp);
		}

		if (letters.size() > mKeys.size()) {
			Toast.makeText(mContext, "ERROR bad sol lenght", Toast.LENGTH_LONG).show();
			mContext.finish();
		}
		
		while (letters.size() != mKeys.size()) {
			letters.add((char)(mRandom.nextInt(26) + 'a'));
		}
		
		int i = 0 ;
		Collections.sort(letters);
		for (Iterator<Character> iterator = letters.iterator(); iterator.hasNext(); i++) {
			Character character = (Character) iterator.next();
			mKeys.get(i).setText(character +"");
			mKeys.get(i).setVisibility(View.VISIBLE);
		}
		
		mHelpRemoveLetterBt.setEnabled(true);
		
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.help_add_letter:
			if (mGameListener.getUserEntity().mCoins > 0) {
				SoundPlayer.playMagic1();
				setUserInputError(false);
				revealLetter();
				mGameListener.onUsingHelpRevealLetter();
			} else {
				SoundPlayer.playKeyboardClickWrong();
				mGameListener.noEnoughConis();
			}
			break;
		case R.id.help_remove_letter:
			if (mGameListener.getUserEntity().mCoins > 0) {
				SoundPlayer.playMagic2();
				setUserInputError(false);
				removeKeyboardLetter();
				mGameListener.onUsingHelpRemoveLetter();				
			} else {
				SoundPlayer.playKeyboardClickWrong();
				mGameListener.noEnoughConis();
			}
			break;
		case R.id.clear_del_btn:
			setUserInputError(false);
			clearUserInput();
			break;
		default:
			break;
		}
		if (v instanceof TextView) {
			TextView tv = (TextView) v;
//			mCurrentSol.setText(tv.getText());
		}
		
	}
	
	private View createSolLetterBtnView() {
		Button b = new Button(mContext);
		b.setBackgroundResource(R.drawable.btn_keyboard_key_light_pressed_holo);
		b.setTextColor(Color.WHITE);
		b.setMinWidth((int)b.getPaint().density * 30);
		b.setOnClickListener(mSolutionClick);
		return b;
	}
	
	private void checkSol() {
		if (getCurrentUSerSol().equals(new String(mSolution))) {
//			Toast.makeText(mContext, "TRUE", Toast.LENGTH_SHORT).show();
//			ToastManager.showToast(mContext, "TRUE");
//			initKeyboard("man");
			mGameListener.onSuccess();
		} else {
//			clearUserInput();
//			Toast.makeText(mContext, "TRUE", Toast.LENGTH_SHORT).show();
//			ToastManager.showToast(mContext, "False");
			setUserInputError(true);
			SoundPlayer.playOpenbeep10();
		}
	}
	
	private void clearUserInput() {
		for (int i = 0; i < mCurrentSol.getChildCount(); i++) {
			TextView tv = (TextView) mCurrentSol.getChildAt(i);
			if (!tv.isEnabled()){
				continue;
			}
			if (mMapBetweenKeyAndSol.containsKey(tv)) {
				mMapBetweenKeyAndSol.get(tv).setVisibility(View.VISIBLE);
				mMapBetweenKeyAndSol.remove(tv);
			}
			tv.setText("");
		}
	}
	
	private String getCurrentUSerSol() {
		StringBuilder sol = new StringBuilder();
		for (int i = 0; i < mCurrentSol.getChildCount(); i++) {
			TextView tv = (TextView) mCurrentSol.getChildAt(i);
			sol.append(tv.getText());
		}
		return sol.toString();
	}
	
	private void removeKeyboardLetter() {
		int ind = mRandom.nextInt(11);
		ArrayList<Character> sol = new ArrayList<Character>();
		for (int i = 0; i < mKeys.size(); i++) {
			if (mKeys.get(i).getVisibility() == View.VISIBLE)
				sol.add(mKeys.get(i).getText().charAt(0));
		}
		for (int i = 0; i < mCurrentSol.getChildCount(); i++) {
			TextView tv = (TextView) mCurrentSol.getChildAt(i);
			if (!tv.getText().toString().equals("")) {
				sol.add(tv.getText().charAt(0));
			}
		}
		
		
		for (char c : mSolution) { 
			for (int i = 0; i < sol.size(); i++) {
				if (c == sol.get(i)) {
					sol.remove(i);
					break;
				}
			}
		}
		boolean isRemoved = false;
		for (int j = 0; j < sol.size(); j++) {
			for (int i = 0; i < mKeys.size(); i++) {
				if (mKeys.get(i).getText().charAt(0) == sol.get(j) && mKeys.get(i).getVisibility() == View.VISIBLE) {
					mKeys.get(i).setVisibility(View.INVISIBLE);
					isRemoved = true;
					break;
//					return;
				}
			}
			if (isRemoved) {
				isRemoved = false;
				continue;
			}
			for (int i = 0; i < mCurrentSol.getChildCount(); i++) {
				TextView tv = (TextView) mCurrentSol.getChildAt(i);
				if (!tv.getText().toString().equals("") && sol.get(j) == tv.getText().charAt(0)) {
					tv.setText("");
//					return;
				}
			}
		}
		mHelpRemoveLetterBt.setEnabled(false);
		
	}
	
	private boolean isSolutionLayoutFull() {
		for (int i = 0; i < mCurrentSol.getChildCount(); i ++){
			TextView tv = (TextView) mCurrentSol.getChildAt(i);
			if (tv.getText().toString().equals("")) {
				return false;
			}
		}
		return true;
	}
	private void revealLetter() {
		clearUserInput();
		int ind = mRandom.nextInt(mSolution.length - 1);
		for (int i = 0; i < mCurrentSol.getChildCount(); i ++){
			TextView tv = (TextView) mCurrentSol.getChildAt((i + ind)%mCurrentSol.getChildCount());
			if (tv.getText().toString().equals("")) {
				char c = mSolution[(i + ind)%mCurrentSol.getChildCount()];
				tv.setText(c + "");
//				tv.setOnClickListener(null);
				tv.setBackgroundResource(R.drawable.btn_keyboard_key_light_normal_holo_fixed);
				tv.setEnabled(false);
				
				// remove letter
				for (int j = 0; j < mKeys.size(); j++) {
					if (mKeys.get(j).getText().charAt(0) == c && mKeys.get(j).getVisibility() == View.VISIBLE) {
						mKeys.get(j).setVisibility(View.INVISIBLE);
						if (isSolutionLayoutFull()) {
							checkSol();
						}
						return;
					}
				}
				
				return;
			}
		}
	}
	
	private void setUserInputError (boolean val) {
		for (int i = 0; i < mCurrentSol.getChildCount(); i ++){
			TextView tv = (TextView) mCurrentSol.getChildAt(i);
			if (tv.isEnabled()) {
				tv.setTextColor(val ? Color.RED : Color.WHITE);
			}
		}
	}

}
