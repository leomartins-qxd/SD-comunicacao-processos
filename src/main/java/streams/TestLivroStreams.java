package streams;
import java.io.IOException;

import entidades.Livro;

public class TestLivroStreams {
    public static void main(String[] args) throws IOException {
        Livro[] livros = new Livro[1];
        LivrosInputStream lis = new LivrosInputStream(livros, System.in, System.out);
        livros = lis.readSystem();
        lis.close();

        LivrosOutputStream los = new LivrosOutputStream(livros, System.out);
        los.writeSystem();
        los.close();
    }
}
