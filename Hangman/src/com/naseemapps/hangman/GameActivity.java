package com.naseemapps.hangman;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements Game.Listener {

	Game gameInstance;
	int mCurrentLevel;
	ProgressDialog mProgressDialog;
	TextView mGameTitleTv;
	TextView mUserSolTv;
	
	private void initProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading....");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		initProgressDialog();
		
		mGameTitleTv = (TextView) findViewById(R.id.game_title_tv);
//		mGameTitleTv.setText("SDFSDF");
		mUserSolTv = (TextView) findViewById(R.id.game_current_solution_tv);
		
		new ReadGameFromFile().execute();
		
		try {
			gameInstance = Game.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
		
		gameInstance.setListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSuccess() {
		gameInstance.restartGame();		
	}

	@Override
	public void onFail() {
		gameInstance.restartGame();		
//		finish();		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
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
            return ReadFromfile("1.txt.java", GameActivity.this);
        }

        @Override
        protected void onPostExecute(String result) {
			Toast.makeText(GameActivity.this, result, Toast.LENGTH_SHORT).show();
			
			try {
				JSONObject jo = new JSONObject("{\"title\":\"ما هي الكلمة التي؟\", \"solution\":\"كلمة\", \"height\":100}");
				
				Toast.makeText(GameActivity.this, jo.getString("title"), Toast.LENGTH_SHORT).show();

				mGameTitleTv.setText(jo.getString("title"));
				mUserSolTv.setText(jo.getString("solution"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
        	mProgressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
        	mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
	
}
