import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends java.rmi.Remote {
  String getName() throws RemoteException;;
  void notify(Message message) throws RemoteException;
}
