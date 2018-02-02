import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends java.rmi.Remote {
  void login(IClient user) throws RemoteException;
  void subscribe(IClient user, String topic) throws RemoteException;
  void unsubscribe(IClient user, String topic) throws RemoteException;
  void publish(Message message) throws RemoteException;
}
