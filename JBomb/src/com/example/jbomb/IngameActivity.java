package com.example.jbomb;

import android.os.Bundle;
import android.app.Activity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.Layout;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class IngameActivity extends Activity {
	
	private android.widget.RelativeLayout.LayoutParams layoutParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		
		this.findViewById(R.id.ingameBombImage).setOnTouchListener(new TouchListener());
		this.findViewById(R.id.ingamePlayerTopImage).setOnDragListener(new DragListener());
		this.findViewById(R.id.ingamePlayerRightImage).setOnDragListener(new DragListener());
		this.findViewById(R.id.ingamePlayerBottomImage).setOnDragListener(new DragListener());
		this.findViewById(R.id.ingamePlayerLeftImage).setOnDragListener(new DragListener());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingame, menu);
		return true;
	}

	
	public void openQuiz(View view)
	{
    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);

    	IngameActivity.this.startActivity(myIntent);
	}
	
	@SuppressLint("NewApi")
	class TouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			// TODO Auto-generated method stub
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				//view.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}
	
	class DragListener implements OnDragListener
	{
		@Override
		public boolean onDrag(View v, DragEvent event) {	
			
			switch (event.getAction())
			{
			case DragEvent.ACTION_DRAG_ENTERED:

			    Toast.makeText(IngameActivity.this.getApplicationContext(), "Entre", Toast.LENGTH_SHORT).show();		
				break;
			
			case DragEvent.ACTION_DRAG_EXITED:
			    Toast.makeText(IngameActivity.this.getApplicationContext(), "Sali", Toast.LENGTH_SHORT).show();	
				break;
				
			case DragEvent.ACTION_DROP:
			    Toast.makeText(IngameActivity.this.getApplicationContext(), "Solte", Toast.LENGTH_SHORT).show();				
			break;
			}
		    
			return true;
		}		
	}
}
