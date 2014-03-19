package com.example.jbomb;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

public class IntroActivity extends Activity {
	
	private VideoView videoHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_intro);
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		
		videoHolder = (VideoView) this.findViewById(R.id.introVideoView);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		if (metrics.heightPixels < 720)
		{
			videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dvorak_dev_intro_480));
		}
		else
		{
			videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dvorak_dev_intro_720));
		}
		
		videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				
				videoHolder.start();				
			}
		});
		
		videoHolder.setOnTouchListener(new View.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
		    	
		    	finish();
		    	
		    	return true;
			}
			
		});
		
		videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub			
		    	
		    	finish();
			}
			
		});	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro, menu);
		return true;
	}

}
