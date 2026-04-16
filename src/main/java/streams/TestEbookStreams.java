package streams;

import java.io.IOException;
import entidades.Ebook;

public class TestEbookStreams {
    public static void main(String[] args) throws IOException {
        Ebook[] ebooks = new Ebook[1];
        
        EbookInputStream eis = new EbookInputStream(ebooks, System.in, System.out);
        ebooks = eis.readSystem();

        EbookOutputStream eos = new EbookOutputStream(ebooks, System.out);
        eos.writeSystem();
        eos.close();
    }
}