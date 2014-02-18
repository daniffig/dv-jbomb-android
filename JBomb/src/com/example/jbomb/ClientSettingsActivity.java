package com.example.jbomb;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

public class ClientSettingsActivity extends Activity {

    public static final String PREFS_NAME = "ClientSettingsFile3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_settings);
		
		// Restore preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    
	    EditText InetIPAddressEditText = (EditText) this.findViewById(R.id.InetIPAddressEditText);
	    EditText InetPortEditText = (EditText) this.findViewById(R.id.InetPortEditText);
	    
	    InetIPAddressEditText.setText(settings.getString("InetIPAddress", "127.0.0.1"));
	    InetPortEditText.setText(String.valueOf(settings.getInt("InetPort", 4321)));
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
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
	    
	    EditText InetIPAddressEditText = (EditText) this.findViewById(R.id.InetIPAddressEditText);
	    EditText InetPortEditText = (EditText) this.findViewById(R.id.InetPortEditText);
	    
	    settings.putString("InetIPAddress", InetIPAddressEditText.getText().toString());    
	    settings.putInt("InetPort", Integer.parseInt(InetPortEditText.getText().toString()));
	    
	    settings.commit();
	    
	    Toast.makeText(this.getApplicationContext(), "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
	    
	    this.closeClientSettings(view);
    }
    
    public void closeClientSettings(View view)
    {
    	this.finish();
    }
    
    public void testServiceBind(View view)
    {
    	this.finish();
    }

}
