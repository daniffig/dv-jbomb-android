package com.example.jbomb;

import java.util.Observable;
import java.util.Observer;

import core.GameClient;

import network.JBombCommunicationObject;

import services.GameServerService;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.VideoView;

public class ExplosionActivity extends Activity implements Observer {
	
	private GameServerService myService = MainActivity.getService();
	private VideoView videoHolder; 
	private Vibrator v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.myService.suscribe(this);
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		
		videoHolder = new VideoView(this);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		if (metrics.heightPixels < 720)
		{
			videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bomb_explosion_480));
		}
		else
		{
			videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bomb_explosion_720));
		}
		
		v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		
		setContentView(videoHolder);
		
		videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				
				videoHolder.start();
				v.vibrate(videoHolder.getDuration());					
			}
		});
		
		videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub			
				
		    	startActivity(new Intent(ExplosionActivity.this, GamePositionsActivity.class));
		    	
		    	finish();
			}
			
		});		
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explosion, menu);
		return true;
	}

	@Override
	public void update(Observable observable, final Object data) {
		// TODO Auto-generated method stub
		
		this.runOnUiThread(new Runnable()
		{			 
			JBombCommunicationObject response = (JBombCommunicationObject) data;
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				switch (response.getType())
				{
				case PLAYER_ADDED:
					addPlayer(response.getGamePlayInformation().getTotalPlayers());
					break;			
				case CLOSE_CONNECTION_RESPONSE:
					finish();
					break;
				default:
					// Si me mandan otra cosa, no me corresponde hacer nada.
					break;
				}						
			}			
		});
	}
	
	private void addPlayer(Integer totalPlayers)
	{
		GameClient.getInstance().setCurrentPlayers(totalPlayers);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		this.myService.unsuscribe(this);
	}
}
