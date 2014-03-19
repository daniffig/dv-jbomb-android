package com.example.jbomb;

import java.util.Observable;
import java.util.Observer;

import network.JBombCommunicationObject;
import network.Player;
import reference.JBombRequestResponse;
import services.GameServerService;
import core.GameClient;
import core.GameServer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.annotation.SuppressLint;
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

public class MainActivity extends Activity implements Observer {
	
	private static GameServerService myService;
	private static Toast toast;
	
	public static GameServerService getService()
	{
		return myService;
	}
	
	private Boolean isBound = false;
	
	private Intent myIntent;
	
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
	
	private MediaPlayer introSong;

    @SuppressLint("ShowToast")
	@Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        
        if (!GameClient.getInstance().appStarted)
        {
        	this.startActivity(new Intent(this, IntroActivity.class));
        	
        	GameClient.destroyInstance();        	
        	GameClient.getInstance().appStarted = true;
        	
            introSong = MediaPlayer.create(MainActivity.this, R.raw.intro_song);	
            introSong.setVolume(0.3f, 0.3f);
            
            introSong.start();
        }        
		
		this.myIntent = new Intent(this, GameServerService.class);
		
		toast = Toast.makeText(this.getApplicationContext(), "", Toast.LENGTH_SHORT);

        SharedPreferences settings_ro = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
	    Editor settings_rw = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0).edit();
	    
	    GameServer.InetIPAddress = settings_ro.getString("InetIPAddress", "127.0.0.1");
	    GameServer.InetPort = settings_ro.getInt("InetPort", 4321);

	    settings_rw.putString("PlayerName", settings_ro.getString("PlayerName", "default"));	    
	    settings_rw.putString("InetIPAddress", GameServer.InetIPAddress);    
	    settings_rw.putInt("InetPort", GameServer.InetPort);
	    
	    settings_rw.commit();

    	GameClient.getInstance().setMyPlayer(new Player(0, settings_ro.getString("PlayerName", "default")));
    	GameClient.getInstance().myPlayerName = settings_ro.getString("PlayerName", "default");
	    
	    this.bindService(myIntent, myConnection, Context.BIND_AUTO_CREATE);
	    this.startService(myIntent);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void stopIntroSong()
    {    	
    	if (this.introSong.isPlaying())
    	{
    		this.introSong.stop();
    	}
    }
 
    @Override
	protected void onDestroy()
    {
    	super.onDestroy();
    	
    	this.stopIntroSong();
    	this.introSong.release();
    	
    	if (this.isBound && myService.getIsLinked())
    	{        	
        	JBombCommunicationObject request = new JBombCommunicationObject(JBombRequestResponse.CLOSE_CONNECTION_REQUEST);
        	
        	myService.sendObject(request);
        	
        	this.unbindService(myConnection);    		
    	}
    	
    	GameClient.destroyInstance();
    	
    	this.stopService(myIntent);
    }
 
    public void openClientSettings(View view)
    {
    	this.stopIntroSong();
    	
    	Intent myIntent = new Intent(MainActivity.this, ClientSettingsActivity.class);

    	MainActivity.this.startActivityForResult(myIntent, ClientSettingsActivity.REQUEST_CODE);
    }
    
	public void openGameSelection(View view)
    {
    	this.stopIntroSong();
    	
    	if (this.isBound && myService.getIsLinked())
    	{
    		this.startActivity(new Intent(this, GameSelectionActivity.class));
    	}
    	else
    	{
    		showToast("Aún no se ha conectado con el servidor.");
    	}
    }
    
    public void openNewGame(View view)
    {
    	this.stopIntroSong();
    	
    	if (this.isBound && myService.getIsLinked())
    	{    	
    		this.startActivity(new Intent(this, NewGameActivity.class));
    	}
    	else
    	{
    		showToast("Aún no se ha conectado con el servidor.");
    	}
    }
    
    public void openExplosion(View view)
    {
		this.startActivity(new Intent(this, ExplosionActivity.class));
    }


	@Override
	public void update(Observable observable, Object data) {
		
		GameClient.printNotification("Me mandaron algo: " + ((JBombCommunicationObject) data).getType());
		
	}    
	
	public static void showToast(String message)
	{
		if (toast == null)
		{
			return;
		}
		
		toast.setText(message);
		
		toast.show();
	}
	
	public static void hideToast()
	{
		if (toast == null)
		{
			return;
		}
		
		toast.cancel();		
	}
}
