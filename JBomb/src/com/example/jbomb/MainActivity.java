package com.example.jbomb;

import com.example.jbomb.GameServerService.GameServerServiceBinder;
import com.example.jbomb.GameServerService;

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
    private GameServerService GameServerService;
    private boolean isBound = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        //TextView tv = (TextView)this.findViewById(R.id.Titulo);
        //tv.setTextSize(60 * getResources().getDisplayMetrics().density);
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
    	//startService(new Intent(this, GameServerService.class));
    	Intent i =new Intent(this, GameServerService.class);
        getApplicationContext().bindService(i, mConnection, Context.BIND_AUTO_CREATE);

        GameServerService.sendString("Hola Server!");
        
        TextView tv = (TextView)findViewById(R.id.serverNotice);  
        tv.setText(GameServerService.receiveString());
    }
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    
}
