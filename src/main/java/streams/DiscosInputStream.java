package streams;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import entidades.Disco;

public class DiscosInputStream extends InputStream {
    private InputStream is;
    private Disco[] discos;

    public DiscosInputStream(Disco[] l, InputStream is){
        this.discos = l;
        this.is = is;
    }

    public Disco[] readSystem(){
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
        System.out.println("Informe a descricao do disco");
        String descricao = sc.nextLine();
        System.out.println("Informe o genero do disco");
        String genero = sc.nextLine();
        System.out.println("Informe o tipo do disco");
        String tipo = sc.nextLine();
        System.out.println("Informe a produtora do disco");
        String produtora = sc.nextLine();
        System.out.println("Informe a duração do disco");
        LocalTime duracao = LocalTime.parse(sc.nextLine());

        discos[0] = new Disco(genero, tipo, produtora, duracao, descricao, idioma, data, quantidade, preco, nome);
        sc.close();

        return discos;
    }

    public Disco[] readFile() {
        // TODO
        return discos;
    }

    public Disco[] readTCP() {
        // TODO
        return discos;
    }

    @Override
    public int read() throws IOException {
        // TODO
        return 0;
    }
}
