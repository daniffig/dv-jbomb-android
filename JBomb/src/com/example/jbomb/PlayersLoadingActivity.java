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
import android.view.Menu;
import android.widget.ProgressBar;

public class PlayersLoadingActivity extends Activity {
	
	private static GameServerService GameServerService;
    private static boolean isBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players_loading);
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.loadingPlayersProgressBar);	

    	System.out.println("Actuales jugadores2: " + GameClient.getInstance().getCurrentPlayers());
    	System.out.println("Maximos jugadores2: " + GameClient.getInstance().getMaxPlayers());	
    	
    	Integer progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();

		pb.setProgress(progressStatus);
		
		System.out.println("Tengo: " + progressStatus + "%");
		
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
				
				//fede

				System.out.println("VOy a esperar.");
				JBombComunicationObject response = GameServerService.receiveObject();

				System.out.println("Recibi algo1.");
				GameClient instance = GameClient.getInstance();
		while (!response.getType().equals(JBombRequestResponse.FINISH_CONNECTION_REQUEST) && instance.getCurrentPlayers() < instance.getMaxPlayers())
		{
			if (response.getType().equals(JBombRequestResponse.PLAYER_ADDED))
			{
				System.out.println("Lei que había: " + response.getGamePlayInformation().getTotalPlayers() + " jugadores.");
				
				GameClient.getInstance().setCurrentPlayers(response.getGamePlayInformation().getTotalPlayers());

		    	Integer progressStatus = (GameClient.getInstance().getCurrentPlayers() * 100) / GameClient.getInstance().getMaxPlayers();
				
				pb.setProgress(progressStatus);
				
				System.out.println("Progreso: " + progressStatus + "%");
			}

			System.out.println("Recibi algo2.");
			
			response = GameServerService.receiveObject();
		}	
			}});		
		
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
