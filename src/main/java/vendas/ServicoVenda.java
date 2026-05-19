package vendas;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoVenda extends Remote {
    byte[] comunicar(byte[] requisicaoBytes) throws RemoteException;
}