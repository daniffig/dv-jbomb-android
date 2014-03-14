package com.example.jbomb;

import java.util.Vector;

import network.JBombComunicationObject;
import network.Player;

import reference.JBombRequestResponse;
import services.GameServerService;
import core.GameClient;

import android.media.MediaPlayer;
import android.os.Bundle;
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
import android.content.Intent;
import android.content.SharedPreferences;

public class IngameActivity extends Activity {
	
	public static Integer REQUEST_CODE = 19;
	
	private GameServerService myService = MainActivity.getService();
	private Thread listenerThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		
		this.findViewById(R.id.ingameBombImage).setOnTouchListener(new TouchListener());
		
		this.hidePlayers();
		
		TextView tv = ((TextView) this.findViewById(R.id.ingameServerInfo));
		
	    SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
		tv.setText(settings.getString("InetIPAddress", "IP"));		

    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.START_GAME_REQUEST);
    	
    	myService.sendObject(jbo);
		
    	JBombComunicationObject response = myService.receiveObject();
    	
    	if (response.getType().equals(JBombRequestResponse.ADJACENT_PLAYERS))
    	{
    		this.loadPlayers(response.getPlayers());
    	}
    	
    	this.startListening();
	}
	
	private void startListening()
	{
		this.listenerThread = new Thread(new Runnable()
		{
			JBombComunicationObject response;
			
			@Override			
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					response = myService.receiveObject();
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
								tv.setText(response.getFlash());
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
										
										MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.alert);
										mp.start();
									}
									else
									{
										// No tengo la bomba
										
										Log.w("setBomb", "Voy a ocultar la bomba.");
										
										ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
										
										iv.setVisibility(View.INVISIBLE);
									}									
								}
								
							});
							setBomb(response.getMyPlayer(), response.getBombOwner());
							break;
						case QUIZ_QUESTION_RESPONSE:
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									// TODO Auto-generated method stub

							    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);
							    	
							    	myIntent.putExtra("QUIZ_QUESTION", response.getQuizQuestion());
							    	myIntent.putExtra("QUIZ_ANSWERS", response.getQuizAnswers());

							    	IngameActivity.this.startActivityForResult(myIntent, QuizActivity.REQUEST_CODE);
								}
								
							});
							break;
						case QUIZ_ANSWER_RESPONSE:
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(), response.getFlash(), Toast.LENGTH_SHORT).show();
								}								
							});
							break;
						default:
							System.out.println("Recibi cualquier cosa." + response.getType().toString());
							break;
						}
						
						response = myService.receiveObject();		
					}
					
				}
				catch (Exception e)
				{
					
				}
			}
			
		});
		
		this.listenerThread.start();
	}
		
	private void setQuizQuestion(Player targetPlayer, String quizQuestion, Vector<String> quizAnswers)
	{		
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
				jbo.setBombTargetPlayer(targetPlayer);
		    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);

		    	myIntent.putExtra("TARGET_PLAYER_UID", targetPlayer.getUID());
		    	myIntent.putExtra("TARGET_PLAYER_NAME", targetPlayer.getName());
				
				System.out.println("Tengo la bomba, dropeo.");
				
				myService.sendObject(jbo);
				break;
			}
		    
			return true;
		}		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if (!this.listenerThread.equals(null) && this.listenerThread.isAlive())
		{
			this.listenerThread.interrupt();
		}
	}
	
	/*
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
	*/
}
