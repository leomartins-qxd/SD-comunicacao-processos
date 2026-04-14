package streams;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import entidades.Disco;


public class DiscosInputStream extends InputStream{
    private InputStream is;
    private OutputStream os;
    private Disco[] discos;


    public DiscosInputStream(Disco[] d, InputStream is, OutputStream os){
        this.discos = d;
        this.is = is;
        this.os = os;
    }

    private void readDisco(){
        Scanner sc = new Scanner(is);

        System.out.println("Informe o nome do disco");
        String nome = sc.nextLine();
        System.out.println("Informe o preço do disco");
        double preco = Double.parseDouble(sc.nextLine());
        System.out.println("Informe a quantidade do disco");
        int quantidade = Integer.parseInt(sc.nextLine());
        System.out.println("Informe a data do disco");
        LocalDate data = LocalDate.parse(sc.nextLine());
        System.out.println("Informe o idioma do disco");
        String idioma = sc.nextLine();
        System.out.println("Informe a descrição do disco");
        String descricao = sc.nextLine();
        System.out.println("Informe o gênero do disco");
        String genero = sc.nextLine();
        System.out.println("Informe o tipo do disco");
        String tipo = sc.nextLine();
        System.out.println("Informe a produtora do disco");
        String produtora = sc.nextLine();
        System.out.println("Informe a duração do disco");
        LocalTime duracao = LocalTime.parse(sc.nextLine());

        discos[0] = new Disco(genero, tipo, produtora, duracao, descricao, idioma, data, quantidade, preco, nome);
    }

    public Disco[] readSystem(){
        readDisco();
        Scanner sc = new Scanner(is);
        sc.close();
        return discos;
    }

    public void readFile(String nomeArq) {
        try (ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(nomeArq))) {

            this.discos = (Disco[]) objInput.readObject();

            System.out.println("--- Dados recuperados do ficheiro: " + nomeArq + " ---");
            if (this.discos != null) {
                for (Disco disco : this.discos) {
                    if (disco != null) {
                        System.out.println("Nome: " + disco.getNome());
                        System.out.println("Produtora: " + disco.getProdutora());
                        System.out.println("Preço: " + disco.getPreco());
                        System.out.println("Quantidade: " + disco.getQuantidade());
                        System.out.println("Data: " + disco.getData());
                        System.out.println("Idioma: " + disco.getIdioma());
                        System.out.println("Descricao: " + disco.getDescricao());
                        System.out.println("---------------------------");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler dados do ficheiro: " + e.getMessage(), e);
        }
    }

    public Disco[] readTCP() {
        PrintStream ps = new PrintStream(this.os);

        ps.println("Preparando envio de " + discos.length + " objetos"); // (ii)

        for (int i = 0; i < this.discos.length; i++) {
            Disco disco = this.discos[i];

            if (disco != null) {
                byte[] bNome = disco.getNome().getBytes();
                byte[] bProdutora = disco.getProdutora().getBytes();
                byte[] bGenero = disco.getGenero().getBytes();

                ps.println("Objeto [" + i + "] - Dados de envio:");
                ps.println(" Bytes do Nome: " + bNome.length);
                ps.println(" Bytes da Produtora: " + bProdutora.length);
                ps.println(" Bytes do Gênero: " + bGenero.length);
            }
        }

        return this.discos;
    }

    @Override
    public int read() throws IOException {
        // TODO
        return 0;
    }
}