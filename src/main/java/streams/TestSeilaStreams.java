package streams;
import java.io.IOException;

import entidades.Livro;

public class TestSeilaStreams {
    public static void main(String[] args) throws IOException {
        Livro[] livros = new Livro[1];
        LivrosInputStream lis = new LivrosInputStream(livros, System.in);
        lis.readFile();
        lis.close();


        LivrosOutputStream los = new LivrosOutputStream(System.out);
        los.writeFile("livros.dat");
        //los.close();
    }
}
