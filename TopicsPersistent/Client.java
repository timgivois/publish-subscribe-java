import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements IClient {

	private String name;
	private IServer server;
	private String serverURL;
	public ArrayList<String> subscribedTopics;

	public Client( String name, String url ) throws RemoteException {
		this.name = name;
		serverURL = url;
		this.subscribedTopics = new ArrayList<String>(); // Subscribed topics of this client
		connect();
	}

	private void connect() { // Connect and identify client
		try {
			server=(IServer) Naming.lookup("rmi://"+serverURL+"/Server");
			System.out.println("Successfully connected");
			server.login(this);
			System.out.println("Successfully identified");
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}

	private void publishIn(String text, String topic) { // Publish in a topic
		try {
			server.publish(new Message(name, topic, text)); // publish a message object
		}
		catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

	private void subscribeTo(String topic) { // Subscribe to a topic
		subscribedTopics.add(topic); // Save it locally
		try {
			server.subscribe(this, topic); // Ask the server to subscribe
		}
		catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

	private void unsubscribeFrom(String topic) { // unsubscribe from a topic
		subscribedTopics.remove(topic); // Delete locally
		try {
			server.unsubscribe(this, topic); // Tell the server to unsubscribe
		}
		catch( RemoteException e ) {
			e.printStackTrace();
		}
	}


	public String getName() {
		return this.name;
	}

	public void notify( Message message ) {
			System.out.println(message.user + "("+ message.topic +"): " + message.text);
	}

	public static String pideCadena(String letrero) {
		StringBuffer strDato = new StringBuffer();
		String strCadena = "";
		try {
			System.out.print(letrero);
			BufferedInputStream bin = new BufferedInputStream(System.in);
 			byte bArray[] = new byte[256];
 			int numCaracteres = bin.read(bArray);
			while (numCaracteres==1 && bArray[0]<32)
				numCaracteres = bin.read(bArray);
			for(int i=0;bArray[i]!=13 && bArray[i]!=10 && bArray[i]!=0; i++) {
				strDato.append((char) bArray[i]);
			}
			strCadena = new String( strDato );
		}
		catch( IOException ioe ) {
			System.out.println(ioe.toString());
		}
		return strCadena;
	}

	public static void main( String[] args ) {
		String strCad = "";
		try {
			System.out.println("Connecting to " + args[1]);
			Client clte = new Client( args[0],args[1] );

			// Basic UI for the client, it lets the client subscribe, unsubscribe and send messages to a topic
			while( !strCad.equals("4") ) {
				strCad = pideCadena("\nElige las siguientes opciones\n[1] Suscribirse a topic\n[2] Desuscribirse a topic\n[3] Enviar mensaje a Topic\n[4] Salir\n ");
				if (strCad.equals("1")) {
					strCad = pideCadena("\nCual es el nombre del topic: ");
					clte.subscribeTo(strCad);
					strCad = "";
				} else if ( strCad.equals("2") ) {
					System.out.println("\nTopics subscrito:\n");
					for (String subscribed : clte.subscribedTopics) {
						System.out.println(subscribed);
					}
					strCad = pideCadena("\nCual es el nombre del topic: ");
					clte.unsubscribeFrom(strCad);
					strCad = "";

				} else if ( strCad.equals("3") ) {
					String topic = pideCadena("\nCual es el nombre del topic al que enviara mensaje: ");
					String message = pideCadena("\nCual es el mensaje: ");
					clte.publishIn(message, topic);
				} else {
					System.out.println("\nVuelva a intentarlo... :(\n");
				}
			}
			System.out.println("Local console "+clte.name+", going down");
			for (String subscribed : new ArrayList<String>(clte.subscribedTopics)) {
				clte.unsubscribeFrom(subscribed);
			}
			System.exit(0);
		}
		catch( RemoteException e ) {
			e.printStackTrace();
		}
	}

}
