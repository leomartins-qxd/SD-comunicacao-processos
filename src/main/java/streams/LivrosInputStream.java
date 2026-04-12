package streams;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

import entidades.Livro;

public class LivrosInputStream extends InputStream{
    private InputStream is;
    private OutputStream os;
    private Livro[] livros;


    public LivrosInputStream(Livro[] l, InputStream is, OutputStream os){
        this.livros = l;
        this.is = is;
        this.os = os;
    }

    private void readLivro(){
        Scanner sc = new Scanner(is);

        System.out.println("Informe o nome do livro");
        String nome = sc.nextLine();
        System.out.println("Informe o preço do livro");
        double preco = Double.parseDouble(sc.nextLine());
        System.out.println("Informe a quantidade do livro");
        int quantidade = Integer.parseInt(sc.nextLine());
        System.out.println("Informe a data do livro");
        LocalDate data = LocalDate.parse(sc.nextLine());
        System.out.println("Informe o idioma do livro");
        String idioma = sc.nextLine();
        System.out.println("Informe a descrição do livro");
        String descricao = sc.nextLine();
        System.out.println("Informe as quantidade de páginas do livro");
        int paginas = Integer.parseInt(sc.nextLine());
        System.out.println("Informe o autor do livro");
        String autor = sc.nextLine();
        System.out.println("Informe o gênero do livro");
        String genero = sc.nextLine();
        System.out.println("Informe o nome da editora do livro");
        String editora = sc.nextLine();
        System.out.println("Informe o número da edição do livro");
        int edicao = Integer.parseInt(sc.nextLine());

        livros[0] = new Livro(paginas, autor, genero, editora, edicao, nome, preco, quantidade, data, idioma, descricao);
    }

    public Livro[] readSystem(){
        readLivro();
        Scanner sc = new Scanner(is);
        sc.close();
        return livros;
    }

    public void readFile(String nomeArq) {
        try (ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(nomeArq))) {

            this.livros = (Livro[]) objInput.readObject();

            System.out.println("--- Dados recuperados do ficheiro: " + nomeArq + " ---");
            if (this.livros != null) {
                for (Livro livro : this.livros) {
                    if (livro != null) {
                        System.out.println("Nome: " + livro.getNome());
                        System.out.println("Autor: " + livro.getAutor());
                        System.out.println("Preço: " + livro.getPreco());
                        System.out.println("Quantidade: " + livro.getQuantidade());
                        System.out.println("Data: " + livro.getData());
                        System.out.println("Idioma: " + livro.getIdioma());
                        System.out.println("Descricao: " + livro.getDescricao());
                        System.out.println("---------------------------");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao ler dados do ficheiro: " + e.getMessage(), e);
        }
    }

    public Livro[] readTCP() {
        PrintStream ps = new PrintStream(this.os);

        ps.println("Preparando envio de " + livros.length + " objetos"); // (ii)

        for (int i = 0; i < this.livros.length; i++) {
            Livro livro = this.livros[i];

            if (livro != null) {
                byte[] bNome = livro.getNome().getBytes();
                byte[] bAutor = livro.getAutor().getBytes();
                byte[] bGenero = livro.getGenero().getBytes();

                ps.println("Objeto [" + i + "] - Dados de envio:");
                ps.println(" Bytes do Nome: " + bNome.length);
                ps.println(" Bytes do Autor: " + bAutor.length);
                ps.println(" Bytes do Gênero: " + bGenero.length);
            }
        }

        return this.livros;
    }

    @Override
    public int read() throws IOException {
        // TODO
        return 0;
    }
}