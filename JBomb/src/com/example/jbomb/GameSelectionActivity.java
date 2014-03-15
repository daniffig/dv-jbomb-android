package com.example.jbomb;

import java.util.Vector;

import core.GameClient;

import network.*;
import reference.JBombRequestResponse;
import services.GameServerService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class GameSelectionActivity extends Activity {
	
	private GameServerService myService = MainActivity.getService();
	
    private Toast connecting;
    private Toast loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_selection);

        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
		
		connecting = Toast.makeText(this.getApplicationContext(), "Conectando con el servidor " + settings.getString("InetIPAddress", null) + "...", Toast.LENGTH_SHORT);
		connecting.show();		

		loading = Toast.makeText(GameSelectionActivity.this.getApplicationContext(), "Cargando juegos...", Toast.LENGTH_SHORT);
		loading.show();
		
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.GAME_LIST_REQUEST);

    	myService.sendObject(jbo);
		
		if (myService.hasErrorState)
		{
			Toast.makeText(this.getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_SHORT).show();
			
			this.finish();
			
			return;
		}
    	
    	JBombComunicationObject response = myService.receiveObject();
	    
		loading.cancel();
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
		
		Player myPlayer = new Player();
		
		myPlayer.setName(settings.getString("PlayerName", "default"));
		
		JBombComunicationObject jbo = new JBombComunicationObject();
		
		jbo.setType(JBombRequestResponse.JOIN_GAME_REQUEST);

		jbo.setRequestedGameId(availableGamesRadioGroup.getCheckedRadioButtonId());		
		jbo.setMyPlayer(myPlayer);
		
		myService.sendObject(jbo);		
		
		RadioButton selectedGame = (RadioButton)findViewById(availableGamesRadioGroup.getCheckedRadioButtonId());
		
		System.out.println("Me quiero conectar con: " + availableGamesRadioGroup.getCheckedRadioButtonId());
		
		Toast.makeText(GameSelectionActivity.this.getApplicationContext(), "Conectando con " + selectedGame.getText() + "...", Toast.LENGTH_SHORT).show();
    	
    	JBombComunicationObject response = myService.receiveObject();
    	
    	GameClient.getInstance().setCurrentPlayers(response.getGamePlayInformation().getTotalPlayers());
    	GameClient.getInstance().setMaxPlayers(response.getGamePlayInformation().getMaxPlayers());

    	Intent myIntent = new Intent(GameSelectionActivity.this, PlayersLoadingActivity.class);

    	GameSelectionActivity.this.startActivity(myIntent);
    	
    	this.finish();
	}
}
