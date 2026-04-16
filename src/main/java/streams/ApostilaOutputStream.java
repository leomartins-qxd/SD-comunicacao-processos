package streams;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import entidades.Apostila;

public class ApostilaOutputStream extends OutputStream {
    private OutputStream op;
    private Apostila[] apostilas;
    
    public ApostilaOutputStream() {}
    
    public ApostilaOutputStream(Apostila[] a, OutputStream os) {
        this.apostilas = a;
        this.op = os;
    }

    public ApostilaOutputStream(OutputStream os) {
        this.op = os;
    }

    public void writeSystem() {
        PrintStream opLocal = new PrintStream(op);
        int qtdapostila = apostilas.length;
        opLocal.println("Quantidade de apostilas: " + qtdapostila);
        
        for (Apostila apostila : apostilas) {
            if (apostila != null) {
                int tamanhoNomeApostila = apostila.getNome().getBytes().length;
                opLocal.println(" tamanhoNomeApostila: " + tamanhoNomeApostila + "\n" +
                                " nomeApostila: " + apostila.getNome() + "\n" +
                                " preco: " + apostila.getPreco() + "\n" +
                                " quantidade: " + apostila.getQuantidade() + "\n" +
                                " data: " + apostila.getData() + "\n" +
                                " idioma: " + apostila.getIdioma() + "\n" +
                                " descricao: " + apostila.getDescricao() + "\n" +
                                " paginas: " + apostila.getPaginas() + "\n" +
                                " autor: " + apostila.getAutor() + "\n" +
                                " area: " + apostila.getArea() + "\n" +
                                " editora: " + apostila.getEditora() + "\n" +
                                " edicao: " + apostila.getEdicao());
            }
        }
    }

    public void writeFile(String nomeArq) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArq))) {
            oos.writeObject(this.apostilas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeTCP() {
        new Thread(() -> {
            try (ServerSocket servidor = new ServerSocket(6789)) {
                try (Socket conexao = servidor.accept();
                     Scanner entradaSocket = new Scanner(conexao.getInputStream())) {
                    while (entradaSocket.hasNextLine()) {
                        System.out.println(entradaSocket.nextLine());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    @Override
    public void write(int b) throws IOException {}

    @Override
    public String toString() {
        return super.toString();
    }
}