package com.naseemapps.guess;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;

public class GameActivity extends Activity implements GameListener{

	static final String FILE_NAME = "lvls.db";
	static final int DEFAULT_USER_LVL = 1;
	static final int DEFAULT_USER_COINS = 15;
	
	KeyboardView mKeyboardView;
	UserPlayerEntity mUser;
	LevelHolder mLevelHolder;
	ArrayList<LevelHolder> mLevelSolutions = new ArrayList<LevelHolder>();
	View mImage;
	TextView mLevelTv, mUserCoinsTv;
	Dialog mSuccessDialog;// = new Dialog(this, R.style.AlertDialogCustom);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		mImage = findViewById(R.id.game_main_image);
		mLevelTv = (TextView) findViewById(R.id.level_tv);
		mUserCoinsTv = (TextView) findViewById(R.id.user_coins);
			

		mUser = loadUserPref();
		
		mLevelTv.setText(mUser.mLevel + "");
		mUserCoinsTv.setText(mUser.mCoins + "");
		
		mKeyboardView = new KeyboardView(this, this);
		InitLevels();
//		updateCurrentLvl(16);
		loadLevel(mUser.mLevel);
		
		initSuccessDialog();

//		mKeyboardView.initKeyboard("abcdefg" + "" + "");
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/


	
	public class UserPlayerEntity {
		int mLevel = 0;
		int mCoins = 0;

		public UserPlayerEntity(int level, int coins) {
			mLevel = level;
			mCoins = coins;
		}
		
		public boolean hadEnoughCoins() {
			return mCoins > 0;
		}
	}
	
	
	public class LevelHolder {
		String mImgQ;
		String mImgS;
		String mSolution;

		public LevelHolder(String sol, String qImg, String sImg) {
			mImgS = sImg;
			mImgQ = qImg;
			mSolution = sol;
		}
		
		/*public void setLevel(int level) {
//			mImage.setBackgroundResource(R.drawable.btn_keyboard_key_light_normal_holo);
			mImage.setBackgroundResource(MainActivity.this.getResources().getIdentifier("q" + level, "drawable", MainActivity.this.getPackageName()));
			mKeyboardView.initKeyboard("man");
		}*/
	}
	
	private UserPlayerEntity loadUserPref() {
		int coins = GameSharedPref.getInstance(this).loadPrefInt(GameSharedPref.PREF_USER_GAME_COINS, DEFAULT_USER_COINS);
		int lvl = GameSharedPref.getInstance(this).loadPrefInt(GameSharedPref.PREF_USER_GAME_LVL, DEFAULT_USER_LVL);
		return new UserPlayerEntity(lvl, coins);
	}
	
	private void updateCurrentLvl(int level) {
		mUser.mLevel = level;
		GameSharedPref.getInstance(this).savePrefInt(GameSharedPref.PREF_USER_GAME_LVL, mUser.mLevel);
		mLevelTv.setText(mUser.mLevel + "");
	}
	
	private void updateCurrentCoins(int coins) {
		mUser.mCoins = coins;
		GameSharedPref.getInstance(this).savePrefInt(GameSharedPref.PREF_USER_GAME_COINS, mUser.mCoins);
		mUserCoinsTv.setText(mUser.mCoins + "");
	}
	
//	int drawableResourceId = this.getResources().getIdentifier("nameOfDrawable", "drawable", this.getPackageName());

	@Override
	public void onSuccess() {
		updateCurrentLvl(mUser.mLevel + 1);
		updateCurrentCoins(mUser.mCoins + 1);
		SoundPlayer.playVictory();
		showDialogOnSuccess();
	}

	@Override
	public void onFail() {
		finish();
	}

	@Override
	public void onUsingHelpRemoveLetter() {
		if (mUser.mCoins == 0) {
			return;
		}
		updateCurrentCoins(mUser.mCoins - 1);		
	}

	@Override
	public void onUsingHelpRevealLetter() {
		if (mUser.mCoins == 0) {
			return;
		}
		updateCurrentCoins(mUser.mCoins - 1);		
	}
	
