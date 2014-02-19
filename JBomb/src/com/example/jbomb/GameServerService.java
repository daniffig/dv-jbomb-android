package com.example.jbomb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class GameServerService extends IntentService {
	private Socket socket;
	private final IBinder mBinder = new GameServerServiceBinder();
	
	public GameServerService() {
		super("GameServerService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("GAME_SERVER_SERVICE", "Estoy en el onHandleIntent");
		this.stablishConnection();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("GAME_SERVER_SERVICE", "Estoy en el onBind");
		return mBinder;
	}
	
	private void stablishConnection()
	{
		try{
			socket = new Socket("192.168.1.101", 4321);
			
			Log.i("GAME_SERVER_SERVICE", "Establecida la conexión con el server");
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
	
	public void sendString(String s){
	    try
		{
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
		
			outToClient.writeBytes(s + '\n');
		}
		catch(IOException e)
		{
			Log.e("GAME_SERVER_SERVICE", "fallo el envio del string");
		}
	}
	
	public String receiveString(){
		try
   		{
   			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   			  
   			return inFromServer.readLine();
   		}
   		catch(IOException e)
   		{
   			Log.e("GAME_SERVER_SERVICE", "fallo la recepción del string");
   			
   			return null;
  		}
	}
	
    public class GameServerServiceBinder extends Binder {
    	GameServerService getService() {
            return GameServerService.this;
        }
    }
}
