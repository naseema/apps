package com.naseemapps.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements Game.Listener, OnClickListener {

	static final String BUNDLE_USER_SOL = "BUNDLE_USER_SOL";
	static final String BUNDLE_GAME_TITLE = "BUNDLE_GAME_TITLE";
	static final String BUNDLE_CUREENT_LEVEL = "BUNDLE_CUREENT_LEVEL";

	static final int DEFAULT_GAME_START = 1;
	static final int DEFAULT_HELP_ME_VAL = 10;
	
	Game gameInstance;
	int mCurrentLevel = 1;
	int mCurrentHelpMe = 0;
	
	ProgressDialog mProgressDialog;
	TextView mGameTitleTv;
	TextView mUserSolTv;
	TextView mUserLevelView;
	TextView mHelpMeRemain;
	View mHelpMeView;
	ImageView mImageStatus;

	private void initProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Loading....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		FlurryAgent.onEndSession(this);
//		FlurryAgent.logEvent("TEST");

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
//		getSupportActionBar().setHomeButtonEnabled(true);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
/*		///////////
//		FlurryAgent.setReportLocation(false);
		FlurryAgent.onStartSession(this, "VBPS296NK4SQH85Y2XXP");
		FlurryAgent.onPageView();
		///////////
*/		
		
		initProgressDialog();

		mGameTitleTv = (TextView) findViewById(R.id.game_title_tv);
		// mGameTitleTv.setText("SDFSDF");
		mUserSolTv = (TextView) findViewById(R.id.game_current_solution_tv);
		mImageStatus = (ImageView) findViewById(R.id.imageView1);
		mCurrentLevel = GameSharedPref.getInstance(this).loadPrefInt(GameSharedPref.PREF_USER_GAME_LVL, DEFAULT_GAME_START);
		mCurrentHelpMe = GameSharedPref.getInstance(this).loadPrefInt(GameSharedPref.PREF_USER_GAME_HELP_ME, DEFAULT_HELP_ME_VAL);
		mUserLevelView = (TextView) findViewById(R.id.level_tv);
		mHelpMeRemain = (TextView) findViewById(R.id.help_me_remain_tv);
		mHelpMeView = findViewById(R.id.help_me);
		mHelpMeView.setOnClickListener(this);
		mHelpMeRemain.setText("" + mCurrentHelpMe);
		
		try {
			gameInstance = Game.getInstance();
			gameInstance.setListener(this);
			gameInstance.setSolutionView(mUserSolTv, mImageStatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}

		
		
		if (savedInstanceState == null) {
			new ReadGameFromFile().execute(mCurrentLevel + "");
	
		} else {
			mUserSolTv.setText(savedInstanceState.getString(BUNDLE_USER_SOL));
			mGameTitleTv.setText(savedInstanceState.getString(BUNDLE_GAME_TITLE));

			gameInstance.restoreInstance(savedInstanceState);
			mUserLevelView.setText(mCurrentLevel + "");

		}

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

	@Override
	public void onSuccess() {
		new NotifiLVL().execute(mCurrentLevel+"" , mCurrentHelpMe +"");

		
		GameSharedPref.getInstance(this).savePrefInt(GameSharedPref.PREF_USER_GAME_LVL, mCurrentLevel + 1);
		showDialogOnSuccess();
		SoundPlayer.playVictory();
		GameSharedPref.getInstance(this).savePrefInt(GameSharedPref.PREF_USER_GAME_HELP_ME, ++mCurrentHelpMe);
		mHelpMeRemain.setText("" + mCurrentHelpMe);
//		gameInstance.restartGame();
	}

	@Override
	public void onFail() {
		showDialogOnfailed();
		SoundPlayer.playGameover();
//		gameInstance.restartGame();
		// finish();
	}
	
	

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		mCurrentLevel = savedInstanceState.getInt(BUNDLE_CUREENT_LEVEL);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString(BUNDLE_USER_SOL, mUserSolTv.getText().toString());
		outState.putString(BUNDLE_GAME_TITLE, mGameTitleTv.getText().toString());
		outState.putInt(BUNDLE_CUREENT_LEVEL, mCurrentLevel);
		

		gameInstance.saveInstance(outState);
	}

	public String ReadFromfile(String fileName, Context context) {
		StringBuilder returnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets()
					.open(fileName, Context.MODE_WORLD_READABLE);
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

	private class ReadGameFromFile extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			return ReadFromfile(params[0]+".lvl", GameActivity.this);
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			if (!result.equals("")) {
				try {
					JSONObject jo = new JSONObject(result);
					gameInstance.setCorrectSolution(jo.getString("solution"));
					mGameTitleTv.setText(jo.getString("title"));
					mUserLevelView.setText(mCurrentLevel + "");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showDialogWaitForMoreLevel();
					}
				});
			}


		}

		@Override
		protected void onPreExecute() {
//			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
	
	
	private void showDialogOnfailed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
				alertDialogBuilder.setTitle(R.string.game_over);
 
			// set dialog message
			alertDialogBuilder
//				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setPositiveButton(R.string.try_again,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						gameInstance.restartGame();
						new ReadGameFromFile().execute(mCurrentLevel + "");
					}
				  });
			
			/*alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});*/
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
	private void showDialogWaitForMoreLevel() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
				alertDialogBuilder.setTitle(R.string.wait_for_more_level);
 
			alertDialogBuilder
				.setMessage(R.string.new_game_restart)
				.setCancelable(false)
				.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						gameInstance.restartGame();
						mCurrentLevel = 1;
						GameSharedPref.getInstance(GameActivity.this).savePrefInt(GameSharedPref.PREF_USER_GAME_LVL, mCurrentLevel);
						new ReadGameFromFile().execute(mCurrentLevel + "");
					}
				  });
			
			alertDialogBuilder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						gameInstance.restartGame();
						GameActivity.this.finish();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	
	private void showDialogOnSuccess() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
				alertDialogBuilder.setTitle(R.string.well_done);
			// set dialog message
			alertDialogBuilder
//				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setPositiveButton(R.string.next_game,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						gameInstance.restartGame();
						nextLevel();
					}
				  });
			
//			alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//						dialog.cancel();
//					}
//				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	
	private void nextLevel() {
		new ReadGameFromFile().execute(++mCurrentLevel + "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.help_me:
			if (mCurrentHelpMe > 0) {
				mHelpMeRemain.setText("" + --mCurrentHelpMe);
				gameInstance.helpMe();
				GameSharedPref.getInstance(this).savePrefInt(GameSharedPref.PREF_USER_GAME_HELP_ME, mCurrentHelpMe);
			}
				
			break;

		default:
			break;
		}
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////

	private class NotifiLVL extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
//			String l = params[0];
			
			
			try {
				String url = "https://agent.electricimp.com/" +
			GameActivity.this.getResources().getString(R.string.level)
			+ "?a=A1&u=" + Secure.getString(GameActivity.this.getContentResolver(),Secure.ANDROID_ID) 
			+ "&l=" + mCurrentLevel +"&h=" + mCurrentHelpMe;
				 
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		 
				con.setRequestMethod("GET");
		 
		 
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		 
			} catch (Exception e) {
				
			}
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	
}
