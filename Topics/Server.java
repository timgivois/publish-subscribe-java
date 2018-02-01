import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements IServer {

	Hashtable<String, IClient> clients = new Hashtable<String, IClient>();

	public Server() throws RemoteException  {}
	public synchronized void login(String clientName, IClient clientObject) throws RemoteException {
		clients.put(clientName, clientObject);
		Enumeration entChater = clients.elements();
		while( entChater.hasMoreElements() ){
			IClient nextClient = (IClient) entChater.nextElement();
			if (!nextClient.getName().equals(clientName)) {
				nextClient.receiveEnter(clientName);
			}
		}
		System.out.println("Client " + clientName + " has logged in");
	}

	public synchronized void logout(String name) throws RemoteException {
		System.out.println("Client " + name + " has logged out");
		Enumeration entChater = clients.elements();
		while( entChater.hasMoreElements()) {
			((IClient) entChater.nextElement()).receiveExit(name);
		}
		clients.remove(name);
  }

	public synchronized void send(Message message) throws RemoteException {
		Enumeration entChater = clients.elements();
		while( entChater.hasMoreElements() ) {
			((IClient) entChater.nextElement()).receiveMessage(message);
		}
		System.out.println("Message from client "+message.name+":\n"+message.text);
	}

	public static void main( String[] args ){
		String serverURL = "/Server";
		try {
			Server server = new Server();
			Naming.rebind(serverURL, server);
			System.out.println("Chat server ready");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
