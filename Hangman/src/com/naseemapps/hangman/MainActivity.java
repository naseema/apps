package com.naseemapps.hangman;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		Game.intiGame(this);
		
		Button startBtn = (Button) findViewById(R.id.start_game_btn);
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				MainActivity.this.startActivity(new Intent(MainActivity.this, GameActivity.class));
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