	/*@TargetApi(14)
	public AlertDialog.Builder getAlertDailog() {
		if (Integer.valueOf(android.os.Build.VERSION.SDK) >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			return new AlertDialog.Builder(
					this, android.R.style.Theme_DeviceDefault_Dialog);
		} else {
			return new AlertDialog.Builder(this);
//		}
	}*/
	

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public AlertDialog.Builder initAlertDailog() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new AlertDialog.Builder(
					this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		} else {
			return new AlertDialog.Builder(this);
        }
	}
	
	private void showDialogWaitForMoreLevel() {
		
		final Dialog dialog = new Dialog(this, R.style.AlertDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		dialog.setContentView(R.layout.custom_dialog);
		
		TextView title = (TextView) dialog.findViewById(R.id.custom_dialog_title_tv);
		TextView msg = (TextView) dialog.findViewById(R.id.custom_dialog_msg_tv);
		TextView button = (TextView) dialog.findViewById(R.id.custom_dialog_btn);
		
		title.setTypeface(FontsUtils.getFontRobotoCondensedBold(this));
		msg.setTypeface(FontsUtils.getFontRobotoCondensedBold(this));
		button.setTypeface(FontsUtils.getFontRobotoCondensedBold(this));
		
		title.setText(R.string.congratulations);
		msg.setText(R.string.finish_all_levels);
		button.setText(R.string.play_again);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateCurrentLvl(1);
				loadLevel(mUser.mLevel);
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				GameActivity.this.finish();
				
			}
		});
		
		/*AlertDialog.Builder alertDialogBuilder = initAlertDailog();
 
				alertDialogBuilder.setTitle(R.string.finish_all_levels);
 
			alertDialogBuilder
//				.setMessage(R.string.play_again)
				.setCancelable(false)
				.setPositiveButton(R.string.play_again,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
//						gameInstance.restartGame();
						updateCurrentLvl(1);
						loadLevel(mUser.mLevel);
					}
				  });
			
			alertDialogBuilder.setNegativeButton(R.string.back,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						MainActivity.this.finish();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();*/
	}
	
	
	
	public String readfileFromAssets(String fileName, Context context) {
		StringBuilder returnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets().open(fileName, Context.MODE_PRIVATE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				returnString.append(line);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return returnString.toString();
	}


	private void loadLevel(int lvl) {
		
		if (lvl > mLevelSolutions.size()) {
			showDialogWaitForMoreLevel();
			return;
		}
		mImage.setBackgroundResource(GameActivity.this.getResources().getIdentifier(mLevelSolutions.get(lvl - 1).mImgQ, "drawable", GameActivity.this.getPackageName()));
		mKeyboardView.initKeyboard(mLevelSolutions.get(lvl - 1).mSolution);
		
		initSuccessDialog();
	}
	
	private void InitLevels () {
		String fileContent = readfileFromAssets(FILE_NAME, this);
		try {
			JSONObject jo = new JSONObject(fileContent);
			JSONArray jArr = jo.getJSONArray("sols");
			for (int i = 0; i < jArr.length(); i++) {
					mLevelSolutions.add(new LevelHolder(jArr.getJSONObject(i).getString("sol"),
							jArr.getJSONObject(i).getString("q"),
							jArr.getJSONObject(i).getString("s")));
			}
		} catch (JSONException e) {
		}
	}
	
	private void showDialogOnSuccess() {
		
		TextView answer = (TextView) mSuccessDialog.findViewById(R.id.answer_tv);
		TextView coinVal = (TextView) mSuccessDialog.findViewById(R.id.coin_you_have_tv);
		View image = mSuccessDialog.findViewById(R.id.answer_img);

		coinVal.setText(mUser.mCoins + "");
		answer.setText(mLevelSolutions.get(mUser.mLevel - 2).mSolution);
		image.setBackgroundResource(GameActivity.this.getResources().getIdentifier(mLevelSolutions.get(mUser.mLevel - 2).mImgS, "drawable", GameActivity.this.getPackageName()));

		mSuccessDialog.show();
	}
	
	
	private void initSuccessDialog() {
		if (mSuccessDialog == null) {
			mSuccessDialog = new Dialog(this, R.style.AlertDialogCustom);
			mSuccessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			mSuccessDialog.setContentView(R.layout.on_success_view);
			Button dialogButton = (Button) mSuccessDialog.findViewById(R.id.next_level);
			TextView title = (TextView) mSuccessDialog.findViewById(R.id.title_dialog_tv);
			TextView answer = (TextView) mSuccessDialog.findViewById(R.id.answer_tv);
			TextView answerLabel = (TextView) mSuccessDialog.findViewById(R.id.answer_lable_tv);
			TextView coinLabel = (TextView) mSuccessDialog.findViewById(R.id.coin_you_have_lable_tv);
			TextView coinVal = (TextView) mSuccessDialog.findViewById(R.id.coin_you_have_tv);


			title.setTypeface(FontsUtils.getFontRobotoCondensedBold(GameActivity.this));
			answer.setTypeface(FontsUtils.getFontRobotoCondensedBold(GameActivity.this));
			answerLabel.setTypeface(FontsUtils.getFontRobotoCondensedBold(GameActivity.this));
			coinLabel.setTypeface(FontsUtils.getFontRobotoLight(GameActivity.this));
			coinVal.setTypeface(FontsUtils.getFontRobotoLight(GameActivity.this));
			dialogButton.setTypeface(FontsUtils.getFontRobotoCondensedBold(GameActivity.this));
			
			mSuccessDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					GameActivity.this.finish();
				}
			});
			
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadLevel(mUser.mLevel);
					mSuccessDialog.dismiss();
				}
			});	
		
		}
	}

	@Override
	public UserPlayerEntity getUserEntity() {
		return mUser;
	}

	@Override
	public void noEnoughConis() {
		// TODO Auto-generated method stub
		
	}
	
}
