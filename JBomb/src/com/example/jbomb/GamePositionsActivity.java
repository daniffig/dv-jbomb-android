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
import network.JBombComunicationObject;
import core.GameClient;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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
		
		ImageView roundResultImageView = (ImageView) findViewById(R.id.roundResultImageView);
		
		if (GameClient.getInstance().isLoser)
		{			
			roundResultImageView.setBackgroundResource(R.drawable.you_lose);
		}
		else
		{
			roundResultImageView.setBackgroundResource(R.drawable.ursafe);
		}		
		
		TextView roundCountTextView = (TextView) findViewById(R.id.roundCountTextView);
		
		roundCountTextView.setText(String.format("Ronda %s de %s", GameClient.getInstance().getGamePlayInformation().getCurrentRound().toString(), GameClient.getInstance().getGamePlayInformation().getMaxRounds().toString()));

		List<Player> players = new ArrayList<Player>();
		
		for (Player p : GameClient.getInstance().getPlayers())
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
	    
		for (int i = 0; i <= players.size(); i++)
		{
			Player p = players.get(i + 1);
			
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
	
	public void joinNewRound(View view)
	{				
    	GameClient.getInstance().setCurrentPlayers(0);

    	this.startActivity(new Intent(this, PlayersLoadingActivity.class));
    	
    	this.finish();
    	
		JBombComunicationObject jbo = new JBombComunicationObject();
		
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
			JBombComunicationObject response = (JBombComunicationObject) data;
			
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
