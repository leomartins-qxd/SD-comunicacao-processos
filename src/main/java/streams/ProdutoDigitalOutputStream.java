package streams;

import java.io.*;
import entidades.ProdutoDigital;

public class ProdutoDigitalOutputStream extends OutputStream {
    private OutputStream op;
    private ProdutoDigital[] produtos;

    public ProdutoDigitalOutputStream(ProdutoDigital[] p, OutputStream os) {
        this.produtos = p;
        this.op = os;
    }

    public void writeSystem() {
        PrintStream opLocal = new PrintStream(op);
        opLocal.println("Quantidade de produtos digitais: " + produtos.length);

        for (ProdutoDigital p : produtos) {
            if (p != null) {
                int tamanhoNome = p.getNome().getBytes().length;
                opLocal.println(" tamanhoNome: " + tamanhoNome + "\n" +
                                " nome: " + p.getNome() + "\n" +
                                " preco: " + p.getPreco() + "\n" +
                                " data: " + p.getData() + "\n" +
                                " idioma: " + p.getIdioma() + "\n" +
                                " descricao: " + p.getDescricao());
            }
        }
    }

    @Override
    public void write(int b) throws IOException { }
}