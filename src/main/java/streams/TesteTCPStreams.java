package streams;
import java.io.IOException;
import java.time.LocalDate;

import entidades.Livro;

public class TesteTCPStreams {
    public static void main(String[] args) throws IOException {
        Livro[] livros = new Livro[1];

        LivrosInputStream livrosInputStream = new LivrosInputStream(livros, System.in);
        System.out.println(livrosInputStream.readTCP()[0].getNome());
    }
}