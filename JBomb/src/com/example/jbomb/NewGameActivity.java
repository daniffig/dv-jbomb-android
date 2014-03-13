package com.example.jbomb;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import network.JBombComunicationObject;

import reference.JBombRequestResponse;
import services.GameServerService;
import services.GameServerService.GameServerServiceBinder;

import android.os.Bundle;
import android.os.IBinder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class NewGameActivity extends Activity {

	private static GameServerService GameServerService;
    private static boolean isBound = false;
    private Toast loading;
    
    private Map<Integer, String> topologyIDs = new HashMap<Integer, String>();
    private Map<Integer, String> quizIDs = new HashMap<Integer, String>();
    private Map<Integer, String> modeIDs = new HashMap<Integer, String>();
    private Map<Integer, String> roundDurationIDs = new HashMap<Integer, String>();

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
    	
    	this.setTopologyIDs(response.getGameSettingsInformation().getTopologies());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameTopologySpinner), this.getTopologyIDs());
    	
    	this.setQuizIDs(response.getGameSettingsInformation().getQuizzes());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameQuizSpinner), this.getQuizIDs());
    	
    	this.setModeIDs(response.getGameSettingsInformation().getModes());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameModeSpinner), this.getModeIDs());
    	
    	this.loadMaxPlayers((Spinner) this.findViewById(R.id.newGameMaxPlayersSpinner), response.getGameSettingsInformation().getMaxPlayersAllowed());
    	this.loadMaxRounds((Spinner) this.findViewById(R.id.newGameMaxRoundsSpinner), response.getGameSettingsInformation().getMaxRoundsAllowed());
    	
    	this.setRoundDurationIDs(response.getGameSettingsInformation().getRoundDurations());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameRoundDurationSpinner), this.getRoundDurationIDs());
	}
	
	private void loadSpinner(Spinner spinner, Map<Integer, String> map)
	{		
		Vector<String> vector = new Vector<String>();
		
		for (String s : map.values())
		{
			vector.add(s);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, vector);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}


	public Map<Integer, String> getTopologyIDs() {
		return topologyIDs;
	}

	public void setTopologyIDs(Map<Integer, String> topologyIDs) {
		this.topologyIDs = topologyIDs;
	}

	public Map<Integer, String> getQuizIDs() {
		return quizIDs;
	}

	public void setQuizIDs(Map<Integer, String> quizIDs) {
		this.quizIDs = quizIDs;
	}

	public Map<Integer, String> getModeIDs() {
		return modeIDs;
	}

	public void setModeIDs(Map<Integer, String> modeIDs) {
		this.modeIDs = modeIDs;
	}

	public Map<Integer, String> getRoundDurationIDs() {
		return roundDurationIDs;
	}

	public void setRoundDurationIDs(Map<Integer, String> roundDurationIDs) {
		this.roundDurationIDs = roundDurationIDs;
	}
 
    public void createGame(View view)
    {
    	/*
    	 * TODO: Validaciones!
    	 */
    	
    	
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
