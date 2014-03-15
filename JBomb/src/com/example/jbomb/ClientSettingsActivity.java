package com.example.jbomb;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ClientSettingsActivity extends Activity {
	
	public static Integer REQUEST_CODE = 23;

    public static final String PREFS_NAME = "ClientSettingsFile";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_settings);
		
		// Restore preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    
	    EditText PlayerNameEditText = (EditText) this.findViewById(R.id.PlayerNameEditText);
	    EditText InetIPAddressEditText = (EditText) this.findViewById(R.id.InetIPAddressEditText);
	    EditText InetPortEditText = (EditText) this.findViewById(R.id.InetPortEditText);
	    
	    PlayerNameEditText.setText(settings.getString("PlayerName", "default"));	    
	    InetIPAddressEditText.setText(settings.getString("InetIPAddress", "127.0.0.1"));
	    InetPortEditText.setText(String.valueOf(settings.getInt("InetPort", 4321)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
 
    public void saveClientSettings(View view)
    {
	    Editor settings = getSharedPreferences(PREFS_NAME, 0).edit();
	    
	    EditText PlayerNameEditText = (EditText) this.findViewById(R.id.PlayerNameEditText);
	    EditText InetIPAddressEditText = (EditText) this.findViewById(R.id.InetIPAddressEditText);
	    EditText InetPortEditText = (EditText) this.findViewById(R.id.InetPortEditText);
	    
	    settings.putString("PlayerName", PlayerNameEditText.getText().toString());
	    settings.putString("InetIPAddress", InetIPAddressEditText.getText().toString());    
	    settings.putInt("InetPort", Integer.parseInt(InetPortEditText.getText().toString()));
	    
	    settings.commit();
	    
	    Toast.makeText(this.getApplicationContext(), "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
	    
	    this.setResult(ClientSettingsActivity.REQUEST_CODE);
	    
	    System.out.println("Cerré la configuración.");
	    
    	this.finish();
    }
    
    public void closeClientSettings(View view)
    {    
	    this.setResult(Activity.RESULT_CANCELED);
	    
	    System.out.println("Cerré la configuración.");
	    
    	this.finish();    	
    }
}

