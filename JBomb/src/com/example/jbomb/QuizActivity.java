package com.example.jbomb;

import core.GameClient;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.Intent;

public class QuizActivity extends Activity {
	
	public static int REQUEST_CODE = 19;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		TextView tv = (TextView)findViewById(R.id.quizNotice);  
		tv.setText("Debes responder la siguiente pregunta para pasar la bomba a " + this.getIntent().getExtras().getString("TARGET_PLAYER_NAME"));
		
		TextView qq = (TextView) this.findViewById(R.id.quizQuestion);
		qq.setText(GameClient.getInstance().getQuizQuestion());
		
		RadioGroup qqa = (RadioGroup) this.findViewById(R.id.quizQuestionAnswers);
		
		for (String answer : GameClient.getInstance().getQuizQuestionAnswers()) {
			
			RadioButton rb = new RadioButton(this.getBaseContext());
			
			rb.setId(qqa.getChildCount());
			rb.setText(answer);
			
			qqa.addView(rb);
		}
			
		
		
		// Show the Up button in the action bar.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}
	
	public void sendAnswer(View view) {

		RadioGroup qqa = (RadioGroup) findViewById(R.id.quizQuestionAnswers);
		
		System.out.println("Id Respuesta: " + qqa.getCheckedRadioButtonId());
		
		if (GameClient.getInstance().isCorrectQuizQuestionAnswer(qqa.getCheckedRadioButtonId())) {
			
			this.setResult(GameClient.CORRECT_ANSWER);
		}
		else {
			
			this.setResult(GameClient.INCORRECT_ANSWER);
		}
		
		this.finish();		
	}
	
	public void openGameOver(View view)
	{
		RadioGroup answersRadioGroup = (RadioGroup) findViewById(R.id.quizQuestionAnswers);
		
    	Intent myIntent = new Intent(QuizActivity.this, GameOverActivity.class);
    	myIntent.putExtra("answerID", answersRadioGroup.getCheckedRadioButtonId());
    	
    	QuizActivity.this.startActivity(myIntent);
    	
    	QuizActivity.this.setResult(RESULT_OK);    	
    	QuizActivity.this.finish();
	}

}
