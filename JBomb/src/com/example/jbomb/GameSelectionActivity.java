package com.example.jbomb;

import java.util.Vector;

import core.GameClient;

import network.*;
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
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class GameSelectionActivity extends Activity {
	
	private static GameServerService GameServerService;
    private static boolean isBound = false;
    private Toast connecting;
    private Toast loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_selection);

        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
		
		this.connecting = Toast.makeText(this.getApplicationContext(), "Conectando con el servidor " + settings.getString("InetIPAddress", null) + "...", Toast.LENGTH_SHORT);
		this.connecting.show();
		
		if (GameSelectionActivity.isBound)
		{    		
    		loading = Toast.makeText(GameSelectionActivity.this.getApplicationContext(), "Cargando juegos...", Toast.LENGTH_SHORT);
    		loading.show();
            
            GameSelectionActivity.this.onServiceConnected();			
		}
		else
		{			
	        this.startService(new Intent(this, GameServerService.class));	    	
	    	this.getApplicationContext().bindService(new Intent(this, GameServerService.class), mConnection, Context.BIND_AUTO_CREATE);			
		}
	}
	
	// Una mentira.
	
	protected void onServiceConnected()
	{    	
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.GAME_LIST_REQUEST);

		GameServerService.sendObject(jbo);
	    
		this.loading.cancel();
    	
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
    	this.loadGames((RadioGroup) this.findViewById(R.id.availableGamesRadioGroup), response.getAvailableGames());    	
	}
	
	private void loadGames(RadioGroup availableGamesRadioGroup, Vector<GameInformation> availableGames)
	{
		for (GameInformation ag : availableGames)
		{			
			RadioButton rb = new RadioButton(this.getBaseContext());
			
			System.out.println("El ID del juego es: " + ag.getUID());
			
			rb.setId(ag.getUID());
			rb.setText(ag.getName() + " " + ag.getTotalPlayers() + "/" + ag.getMaxPlayers() + " | " + ag.getMode());
			
			availableGamesRadioGroup.addView(rb);			
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_selection, menu);
		return true;
	}
	
	public void connectToGame(View view)
	{
        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
        
		RadioGroup availableGamesRadioGroup = (RadioGroup) findViewById(R.id.availableGamesRadioGroup);
		
		JBombComunicationObject jbo = new JBombComunicationObject();
		
		jbo.setType(JBombRequestResponse.JOIN_GAME_REQUEST);

		jbo.setRequestedGameId(availableGamesRadioGroup.getCheckedRadioButtonId());
		jbo.setMyPlayerName(settings.getString("PlayerName", "default"));
		
		GameServerService.sendObject(jbo);		
		
		RadioButton selectedGame = (RadioButton)findViewById(availableGamesRadioGroup.getCheckedRadioButtonId());
		
		System.out.println("Me quiero conectar con: " + availableGamesRadioGroup.getCheckedRadioButtonId());
		
		Toast.makeText(GameSelectionActivity.this.getApplicationContext(), "Conectando con " + selectedGame.getText() + "...", Toast.LENGTH_SHORT).show();
    	
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
    	GameClient.getInstance().setCurrentPlayers(response.getGamePlayInformation().getTotalPlayers());
    	GameClient.getInstance().setMaxPlayers(response.getGamePlayInformation().getMaxPlayers());

    	Intent myIntent = new Intent(GameSelectionActivity.this, PlayersLoadingActivity.class);

    	GameSelectionActivity.this.startActivity(myIntent);
	}
    
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
            
            connecting.cancel();
    		
    		loading = Toast.makeText(GameSelectionActivity.this.getApplicationContext(), "Cargando juegos...", Toast.LENGTH_SHORT);
    		loading.show();
            
            GameSelectionActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
}
