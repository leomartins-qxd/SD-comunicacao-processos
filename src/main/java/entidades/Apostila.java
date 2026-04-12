package entidades;
import java.time.LocalDate;

public class Apostila extends ProdutoFisico {
    private int paginas;
    private String autor;
    private String area;
    private String editora;
    private int edicao;

    public Apostila(int edicao, String editora, String area, String autor, int paginas,
        String descricao, String idioma, LocalDate data, int quantidade, double preco, String nome) {
        super(descricao, idioma, data, quantidade, preco, nome);
        this.edicao = edicao;
        this.editora = editora;
        this.area = area;
        this.autor = autor;
        this.paginas = paginas;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }
}