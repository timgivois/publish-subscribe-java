import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements IServer {

	Hashtable<String, IClient> chatters = new Hashtable<String, IClient>();

	public Server() throws RemoteException  {}
	public synchronized void login(String name, IClient nc) throws RemoteException {
		chatters.put(name,nc);
		Enumeration entChater = chatters.elements();
		while( entChater.hasMoreElements() ){
			((IClient) entChater.nextElement()).receiveEnter(name);
		}
		System.out.println("Client " + name + " has logged in");
	}

	public synchronized void logout(String name) throws RemoteException {
		System.out.println("Client " + name + " has logged out");
		Enumeration entChater = chatters.elements();
		while( entChater.hasMoreElements()) {
			((IClient) entChater.nextElement()).receiveExit(name);
		}
		chatters.remove(name);
  }

	public synchronized void send(Message message) throws RemoteException {
		Enumeration entChater = chatters.elements();
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
