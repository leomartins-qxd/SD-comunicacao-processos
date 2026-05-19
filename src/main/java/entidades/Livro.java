package entidades;
import java.time.LocalDate;

import interfaces.Trocavel;


public class Livro extends ProdutoFisico implements Trocavel {
    private int paginas;
    private String autor;
    private String genero;
    private Editora editora;
    private int edicao;

    public Livro(){}

    public Livro(int paginas, String autor, String genero, Editora editora, int edicao, String nome, double preco, int quantidade, LocalDate data, String idioma, String descricao) {
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

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }


    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public double calcularValorDeTroca() {
        return 10.0; // Para simplificar, estamos usando um valor fixo de R$ 10.0 para livros usados, independentemente do preço original.
    }

    @Override
    public boolean validarCondicaoTroca(String estadoConservacao) {
        // Livros não são aceitos se estiverem rasgados
        estadoConservacao = estadoConservacao.toLowerCase();
        if (estadoConservacao.contains("rasgado")) {
            return false;
        }
        return true;
    }

}