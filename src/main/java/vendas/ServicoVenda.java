package vendas;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoVenda extends Remote {
    // Protocolo de Requisição-Resposta adaptado para RMI e JSON
    String doOperation(int methodId, String jsonArguments) throws RemoteException;
}