package com.example.jbomb;

import core.GameClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings_ro = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
	    Editor settings_rw = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0).edit();
	    
	    settings_rw.putString("InetIPAddress", settings_ro.getString("InetIPAddress", "127.0.0.1"));    
	    settings_rw.putInt("InetPort", settings_ro.getInt("InetPort", 4321));
	    
	    settings_rw.commit();
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
 
    public void openGameSelection(View view)
    {
    	Intent myIntent = new Intent(MainActivity.this, GameSelectionActivity.class);
    	
    	MainActivity.this.startActivity(myIntent);
    }
 
    public void openGameplay(View view)
    {
    	Intent myIntent = new Intent(MainActivity.this, IngameActivity.class);
    	
    	MainActivity.this.startActivity(myIntent);
    }    

    
    @Override
	protected void onDestroy()
    {
    	System.out.println(1234123);
    	GameClient.destroyInstance();
    	
    	super.onDestroy();
    }
    
}
