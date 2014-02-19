package com.example.jbomb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Intent;

public class QuizActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		TextView tv = (TextView)findViewById(R.id.quizNotice);  
		tv.setText("Debes responder la siguiente pregunta para pasar la bomba a " + this.getIntent().getExtras().getString("TARGET_PLAYER_NAME"));
		
		
		// Show the Up button in the action bar.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}
	
	public void openGameOver(View view)
	{
		RadioGroup answersRadioGroup = (RadioGroup) findViewById(R.id.answersRadioGroup);
		
    	Intent myIntent = new Intent(QuizActivity.this, GameOverActivity.class);
    	myIntent.putExtra("answerID", answersRadioGroup.getCheckedRadioButtonId());
    	
    	QuizActivity.this.startActivity(myIntent);
    	
    	QuizActivity.this.finish();
	}

}
