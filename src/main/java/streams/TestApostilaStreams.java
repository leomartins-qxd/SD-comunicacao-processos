package streams;

import java.io.IOException;
import entidades.Apostila;


public class TestApostilaStreams {
    public static void main(String[] args) throws IOException {
        Apostila[] apostilas = new Apostila[1];
        
        ApostilaInputStream ais = new ApostilaInputStream(apostilas, System.in, System.out);
        apostilas = ais.readSystem();

        ApostilaOutputStream aos = new ApostilaOutputStream(apostilas, System.out);
        aos.writeSystem();
        aos.close();
    }
}