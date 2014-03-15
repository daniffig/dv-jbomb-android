package com.example.jbomb;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import core.GameClient;

import network.GameSettings;
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
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class NewGameActivity extends Activity {
	
	private GameServerService myService = MainActivity.getService();
	
    private Toast loading;
    
    private Map<Integer, String> topologyIDs = new HashMap<Integer, String>();
    private Map<Integer, String> quizIDs = new HashMap<Integer, String>();
    private Map<Integer, String> modeIDs = new HashMap<Integer, String>();
    private Map<Integer, String> roundDurationIDs = new HashMap<Integer, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		loading = Toast.makeText(this.getApplicationContext(), "Cargando juegos...", Toast.LENGTH_SHORT);
		loading.show();		

    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.GAME_SETTINGS_INFORMATION_REQUEST);

		myService.sendObject(jbo);
		
		if (myService.hasErrorState)
		{
			Toast.makeText(this.getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_SHORT).show();
			
			this.finish();
			
			return;
		}
	    
		loading.cancel();
		
    	JBombComunicationObject response = myService.receiveObject();
    	
    	this.setTopologyIDs(response.getGameSettingsInformation().getTopologies());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameTopologySpinner), this.getTopologyIDs());
    	
    	this.setQuizIDs(response.getGameSettingsInformation().getQuizzes());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameQuizSpinner), this.getQuizIDs());
    	
    	this.setModeIDs(response.getGameSettingsInformation().getModes());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameModeSpinner), this.getModeIDs());
    	
    	this.loadMaxPlayers((Spinner) this.findViewById(R.id.newGameMaxPlayersSpinner), response.getGameSettingsInformation().getMaxPlayersAllowed());
    	this.loadMaxRounds((Spinner) this.findViewById(R.id.newGameMaxRoundsSpinner), response.getGameSettingsInformation().getMaxRoundsAllowed());
    	
    	this.setRoundDurationIDs(response.getGameSettingsInformation().getRoundDurations());
    	this.loadSpinner((Spinner) this.findViewById(R.id.newGameRoundDurationSpinner), this.getRoundDurationIDs());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}


	public Map<Integer, String> getTopologyIDs() {
		return topologyIDs;
	}

	public void setTopologyIDs(Map<Integer, String> topologyIDs) {
		this.topologyIDs = topologyIDs;
	}

	public Map<Integer, String> getQuizIDs() {
		return quizIDs;
	}

	public void setQuizIDs(Map<Integer, String> quizIDs) {
		this.quizIDs = quizIDs;
	}

	public Map<Integer, String> getModeIDs() {
		return modeIDs;
	}

	public void setModeIDs(Map<Integer, String> modeIDs) {
		this.modeIDs = modeIDs;
	}

	public Map<Integer, String> getRoundDurationIDs() {
		return roundDurationIDs;
	}

	public void setRoundDurationIDs(Map<Integer, String> roundDurationIDs) {
		this.roundDurationIDs = roundDurationIDs;
	}
 
    public void createGame(View view)
    {
    	/*
    	 * TODO: Validaciones!
    	 */
    	
    	JBombComunicationObject jbo = new JBombComunicationObject();
    	jbo.setType(JBombRequestResponse.CREATE_GAME_REQUEST);
    	
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
    	
    	jbo.setGameSettings(gs);
    	
    	System.out.println("Voy a mandar: " + jbo.getGameSettings().getName());

    	myService.sendObject(jbo);
		
		Toast.makeText(NewGameActivity.this.getApplicationContext(), "Conectando con el servidor...", Toast.LENGTH_SHORT).show();
    	
    	System.out.println("Mande: " + jbo.getGameSettings().getName());
    	
    	JBombComunicationObject response = myService.receiveObject();
    	
    	if (response.getType().equals(JBombRequestResponse.CREATE_GAME_RESPONSE) && (response.getAvailableGames().size() > 0))
    	{
            SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);
    		
    		Player myPlayer = new Player();
    		
    		myPlayer.setName(settings.getString("PlayerName", "default"));
    		
    		jbo = new JBombComunicationObject();
    		
    		jbo.setType(JBombRequestResponse.JOIN_GAME_REQUEST);
    		jbo.setRequestedGameId(response.getAvailableGames().get(0).getUID());		
    		jbo.setMyPlayer(myPlayer);
    		
    		myService.sendObject(jbo);		    		
        	
        	response = myService.receiveObject();
        	
        	GameClient.getInstance().setCurrentPlayers(response.getGamePlayInformation().getTotalPlayers());
        	GameClient.getInstance().setMaxPlayers(response.getGamePlayInformation().getMaxPlayers());

        	Intent myIntent = new Intent(NewGameActivity.this, PlayersLoadingActivity.class);

        	NewGameActivity.this.startActivity(myIntent);
        	
        	NewGameActivity.this.finish();
    	}
    	else
    	{    		
    		Toast.makeText(NewGameActivity.this.getApplicationContext(), "Ocurrió un error al crear el juego.", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /*
     * FIXME: Método del mal
     */
    
    private Integer getKeyForValue(Map<Integer, String> map, Object value)
    {
    	for (Integer i : map.keySet())
    	{
    		if (value.equals(map.get(i))) return i;
    	}
    	
    	return null;
    }
}
