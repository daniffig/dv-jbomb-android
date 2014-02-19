package com.example.jbomb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class GameServerService extends Service {
	
	private NetworkThread networkThread = new NetworkThread();

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
	
	private void stablishConnection()
	{
		this.networkThread.start();
	}
	
	public void sendString(String s){
		try{
			this.networkThread.sendString(s);
		} catch(Exception e){
			Log.i("GAME_SERVER_SERVICE", "Fallo el envio");
		}
	}
	
	public String receiveString(){
		return this.networkThread.receiveString();
	}
		
    public class GameServerServiceBinder extends Binder {
    	GameServerService getService() {
            return GameServerService.this;
        }
    }
    
    
    public class NetworkThread extends Thread{

    	private Socket socket;
    	
		@Override
		public void run() {
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
				Log.i("GAME_SERVER_SERVICE", "Mande el string");
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
    	
    }
}
