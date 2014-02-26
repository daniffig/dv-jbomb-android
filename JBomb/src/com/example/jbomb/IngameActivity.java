package com.example.jbomb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import network.JBombComunicationObject;
import network.Player;

import reference.JBombRequestResponse;
import services.GameServerService;
import services.GameServerService.GameServerServiceBinder;

import core.GameClient;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

public class IngameActivity extends Activity {
	
	private static GameServerService GameServerService;
    private static boolean isBound = false;
	
	public static int REQUEST_CODE = 17;
	
	public TextView timeLabel;
	private Long detonationTime;
	private Long startTimePoint;
	
	private SimpleDateFormat screenFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
	
	public static GameServerService getService()
	{
		return GameServerService;
	}
	
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
		@Override
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
		
		this.hidePlayers();
		
		TextView tv = ((TextView) this.findViewById(R.id.ingameServerInfo));
		
	    SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
		tv.setText(settings.getString("InetIPAddress", "IP"));
		
		if (IngameActivity.isBound)
		{            
			IngameActivity.this.onServiceConnected();			
		}
		else
		{			
	        //this.startService(new Intent(this, GameServerService.class));	    	
	    	this.getApplicationContext().bindService(new Intent(this, GameServerService.class), mConnection, Context.BIND_AUTO_CREATE);			
		}
		
		this.afterInit();
	}
	
	private void onServiceConnected()
	{
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.START_GAME_REQUEST);
    	
		GameServerService.sendObject(jbo);
		
    	JBombComunicationObject response = GameServerService.receiveObject();
    	
    	if (response.getType().equals(JBombRequestResponse.ADJACENT_PLAYERS))
    	{
    		this.loadPlayers(response.getPlayers());
    	}		

		Thread t = new Thread(new Runnable()
		{
			JBombComunicationObject response;
			
			@Override			
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					response = GameServerService.receiveObject();
					Log.w("setBomb", "Recibi algo del servidor magico feo antes.");

					TextView tv = (TextView) IngameActivity.this.findViewById(R.id.notificationText);
					//JBombRequestResponse.
					
					while (!response.getType().equals(JBombRequestResponse.BOMB_DETONATED_RESPONSE))
					{												
						runOnUiThread(new Runnable()
						{
							@Override
							public void run() {
								// TODO Auto-generated method stub

								TextView tv = (TextView) IngameActivity.this.findViewById(R.id.notificationText);
								tv.setText(response.getFlash().toString());
							}
							
						});
						
						System.out.println("Escribi: " + response.getFlash());
						
						switch(response.getType())
						{
						case BOMB_OWNER_RESPONSE:
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									Log.w("setBomb", "Soy " + response.getMyPlayer().getUID() + " y entre al setBomb.");
									
									if (response.getMyPlayer().getUID() == response.getBombOwner().getUID())
									{
										// Tengo la bomba!
										
										ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
										
										iv.setVisibility(View.VISIBLE);
										
										Toast.makeText(getApplicationContext(), "¡Tenés la bomba!", Toast.LENGTH_SHORT).show();
									}
									else
									{
										// No tengo la bomba
										
										ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
										
										iv.setVisibility(View.INVISIBLE);
									}									
								}
								
							});
							setBomb(response.getMyPlayer(), response.getBombOwner());
							break;
						case QUIZ_QUESTION_RESPONSE:
							setQuizQuestion(response.getBombTargetPlayer(), response.getQuizQuestion(), response.getQuizAnswers());
							break;
						default:
							System.out.println("Recibi cualquier cosa.");
							break;
						}
						Log.w("setBomb", "Voy a recibir.");
						
						response = GameServerService.receiveObject();		
						Log.w("setBomb", "Recibi algo del servidor magico feo.");
					}
					
				}
				catch (Exception e)
				{
					
				}
			}
			
		});
		
		t.start();
	}
		
	private void setQuizQuestion(Player targetPlayer, String quizQuestion, Vector<String> quizAnswers)
	{		
    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);

    	myIntent.putExtra("TARGET_PLAYER_UID", targetPlayer.getUID());
    	myIntent.putExtra("TARGET_PLAYER_NAME", targetPlayer.getName());
    	myIntent.putExtra("QUIZ_QUESTION", quizQuestion);
    	myIntent.putExtra("QUIZ_ANSWERS", quizAnswers);

    	IngameActivity.this.startActivityForResult(myIntent, QuizActivity.REQUEST_CODE);
	}
	
	private void setBomb(Player currentPlayer, Player bombOwner)
	{
	}
	
	private void hidePlayers() {
		this.findViewById(R.id.PlayerTop).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.PlayerRight).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.PlayerBottom).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.PlayerLeft).setVisibility(View.INVISIBLE);	
		this.findViewById(R.id.PlayerTopImage).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.PlayerRightImage).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.PlayerBottomImage).setVisibility(View.INVISIBLE);
		this.findViewById(R.id.PlayerLeftImage).setVisibility(View.INVISIBLE);		
		this.findViewById(R.id.ingameBombImage).setVisibility(View.INVISIBLE);
	}
	
	private void loadPlayers(Vector<Player> players)
	{
		int i = 0;
		
		for (Player player : players)
		{
			this.loadPlayer(player.getUID(), (TextView) this.findViewById(GameClient.getInstance().getIdForPlayer(i)), (ImageView) this.findViewById(GameClient.getInstance().getImageForPlayer(i)), player.getName());
			
			i++;
		}
	}
	
	private void loadPlayer(int UID, TextView tv, ImageView iv, String playerName)
	{		
		tv.setText(playerName);
		iv.setOnDragListener(new DragListener());
		iv.setId(UID);
		iv.setContentDescription(playerName);
		tv.setVisibility(View.VISIBLE);
		iv.setVisibility(View.VISIBLE);
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
				
				JBombComunicationObject jbo = new JBombComunicationObject();
				
				jbo.setType(JBombRequestResponse.SEND_BOMB_REQUEST);
				
				Player targetPlayer = new Player();
				
				targetPlayer.setUID(ingameImage.getId());
				targetPlayer.setName(String.valueOf(ingameImage.getContentDescription()));
				
				System.out.println("Tengo la bomba, dropeo.");
				
				GameServerService.sendObject(jbo);
				break;
			}
		    
			return true;
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		System.out.println("IngameActivity request: " + requestCode);
		
		TextView na = (TextView) this.findViewById(R.id.notificationText);
		
		if (requestCode == QuizActivity.REQUEST_CODE) {
			
			System.out.println("IngameActivity result: " + resultCode);
			
			if (resultCode == GameClient.CORRECT_ANSWER) {
				
				na.setText(R.string.correct_answer);
			}
			
			if (resultCode == GameClient.INCORRECT_ANSWER) {
				
				na.setText(R.string.incorrect_answer);
			}			
			
	        if (resultCode == RESULT_OK) {
	        	
	        	IngameActivity.this.finish();
	        }			
		}
	}
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	GameServerServiceBinder binder = (GameServerServiceBinder) service;
            GameServerService = binder.getService();
            isBound = true;
            
    		IngameActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
}
