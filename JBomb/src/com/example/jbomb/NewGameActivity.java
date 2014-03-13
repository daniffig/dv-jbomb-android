package com.example.jbomb;

import java.util.Vector;

import network.JBombComunicationObject;

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
    	jbo.setType(JBombRequestResponse.GAME_SETTINGS_INFORMATION_REQUEST);

		GameServerService.sendObject(jbo);
	    
		this.loading.cancel();
		
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
    	this.loadTopologies((Spinner) this.findViewById(R.id.newGameTopologySpinner), response.getGameSettingsInformation().getTopologies());
    	this.loadQuizzes((Spinner) this.findViewById(R.id.newGameQuizSpinner), response.getGameSettingsInformation().getQuizzes());
    	this.loadModes((Spinner) this.findViewById(R.id.newGameModeSpinner), response.getGameSettingsInformation().getModes());
    	this.loadMaxPlayers((Spinner) this.findViewById(R.id.newGameMaxPlayersSpinner), response.getGameSettingsInformation().getMaxPlayersAllowed());
    	this.loadMaxRounds((Spinner) this.findViewById(R.id.newGameMaxRoundsSpinner), response.getGameSettingsInformation().getMaxRoundsAllowed());
    	this.loadRoundDurations((Spinner) this.findViewById(R.id.newGameRoundDurationSpinner), response.getGameSettingsInformation().getRoundDurations());
	}
	
	private void loadTopologies(Spinner topologySpinner, Vector<String> topologies)
	{
		ArrayAdapter<String> topologyAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, topologies);
		
		topologyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		topologySpinner.setAdapter(topologyAdapter);		
	}
	
	private void loadQuizzes(Spinner quizSpinner, Vector<String> quizzes)
	{
		ArrayAdapter<String> quizAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, quizzes);
		
		quizAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		quizSpinner.setAdapter(quizAdapter);		
	}
	
	private void loadModes(Spinner modeSpinner, Vector<String> modes)
	{
		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, modes);
		
		modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		modeSpinner.setAdapter(modeAdapter);		
	}
	
	private void loadMaxRounds(Spinner maxRoundsSpinner, Integer maxRoundsAllowed)
	{
		Vector<Integer> maxRounds = new Vector<Integer>();
		
		for (Integer i = 2; i <= maxRoundsAllowed; i++)
		{
			maxRounds.add(i);
		}
		
		ArrayAdapter<Integer> maxRoundsAdapter = new ArrayAdapter<Integer>(this,
			android.R.layout.simple_spinner_item, maxRounds);
		
		maxRoundsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		maxRoundsSpinner.setAdapter(maxRoundsAdapter);		
	}
	
	private void loadMaxPlayers(Spinner maxPlayersSpinner, Integer maxPlayersAllowed)
	{
		Vector<Integer> maxPlayers = new Vector<Integer>();
		
		for (Integer i = 2; i <= maxPlayersAllowed; i++)
		{
			maxPlayers.add(i);
		}
		
		ArrayAdapter<Integer> maxPlayersAdapter = new ArrayAdapter<Integer>(this,
			android.R.layout.simple_spinner_item, maxPlayers);
		
		maxPlayersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		maxPlayersSpinner.setAdapter(maxPlayersAdapter);		
	}
	
	private void loadRoundDurations(Spinner roundDurationSpinner, Vector<String> roundDurations)
	{
		ArrayAdapter<String> roundDurationAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, roundDurations);
		
		roundDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		roundDurationSpinner.setAdapter(roundDurationAdapter);		
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
