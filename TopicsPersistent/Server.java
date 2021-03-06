import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements IServer {
	// subscriptions of the topic-> clients should be in a hashtable
	HashMap<String, ArrayList<IClient>> subscriptions = new HashMap<String, ArrayList<IClient>>();
	HashMap<String, ArrayList<Message>> messages = new HashMap<String, ArrayList<Message>>();

	public Server() throws RemoteException  {}
	// Just to know who logged in
	public synchronized void login(IClient user) throws RemoteException {
		System.out.println("Client " + user.getName() + " has logged in");
	}

	public synchronized void subscribe(IClient user, String topic) throws RemoteException {

		if (! subscriptions.containsKey(topic)) { // check if it doesn't exists
			System.out.println("Creating topic " + topic); // create the topic
			subscriptions.put(topic, new ArrayList<IClient>()); // save it to the local memory
			messages.put(topic, new ArrayList<Message>()); // create messageList if the topic is new
		}

		ArrayList<IClient> subcriptorsList = this.subscriptions.get(topic); // get all the subscriptions
		ArrayList<Message> messagesList = this.messages.get(topic); // get all messages for topic

		for (IClient nextClient: subcriptorsList) {
			nextClient.notify(new Message(user.getName(), topic, "has subscribed to topic")); // tell someone logged in
		}

		for (Message pastMessage: messagesList) { // send historical data of the messages of the topic
			user.notify(pastMessage);
		}
		System.out.println(user.getName() + " has subscribed to " + topic); // Show locally who subscribed to a topic

		subcriptorsList.add(user); // add the user to the subscriptors of the topic
  }

	public synchronized void unsubscribe(IClient user, String topic) throws RemoteException {
		System.out.println(user.getName() + " has unsubscribed to " + topic);
		ArrayList<IClient> subcriptorsList = this.subscriptions.get(topic); // get the array list of subscribers
		subcriptorsList.remove(user); // remove a user if he already subscribed

		for (IClient nextClient: subcriptorsList) {
			nextClient.notify(new Message(user.getName(), topic, "has unsubscribed to topic")); // tell the other subscribers someone unsubscribed
		}

	}

	public synchronized void publish(Message message) throws RemoteException {
		ArrayList<IClient> subcriptorsList = this.subscriptions.get(message.topic); // get the users for the topic
		ArrayList<Message> messagesList = this.messages.get(message.topic); // get the messages from that topic
		messagesList.add(message); // add the message to the topic

		for (IClient nextClient: subcriptorsList) {
			nextClient.notify(message); // notify every user
		}
	}

	public static void main( String[] args ){
		String serverURL = "/Server";
		try {
			Server server = new Server();
			Naming.rebind(serverURL, server);
			System.out.println("Topics server ready");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
