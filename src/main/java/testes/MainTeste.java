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
            // 1. Preparação dos Dados (POJO) [cite: 10, 13]
            Livro[] livros = new Livro[1];
            livros[0] = new Livro(300, "Machado de Assis", "Romance", "Typographia", 1,
                    "Dom Casmurro", 45.0, 5, LocalDate.now(), "PT", "Clássico");

            System.out.println("=== TESTE 1: SAÍDA E ENTRADA PADRÃO (System.out/in) ===");
            // O OutputStream escreve na tela [cite: 19]
            LivrosOutputStream losSys = new LivrosOutputStream(livros, System.out);
            losSys.writeSystem();

            System.out.println("\n=== TESTE 2: ARQUIVO (FileOutputStream/FileInputStream) ===");
            String arquivoNome = "dados_livros.dat";
            // Lado da Escrita (OutputStream) grava no disco [cite: 21]
            LivrosOutputStream losFile = new LivrosOutputStream(livros, new FileOutputStream(arquivoNome));
            losFile.writeFile(arquivoNome);

            // Lado da Leitura (InputStream) recupera do disco [cite: 31]
            LivrosInputStream lisFile = new LivrosInputStream(new Livro[1], new FileInputStream(arquivoNome), System.out);
            lisFile.readFile(arquivoNome);

            System.out.println("\n=== TESTE 3: SERVIDOR REMOTO (TCP) ===");
            // Lado do Servidor (InputStream) fica à espera de conexão [cite: 23, 32]
            LivrosOutputStream servidor = new LivrosOutputStream(System.out);
            servidor.writeTCP(); // Inicia o servidor em paralelo (Thread)

            // Pequena pausa para o servidor subir
            Thread.sleep(1000);

            // Lado do Cliente (InputStream agindo como remetente no seu código atual) [cite: 36]
            try (Socket socket = new Socket("localhost", 6789)) {
                LivrosInputStream cliente = new LivrosInputStream(livros, System.in, socket.getOutputStream());
                cliente.readTCP(); // Envia os metadados (contagem de bytes) via rede
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}