package streams;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import entidades.Ebook;

public class EbookOutputStream extends OutputStream {
    private OutputStream op;
    private Ebook[] ebooks;
    
    public EbookOutputStream() {}
    
    public EbookOutputStream(Ebook[] e, OutputStream os) {
        this.ebooks = e;
        this.op = os;
    }

    public void writeSystem() {
        PrintStream opLocal = new PrintStream(op);
        opLocal.println("Quantidade de ebooks: " + ebooks.length);
        
        for (Ebook e : ebooks) {
            if (e != null) {
                int tamanhoNomeEbook = e.getNome().getBytes().length;
                opLocal.println(" tamanhoNomeEbook: " + tamanhoNomeEbook + "\n" +
                                " nomeEbook: " + e.getNome() + "\n" +
                                " preco: " + e.getPreco() + "\n" +
                                " data: " + e.getData() + "\n" +
                                " idioma: " + e.getIdioma() + "\n" +
                                " descricao: " + e.getDescricao() + "\n" +
                                " paginas: " + e.getPaginas() + "\n" +
                                " tamanho_arquivo: " + e.getTamanho_arquivo() + "\n" +
                                " autor: " + e.getAutor() + "\n" +
                                " genero: " + e.getGenero() + "\n" +
                                " editora: " + e.getEditora() + "\n" +
                                " edicao: " + e.getEdicao());
            }
        }
    }

    public void writeFile(String nomeArq) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArq))) {
            oos.writeObject(this.ebooks);
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
}