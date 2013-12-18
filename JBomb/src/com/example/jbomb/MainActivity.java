package com.example.jbomb;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button settingsButton = (Button) this.findViewById(R.id.settingsButton);
        
        settingsButton.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
                Toast.makeText(MainActivity.this, "Clicked on Button", Toast.LENGTH_LONG).show();
        	}
        });        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    
}
