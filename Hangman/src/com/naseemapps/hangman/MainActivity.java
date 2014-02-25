package com.naseemapps.hangman;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Game.intiGame(this);
		SoundPlayer.init(this);

		View startBtn = findViewById(R.id.start_game_btn);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SoundPlayer.playOpenSound();
				MainActivity.this.startActivity(new Intent(MainActivity.this,
						GameActivity.class));
			}
		});
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
