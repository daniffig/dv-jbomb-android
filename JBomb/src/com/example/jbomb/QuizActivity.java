package com.example.jbomb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class QuizActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		TextView tv = (TextView)findViewById(R.id.quizNotice);  
		tv.setText("Para poder pasarle la bomba al jugador x responde la pregunta!");
		
		
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
	}

}
