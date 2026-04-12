package streams;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

import entidades.Livro;

public class LivrosInputStream extends InputStream{
    private InputStream is;
    private Livro[] livros;

    public LivrosInputStream(Livro[] l, InputStream is){
        this.livros = l;
        this.is = is;
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

    public void readFile() {
        readLivro();

        Scanner sc = new Scanner(is);
        System.out.println("Informe o nome do arquivo: ");
        String nomeArq = sc.nextLine();
        sc.close();

        File arq =  new File(nomeArq);
        try {
            arq.delete();
            arq.createNewFile();

            ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
            objOutput.writeObject(livros);
            objOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Livro[] readTCP() {
        // TODO
        return null;
    }

    @Override
    public int read() throws IOException {
        // TODO
        return 0;
    }
}