package vendas;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoVenda extends Remote {
    String doOperation(int methodId, String jsonArguments) throws RemoteException;
}