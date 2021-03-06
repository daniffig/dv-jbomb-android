package services;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;

import reference.JBombRequestResponse;

import com.example.jbomb.ClientSettingsActivity;

import core.GameClient;

import network.JBombCommunicationObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class GameServerService extends Service {
	
	public Boolean getIsLinked() {
		return isLinked;
	}

	public void setIsLinked(Boolean isLinked) {
		this.isLinked = isLinked;
	}

	private Boolean isLinked = false;
	private GameServerListener listener;
	
	public void suscribe(Observer observer)
	{
		this.getListener().addObserver(observer);
	}
	
	public void unsuscribe(Observer observer)
	{
		this.getListener().deleteObserver(observer);
	}
	
	public class GameServerServiceBinder extends Binder {
    	public GameServerService getService() {
            return GameServerService.this;
        }
    }

	public class SendObjectThread implements Runnable {
		@Override
		public void run() {			
			try
			{
				ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
				
				outToClient.writeObject(communication_object);
			}
			catch(Exception e)
			{
				Log.e(LOGCAT, "Fall� el envio del objeto - " + e.toString());
			}
		}
    }
	
	private static final String LOGCAT = "GAME_SERVER_SERVICE";
	
	private Socket socket;

	private JBombCommunicationObject communication_object;
	
	private final IBinder myBinder = new GameServerServiceBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(LOGCAT, "I'm being binded to a client");
		
		return myBinder;
	}
	
	@Override
	public void onCreate(){
		Log.i(LOGCAT, "El servicio fue creado");
		
		Thread t = new Thread(new Runnable(){
	    	
	        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);

			@Override
			public void run() {
				try{
					socket = new Socket(settings.getString("InetIPAddress", "127.0.0.1"), settings.getInt("InetPort", 4321));
					
					GameClient.printNotification(String.format("Me conecté con: %s:%s", settings.getString("InetIPAddress", "127.0.0.1"), settings.getInt("InetPort", 4321)));
					
					JBombCommunicationObject request = new JBombCommunicationObject(JBombRequestResponse.TRY_CONNECTION_REQUEST);
					
					ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
					
					outToClient.writeObject(request);
					
					JBombCommunicationObject response = (JBombCommunicationObject) (new ObjectInputStream(socket.getInputStream())).readObject();

					isLinked = (response.getType().equals(JBombRequestResponse.CONNECTION_ACCEPTED_RESPONSE));
				} 
				catch(UnknownHostException e1){
					e1.printStackTrace();
					Log.e(LOGCAT, "Uknown host");
				}
				catch(IOException e1){
					e1.printStackTrace();
					Log.e(LOGCAT, "Error de IO");
				}
				catch(NetworkOnMainThreadException e1){
					e1.printStackTrace();
					Log.e(LOGCAT, "SE ESTA CREANDO LA RED EN EL MAIN THREAD!");
				}
				catch(Exception e)
				{
					
				}
				
			}
		});
		
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        this.listener = new GameServerListener(this.socket);
	}
		
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGCAT, "Received start id " + startId + ": " + intent);
        
        if (this.isLinked)
        {
        	this.listener.start();
                
        	return START_STICKY;
        }
        else
        {            
            return START_REDELIVER_INTENT;
        }
    }
    
    public Boolean sendObject(JBombCommunicationObject communicationObject){
    	
    	GameClient.printNotification("Voy a enviar: " + communicationObject.getType().toString());
    	
		this.communication_object = communicationObject;
    	
    	if (this.isLinked)
    	{
    		(new Thread(new SendObjectThread())).start();
    		
    		return true;
    	}
    	else
    	{
    		GameClient.printNotification("El servicio no está conectado.");
    		
    		return false;
    	}
	}

	public GameServerListener getListener() {
		return listener;
	}

	public void setListener(GameServerListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		this.isLinked = false;
		
		this.listener.stop();
	}
		    	
}
