package com.example.jbomb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;

public class IngameActivity extends Activity {
	
	public static int REQUEST_CODE = 17;
	
	public TextView timeLabel;
	private Long detonationTime;
	private Long startTimePoint;
	
	private SimpleDateFormat screenFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
	
	private void afterInit() {
		timeLabel = (TextView) this.findViewById(R.id.Watch);
		this.detonationTime = 60000L;
		timeLabel.setText(screenFormat.format(this.detonationTime));
		this.startTimePoint = System.currentTimeMillis();//Long.valueOf(0);
		this.startCounting();
	}
	
	private Handler tasksHandler = new Handler();
	
	public void startCounting() {
		this.tasksHandler.removeCallbacks(timeTickRunnable);
		this.tasksHandler.postDelayed(timeTickRunnable, 100);
	}
	
	public String currentTimeString() {
		Long interval = detonationTime - (System.currentTimeMillis() - startTimePoint);
		final Date date = new Date(interval);
		return screenFormat.format(date);
	}
	
	private Runnable timeTickRunnable = new Runnable() {
		public void run() {
			IngameActivity.this.timeLabel.setText(currentTimeString());
			IngameActivity.this.tasksHandler.postDelayed(timeTickRunnable, 100);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		
		this.findViewById(R.id.ingameBombImage).setOnTouchListener(new TouchListener());
		this.findViewById(R.id.PlayerTopImage).setOnDragListener(new DragListener());
		this.findViewById(R.id.PlayerRightImage).setOnDragListener(new DragListener());
		this.findViewById(R.id.PlayerBottomImage).setOnDragListener(new DragListener());
		this.findViewById(R.id.PlayerLeftImage).setOnDragListener(new DragListener());
		
		TextView tv = ((TextView) this.findViewById(R.id.ingameServerInfo));
		
	    SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
		tv.setText(settings.getString("InetIPAddress", "IP"));
		
		this.afterInit();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingame, menu);
		return true;
	}

	
	public void openQuiz(View view)
	{
	}
	
	@SuppressLint("NewApi")
	class TouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			// TODO Auto-generated method stub
			
			switch (motionEvent.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				break;
			}
			
			return true;
		}
	}
	
	class DragListener implements OnDragListener
	{
		@Override
		public boolean onDrag(View v, DragEvent event) {	
			
			View ingameBomb = IngameActivity.this.findViewById(R.id.ingameBombImage);
			View ingameImage = IngameActivity.this.findViewById(v.getId());
						
			switch (event.getAction())
			{
			case DragEvent.ACTION_DRAG_EXITED:
				ingameBomb.setVisibility(View.VISIBLE);
				break;

			case DragEvent.ACTION_DROP:

		    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);
		    	
		    	myIntent.putExtra("TARGET_PLAYER_NAME", ingameImage.getContentDescription());

		    	IngameActivity.this.startActivityForResult(myIntent, QuizActivity.REQUEST_CODE);
				break;
			}
		    
			return true;
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		System.out.println("IngameActivity request: " + requestCode);
		
		if (requestCode == QuizActivity.REQUEST_CODE) {
			
			System.out.println("IngameActivity result: " + resultCode);
			
	        if (resultCode == RESULT_OK) {
	        	
	        	IngameActivity.this.finish();
	        }			
		}
	}
}
