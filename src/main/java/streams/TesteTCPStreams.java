package streams;

import entidades.Livro;
import java.net.Socket;

public class TesteTCPStreams {
    public static void main(String[] args) throws Exception {
        Livro[] livros = new Livro[1];
        livros[0] = new Livro(300, "Autor Teste", "Ficção", "Editora X", 1, "Meu Livro", 50.0, 10, null, "PT", "Desc");

        LivrosOutputStream los = new LivrosOutputStream(System.out);
        los.writeTCP();

        Thread.sleep(1000);

        try (Socket socketCliente = new Socket("localhost", 6789)) {
            LivrosInputStream lis = new LivrosInputStream(livros, System.in, socketCliente.getOutputStream());

            lis.readTCP();
        }
    }
}