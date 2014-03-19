package com.example.jbomb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import network.Player;

import reference.JBombRequestResponse;
import services.GameServerService;
import network.JBombCommunicationObject;
import core.GameClient;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GamePositionsActivity extends Activity implements Observer {
	
	private GameServerService myService = MainActivity.getService();

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_positions);
		
		GameClient instance = GameClient.getInstance();
		
		ImageView roundResultImageView = (ImageView) findViewById(R.id.roundResultImageView);
		ImageButton nextActionImageButton = (ImageButton) findViewById(R.id.nextActionImageButton);
		
		if (instance.isLoser)
		{			
			roundResultImageView.setBackgroundResource(R.drawable.you_lose);
		}
		else
		{
			roundResultImageView.setBackgroundResource(R.drawable.ursafe);
		}	
		
		if (instance.isGameOver())
		{
			nextActionImageButton.setBackgroundResource(R.drawable.back);
		}
		else
		{
			nextActionImageButton.setBackgroundResource(R.drawable.next_round);
		}
		
		if (GameClient.getInstance().isWinner())
		{
			roundResultImageView.setBackgroundResource(R.drawable.you_win);
		}
		
		TextView roundCountTextView = (TextView) findViewById(R.id.roundCountTextView);
		
		roundCountTextView.setText(String.format("Ronda %s de %s", instance.getGamePlayInformation().getCurrentRound().toString(), instance.getGamePlayInformation().getMaxRounds().toString()));

		List<Player> players = new ArrayList<Player>();
		
		for (Player p : instance.getPlayers())
		{
			players.add(p);
		}
		
		Collections.sort(players, new Comparator<Player>()
		{
			@Override
			public int compare(Player arg0, Player arg1) {
				if (arg0.getGeneralPoints() > arg1.getGeneralPoints())
					return 1;
				if (arg0.getGeneralPoints() < arg1.getGeneralPoints())
					return -1;
				
				return 0;
			}			
		});

	    TableLayout gamePositionsTable = (TableLayout)findViewById(R.id.GamePositionsTableLayout);
	    
		for (int i = 0; i < players.size(); i++)
		{
			Player p = players.get(i);
			
		    TableRow playerRow = new TableRow(this);
		    
		    TextView positionText = new TextView(this);
		    TextView playerName = new TextView(this);
		    TextView roundPoints = new TextView(this);
		    TextView generalPoints = new TextView(this);
		    
		    positionText.setText(String.format("%s.", Integer.valueOf(i + 1)));
		    positionText.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Large);
		    
		    playerName.setText(p.getName());
		    playerName.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Large);
		    
		    roundPoints.setText(String.format("(%s)", p.getCurrentRoundPoints().toString()));
		    
		    generalPoints.setText(p.getGeneralPoints().toString());
		    generalPoints.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Large);
		    
		    playerRow.addView(positionText);
		    playerRow.addView(playerName);
		    playerRow.addView(roundPoints);
		    playerRow.addView(generalPoints);
		    
		    gamePositionsTable.addView(playerRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)); 
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_positions, menu);
		return true;
	}
	
	public void nextAction(View view)
	{
		if (GameClient.getInstance().isGameOver())
		{						
			myService.sendObject(new JBombCommunicationObject(JBombRequestResponse.CONNECTION_RESET_REQUEST));
			
	    	this.finish();		
		}
		else
		{
			this.joinNewRound(view);
		}
	}
	
	public void joinNewRound(View view)
	{				
    	GameClient.getInstance().setCurrentPlayers(0);

    	this.startActivity(new Intent(this, PlayersLoadingActivity.class));
    	
    	this.finish();
    	
		JBombCommunicationObject jbo = new JBombCommunicationObject();
		
		jbo.setType(JBombRequestResponse.START_NEW_ROUND_REQUEST);
		
		jbo.setRequestedGameId(GameClient.getInstance().getGamePlayInformation().getId());		
		jbo.setMyPlayer(GameClient.getInstance().getMyPlayer());
		
		myService.sendObject(jbo);		
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
