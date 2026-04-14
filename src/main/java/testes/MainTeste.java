package testes;

import entidades.Livro;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import streams.LivrosOutputStream;
import streams.LivrosInputStream;

public class MainTeste {
    public static void main(String[] args) {
        try {
            Livro[] livros = new Livro[1];
            livros[0] = new Livro(300, "Machado de Assis", "Romance", "Typographia", 1,
                    "Dom Casmurro", 45.0, 5, LocalDate.now(), "PT", "Clássico");

            System.out.println("=== TESTE 1: SAÍDA E ENTRADA PADRÃO (System.out/in) ===");
            LivrosOutputStream losSys = new LivrosOutputStream(livros, System.out);
            losSys.writeSystem();

            System.out.println("\n=== TESTE 2: ARQUIVO (FileOutputStream/FileInputStream) ===");
            String arquivoNome = "dados_livros.dat";
            LivrosOutputStream losFile = new LivrosOutputStream(livros, new FileOutputStream(arquivoNome));
            losFile.writeFile(arquivoNome);

            LivrosInputStream lisFile = new LivrosInputStream(new Livro[1], new FileInputStream(arquivoNome), System.out);
            lisFile.readFile(arquivoNome);

            System.out.println("\n=== TESTE 3: SERVIDOR REMOTO (TCP) ===");
            LivrosOutputStream servidor = new LivrosOutputStream(System.out);
            servidor.writeTCP();

            Thread.sleep(1000);

            try (Socket socket = new Socket("localhost", 6789)) {
                LivrosInputStream cliente = new LivrosInputStream(livros, System.in, socket.getOutputStream());
                cliente.readTCP();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}