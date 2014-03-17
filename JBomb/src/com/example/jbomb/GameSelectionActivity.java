package com.example.jbomb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import core.GameClient;


import network.*;
import reference.JBombRequestResponse;
import services.GameServerService;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class GameSelectionActivity extends Activity implements Observer {
	
	private GameServerService myService = MainActivity.getService();
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer, Integer> selectableGames = new HashMap<Integer, Integer>();

	public void requestJoinGame(View view)
	{        
		RadioGroup availableGamesRadioGroup = (RadioGroup) findViewById(R.id.availableGamesRadioGroup);
		
		if (availableGamesRadioGroup.getCheckedRadioButtonId() == -1)
		{
			MainActivity.showToast("No se ha seleccionado ningún juego.");
			
			return;
		}
		
		JBombComunicationObject jbo = new JBombComunicationObject();
		
		jbo.setType(JBombRequestResponse.JOIN_GAME_REQUEST);
		
		GameClient.printNotification("El jugador eligió " + availableGamesRadioGroup.getCheckedRadioButtonId());
		
		jbo.setRequestedGameId(availableGamesRadioGroup.getCheckedRadioButtonId());		
		jbo.setMyPlayer(GameClient.getInstance().getMyPlayer());
		
		MainActivity.showToast("Envie: " + jbo.getType().toString());
		
		myService.sendObject(jbo);
	}
	
	public void joinGame(Player myPlayer, GamePlayInformation gamePlayInformation)
	{		
		GameClient.getInstance().setMyPlayer(myPlayer);
    	GameClient.getInstance().setCurrentPlayers(gamePlayInformation.getTotalPlayers());
    	GameClient.getInstance().setMaxPlayers(gamePlayInformation.getMaxPlayers());

    	this.startActivity(new Intent(GameSelectionActivity.this, PlayersLoadingActivity.class));
    	
    	this.finish();		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_selection);
		
		this.myService.suscribe(this);
		
		this.requestAvailableGames();
	}
	
	private void requestAvailableGames()
	{
		MainActivity.showToast("Cargando juegos...");
		
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.GAME_LIST_REQUEST);

    	myService.sendObject(jbo);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_selection, menu);
		return true;
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
				
				switch (response.getType())
				{
				case GAME_LIST_RESPONSE:
			    	loadGames(response.getAvailableGames()); 
					break;
				case GAMEPLAY_INFORMATION_RESPONSE:
			    	joinGame(response.getMyPlayer(), response.getGamePlayInformation()); 
					break;
				case GAME_NOT_FOUND_ERROR:
					MainActivity.showToast("El juego solicitado no ha sido encontrado en el servidor.");
					break;
				case GAME_FULL_ERROR:
					MainActivity.showToast("El juego solicitado ha alcanzado el máximo número de jugadores.");
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
	
	private void loadGames(Collection<GameInformation> availableGames)
	{		
		MainActivity.hideToast();
		
		RadioGroup availableGamesRadioGroup = (RadioGroup) this.findViewById(R.id.availableGamesRadioGroup);
		
		for (GameInformation ag : availableGames)
		{			
			RadioButton rb = new RadioButton(this.getBaseContext());
			
			rb.setId(ag.getUID());
			rb.setText(ag.getName() + " " + ag.getTotalPlayers() + "/" + ag.getMaxPlayers() + " | " + ag.getMode());
			
			availableGamesRadioGroup.addView(rb);			
		}		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		this.myService.unsuscribe(this);
	}
}
