package com.example.jbomb;

import core.GameClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
