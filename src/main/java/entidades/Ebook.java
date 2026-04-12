package entidades;
import java.time.LocalDate;

public class Ebook extends ProdutoDigital {
    private int paginas;
    private int tamanho_arquivo;
    private String autor;
    private String genero;
    private String editora;
    private int edicao;

    public Ebook(int paginas, int tamanho_arquivo, String autor, String genero, String editora, int edicao, String nome, 
        double preco, LocalDate data, String idioma, String descricao) {
        super(nome, preco, data, idioma, descricao);
        this.paginas = paginas;
        this.tamanho_arquivo = tamanho_arquivo;
        this.autor = autor;
        this.genero = genero;
        this.editora = editora;
        this.edicao = edicao;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getTamanho_arquivo() {
        return tamanho_arquivo;
    }

    public void setTamanho_arquivo(int tamanho_arquivo) {
        this.tamanho_arquivo = tamanho_arquivo;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }
}