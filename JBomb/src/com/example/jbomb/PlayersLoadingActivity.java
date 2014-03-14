package com.example.jbomb;

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

public class PlayersLoadingActivity extends Activity {

	private GameServerService myService = MainActivity.getService();
	private Thread listenerThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players_loading);
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);
    	
    	Integer progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();

		pb.setProgress(progressStatus);
		
		this.startListening();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.players_loading, menu);
		return true;
	}
	
	public void startGame(View view)
	{
    	Intent myIntent = new Intent(PlayersLoadingActivity.this, IngameActivity.class);

    	PlayersLoadingActivity.this.startActivity(myIntent);	
    	
    	this.finish();
	}
	
	private void startListening()
	{		
		this.listenerThread = new Thread(new Runnable() 
		{
			@Override
			public void run() {
				
				try
				{
					JBombComunicationObject response = myService.receiveObject();
					
					ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);
					
					while (!response.getType().equals(JBombRequestResponse.MAX_PLAYERS_REACHED))
					{
						if (response.getType().equals(JBombRequestResponse.PLAYER_ADDED))
						{						
							GameClient.getInstance().setCurrentPlayers(response.getGamePlayInformation().getTotalPlayers());
			
					    	Integer progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();
							
							pb.setProgress(progressStatus);
						}
						
						response = myService.receiveObject();
					}
				}
				catch (Exception e)
				{
					
				}
				
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						TextView tv = (TextView) PlayersLoadingActivity.this.findViewById(R.id.loadingPlayersTitle);
						ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);		
						ImageView iv = (ImageView) PlayersLoadingActivity.this.findViewById(R.id.loadingPlayersGetReady);
							
						tv.setVisibility(View.INVISIBLE);
						pb.setVisibility(View.INVISIBLE);
						iv.setVisibility(View.INVISIBLE);					
							
						ImageButton b = (ImageButton) PlayersLoadingActivity.this.findViewById(R.id.playButton);
							
						b.setVisibility(View.VISIBLE);	
						
					}
					
				});

			}
			
		});
		
		this.listenerThread.start();		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if (!this.listenerThread.equals(null) && this.listenerThread.isAlive())
		{
			this.listenerThread.interrupt();
		}
	}

}
