package entidades;
import java.io.Serializable;
import java.time.LocalDate;

public class ProdutoFisico implements Serializable {
    private String nome;
    private double preco;
    private int quantidade;
    private LocalDate data;
    private String idioma;
    private String descricao;

    public ProdutoFisico(){}

    public ProdutoFisico(String descricao, String idioma, LocalDate data, int quantidade, double preco, String nome) {
        this.descricao = descricao;
        this.idioma = idioma;
        this.data = data;
        this.quantidade = quantidade;
        this.preco = preco;
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}