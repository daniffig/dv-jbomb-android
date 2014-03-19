package com.example.jbomb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import core.GameClient;
import core.GameServer;

import network.GamePlayInformation;
import network.JBombCommunicationObject;
import network.Player;

import reference.JBombRequestResponse;
import services.GameServerService;

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
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;

public class IngameActivity extends Activity implements Observer {
	
	private TextView flash;
	private MediaPlayer alert;
	
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
				
				JBombCommunicationObject jbo = new JBombCommunicationObject();
				
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
		
		this.myService.suscribe(this);
		
		this.findViewById(R.id.ingameBombImage).setOnTouchListener(new TouchListener());
		
		this.hidePlayers();
		this.loadPlayers();
		this.alert = MediaPlayer.create(this, R.raw.alert);
		
		flash = ((TextView) this.findViewById(R.id.notificationText));
		
		TextView serverInfo = ((TextView) this.findViewById(R.id.ingameServerInfo));
	    
	    serverInfo.setText(GameServer.InetIPAddress);
		
		// FIXME: El observer no llega a registrarse y no es notificado cuando llega un MAX_PLAYERS_REACHED si es el último, por eso hacemos este parche.
	    JBombCommunicationObject lastResponse = this.myService.getListener().getLastResponse();
	    
		if (lastResponse.getType().equals(JBombRequestResponse.BOMB_OWNER_RESPONSE))
		{
			changeBombOwner(lastResponse.getBombOwner());
		}

		if (!(lastResponse.getFlash() == null))
		{			
			flash.setText(lastResponse.getFlash());
		}
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
		
		this.alert.release();
		
		this.myService.unsuscribe(this);
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
				case BOMB_OWNER_RESPONSE:
					changeBombOwner(response.getBombOwner()); 
					break;			
				case QUIZ_QUESTION_RESPONSE:
					openQuizQuestion(response.getQuizQuestion(), response.getQuizAnswers()); 
					break;			
				case BOMB_DETONATED_RESPONSE:
					detonateBomb(response.getLoser(), response.getPlayers(), response.getGamePlayInformation());
					break;
				case CLOSE_CONNECTION_RESPONSE:
					finish();
					break;
				default:
					// Si me mandan otra cosa, no me corresponde hacer nada.
					break;
				}		

				if (!(response.getFlash() == null))
				{			
					flash.setText(response.getFlash());
				}							
			}			
		});
	}
	
	private void loadPlayers()
	{
		Iterator<Integer> playerNameIDsIterator = GameClient.getInstance().getPlayerNameIDs().iterator();
		Iterator<Integer> playerImageIDsIterator = GameClient.getInstance().getPlayerImageIDs().iterator();
		
		for (Player p : GameClient.getInstance().adjacentPlayers.values())
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
	
	private void changeBombOwner(Player bombOwner)
	{				
		if (GameClient.getInstance().getMyPlayer().getUID().equals(bombOwner.getUID()))
		{				
			if (this.alert.isPlaying())
			{
				this.alert.stop();
				this.alert.release();
			}
			
			this.alert = MediaPlayer.create(this, R.raw.alert);			
			this.alert.start();
			
			GameClient.printNotification(String.format("¡Tengo la bomba!"));
			
			ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
			
			iv.setVisibility(View.VISIBLE);
		}
		else
		{									
			GameClient.printNotification(String.format("No tengo la bomba. :)"));
			
			ImageView iv = (ImageView) findViewById(R.id.ingameBombImage);
			
			iv.setVisibility(View.INVISIBLE);
		}							
	}
	
	private void openQuizQuestion(String quizQuestion, Vector<String> quizAnswers)
	{		
    	Intent myIntent = new Intent(IngameActivity.this, QuizActivity.class);
    	
    	myIntent.putExtra("QUIZ_QUESTION", quizQuestion);
    	myIntent.putExtra("QUIZ_ANSWERS", quizAnswers);
    	
    	this.startActivity(myIntent);
	}
	
	private void detonateBomb(Player loser, Collection<Player> players, GamePlayInformation gamePlayInformation)
	{					
		GameClient.getInstance().setPlayers(players);
		GameClient.getInstance().setGamePlayInformation(gamePlayInformation);
		GameClient.getInstance().isLoser = GameClient.getInstance().getMyPlayer().getUID().equals(loser.getUID());
		
		if (GameClient.getInstance().isLoser)
		{	    	
	    	this.startActivity(new Intent(this, ExplosionActivity.class));
		}
		else
		{
	    	this.startActivity(new Intent(this, GamePositionsActivity.class));
		}
		
		this.finish();
	}
}
