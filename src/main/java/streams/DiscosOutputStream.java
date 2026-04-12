package streams;
import entidades.Disco;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;

public class DiscosOutputStream extends OutputStream{
    private OutputStream op;
    private Disco[] discos;

    public DiscosOutputStream() {}

    public DiscosOutputStream(Disco[] d, OutputStream os) {
        this.discos = d;
        this.op = os;
    }

    public void writeSystem() {
        PrintStream opLocal = new PrintStream(op);

        int qtdDisco = discos.length;
        opLocal.println("Quantidade de livros: "+qtdDisco);

        for (Disco disco : discos) {
            if (disco != null) {
                int tamanhoNomeDisco = disco.getNome().getBytes().length;
                String nome = disco.getNome();
                double preco = disco.getPreco();
                int quantidade = disco.getQuantidade();
                LocalDate data = disco.getData();
                String idioma = disco.getIdioma();
                String descricao = disco.getDescricao();
                String genero = disco.getGenero();
                String tipo = disco.getTipo();
                String produtora = disco.getProdutora();
                LocalTime duracao = disco.getDuracao();

                opLocal.println(" tamanhoNomeDisco: "+tamanhoNomeDisco+ "\n"+
                                " nomeDisco: "+nome+ "\n"+
                                " preco: "+preco+ "\n"+
                                " quantidade: "+quantidade+ "\n"+
                                " data: "+data+ "\n"+
                                " idioma: "+idioma+ "\n"+
                                " descricao: "+descricao+ "\n"+
                                " genero: "+genero+ "\n"+
                                " tipo: "+tipo+ "\n"+
                                " produtora: "+produtora+ "\n"+
                                " duracao: "+duracao);
            }
        }
    }

    public void writeFile() {
		// envia os dados de um conjunto (array) de Discos
	}
	
	public void writeTCP() {
		// envia os dados de um conjunto (array) de Discos
	}		
	
	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		return "Ola, mundo! Estamos sobrescrevendo o método toString()!\n"
				+ " DiscosOutputStream [ \n"
				+ " getClass()=" + getClass() +",\n"
				+ " hashCode()=" + hashCode() +",\n"
				+ " toString()="+ super.toString() + "]";
	}
}
