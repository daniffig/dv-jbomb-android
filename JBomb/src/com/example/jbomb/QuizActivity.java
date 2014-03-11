package com.example.jbomb;

import java.util.ArrayList;
import java.util.Collections;
import reference.JBombRequestResponse;
import services.GameServerService;
import services.GameServerService.GameServerServiceBinder;
import network.JBombComunicationObject;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class QuizActivity extends Activity {
	
	public static int REQUEST_CODE = 19;
	
	private static GameServerService GameServerService;
    private static boolean isBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		TextView tv = (TextView)findViewById(R.id.quizNotice);  
		tv.setText("Debes responder la siguiente pregunta para pasar la bomba a " + this.getIntent().getExtras().getString("TARGET_PLAYER_NAME"));
		
		TextView qq = (TextView) this.findViewById(R.id.quizQuestion);
		qq.setText(this.getIntent().getExtras().getString("QUIZ_QUESTION"));
		
		RadioGroup qqa = (RadioGroup) this.findViewById(R.id.quizQuestionAnswers);
		
		ArrayList<String> answers = this.getIntent().getExtras().getStringArrayList("QUIZ_ANSWERS");
		
		Collections.shuffle(answers);		
		
		for (String answer : answers)
		{			
			RadioButton rb = new RadioButton(this.getBaseContext());

			rb.setText(answer);
			
			qqa.addView(rb);
		}		
		
		if (QuizActivity.isBound)
		{            
		}
		else
		{			
	        //this.startService(new Intent(this, GameServerService.class));	    	
	    	this.getApplicationContext().bindService(new Intent(this, GameServerService.class), mConnection, Context.BIND_AUTO_CREATE);			
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
		
		RadioButton rb = (RadioButton) this.findViewById(qqa.getCheckedRadioButtonId());
		
		JBombComunicationObject jbo = new JBombComunicationObject();
		
		jbo.setType(JBombRequestResponse.QUIZ_ANSWER_REQUEST);
		jbo.setSelectedQuizAnswer(String.valueOf(rb.getText()));
		
		System.out.println("Te respondí: " + jbo.getSelectedQuizAnswer());
		
		Toast.makeText(this.getApplicationContext(), "Enviando respuesta al servidor...", Toast.LENGTH_SHORT).show();
		
		GameServerService.sendObject(jbo);
		
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
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

}
