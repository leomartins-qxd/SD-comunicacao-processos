package vendas;

import entidades.*;
// Importações do pacote Gson para lidar com JSON de forma nativa e segura
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ServidorVenda extends UnicastRemoteObject implements ServicoVenda {
    
    private Map<Integer, ProdutoFisico> catalogoFisico;
    private Map<Integer, ProdutoDigital> catalogoDigital;
    private Map<String, Cliente> clientes;
    private Gson gson; // Conversor JSON

    protected ServidorVenda() throws RemoteException {
        super();
        catalogoFisico = new HashMap<>();
        catalogoDigital = new HashMap<>();
        clientes = new HashMap<>();
        gson = new Gson(); // Inicializando o conversor JSON
        
        // Dados de teste
        catalogoFisico.put(1, new Livro(300, "Machado de Assis", "Romance", "Typographia", 1,
                "Dom Casmurro", 45.0, 5, LocalDate.now(), "PT", "Clássico da literatura"));
                
        catalogoDigital.put(2, new Ebook(150, 2048, "Autor Digital", "Tecnologia", "TechBooks", 1, 
                "Java RMI Guide", 25.0, LocalDate.now(), "PT", "Aprenda RMI passo a passo"));
        
        clientes.put("leomartins", new Cliente("leomartins", "Leonardo Martins", 100.0));
        clientes.put("rodrigo", new Cliente("rodrigo", "Rodrigo Albuquerque", 50.0));
    }

    @Override
    public String doOperation(int methodId, String jsonArguments) throws RemoteException {
        System.out.println("[SERVIDOR] Operação solicitada: " + methodId);
        
        switch (methodId) {
            case 1: return listarProdutos();
            case 2: return consultarSaldo(jsonArguments);
            case 3: return realizarCompraFisica(jsonArguments);
            case 4: return realizarCompraDigital(jsonArguments);
            case 5: return avaliarTrocaLivro(jsonArguments);
            default: 
                // Usando JsonObject para montar um erro formal em JSON
                JsonObject erro = new JsonObject();
                erro.addProperty("status", "erro");
                erro.addProperty("mensagem", "Método inválido");
                return gson.toJson(erro);
        }
    }

    private String listarProdutos() {
        JsonObject resposta = new JsonObject();
        JsonArray produtosArray = new JsonArray();
        
        for (Map.Entry<Integer, ProdutoFisico> entry : catalogoFisico.entrySet()) {
            JsonObject p = new JsonObject();
            p.addProperty("id", entry.getKey());
            p.addProperty("nome", entry.getValue().getNome());
            p.addProperty("tipo", "Fisico");
            p.addProperty("preco", entry.getValue().getPreco());
            produtosArray.add(p);
        }
        for (Map.Entry<Integer, ProdutoDigital> entry : catalogoDigital.entrySet()) {
            JsonObject p = new JsonObject();
            p.addProperty("id", entry.getKey());
            p.addProperty("nome", entry.getValue().getNome());
            p.addProperty("tipo", "Digital");
            p.addProperty("preco", entry.getValue().getPreco());
            produtosArray.add(p);
        }
        
        resposta.add("produtos", produtosArray);
        return gson.toJson(resposta); // Converte o objeto Java para String JSON
    }

    private String consultarSaldo(String jsonArgs) {
        // Deserializando a string JSON recebida para um Objeto Java (JsonObject)
        JsonObject request = gson.fromJson(jsonArgs, JsonObject.class);
        String clienteId = request.get("clienteId").getAsString();
        
        JsonObject response = new JsonObject();
        Cliente c = clientes.get(clienteId);
        
        if (c != null) {
            response.addProperty("status", "sucesso");
            response.addProperty("saldo", c.getSaldo());
        } else {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Cliente não encontrado");
        }
        return gson.toJson(response);
    }

    private String realizarCompraFisica(String jsonArgs) {
        JsonObject request = gson.fromJson(jsonArgs, JsonObject.class);
        String clienteId = request.get("clienteId").getAsString();
        int produtoId = request.get("produtoId").getAsInt();
        
        JsonObject response = new JsonObject();
        Cliente c = clientes.get(clienteId);
        ProdutoFisico p = catalogoFisico.get(produtoId);

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
        if (p.getQuantidade() <= 0) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Produto esgotado");
            return gson.toJson(response);
        }

        if (c.debitar(p.getPreco())) {
            p.setQuantidade(p.getQuantidade() - 1); 
            response.addProperty("status", "sucesso");
            response.addProperty("mensagem", "Compra do livro '" + p.getNome() + "' efetuada!");
            response.addProperty("saldoRestante", c.getSaldo());
        } else {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Saldo insuficiente");
        }
        return gson.toJson(response);
    }

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

        Livro livroOferecido = new Livro(200, "Desconhecido", "Diversos", "Independente", 1, 
                                         nomeLivro, 50.0, 1, LocalDate.now(), "PT", "Livro usado");

        if (!livroOferecido.validarCondicaoTroca(estado)) {
            response.addProperty("status", "erro");
            response.addProperty("mensagem", "Troca recusada. Não aceitamos livros no estado: " + estado);
            return gson.toJson(response);
        }

        double creditosGanhos = livroOferecido.calcularValorDeTroca();
        c.adicionarCredito(creditosGanhos);

        int novoId = catalogoFisico.size() + catalogoDigital.size() + 1;
        catalogoFisico.put(novoId, livroOferecido);

        response.addProperty("status", "sucesso");
        response.addProperty("mensagem", "Livro aceito! Você recebeu R$ " + String.format("%.2f", creditosGanhos) + " de créditos.");
        response.addProperty("saldo", c.getSaldo());
        
        return gson.toJson(response);
    }

    public static void main(String[] args) {
        try {
            ServidorVenda servidor = new ServidorVenda();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServicoSebo", servidor);
            System.out.println("Servidor do Sebo aguardando conexões");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}