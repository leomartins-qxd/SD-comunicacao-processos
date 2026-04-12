package serializacao;

import java.io.*;
import java.net.*;
import entidades.Livro;

public class ServidorTCP {
    public static void main(String[] args) {
        int porta = 6789;

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor TCP aguardando conexão na porta " + porta + "...");

            while (true) {
                try (Socket conexao = servidor.accept();
                     ObjectInputStream entrada = new ObjectInputStream(conexao.getInputStream());
                     ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream())) {

                    System.out.println("Cliente conectado: " + conexao.getInetAddress());

                    Livro livroRecebido = (Livro) entrada.readObject();

                    System.out.println("--- Request Recebido ---");
                    System.out.println("Livro: " + livroRecebido.getNome());
                    System.out.println("Autor: " + livroRecebido.getAutor());
                    System.out.println("Preço: " + livroRecebido.getPreco());

                    String resposta = "Sucesso: O livro '" + livroRecebido.getNome() + "' foi recebido.";
                    saida.writeObject(resposta);
                    saida.flush();

                } catch (ClassNotFoundException e) {
                    System.err.println("Erro: Classe Livro não encontrada no classpath.");
                } catch (Exception e) {
                    System.err.println("Erro na comunicação: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}