package entidades;
import java.io.Serializable;
import java.time.LocalDate;


public class Livro extends ProdutoFisico implements Serializable {
    private int paginas;
    private String autor;
    private String genero;
    private String editora;
    private int edicao;

    public Livro(){}

    public Livro(int paginas, String autor, String genero, String editora, int edicao, String nome, double preco, int quantidade, LocalDate data, String idioma, String descricao) {
        super(descricao, idioma, data, quantidade, preco, nome);
        this.paginas = paginas;
        this.autor = autor;
        this.genero = genero;
        this.editora = editora;
        this.edicao = edicao;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
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

}