package vendas;

import entidades.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ServidorVenda {
    
    // Estruturas de dados em memória para simular um banco de dados
    private Map<Integer, ProdutoFisico> catalogoFisico;
    private Map<Integer, ProdutoDigital> catalogoDigital;
    private Map<String, Cliente> clientes;
    
    // Objeto Gson utilizado para serializar e desserializar JSON
    private Gson gson;

    public ServidorVenda() {
        // Inicialização das estruturas de dados
        catalogoFisico = new HashMap<>();
        catalogoDigital = new HashMap<>();
        clientes = new HashMap<>();
        gson = new Gson();
        
        // Povoamento inicial
        
        // Adicionando um livro físico ao catálogo
        catalogoFisico.put(1, new Livro(300, "Machado de Assis", "Romance", 
                new Editora("Typographia", "Rio de Janeiro"), 1,
                "Dom Casmurro", 45.0, 5, LocalDate.now(), "PT", "Clássico da literatura"));
                
        // Adicionando um ebook ao catálogo digital
        catalogoDigital.put(2, new Ebook(150, 2048, "Autor Digital", "Tecnologia", 
                new Editora("TechBooks", "São Paulo"), 1, 
                "Java RMI Guide", 25.0, LocalDate.now(), "PT", "Aprenda RMI passo a passo"));
        
        // Cadastrando clientes com saldos iniciais
        clientes.put("leomartins", new Cliente("leomartins", "Leonardo Martins", 100.0));
        clientes.put("rodrigo", new Cliente("rodrigo", "Rodrigo Albuquerque", 50.0));
    }

    /**
     * Retorna a lista completa de produtos (físicos e digitais) em formato JSON.
     */
    private String listarProdutos() {
        JsonObject resposta = new JsonObject();
        JsonArray produtosArray = new JsonArray();
        
        // Itera sobre o catálogo físico e formata os dados
        for (Map.Entry<Integer, ProdutoFisico> entry : catalogoFisico.entrySet()) {
            JsonObject p = new JsonObject();
            p.addProperty("id", entry.getKey());
            p.addProperty("nome", entry.getValue().getNome());
            p.addProperty("type", "Fisico");
            p.addProperty("preco", entry.getValue().getPreco());
            produtosArray.add(p);
        }
        
        // Itera sobre o catálogo digital e formata os dados
        for (Map.Entry<Integer, ProdutoDigital> entry : catalogoDigital.entrySet()) {
            JsonObject p = new JsonObject();
            p.addProperty("id", entry.getKey());
            p.addProperty("nome", entry.getValue().getNome());
            p.addProperty("type", "Digital");
            p.addProperty("preco", entry.getValue().getPreco());
            produtosArray.add(p);
        }
        
        // Agrupa tudo no array final e converte para string JSON
        resposta.add("produtos", produtosArray);
        return gson.toJson(resposta);
    }

    /**
     * Consulta o saldo de um cliente específico.
     * Recebe um JSON com o "clienteId".
     */
    private String consultarSaldo(String jsonArgs) {
        // Converte a string recebida num objeto JSON para extrair os argumentos
        JsonObject request = gson.fromJson(jsonArgs, JsonObject.class);
        String clienteId = request.get("clienteId").getAsString();
        
        JsonObject response = new JsonObject();
        Cliente c = clientes.get(clienteId);
        
        // Verifica se o cliente existe no sistema
        if (c != null) {
            response.addProperty("status", "sucesso");
            response.addProperty("saldo", c.getSaldo());
        } else {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Cliente não encontrado");
        }
        return gson.toJson(response);
    }

    /**
     * Processa a compra de um item físico, validando o cliente, produto, stock e saldo.
     */
    private String realizarCompraFisica(String jsonArgs) {
        // Extrai identificadores da requisição
        JsonObject request = gson.fromJson(jsonArgs, JsonObject.class);
        String clienteId = request.get("clienteId").getAsString();
        int produtoId = request.get("produtoId").getAsInt();
        
        JsonObject response = new JsonObject();
        Cliente c = clientes.get(clienteId);
        ProdutoFisico p = catalogoFisico.get(produtoId);

        // Validações de existência do cliente e produto
        if (c == null) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Cliente não encontrado");
            return gson.toJson(response);
        }
        if (p == null) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Produto não encontrado");
            return gson.toJson(response);
        }
        
        // Verifica se há stock disponível
        if (p.getQuantidade() <= 0) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Produto esgotado");
            return gson.toJson(response);
        }

        // Tenta debitar o valor do saldo do cliente
        if (c.debitar(p.getPreco())) {
            // Se o débito for bem-sucedido, reduz o stock do produto
            p.setQuantidade(p.getQuantidade() - 1); 
            response.addProperty("status", "sucesso");
            response.addProperty("mensagem", "Compra do livro '" + p.getNome() + "' efetuada!");
            response.addProperty("saldoRestante", c.getSaldo());
        } else {
            // Caso o cliente não tenha saldo suficiente
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Saldo insuficiente");
        }
        return gson.toJson(response);
    }

    /**
     * Processa a compra de um item digital (sem controle de stock)
     */
    private String realizarCompraDigital(String jsonArgs) {
        JsonObject request = gson.fromJson(jsonArgs, JsonObject.class);
        String clienteId = request.get("clienteId").getAsString();
        int produtoId = request.get("produtoId").getAsInt();
        
        JsonObject response = new JsonObject();
        Cliente c = clientes.get(clienteId);
        ProdutoDigital p = catalogoDigital.get(produtoId);

        if (c == null || p == null) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Cliente ou Produto inválido");
            return gson.toJson(response);
        }

        // Tenta debitar o valor e, em caso de sucesso, libera o download
        if (c.debitar(p.getPreco())) {
            response.addProperty("status", "sucesso");
            response.addProperty("mensagem", "Download do produto '" + p.getNome() + "' liberado!");
            response.addProperty("saldoRestante", c.getSaldo());
        } else {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Saldo insuficiente");
        }
        return gson.toJson(response);
    }

    /**
     * Avalia a possibilidade de receber um livro usado em troca de créditos.
     */
    private String avaliarTrocaLivro(String jsonArgs) {
        JsonObject request = gson.fromJson(jsonArgs, JsonObject.class);
        String clienteId = request.get("clienteId").getAsString();
        String nomeLivro = request.get("nomeLivro").getAsString();
        String estado = request.get("estado").getAsString();

        JsonObject response = new JsonObject();
        Cliente c = clientes.get(clienteId);
        
        if (c == null) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Cliente não encontrado");
            return gson.toJson(response);
        }

        // Cria uma instância temporária para utilizar as regras de negócio da classe Livro
        Livro livroOferecido = new Livro(200, "Desconhecido", "Diversos", 
                new Editora("Independente", "Desconhecido"), 1, 
                nomeLivro, 50.0, 1, LocalDate.now(), "PT", "Livro usado");

        // Utiliza o método implementado pela interface "Trocavel" para validar o estado
        if (!livroOferecido.validarCondicaoTroca(estado)) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Troca recusada. Não aceitamos livros no estado: " + estado);
            return gson.toJson(response);
        }

        // Calcula quantos créditos o cliente vai receber
        double creditosGanhos = livroOferecido.calcularValorDeTroca();
        c.adicionarCredito(creditosGanhos);

        // Gera um ID sequencial rudimentar e adiciona o livro oferecido ao catálogo do Sebo
        int novoId = catalogoFisico.size() + catalogoDigital.size() + 1;
        catalogoFisico.put(novoId, livroOferecido);

        response.addProperty("status", "sucesso");
        response.addProperty("mensagem", "Livro aceito! Você recebeu R$ " + String.format("%.2f", creditosGanhos) + " de créditos.");
        response.addProperty("saldo", c.getSaldo());
        
        return gson.toJson(response);
    }

    public static void main(String[] args) {
        ServidorVenda servidor = new ServidorVenda();

        // Inicialização do servidor HTTP usando Javalin
        Javalin app = Javalin.create(config -> {

            // Permite requisições de diferentes origens
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));

            // Definição das rotas (Endpoints da API REST)
            
            // Rota GET para leitura do catálogo completo
            config.routes.get("/produtos", ctx -> {
                ctx.contentType("application/json");
                ctx.result(servidor.listarProdutos());
            });

            // Rotas POST que recebem dados no corpo da requisição e invocam a lógica de negócio correspondente
            config.routes.post("/saldo", ctx -> {
                ctx.contentType("application/json");
                String resposta = servidor.consultarSaldo(ctx.body());
                ctx.result(resposta);
            });

            config.routes.post("/comprar/fisico", ctx -> {
                ctx.contentType("application/json");
                String resposta = servidor.realizarCompraFisica(ctx.body());
                ctx.result(resposta);
            });

            config.routes.post("/comprar/digital", ctx -> {
                ctx.contentType("application/json");
                String resposta = servidor.realizarCompraDigital(ctx.body());
                ctx.result(resposta);
            });

            config.routes.post("/trocar", ctx -> {
                ctx.contentType("application/json");
                String resposta = servidor.avaliarTrocaLivro(ctx.body());
                ctx.result(resposta);
            });
        }).start(8080); // Inicia o servidor escutando na porta 8080

        System.out.println("Servidor do Sebo rodando via API HTTP na porta 8080");
    }
}