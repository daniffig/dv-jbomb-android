package com.example.jbomb;

import java.util.ArrayList;
import java.util.List;

import network.JBombComunicationObject;
import network.QuizInformation;

import reference.JBombRequestResponse;
import services.GameServerService;
import services.GameServerService.GameServerServiceBinder;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class NewGameActivity extends Activity {

	private static GameServerService GameServerService;
    private static boolean isBound = false;
    private Toast loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		if (NewGameActivity.isBound)
		{    		
    		loading = Toast.makeText(NewGameActivity.this.getApplicationContext(), "Cargando juegos...", Toast.LENGTH_SHORT);
    		loading.show();
            
    		NewGameActivity.this.onServiceConnected();			
		}
		else
		{			
	        this.startService(new Intent(this, GameServerService.class));	    	
	    	this.getApplicationContext().bindService(new Intent(this, GameServerService.class), myConnection, Context.BIND_AUTO_CREATE);			
		}	
	}
	
	protected void onServiceConnected()
	{    	
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.GAME_LIST_REQUEST);

		GameServerService.sendObject(jbo);
	    
		this.loading.cancel();
    	
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
    	this.loadTopologies((Spinner) this.findViewById(R.id.newGameTopologySpinner));
    	this.loadQuizzes((Spinner) this.findViewById(R.id.newGameQuizSpinner));
	}
	
	private void loadTopologies(Spinner topologySpinner)
	{		
		List<String> topologyList = new ArrayList<String>();
		topologyList.add("Anillo");
		topologyList.add("Conexa");		

		ArrayAdapter<String> topologyAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, topologyList);
		
		topologyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		topologySpinner.setAdapter(topologyAdapter);		
	}
	
	private void loadQuizzes(Spinner quizSpinner)
	{		
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.QUIZ_LIST_REQUEST);

		GameServerService.sendObject(jbo);
	    
		this.loading.cancel();
    	
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
		List<String> quizList = new ArrayList<String>();
    	
    	for (QuizInformation qi : response.getAvailableQuizzes())
    	{
    		quizList.add(qi.getQuizTitle());
    	}    	

		ArrayAdapter<String> quizAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, quizList);
		
		quizAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		quizSpinner.setAdapter(quizAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}
    
    
    private ServiceConnection myConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
    		
    		loading = Toast.makeText(NewGameActivity.this.getApplicationContext(), "Cargando juegos...", Toast.LENGTH_SHORT);
    		loading.show();
            
    		NewGameActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

}
