package com.example.jbomb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import core.GameClient;
import network.JBombCommunicationObject;
import network.Player;
import reference.JBombRequestResponse;
import services.GameServerService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayersLoadingActivity extends Activity implements Observer {

	private GameServerService myService = MainActivity.getService();
	
	private ProgressBar progressBar;
	private Integer progressStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players_loading);
		
		this.myService.suscribe(this);
		
		this.progressBar = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);
		this.progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();

		this.progressBar.setProgress(this.progressStatus);
		
		// FIXME: El observer no llega a registrarse y no es notificado cuando llega un MAX_PLAYERS_REACHED si es el último, por eso hacemos este parche.
		
		JBombCommunicationObject lastResponse = this.myService.getListener().getLastResponse();
		
		if (lastResponse.getType().equals(JBombRequestResponse.GAME_RUNNABLE))
		{
			loadPlayers(lastResponse.getPlayers());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.players_loading, menu);
		return true;
	}
	
	public void startGame(View view)
	{
    	JBombCommunicationObject jbo = new JBombCommunicationObject();
    	jbo.setType(JBombRequestResponse.START_GAME_REQUEST);
    	
    	myService.sendObject(jbo);
    	
    	this.startActivity(new Intent(this, IngameActivity.class));	
    	
    	this.finish();
	}

	@Override
	public void update(Observable observable, final Object data) {
		// TODO Auto-generated method stub

		this.runOnUiThread(new Runnable()
		{			 
			JBombCommunicationObject response = (JBombCommunicationObject) data;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				switch (response.getType())
				{
				case PLAYER_ADDED:
					addPlayer(response.getGamePlayInformation().getTotalPlayers());
					break;			
				case GAME_RUNNABLE:
					loadPlayers(response.getPlayers());
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
	
	private void addPlayer(Integer totalPlayers)
	{
		GameClient.getInstance().setCurrentPlayers(totalPlayers);

    	this.progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();
		
		this.progressBar.setProgress(progressStatus);
	}
	
	private void loadPlayers(Collection<Player> adjacentPlayers)
	{
		Iterator<Integer> playerImageIDsIterator = GameClient.getInstance().getPlayerImageIDs().iterator();
		
		MainActivity.showToast("Voy a cargar: " + adjacentPlayers.size() + " jugadores.");		
		GameClient.printNotification("Voy a cargar: " + adjacentPlayers.size() + " jugadores.");
		
		for (Player p : adjacentPlayers)
		{			
			GameClient.getInstance().adjacentPlayers.put(playerImageIDsIterator.next(), p);
		}
		
		TextView tv = (TextView) PlayersLoadingActivity.this.findViewById(R.id.loadingPlayersTitle);
		ImageView iv = (ImageView) PlayersLoadingActivity.this.findViewById(R.id.loadingPlayersGetReady);
			
		tv.setVisibility(View.INVISIBLE);
		iv.setVisibility(View.INVISIBLE);					
		
		this.progressBar.setVisibility(View.INVISIBLE);
			
		ImageButton b = (ImageButton) PlayersLoadingActivity.this.findViewById(R.id.playButton);
			
		b.setVisibility(View.VISIBLE);		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		this.myService.unsuscribe(this);
	}

}
