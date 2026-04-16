package streams;

import java.io.IOException;

import entidades.ProdutoDigital;

public class TestProdutoDigitalStreams {
    public static void main(String[] args) throws IOException {
        ProdutoDigital[] produtos = new ProdutoDigital[1];
        
        ProdutoDigitalInputStream pis = new ProdutoDigitalInputStream(produtos, System.in, System.out);
        produtos = pis.readSystem();

        ProdutoDigitalOutputStream pos = new ProdutoDigitalOutputStream(produtos, System.out);
        pos.writeSystem();
        pos.close();
    }
}