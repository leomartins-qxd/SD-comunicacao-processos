package streams;
import java.io.IOException;

import entidades.Disco;


public class TestDiscoStreams {
    public static void main(String[] args) throws IOException {
        Disco[] discos = new Disco[1];
        
        DiscosInputStream lis = new DiscosInputStream(discos, System.in, System.out);
        
        discos = lis.readSystem();
       
        DiscosOutputStream los = new DiscosOutputStream(discos, System.out);
        los.writeSystem();
        los.close();
    }
}