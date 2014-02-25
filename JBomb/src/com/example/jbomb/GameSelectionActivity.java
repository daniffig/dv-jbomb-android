package com.example.jbomb;

import java.util.Vector;

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
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class GameSelectionActivity extends Activity {
	
	private GameServerService GameServerService;
    private boolean isBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_selection);
		
        this.startService(new Intent(this, GameServerService.class));
    	
    	this.getApplicationContext().bindService(new Intent(this, GameServerService.class), mConnection, Context.BIND_AUTO_CREATE);
    	
    	System.out.println(123);
    	
    	if (this.isBound)
    	{        	
    	
    	// ¿Qué hacemos mientras espera?
    	}
	}
	
	protected void onServiceConnected()
	{    	
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.GAME_LIST_REQUEST);

		GameServerService.sendObject(jbo);
    	
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
    	this.loadGames((RadioGroup) this.findViewById(R.id.availableGamesRadioGroup), response.getAvailableGames());		
	}
	
	private void loadGames(RadioGroup availableGamesRadioGroup, Vector<GameInformation> availableGames)
	{
		for (GameInformation ag : availableGames)
		{			
			RadioButton rb = new RadioButton(this.getBaseContext());
			
			rb.setId(availableGamesRadioGroup.getChildCount());
			rb.setText("Fede");
			
			availableGamesRadioGroup.addView(rb);			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_selection, menu);
		return true;
	}
    
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
            
            GameSelectionActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

}
