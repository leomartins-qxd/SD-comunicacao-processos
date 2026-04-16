package streams;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import entidades.ProdutoDigital;


public class ProdutoDigitalInputStream extends InputStream {
    private InputStream is;
    private OutputStream os;
    private ProdutoDigital[] produtos;

    public ProdutoDigitalInputStream(ProdutoDigital[] p, InputStream is, OutputStream os) {
        this.produtos = p;
        this.is = is;
        this.os = os;
    }

    private void readProduto() {
        Scanner sc = new Scanner(is);

        System.out.println("Informe o nome do produto digital");
        String nome = sc.nextLine();
        System.out.println("Informe o preço");
        double preco = Double.parseDouble(sc.nextLine());
        System.out.println("Informe a data (AAAA-MM-DD)");
        LocalDate data = LocalDate.parse(sc.nextLine());
        System.out.println("Informe o idioma");
        String idioma = sc.nextLine();
        System.out.println("Informe a descrição");
        String descricao = sc.nextLine();

        produtos[0] = new ProdutoDigital(nome, preco, data, idioma, descricao);
    }

    public ProdutoDigital[] readSystem() {
        readProduto();
        return produtos;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}