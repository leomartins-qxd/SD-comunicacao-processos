package streams;
import entidades.Livro;


import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;

public class LivrosOutputStream extends OutputStream{
    private OutputStream op;
	private Livro[] livros;
	
	public LivrosOutputStream() {}
	
	public LivrosOutputStream(Livro[] l, OutputStream os) {
		this.livros = l;
		this.op = os;
    }

	public LivrosOutputStream(OutputStream os) {
		this.op = os;
	}

    public void writeSystem() {
		
		PrintStream opLocal = new PrintStream(op);
		
		//envia quantidade de pessoas;
		int qtdlivro = livros.length;
		opLocal.println("Quantidade de livros: "+qtdlivro);
		
		//envia os dados de um conjunto (array) de Livros
		for (Livro livro : livros) {
			if (livro != null) {
				int tamanhoNomeLivro = livro.getNome().getBytes().length;
				String nome = livro.getNome();
				double preco = livro.getPreco();
                int quantidade = livro.getQuantidade();
                LocalDate data = livro.getData();
                String idioma = livro.getIdioma();
                String descricao = livro.getDescricao();
                int paginas = livro.getPaginas();
                String autor = livro.getAutor();
                String genero = livro.getGenero();
                String editora = livro.getEditora();
                int edicao = livro.getEdicao();
							
				opLocal.println(" tamanhoNomeLivro: "+tamanhoNomeLivro+ "\n"+
								" nomeLivro: "+nome+ "\n"+
								" preco: "+preco+ "\n"+
								" quantidade: "+quantidade+ "\n"+
                                " data: "+data+ "\n"+
                                " idioma: "+idioma+ "\n"+
                                " descricao: "+descricao+ "\n"+
                                " paginas: "+paginas+ "\n"+
                                " autor: "+autor+ "\n"+
                                " genero: "+genero+ "\n"+
                                " editora: "+editora+ "\n"+
                                " edicao: "+edicao);
			}
		}
	}
    
    public void writeFile(String nomeArq) {
		// envia os dados de um conjunto (array) de Livro
		try {
			File arq = new File(nomeArq);
			if (arq.exists()) {
				ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(arq));
				this.livros = (Livro[]) objInput.readObject();
				objInput.close();
				writeSystem();
			}
			else
				System.out.println("Arquivo não encontrado.");

		} catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	public void writeTCP() {
		// envia os dados de um conjunto (array) de Livros
	}		
	
	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		return "Ola, mundo! Estamos sobrescrevendo o método toString()!\n"
				+ " LivrosOutputStream [ \n"
				+ " getClass()=" + getClass() +",\n"
				+ " hashCode()=" + hashCode() +",\n"
				+ " toString()="+ super.toString() + "]";
	}
}
