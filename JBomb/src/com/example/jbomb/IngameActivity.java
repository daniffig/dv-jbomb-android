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
					
					if (myService.hasErrorState)
					{
						runOnUiThread(new Runnable()
						{
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								Toast.makeText(IngameActivity.this.getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_SHORT).show();
							}
							
						});
					}
					
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
										
										Toast.makeText(getApplicationContext(), "¡Tenés la bomba!", Toast.LENGTH_SHORT).show();
										
										MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.alert);
										mp.start();
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
							
							GameClient.printNotification(String.format("Me enviaron una pregunta: %s", response.getQuizQuestion()));
							
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
							
							GameClient.printNotification(String.format("¿La respuesta era correcta?: %s", response.getCorrectAnswer()));
							
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(), response.getFlash(), Toast.LENGTH_SHORT).show();
								}								
							});
							break;
						case NOTICE_FLASH:
							GameClient.printNotification("Recibi una actualización de estado.");
							// Esto se maneja por defecto arriba, siempre, así que no hacemos nada.
							break;
						default:							
							GameClient.printNotification("Recibi cualquier cosa.");
							break;
						}
						

						GameClient.printNotification("Puedo fallar.");
						
						response = myService.receiveObject();
						GameClient.printNotification("No fallé. Tengo error: " + myService.hasErrorState);
						
						if (myService.hasErrorState)
						{
							runOnUiThread(new Runnable()
							{
								@Override
								public void run() {
									// TODO Auto-generated method stub

									Toast.makeText(IngameActivity.this.getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_SHORT).show();
								}
								
							});
							
							break;
						}
					}
					
				}
				catch (Exception e)
				{
					System.out.println("Se rompió todo.");
				}
			}
			
		});
		
		this.listenerThread.start();
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
		this.loadPlayer(players.get(0).getUID(), (TextView) this.findViewById(R.id.PlayerTop), (ImageView) this.findViewById(R.id.PlayerTopImage), players.get(0).getName());
		
		if (players.size() > 1)
		{
			this.loadPlayer(players.get(1).getUID(), (TextView) this.findViewById(R.id.PlayerRight), (ImageView) this.findViewById(R.id.PlayerRightImage), players.get(1).getName());
		}
		if (players.size() > 2)
		{
			this.loadPlayer(players.get(2).getUID(), (TextView) this.findViewById(R.id.PlayerBottom), (ImageView) this.findViewById(R.id.PlayerBottomImage), players.get(2).getName());
		}
		if (players.size() > 3)
		{
			this.loadPlayer(players.get(3).getUID(), (TextView) this.findViewById(R.id.PlayerLeft), (ImageView) this.findViewById(R.id.PlayerLeftImage), players.get(3).getName());
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
}
