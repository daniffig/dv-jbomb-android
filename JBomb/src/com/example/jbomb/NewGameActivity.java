package com.example.jbomb;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import core.GameClient;


import network.GamePlayInformation;
import network.GameSettings;
import network.GameSettingsInformation;
import network.JBombComunicationObject;
import network.Player;

import reference.JBombRequestResponse;
import services.GameServerService;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("UseSparseArrays")
public class NewGameActivity extends Activity implements Observer {
	
	private GameServerService myService = MainActivity.getService();
    
    private Map<Integer, String> topologyIDs = new HashMap<Integer, String>();
    private Map<Integer, String> quizIDs = new HashMap<Integer, String>();
    private Map<Integer, String> modeIDs = new HashMap<Integer, String>();
    private Map<Integer, String> roundDurationIDs = new HashMap<Integer, String>();

	public void createGame(View view)
    {
    	/*
    	 * TODO: Validaciones!
    	 */
    	
    	GameSettings gs = new GameSettings();
    	
    	EditText newGameNameEditText = (EditText) this.findViewById(R.id.newGameNameEditText);
    	Spinner newGameTopologySpinner = (Spinner) this.findViewById(R.id.newGameTopologySpinner);
    	Spinner newGameQuizSpinner = (Spinner) this.findViewById(R.id.newGameQuizSpinner);
    	Spinner newGameModeSpinner = (Spinner) this.findViewById(R.id.newGameModeSpinner);
    	Spinner newGameMaxPlayersSpinner = (Spinner) this.findViewById(R.id.newGameMaxPlayersSpinner);
    	Spinner newGameMaxRoundsSpinner = (Spinner) this.findViewById(R.id.newGameMaxRoundsSpinner);
    	Spinner newGameRoundDurationSpinner = (Spinner) this.findViewById(R.id.newGameRoundDurationSpinner);
    	
    	gs.setName(newGameNameEditText.getText().toString());
    	gs.setTopologyId(this.getKeyForValue(this.getTopologyIDs(), newGameTopologySpinner.getSelectedItem()));
    	gs.setQuizId(this.getKeyForValue(this.getQuizIDs(), newGameQuizSpinner.getSelectedItem()));
    	gs.setModeId(this.getKeyForValue(this.getModeIDs(), newGameModeSpinner.getSelectedItem()));
    	gs.setMaxPlayers((Integer)newGameMaxPlayersSpinner.getSelectedItem());
    	gs.setMaxRounds((Integer)newGameMaxRoundsSpinner.getSelectedItem());
    	gs.setRoundDurationId(this.getKeyForValue(this.getRoundDurationIDs(), newGameRoundDurationSpinner.getSelectedItem()));
    	
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.CREATE_GAME_REQUEST);
    	
    	jbo.setGameSettings(gs);

