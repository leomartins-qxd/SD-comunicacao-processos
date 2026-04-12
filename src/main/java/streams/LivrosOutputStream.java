package streams;
import entidades.Livro;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;

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
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArq))) {
			oos.writeObject(this.livros); // Escreve o array no arquivo
			System.out.println("Dados gravados com sucesso em: " + nomeArq);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gravar arquivo", e);
		}
	}

	public void writeTCP() {
		new Thread(() -> {
			try (ServerSocket servidor = new ServerSocket(6789)) {
				System.out.println("Servidor TCP aguardando na porta 6789");

				try (Socket conexao = servidor.accept();
                     Scanner entradaSocket = new Scanner(conexao.getInputStream())) {

					System.out.println("Conexão estabelecida");

					while (entradaSocket.hasNextLine()) {
						String linha = entradaSocket.nextLine();
						System.out.println("Recebido via TCP: " + linha);
					}
				}
			} catch (IOException e) {
				System.err.println("Erro no servidor: " + e.getMessage());
			}
		}).start();
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
