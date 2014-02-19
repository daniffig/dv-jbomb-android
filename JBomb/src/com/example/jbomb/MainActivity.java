package com.example.jbomb;

import core.GameClient;
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
import android.widget.TextView;

public class MainActivity extends Activity {

    //Prueba
    //private GameServerService GameServerService;
    private boolean isBound = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        //TextView tv = (TextView)this.findViewById(R.id.Titulo);
        //tv.setTextSize(60 * getResources().getDisplayMetrics().density);
        this.startService(new Intent(this, GameServerService.class));
    	
    	getApplicationContext().bindService(new Intent(this, GameServerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    public void openClientSettings(View view)
    {
    	Intent myIntent = new Intent(MainActivity.this, ClientSettingsActivity.class);

    	MainActivity.this.startActivity(myIntent);
    }
 
    public void openGameplay(View view)
    {
    	Intent myIntent = new Intent(MainActivity.this, IngameActivity.class);
    	
    	MainActivity.this.startActivity(myIntent);
    }
    
    public void testServiceBind(View view)
    {	
    	
    	if(this.isBound)
    	{/*
    		GameServerService.sendString("Hola Server!");
    
    		Log.i("GAME_SERVER_SERVICE",GameServerService.receiveString());
    		*/
    	}
    	else
    	{
    		Log.e("GAME_SERVER_SERVICE", "I'm not bounded yet!");
    	}
    }
    
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            //GameServerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    

    
    protected void onDestroy()
    {
    	System.out.println(1234123);
    	GameClient.destroyInstance();
    	
    	super.onDestroy();
    }
    
}
