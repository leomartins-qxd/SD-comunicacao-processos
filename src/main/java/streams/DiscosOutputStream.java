package streams;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import entidades.Disco;

public class DiscosOutputStream extends OutputStream {
    private OutputStream op;
    private Disco[] discos;
    
    public DiscosOutputStream() {}
    
    public DiscosOutputStream(Disco[] d, OutputStream os) {
        this.discos = d;
        this.op = os;
    }

    public DiscosOutputStream(OutputStream os) {
        this.op = os;
    }

    public void writeSystem() {
        PrintStream opLocal = new PrintStream(op);
        
        // Envia quantidade de discos
        int qtddisco = discos.length;
        opLocal.println("Quantidade de discos: " + qtddisco);
        
        // Envia os dados de um conjunto (array) de Discos
        for (Disco disco : discos) {
            if (disco != null) {
                int tamanhoNomeDisco = disco.getNome().getBytes().length;
                String nome = disco.getNome();
                double preco = disco.getPreco();
                int quantidade = disco.getQuantidade();
                LocalDate data = disco.getData();
                String idioma = disco.getIdioma();
                String descricao = disco.getDescricao();
                
                // Campos específicos de Disco
                String genero = disco.getGenero();
                String tipo = disco.getTipo();
                String produtora = disco.getProdutora();
                LocalTime duracao = disco.getDuracao();
                            
                opLocal.println(" tamanhoNomeDisco: " + tamanhoNomeDisco + "\n" +
                                " nomeDisco: " + nome + "\n" +
                                " preco: " + preco + "\n" +
                                " quantidade: " + quantidade + "\n" +
                                " data: " + data + "\n" +
                                " idioma: " + idioma + "\n" +
                                " descricao: " + descricao + "\n" +
                                " genero: " + genero + "\n" +
                                " tipo: " + tipo + "\n" +
                                " produtora: " + produtora + "\n" +
                                " duracao: " + duracao);
            }
        }
    }

    public void writeFile(String nomeArq) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArq))) {
            oos.writeObject(this.discos); // Escreve o array no arquivo
            System.out.println("Dados gravados com sucesso em: " + nomeArq);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar arquivo", e);
        }
    }

    public void writeTCP() {
        new Thread(() -> {
            try (ServerSocket servidor = new ServerSocket(6789)) {
                System.out.println("Servidor TCP aguardando na porta 6789");

                try (Socket conexao = servidor.accept();
                     Scanner entradaSocket = new Scanner(conexao.getInputStream())) {

                    System.out.println("Conexão estabelecida");

                    while (entradaSocket.hasNextLine()) {
                        String linha = entradaSocket.nextLine();
                        System.out.println("Recebido via TCP: " + linha);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro no servidor: " + e.getMessage());
            }
        }).start();
    }
    
    @Override
    public void write(int b) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        return "Ola, mundo! Estamos sobrescrevendo o método toString()!\n"
                + " DiscosOutputStream [ \n"
                + " getClass()=" + getClass() + ",\n"
                + " hashCode()=" + hashCode() + ",\n"
                + " toString()=" + super.toString() + "]";
    }
}