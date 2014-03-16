package com.example.jbomb;

import java.util.Iterator;
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
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;

public class IngameActivity extends Activity {
	
	private Vector<Integer> playerNameIDs = new Vector<Integer>();
	private Vector<Integer> playerImageIDs = new Vector<Integer>();
	
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
				
				myService.sendObject(jbo);
				
				break;
			}
		    
			return true;
		}		
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
	
	private GameServerService myService = MainActivity.getService();
	private Thread listenerThread;
	
	private MediaPlayer alert;
	
	private MediaPlayer explosion;
	
	public QuizActivity quizActivity;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		
		this.playerNameIDs.add(R.id.PlayerTop);
		this.playerNameIDs.add(R.id.PlayerRight);
		this.playerNameIDs.add(R.id.PlayerRight);
		this.playerNameIDs.add(R.id.PlayerLeft);

		this.playerImageIDs.add(R.id.PlayerTopImage);
		this.playerImageIDs.add(R.id.PlayerRightImage);
		this.playerImageIDs.add(R.id.PlayerRightImage);
		this.playerImageIDs.add(R.id.PlayerLeftImage);
		
		this.findViewById(R.id.ingameBombImage).setOnTouchListener(new TouchListener());
		
		this.hidePlayers();
		
		alert = MediaPlayer.create(getApplicationContext(), R.raw.alert);
		explosion = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
		
		TextView serverInfo = ((TextView) this.findViewById(R.id.ingameServerInfo));
		
	    SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
	    
	    serverInfo.setText(settings.getString("InetIPAddress", "IP"));		

    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.START_GAME_REQUEST);
		
		GameClient.printNotification(String.format("Voy a pedir los jugadores al servidor. Envío: %s", jbo.getType().toString()));
    	
    	myService.sendObject(jbo);
		
    	JBombComunicationObject response = myService.receiveObject();
    	
    	if (response.getType().equals(JBombRequestResponse.ADJACENT_PLAYERS))
    	{
    		Iterator<Integer> playerNameIDsIterator = this.playerNameIDs.iterator();
    		Iterator<Integer> playerImageIDsIterator = this.playerImageIDs.iterator();
    		
    		for (Player p : response.getPlayers())
    		{
    			Integer nameID = playerNameIDsIterator.next();
    			Integer imageID = playerImageIDsIterator.next();
    			
    			TextView tv = (TextView) this.findViewById(nameID);
    			ImageView iv = (ImageView) this.findViewById(imageID);
    			
    			GameClient.getInstance().adjacentPlayers.put(imageID, p);
    			
    			tv.setText(p.getName());
    			iv.setOnDragListener(new DragListener());
    			iv.setId(p.getUID());
    			iv.setContentDescription(p.getName());
    			tv.setVisibility(View.VISIBLE);
    			iv.setVisibility(View.VISIBLE);
    		}
    	}
    	
    	this.startListening();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingame, menu);
		return true;
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if (!(this.listenerThread == null) && this.listenerThread.isAlive())
		{
			this.listenerThread.interrupt();
		}
		
		Log.e("INGAME_ACTIVITY", "Algo me cerró...");
	}
	
	public void openQuiz(View view)
	{
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
						
						GameClient.printNotification(String.format("Recibì desde el servidor: %s", response.getType().toString()));
						
						switch(response.getType())
						{
						case BOMB_OWNER_RESPONSE:
							
							GameClient.printNotification(String.format("La bomba la tiene: %s", response.getBombOwner().getName()));
							
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									
									if (response.getMyPlayer().getUID() == response.getBombOwner().getUID())
									{										
										GameClient.printNotification(String.format("¡Tengo la bomba!"));
										
										ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
										
										iv.setVisibility(View.VISIBLE);
										
										alert.start();
									}
									else
									{									
										GameClient.printNotification(String.format("No tengo la bomba. :)"));
										
										ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
										
										iv.setVisibility(View.INVISIBLE);
									}									
								}
								
							});
							break;
						case QUIZ_QUESTION_RESPONSE:
							
							if (alert.isPlaying())
							{
								alert.stop();
							}
							
							GameClient.printNotification(String.format("Me enviaron una pregunta: %s", response.getQuizQuestion()));
							
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									// TODO Auto-generated method stub

							    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);
							    	
							    	myIntent.putExtra("QUIZ_QUESTION", response.getQuizQuestion());
							    	myIntent.putExtra("QUIZ_ANSWERS", response.getQuizAnswers());
							    	
							    	IngameActivity.this.startActivity(myIntent);
								}
								
							});
							break;
						case QUIZ_ANSWER_RESPONSE:

							GameClient.printNotification(String.format("¿La respuesta era correcta?: %s", response.getCorrectAnswer()));
							
							break;
						case NOTICE_FLASH:
							GameClient.printNotification("Recibi una actualización de estado.");
							break;
						default:							
							GameClient.printNotification("Recibi cualquier cosa.");
							break;
						}
						
						response = myService.receiveObject();
					}
					
					if (response.getType().equals(JBombRequestResponse.BOMB_DETONATED_RESPONSE))
					{						
						GameClient.printNotification(String.format("Recibì desde el servidor: %s", response.getType().toString()));
						GameClient.printNotification("¡Explotó la bomba!");
						
						explosion.start();
						
						runOnUiThread(new Runnable()
						{
							@SuppressLint("NewApi")
							@Override
							public void run() {
								
								// FIXME: Ver cómo se arregla esto...
								
								/*
								if (!(quizActivity == null))
								{
									quizActivity.finish();
								}
								*/
								
								/*
								for (Integer playerImageID : GameClient.getInstance().adjacentPlayers.keySet())
								{
									((TextView) IngameActivity.this.findViewById(playerImageID)).setVisibility(View.INVISIBLE);
								}
								*/

								TextView tv = (TextView) IngameActivity.this.findViewById(R.id.notificationText);
								tv.setText(response.getFlash());							
								
								ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
								
								iv.setImageResource(R.drawable.explosion);
								iv.setVisibility(View.VISIBLE);								
								iv.setOnTouchListener(null);
							}
							
						});
					}
					
				}
				catch (Exception e)
				{
					Log.e("INGAME_ACTIVITY_LISTENER", "Ocurrió un error. - " + e.toString());
				}
			}
			
		});
		
		this.listenerThread.start();
	}
	
	/*
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if (!this.listenerThread.equals(null) && this.listenerThread.isAlive())
		{
			this.listenerThread.interrupt();
		}
	}
	*/
}
