package services;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observer;

import com.example.jbomb.ClientSettingsActivity;
import com.example.jbomb.R;

import core.GameClient;


import network.JBombComunicationObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class GameServerService extends Service {
	
	private GameServerListener listener;
	
	public void suscribe(Observer observer)
	{
		this.getListener().addObserver(observer);
	}
	
	public void unsuscribe(Observer observer)
	{
		this.getListener().deleteObserver(observer);
	}
	
	/*
	 * FIXME: Ver bien cómo se maneja el tema del audio más adelante, ahora nos está haciendo crashear la aplicación.
	 */
	
	private MediaPlayer alert;	
	private MediaPlayer explosion;
	
	public void playAlert()
	{		
		this.alert.start();		
	}
	
	public void stopAlert()
	{
		this.alert.stop();
	}
	
	public void playExplosion()
	{		
		this.alert.start();				
	}
	
	public void stopExplosion()
	{
		this.explosion.stop();	
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

	private JBombComunicationObject communication_object;
	
	private final IBinder myBinder = new GameServerServiceBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(LOGCAT, "I'm being binded to a client");
		
		return myBinder;
	}
	
	@Override
	public void onCreate(){
		Log.i(LOGCAT, "El servicio fue creado");

		this.alert = MediaPlayer.create(getApplicationContext(), R.raw.alert);
		this.explosion = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
		
		Thread t = new Thread(new Runnable(){
	    	
	        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);

			@Override
			public void run() {
				try{
					socket = new Socket(settings.getString("InetIPAddress", null), settings.getInt("InetPort", 0));
					
					GameClient.printNotification(String.format("Me conecté con: %s:%s", settings.getString("InetIPAddress", null), settings.getInt("InetPort", 0)));
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
        
        this.listener.start();
        
        return START_STICKY;
    }
    
    public void sendObject(JBombComunicationObject communicationObject){
    	
    	GameClient.printNotification("Voy a enviar: " + communicationObject.getType().toString());
		
		this.communication_object = communicationObject;
		
		(new Thread(new SendObjectThread())).start();
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
		
		this.alert.release();
		this.explosion.release();
		
		this.listener.stop();
	}
		    	
}
