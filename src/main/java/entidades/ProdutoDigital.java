package entidades;
import java.time.LocalDate;

public class ProdutoDigital {
    private String nome;
    private double preco;
    private LocalDate data;
    private String idioma;
    private String descricao;

    public ProdutoDigital(String nome, double preco, LocalDate data, String idioma, String descricao) {
        this.nome = nome;
        this.preco = preco;
        this.data = data;
        this.idioma = idioma;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}