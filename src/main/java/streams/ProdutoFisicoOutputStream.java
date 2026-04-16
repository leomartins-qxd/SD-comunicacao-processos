package streams;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import entidades.ProdutoFisico;

public class ProdutoFisicoOutputStream extends OutputStream {
    private OutputStream op;
    private ProdutoFisico[] produtos;

    public ProdutoFisicoOutputStream(ProdutoFisico[] p, OutputStream os) {
        this.produtos = p;
        this.op = os;
    }

    public void writeSystem() {
        PrintStream opLocal = new PrintStream(op);
        opLocal.println("Quantidade de produtos físicos: " + produtos.length);

        for (ProdutoFisico p : produtos) {
            if (p != null) {
                int tamanhoNome = p.getNome().getBytes().length;
                opLocal.println(" tamanhoNome: " + tamanhoNome + "\n" +
                                " nome: " + p.getNome() + "\n" +
                                " preco: " + p.getPreco() + "\n" +
                                " quantidade: " + p.getQuantidade() + "\n" +
                                " data: " + p.getData() + "\n" +
                                " idioma: " + p.getIdioma() + "\n" +
                                " descricao: " + p.getDescricao());
            }
        }
    }

    @Override
    public void write(int b) throws IOException { }
}