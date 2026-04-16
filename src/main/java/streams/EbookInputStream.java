package streams;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import entidades.Ebook;


public class EbookInputStream extends InputStream {
    private InputStream is;
    private OutputStream os;
    private Ebook[] ebooks;

    public EbookInputStream(Ebook[] e, InputStream is, OutputStream os) {
        this.ebooks = e;
        this.is = is;
        this.os = os;
    }

    private void readEbook() {
        Scanner sc = new Scanner(is);

        System.out.println("Informe o nome do ebook");
        String nome = sc.nextLine();
        System.out.println("Informe o preço do ebook");
        double preco = Double.parseDouble(sc.nextLine());
        System.out.println("Informe a data do ebook");
        LocalDate data = LocalDate.parse(sc.nextLine());
        System.out.println("Informe o idioma do ebook");
        String idioma = sc.nextLine();
        System.out.println("Informe a descrição do ebook");
        String descricao = sc.nextLine();
        System.out.println("Informe a quantidade de páginas");
        int paginas = Integer.parseInt(sc.nextLine());
        System.out.println("Informe o tamanho do arquivo");
        int tamanho_arquivo = Integer.parseInt(sc.nextLine());
        System.out.println("Informe o autor do ebook");
        String autor = sc.nextLine();
        System.out.println("Informe o gênero do ebook");
        String genero = sc.nextLine();
        System.out.println("Informe a editora do ebook");
        String editora = sc.nextLine();
        System.out.println("Informe a edição do ebook");
        int edicao = Integer.parseInt(sc.nextLine());

        ebooks[0] = new Ebook(paginas, tamanho_arquivo, autor, genero, editora, edicao, nome, preco, data, idioma, descricao);
    }

    public Ebook[] readSystem() {
        readEbook();
        return ebooks;
    }

    public void readFile(String nomeArq) {
        try (ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(nomeArq))) {
            this.ebooks = (Ebook[]) objInput.readObject();
            if (this.ebooks != null) {
                for (Ebook e : this.ebooks) {
                    if (e != null) {
                        System.out.println(e.getNome());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}