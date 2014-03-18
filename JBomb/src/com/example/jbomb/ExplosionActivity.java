package com.example.jbomb;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

public class ExplosionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		VideoView videoHolder = new VideoView(this);
		Uri video = Uri.parse("android.resource://" + getPackageName() + "/" 
		+ R.raw.bomb_explosion); //do not add any extension
		//if your file is named sherif.mp4 and placed in /raw
		//use R.raw.sherif
		videoHolder.setVideoURI(video);
		videoHolder.setMediaController(new MediaController(this));
		setContentView(videoHolder);
		videoHolder.start();
		
		MediaPlayer mediaPlayer = MediaPlayer.create(this,  R.raw.explosion);
		mediaPlayer.start();
		
		
		 Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(2000);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explosion, menu);
		return true;
	}

}
