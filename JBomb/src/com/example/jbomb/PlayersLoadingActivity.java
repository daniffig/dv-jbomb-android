package com.example.jbomb;

import java.util.Observable;
import java.util.Observer;

import core.GameClient;
import network.JBombComunicationObject;
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
		if (this.myService.getListener().getLastResponse().getType().equals(JBombRequestResponse.MAX_PLAYERS_REACHED))
		{
			this.showPlayButton();
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
    	this.startActivity(new Intent(this, IngameActivity.class));	
    	
    	this.finish();
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
				case PLAYER_ADDED:
					addPlayer(response.getGamePlayInformation().getTotalPlayers());
					break;			
				case MAX_PLAYERS_REACHED:
					showPlayButton(); 
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
	
	private void showPlayButton()
	{		
		GameClient.printNotification("TENGO QUE MOSTRAR EL BOTON");
		
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
