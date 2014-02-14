package com.naseemapps.hangman;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ClickableLetter extends LinearLayout {

	public ClickableLetter(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.letter_key_view, this, true);

//        TextView title = (TextView) getChildAt(0);
        
        
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.letter_key));
        setClickable(true);
//        setma
//        setPadding(10, 10, 10, 10);
//        setMinWidth(100);
//        setTextSize(12);
//        LayoutParams lp = (LayoutParams) getLayoutParams();
//        lp.width = 10;
//        setLayoutParams(lp);
        
        
    }
	
	public ClickableLetter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

}