    	myService.sendObject(jbo);
    }
	
	private Integer getKeyForValue(Map<Integer, String> map, Object value)
    {
    	for (Integer i : map.keySet())
    	{
    		if (value.equals(map.get(i))) return i;
    	}
    	
    	return null;
    }
	
	public Map<Integer, String> getModeIDs() {
		return modeIDs;
	}
	
	public Map<Integer, String> getQuizIDs() {
		return quizIDs;
	}	

	public Map<Integer, String> getRoundDurationIDs() {
		return roundDurationIDs;
	}


	public Map<Integer, String> getTopologyIDs() {
		return topologyIDs;
	}

	private void loadMaxPlayers(Spinner maxPlayersSpinner, Integer maxPlayersAllowed)
	{
		Vector<Integer> maxPlayers = new Vector<Integer>();
		
		for (Integer i = 2; i <= maxPlayersAllowed; i++)
		{
			maxPlayers.add(i);
		}
		
		ArrayAdapter<Integer> maxPlayersAdapter = new ArrayAdapter<Integer>(this,
			android.R.layout.simple_spinner_item, maxPlayers);
		
		maxPlayersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		maxPlayersSpinner.setAdapter(maxPlayersAdapter);		
	}

	private void loadMaxRounds(Spinner maxRoundsSpinner, Integer maxRoundsAllowed)
	{
		Vector<Integer> maxRounds = new Vector<Integer>();
		
		for (Integer i = 2; i <= maxRoundsAllowed; i++)
		{
			maxRounds.add(i);
		}
		
		ArrayAdapter<Integer> maxRoundsAdapter = new ArrayAdapter<Integer>(this,
			android.R.layout.simple_spinner_item, maxRounds);
		
		maxRoundsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		maxRoundsSpinner.setAdapter(maxRoundsAdapter);		
	}

	private void loadSpinner(Spinner spinner, Map<Integer, String> map)
	{		
		Vector<String> vector = new Vector<String>();
		
		for (String s : map.values())
		{
			vector.add(s);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, vector);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		this.myService.suscribe(this);

    	JBombComunicationObject jbo = new JBombComunicationObject();
    	
    	jbo.setType(JBombRequestResponse.GAME_SETTINGS_INFORMATION_REQUEST);

		myService.sendObject(jbo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

	public void setModeIDs(Map<Integer, String> modeIDs) {
		this.modeIDs = modeIDs;
	}

	public void setQuizIDs(Map<Integer, String> quizIDs) {
		this.quizIDs = quizIDs;
	}
 
    public void setRoundDurationIDs(Map<Integer, String> roundDurationIDs) {
		this.roundDurationIDs = roundDurationIDs;
	}
    
    public void setTopologyIDs(Map<Integer, String> topologyIDs) {
		this.topologyIDs = topologyIDs;
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

				myService.stopAlert();

				switch (response.getType())
				{
				case GAME_SETTINGS_INFORMATION_RESPONSE:
					loadGameSettingsInformation(response.getGameSettingsInformation());
					break;
				case CREATE_GAME_RESPONSE:
					requestJoinMyGame(response.getAvailableGames().get(0).getUID());
					break;
				case GAMEPLAY_INFORMATION_RESPONSE:
			    	joinGame(response.getGamePlayInformation()); 
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
	
	private void loadGameSettingsInformation(GameSettingsInformation gameSettingsInformation)
	{    	
    	this.setTopologyIDs(gameSettingsInformation.getTopologies());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameTopologySpinner), this.getTopologyIDs());
    	
    	this.setQuizIDs(gameSettingsInformation.getQuizzes());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameQuizSpinner), this.getQuizIDs());
    	
    	this.setModeIDs(gameSettingsInformation.getModes());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameModeSpinner), this.getModeIDs());
    	
    	this.loadMaxPlayers((Spinner) this.findViewById(R.id.newGameMaxPlayersSpinner), gameSettingsInformation.getMaxPlayersAllowed());
    	this.loadMaxRounds((Spinner) this.findViewById(R.id.newGameMaxRoundsSpinner), gameSettingsInformation.getMaxRoundsAllowed());
    	
    	this.setRoundDurationIDs(gameSettingsInformation.getRoundDurations());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameRoundDurationSpinner), this.getRoundDurationIDs());
	}
	
	private void requestJoinMyGame(Integer myGameUID)
	{
        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
		
		Player myPlayer = new Player();
		
		myPlayer.setName(settings.getString("PlayerName", "default"));
		
		JBombComunicationObject jbo = new JBombComunicationObject();
		
		jbo.setType(JBombRequestResponse.JOIN_GAME_REQUEST);
		jbo.setRequestedGameId(myGameUID);		
		jbo.setMyPlayer(myPlayer);
		
		myService.sendObject(jbo);
	}
	
	private void joinGame(GamePlayInformation gpi)
	{
    	GameClient.getInstance().setCurrentPlayers(gpi.getTotalPlayers());
    	GameClient.getInstance().setMaxPlayers(gpi.getMaxPlayers());

    	Intent myIntent = new Intent(NewGameActivity.this, PlayersLoadingActivity.class);

    	NewGameActivity.this.startActivity(myIntent);
    	
    	NewGameActivity.this.finish();		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		this.myService.unsuscribe(this);
	}
}
