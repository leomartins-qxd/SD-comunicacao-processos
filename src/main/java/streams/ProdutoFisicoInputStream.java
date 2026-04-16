package streams;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import entidades.ProdutoFisico;

public class ProdutoFisicoInputStream extends InputStream {
    private InputStream is;
    private OutputStream os;
    private ProdutoFisico[] produtos;

    public ProdutoFisicoInputStream(ProdutoFisico[] p, InputStream is, OutputStream os) {
        this.produtos = p;
        this.is = is;
        this.os = os;
    }

    private void readProduto() {
        Scanner sc = new Scanner(is);

        System.out.println("Informe a descrição do produto físico");
        String descricao = sc.nextLine();
        System.out.println("Informe o idioma");
        String idioma = sc.nextLine();
        System.out.println("Informe a data (AAAA-MM-DD)");
        LocalDate data = LocalDate.parse(sc.nextLine());
        System.out.println("Informe a quantidade");
        int quantidade = Integer.parseInt(sc.nextLine());
        System.out.println("Informe o preço");
        double preco = Double.parseDouble(sc.nextLine());
        System.out.println("Informe o nome");
        String nome = sc.nextLine();

        produtos[0] = new ProdutoFisico(descricao, idioma, data, quantidade, preco, nome);
    }

    public ProdutoFisico[] readSystem() {
        readProduto();
        return produtos;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}