package com.example.jbomb;

import core.GameClient;
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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayersLoadingActivity extends Activity {
	
	private static GameServerService GameServerService;
    private static boolean isBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players_loading);
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);
    	
    	Integer progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();

		pb.setProgress(progressStatus);
		
		if (PlayersLoadingActivity.isBound)
		{            
    		PlayersLoadingActivity.this.onServiceConnected();			
		}
		else
		{			
	        this.startService(new Intent(this, GameServerService.class));	    	
	    	this.getApplicationContext().bindService(new Intent(this, GameServerService.class), mConnection, Context.BIND_AUTO_CREATE);			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.players_loading, menu);
		return true;
	}
	
	private void onServiceConnected()
	{		
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() {
				
				try
				{
					JBombComunicationObject response = GameServerService.receiveObject();
					
					ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);
					
					while (!response.getType().equals(JBombRequestResponse.MAX_PLAYERS_REACHED))
					{
						if (response.getType().equals(JBombRequestResponse.PLAYER_ADDED))
						{						
							GameClient.getInstance().setCurrentPlayers(response.getGamePlayInformation().getTotalPlayers());
			
					    	Integer progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();
							
							pb.setProgress(progressStatus);
						}
						
						response = GameServerService.receiveObject();
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
		
		t.start();		
	}    
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
            
    		PlayersLoadingActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

}
