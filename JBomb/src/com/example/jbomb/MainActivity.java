package com.example.jbomb;

import services.GameServerService;
import core.GameClient;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static GameServerService myService;
	private Boolean isBound = false;	
	private Intent myIntent;
	
	public static GameServerService getService()
	{
		return myService;
	}
	
	private ServiceConnection myConnection = new ServiceConnection() {
		
        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            myService = ((GameServerService.GameServerServiceBinder) service).getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		this.myIntent = new Intent(this, GameServerService.class);

        SharedPreferences settings_ro = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
	    Editor settings_rw = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0).edit();

	    settings_rw.putString("PlayerName", settings_ro.getString("PlayerName", "default"));    
	    settings_rw.putString("InetIPAddress", settings_ro.getString("InetIPAddress", "127.0.0.1"));    
	    settings_rw.putInt("InetPort", settings_ro.getInt("InetPort", 4321));
	    
	    settings_rw.commit();
	    
	    this.bindService(myIntent, myConnection, Context.BIND_AUTO_CREATE);
	    this.startService(myIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    public void openNewGame(View view)
    {
    	if (this.isBound)
    	{
    		Intent myIntent = new Intent(MainActivity.this, NewGameActivity.class);
    	
    		MainActivity.this.startActivity(myIntent);
    	}
    	else
    	{
			Toast.makeText(getApplicationContext(), "Aún no se ha conectado con el servidor.", Toast.LENGTH_SHORT).show();
    	}
    }
 
    public void openGameSelection(View view)
    {
    	if (this.isBound)
    	{
    		Intent myIntent = new Intent(MainActivity.this, GameSelectionActivity.class);

    		MainActivity.this.startActivity(myIntent);
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), "Aún no se ha conectado con el servidor.", Toast.LENGTH_SHORT).show();
    	}
    }
 
    public void openClientSettings(View view)
    {
    	Intent myIntent = new Intent(MainActivity.this, ClientSettingsActivity.class);

    	MainActivity.this.startActivityForResult(myIntent, ClientSettingsActivity.REQUEST_CODE);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{		
		if (resultCode == ClientSettingsActivity.REQUEST_CODE)
		{	    				
			if (this.isBound)
			{
		    	this.unbindService(myConnection);
			}
			
	    	this.stopService(myIntent);  	
	    	
		    this.bindService(myIntent, myConnection, Context.BIND_AUTO_CREATE);
		    this.startService(myIntent);
		}
	}
    
    @Override
	protected void onDestroy()
    {
    	super.onDestroy();
    	
    	GameClient.destroyInstance();
    	
    	if (this.isBound)
    	{
        	this.unbindService(myConnection);    		
    	}
    	
    	this.stopService(myIntent);
    }    
}
