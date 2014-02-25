package com.naseemapps.hangman;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ClickableLetter extends LinearLayout {

	public static ArrayList<TextView> gameKeyboard = new ArrayList<TextView>();

	public ClickableLetter(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.letter_key_view, this, true);

		TextView keyLetter = (TextView) getChildAt(0);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.LetterKey, 0, 0);
		String text = a.getString(R.styleable.LetterKey_text);
		keyLetter.setText(text);
		setClickable(true);

		try {
			keyLetter.setOnClickListener(Game.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}

		gameKeyboard.add(keyLetter);

	}

	public ClickableLetter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

}
