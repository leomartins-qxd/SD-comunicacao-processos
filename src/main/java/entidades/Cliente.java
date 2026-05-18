package entidades;
import java.io.Serializable;

public class Cliente implements Serializable {
    private String id;
    private String nome;
    private double saldo; // O saldo agora fica diretamente no Cliente

    public Cliente(String id, String nome, double saldoInicial) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldoInicial;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getSaldo() { return saldo; }

    public void adicionarCredito(double valor) {
        this.saldo += valor;
    }

    public boolean debitar(double valor) {
        if (this.saldo >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }
}