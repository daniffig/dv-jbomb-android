package services;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import network.JBombComunicationObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class GameServerService extends Service {
	private Socket socket;
	private JBombComunicationObject communication_object;

	private final IBinder mBinder = new GameServerServiceBinder();
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("GAME_SERVER_SERVICE", "Received start id " + startId + ": " + intent);
        
        return START_STICKY;
    }
	
	@Override
	public void onCreate(){
		Log.i("GAME_SERVER_SERVICE", "El servicio fue creado");
		
        this.stablishConnection();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("GAME_SERVER_SERVICE", "I'm being binded to a client");
		
		return mBinder;
	}
	
	private void stablishConnection(){
		new Thread(new ConnectionThread()).start();
	}
	
	public void sendObject(JBombComunicationObject communicationObject){
		this.communication_object = communicationObject;
		new Thread(new sendObjectThread()).start();
	}
	
	public JBombComunicationObject receiveObject(){
		Thread t = new Thread(new receiveObjectThread());
		t.start();
		try{
			t.join();
		}catch(InterruptedException e){
			Log.e("GAME_SERVER_SERVICE", e.toString());
		}
		return this.communication_object;
	}
		
    public class GameServerServiceBinder extends Binder {
    	public GameServerService getService() {
            return GameServerService.this;
        }
    }
    
    
    public class ConnectionThread implements Runnable{

		@Override
		public void run() {
			try{
				socket = new Socket("192.168.1.32", 4321);
				
				Log.i("GAME_SERVER_SERVICE", "Establecida la conexi�n con el server");
			} 
			catch(UnknownHostException e1){
				e1.printStackTrace();
				Log.e("GAME_SERVER_SERVICE", "Uknown host");
			}
			catch(IOException e1){
				e1.printStackTrace();
				Log.e("GAME_SERVER_SERVICE", "Error de IO");
			}
			catch(NetworkOnMainThreadException e1){
				e1.printStackTrace();
				Log.e("GAME_SERVER_SERVICE", "SE ESTA CREANDO LA RED EN EL MAIN THREAD!");
			}
			
		}
    }
    
    public class sendObjectThread implements Runnable{
		public void run(){
			try
			{
				ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
				
				outToClient.writeObject(communication_object);
			}
			catch(Exception e)
			{
				Log.e("GAME_SERVER_SERVICE", "Fall� el envio del objeto - " + e.toString());
			}
		}
    }

	public class receiveObjectThread implements Runnable{
		
		@Override
		public void run(){
			try
			{
				ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
			
				communication_object =  (JBombComunicationObject) inFromClient.readObject();
			}
			catch(Exception e)
			{
				Log.e("GAME_SERVER_SERVICE", "Fall� la recepci�n del objeto - " + e.toString());
			}
		}

	}
		    	
}
