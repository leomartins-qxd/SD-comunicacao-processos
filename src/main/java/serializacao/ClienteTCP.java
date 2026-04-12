package serializacao;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import entidades.Livro;

public class ClienteTCP {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 6789;

        Livro meuLivro = new Livro(450, "Machado de Assis", "Realismo", "Garnier", 1,
                "Memorias Postumas", 40.0, 1, LocalDate.now(), "PT", "Obra prima");

        try (Socket socket = new Socket(host, porta);
             ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Enviando livro '" + meuLivro.getNome() + "' para o servidor...");
            saida.writeObject(meuLivro);
            saida.flush();

            String reply = (String) entrada.readObject();
            System.out.println("Resposta do Servidor: " + reply);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao conectar ou enviar dados: " + e.getMessage());
        }
    }
}