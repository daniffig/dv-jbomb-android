package services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.Observable;

import core.GameClient;

import network.JBombComunicationObject;

import reference.JBombRequestResponse;

public class GameServerListener extends Observable {
	
	private Socket socket;
	private Thread listenerThread;
	private JBombComunicationObject response;
	private JBombComunicationObject lastResponse;
	private Boolean stopSignal;
	
	public void start()
	{
		this.stopSignal = false;
		
		GameClient.printNotification("Voy a iniciar el thread.");
		
		this.listenerThread = new Thread(new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					
					response = (JBombComunicationObject) (new ObjectInputStream(socket.getInputStream())).readObject();	
					
					setLastResponse(response);		
					
			    	GameClient.printNotification("Recibí: " + response.getType().toString());
					
					while (!response.getType().equals(JBombRequestResponse.CLOSED_CONNECTION) && !stopSignal)
					{								    					
						setChanged();  	
						
						notifyObservers(response);
						
						setLastResponse(response);
						
						response = (JBombComunicationObject) (new ObjectInputStream(socket.getInputStream())).readObject();
						
				    	GameClient.printNotification("Recibí: " + response.getType().toString());
					}					
				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					GameClient.printNotification(e.toString());
					e.printStackTrace();
				} catch (IOException e) {
					GameClient.printNotification(e.toString());
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					GameClient.printNotification(e.toString());
					e.printStackTrace();
				}
			}			
		});
		
		this.listenerThread.start();
	}
	
	public void stop()
	{
		this.stopSignal = true;
	}
	
	public GameServerListener(Socket socket)
	{
		this.socket = socket;
	}

	public JBombComunicationObject getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(JBombComunicationObject lastResponse) {
		this.lastResponse = lastResponse;
	}
}
