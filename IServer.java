import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends java.rmi.Remote {
  void login(String name, IClient newClient) throws RemoteException;
  void logout(String name)throws RemoteException;
  void send(Message message) throws RemoteException;
}
