package services;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.jbomb.ClientSettingsActivity;

import network.JBombComunicationObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class GameServerService extends Service {
	private static final String LOGCAT = "GAME_SERVER_SERVICE";
	private Socket socket;
	private JBombComunicationObject communication_object;

	private final IBinder myBinder = new GameServerServiceBinder();
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGCAT, "Received start id " + startId + ": " + intent);
        
        return START_STICKY;
    }
	
	@Override
	public void onCreate(){
		Log.i(LOGCAT, "El servicio fue creado");
		
        this.stablishConnection();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(LOGCAT, "I'm being binded to a client");
		
		return myBinder;
	}
	
	private void stablishConnection(){
		
		new Thread(new ConnectionThread()).start();
	}
	
	public void sendObject(JBombComunicationObject communicationObject){
		
		this.communication_object = communicationObject;
		
		new Thread(new SendObjectThread()).start();
	}
	
	public JBombComunicationObject receiveObject(){
		
		Log.i("RECEIVE_OBJECT", "Voy a recibir algo.");
		
		Thread t = new Thread(new ReceiveObjectThread());
		
		t.start();
		
		try{
			t.join();
		}catch(InterruptedException e){
			Log.e("RECEIVE_OBJECT", "Fallé donde siempre... - " + e.toString());
			
			t.interrupt();
		}

		return this.communication_object;
	}
		
    public class GameServerServiceBinder extends Binder {
    	public GameServerService getService() {
            return GameServerService.this;
        }
    }
    
    
    public class ConnectionThread implements Runnable {
    	
        SharedPreferences settings = getSharedPreferences(ClientSettingsActivity.PREFS_NAME, 0);

		@Override
		public void run() {
			try{
				socket = new Socket(settings.getString("InetIPAddress", null), settings.getInt("InetPort", 0));
				
				Log.i(LOGCAT, "Establecida la conexi�n con el server");
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
    }
    
    public class SendObjectThread implements Runnable {
		@Override
		public void run() {
			
			Log.i("SEND_OBJECT", communication_object.getType().toString());
			
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

	public class ReceiveObjectThread implements Runnable {
		
		@Override
		public void run(){
			try
			{
				ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
			
				communication_object =  (JBombComunicationObject) inFromClient.readObject();
			}
			catch(Exception e)
			{
				Log.e(LOGCAT, "Fallé al recibir un objeto. - " + e.toString());
			}
		}

	}
		    	
}
