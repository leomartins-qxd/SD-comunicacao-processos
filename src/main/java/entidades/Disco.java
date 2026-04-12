package entidades;
import java.time.LocalDate;
import java.time.LocalTime;

public class Disco extends ProdutoFisico {
    private String genero;
    private String tipo;
    private String produtora;
    private LocalTime duracao;

    public Disco(String genero, String tipo, String produtora, LocalTime duracao, String descricao,
        String idioma, LocalDate data, int quantidade, double preco, String nome) {
        super(descricao, idioma, data, quantidade, preco, nome);
        this.genero = genero;
        this.tipo = tipo;
        this.produtora = produtora;
        this.duracao = duracao;
    }

    public LocalTime getDuracao() {
        return duracao;
    }

    public void setDuracao(LocalTime duracao) {
        this.duracao = duracao;
    }

    public String getProdutora() {
        return produtora;
    }

    public void setProdutora(String produtora) {
        this.produtora = produtora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

}