package streams;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;

import entidades.Apostila;

public class ApostilaInputStream extends InputStream {
    private InputStream is;
    private OutputStream os;
    private Apostila[] apostilas;

    public ApostilaInputStream(Apostila[] a, InputStream is, OutputStream os) {
        this.apostilas = a;
        this.is = is;
        this.os = os;
    }

    private void readApostila() {
        Scanner sc = new Scanner(is);

        System.out.println("Informe o nome da apostila");
        String nome = sc.nextLine();
        System.out.println("Informe o preço da apostila");
        double preco = Double.parseDouble(sc.nextLine());
        System.out.println("Informe a quantidade da apostila");
        int quantidade = Integer.parseInt(sc.nextLine());
        System.out.println("Informe a data da apostila");
        LocalDate data = LocalDate.parse(sc.nextLine());
        System.out.println("Informe o idioma da apostila");
        String idioma = sc.nextLine();
        System.out.println("Informe a descrição da apostila");
        String descricao = sc.nextLine();
        System.out.println("Informe a quantidade de páginas");
        int paginas = Integer.parseInt(sc.nextLine());
        System.out.println("Informe o autor da apostila");
        String autor = sc.nextLine();
        System.out.println("Informe a área da apostila");
        String area = sc.nextLine();
        System.out.println("Informe o nome da editora da apostila");
        String editora = sc.nextLine();
        System.out.println("Informe o número da edição da apostila");
        int edicao = Integer.parseInt(sc.nextLine());

        apostilas[0] = new Apostila(edicao, editora, area, autor, paginas, descricao, idioma, data, quantidade, preco, nome);
    }

    public Apostila[] readSystem() {
        readApostila();
        return apostilas;
    }

    public void readFile(String nomeArq) {
        try (ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(nomeArq))) {
            this.apostilas = (Apostila[]) objInput.readObject();
            if (this.apostilas != null) {
                for (Apostila apostila : this.apostilas) {
                    if (apostila != null) {
                        System.out.println(apostila.getNome());
                        System.out.println(apostila.getAutor());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Apostila[] readTCP() {
        PrintStream ps = new PrintStream(this.os);
        ps.println(apostilas.length);
        for (int i = 0; i < this.apostilas.length; i++) {
            Apostila apostila = this.apostilas[i];
            if (apostila != null) {
                byte[] bNome = apostila.getNome().getBytes();
                ps.println(bNome.length);
            }
        }
        return this.apostilas;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}