package com.example.jbomb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import core.GameClient;

import reference.JBombRequestResponse;
import services.GameServerService;
import network.JBombComunicationObject;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity implements Observer {
	
	private GameServerService myService = MainActivity.getService();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		this.myService.suscribe(this);
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		this.myService.unsuscribe(this);
	}

	@Override
	public void update(Observable observable, final Object data) {
		// TODO Auto-generated method stub

		this.runOnUiThread(new Runnable()
		{			 
			JBombComunicationObject response = (JBombComunicationObject) data;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				GameClient.printNotification(String.format("Recibí desde el servidor: %s", response.getType().toString()));

				switch (response.getType())
				{
				case BOMB_DETONATED_RESPONSE:
					detonateBomb();
					break;
				case CLOSE_CONNECTION_RESPONSE:
					finish();
					break;
				default:
					// Si me mandan otra cosa, no me corresponde hacer nada.
					break;
				}								
			}			
		});
	}
	
	
	public void sendAnswer(View view) {

		RadioGroup qqa = (RadioGroup) findViewById(R.id.quizQuestionAnswers);
		
		if (qqa.getCheckedRadioButtonId() == -1)
		{			
			Toast.makeText(this.getApplicationContext(), "Debe seleccionar una opción.", Toast.LENGTH_SHORT).show();
			
			return;
		}
		
		RadioButton rb = (RadioButton) this.findViewById(qqa.getCheckedRadioButtonId());
		
		JBombComunicationObject jbo = new JBombComunicationObject();
		
		jbo.setType(JBombRequestResponse.QUIZ_ANSWER_REQUEST);
		jbo.setSelectedQuizAnswer(String.valueOf(rb.getText()));
		
		GameClient.printNotification(String.format("Envié una respuesta: %s", jbo.getSelectedQuizAnswer()));
		
		MainActivity.showToast("Enviando respuesta al servidor...");
		
		myService.sendObject(jbo);
		
		GameClient.printNotification("Acabo de mandar la respuesta.");
		
		this.finish();		
	}
	
	private void detonateBomb()
	{		
		this.finish();
	}
	
	/*
	
	public void openGameOver(View view)
	{
		RadioGroup answersRadioGroup = (RadioGroup) findViewById(R.id.quizQuestionAnswers);
		
    	Intent myIntent = new Intent(QuizActivity.this, GameOverActivity.class);
    	myIntent.putExtra("answerID", answersRadioGroup.getCheckedRadioButtonId());
    	
    	QuizActivity.this.startActivity(myIntent);
    	
    	QuizActivity.this.setResult(RESULT_OK);    	
    	QuizActivity.this.finish();
	}
	
	*/
}
