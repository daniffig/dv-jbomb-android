package com.example.jbomb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;

public class GameOverActivity extends Activity {
	
	public static int REQUEST_CODE = 23;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_game_over);
		
		/*

		if(R.id.galeriaDeLaAcademiaRadioButton == this.getIntent().getIntExtra("answerID", 0))
		{
			TextView tv = (TextView)findViewById(R.id.GameResults);  
			tv.setTextSize(50);
			tv.setText("�GANASTE!");
			this.findViewById(R.id.winnerImage).setBackgroundResource(R.drawable.jbomb_winner);
		}
		else
		{
			this.findViewById(R.id.gameOverRelativeLayout).setBackgroundResource(R.drawable.jbomb_loser);
			
			TextView tv = (TextView)findViewById(R.id.GameResults);  
			tv.setText("�PERDISTE!");
			tv.setTextSize(50);
			tv.setTextColor(Color.WHITE);
		}
		
		*/
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}
	
	public void openMainWindow(View view)
	{				
    	GameOverActivity.this.finish();
	}
}
