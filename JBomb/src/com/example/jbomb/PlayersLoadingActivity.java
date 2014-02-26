package com.example.jbomb;

import core.GameClient;
import network.JBombComunicationObject;
import reference.JBombRequestResponse;
import services.GameServerService;
import services.GameServerService.GameServerServiceBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PlayersLoadingActivity extends Activity {
	
	private static GameServerService GameServerService;
    private static boolean isBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players_loading);
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);		

		GameClient instance = GameClient.getInstance();
		pb.setProgress((int) (instance.getCurrentPlayers() / instance.getMaxPlayers()));
		
		System.out.println("Tengo: " + instance.getCurrentPlayers() / instance.getMaxPlayers());
		
		System.out.println("Bind? " + PlayersLoadingActivity.isBound);
		
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
		final ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);

		
		System.out.println("Entre aca, al loading.");
		

		

		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {

				JBombComunicationObject response = GameServerService.receiveObject();
				GameClient instance = GameClient.getInstance();
		while (!response.getType().equals(JBombRequestResponse.FINISH_CONNECTION_REQUEST) && instance.getCurrentPlayers() < instance.getMaxPlayers())
		{
			if (response.getType().equals(JBombRequestResponse.PLAYER_ADDED))
			{
				System.out.println("Lei que había: " + response.getGamePlayInformation().getTotalPlayers() + " jugadores.");
				
				instance.setCurrentPlayers(instance.getCurrentPlayers() + 1);
				
				pb.setProgress((int) (instance.getCurrentPlayers() / instance.getMaxPlayers()));
			}
			
			response = GameServerService.receiveObject();
		}	
			}});		
		
			t.start();
		
		/*
		
		

				
				System.out.println("Entre al thread.");
				
				GameClient instance = GameClient.getInstance();
				
				JBombComunicationObject response = GameServerService.receiveObject();
				
				while (!response.getType().equals(JBombRequestResponse.FINISH_CONNECTION_REQUEST))
				{
					try
					{
						if (response.getType().equals(JBombRequestResponse.PLAYER_ADDED))
						{
							pb.setProgress((int) (instance.getCurrentPlayers() / instance.getMaxPlayers()));
							
							instance.setCurrentPlayers(instance.getCurrentPlayers() + 1);
						}
						
						System.out.println("Porcentaje: " + (int) (instance.getCurrentPlayers() / instance.getMaxPlayers()));
						
						response = GameServerService.receiveObject();
					}
					catch (Exception e)
					{
						System.out.println(e.toString());
					}
				}				
			}			
		});		
		
		t.start();
		
		*/
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
