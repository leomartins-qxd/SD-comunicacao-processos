package streams;

import java.io.IOException;
import entidades.ProdutoFisico;


public class TestProdutoFisicoStreams {
    public static void main(String[] args) throws IOException {
        ProdutoFisico[] produtos = new ProdutoFisico[1];
        
        ProdutoFisicoInputStream pfis = new ProdutoFisicoInputStream(produtos, System.in, System.out);
        produtos = pfis.readSystem();

        ProdutoFisicoOutputStream pfos = new ProdutoFisicoOutputStream(produtos, System.out);
        pfos.writeSystem();
        pfos.close();
    }
}