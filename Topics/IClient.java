import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends java.rmi.Remote {
  String getName() throws RemoteException;;
  void receiveEnter(String name) throws RemoteException;
  void receiveExit(String name)throws RemoteException;
  void receiveMessage(Message message) throws RemoteException;
}
