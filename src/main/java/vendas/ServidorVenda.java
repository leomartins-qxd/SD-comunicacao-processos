package vendas;

import entidades.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ServidorVenda extends UnicastRemoteObject implements ServicoVenda {
    
    // O servidor agrega os mapas de produtos e clientes
    private Map<Integer, ProdutoFisico> catalogoFisico;
    private Map<Integer, ProdutoDigital> catalogoDigital;
    private Map<String, Cliente> clientes;

    protected ServidorVenda() throws RemoteException {
        super();
        catalogoFisico = new HashMap<>();
        catalogoDigital = new HashMap<>();
        clientes = new HashMap<>();
        
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
            default: return "{\"status\": \"erro\", \"mensagem\": \"Método inválido\"}";
        }
    }

    private String listarProdutos() {
        StringBuilder sb = new StringBuilder("{\"produtos\": [");
        boolean primeiro = true;
        
        for (Map.Entry<Integer, ProdutoFisico> entry : catalogoFisico.entrySet()) {
            if (!primeiro) sb.append(", ");
            sb.append(String.format("{\"id\": %d, \"nome\": \"%s\", \"tipo\": \"Fisico\", \"preco\": %.2f}", 
                entry.getKey(), entry.getValue().getNome(), entry.getValue().getPreco()));
            primeiro = false;
        }
        for (Map.Entry<Integer, ProdutoDigital> entry : catalogoDigital.entrySet()) {
            if (!primeiro) sb.append(", ");
            sb.append(String.format("{\"id\": %d, \"nome\": \"%s\", \"tipo\": \"Digital\", \"preco\": %.2f}", 
                entry.getKey(), entry.getValue().getNome(), entry.getValue().getPreco()));
            primeiro = false;
        }
        sb.append("]}");
        return sb.toString();
    }

    private String consultarSaldo(String jsonArgs) {
        String clienteId = extrairValorJson(jsonArgs, "clienteId");
        Cliente c = clientes.get(clienteId);
        if (c != null) {
            // Acesso direto ao saldo do cliente
            return String.format("{\"status\": \"sucesso\", \"saldo\": %.2f}", c.getSaldo());
        }
        return "{\"status\": \"erro\", \"mensagem\": \"Cliente não encontrado\"}";
    }

    private String realizarCompraFisica(String jsonArgs) {
        String clienteId = extrairValorJson(jsonArgs, "clienteId");
        int produtoId = Integer.parseInt(extrairValorJson(jsonArgs, "produtoId"));
        
        Cliente c = clientes.get(clienteId);
        ProdutoFisico p = catalogoFisico.get(produtoId);

        if (c == null) return "{\"status\": \"erro\", \"mensagem\": \"Cliente não encontrado\"}";
        if (p == null) return "{\"status\": \"erro\", \"mensagem\": \"Produto não encontrado\"}";
        if (p.getQuantidade() <= 0) return "{\"status\": \"erro\", \"mensagem\": \"Produto esgotado\"}";

        // Débito feito diretamente no cliente
        if (c.debitar(p.getPreco())) {
            p.setQuantidade(p.getQuantidade() - 1); 
            return String.format("{\"status\": \"sucesso\", \"mensagem\": \"Compra do livro '%s' efetuada!\", \"saldoRestante\": %.2f}", p.getNome(), c.getSaldo());
        }
        return "{\"status\": \"erro\", \"mensagem\": \"Saldo insuficiente\"}";
    }

    private String realizarCompraDigital(String jsonArgs) {
        String clienteId = extrairValorJson(jsonArgs, "clienteId");
        int produtoId = Integer.parseInt(extrairValorJson(jsonArgs, "produtoId"));
        
        Cliente c = clientes.get(clienteId);
        ProdutoDigital p = catalogoDigital.get(produtoId);

        if (c == null || p == null) return "{\"status\": \"erro\", \"mensagem\": \"Cliente ou Produto inválido\"}";

        // Débito feito diretamente no cliente
        if (c.debitar(p.getPreco())) {
            return String.format("{\"status\": \"sucesso\", \"mensagem\": \"Download do produto '%s' liberado!\", \"saldoRestante\": %.2f}", p.getNome(), c.getSaldo());
        }
        return "{\"status\": \"erro\", \"mensagem\": \"Saldo insuficiente\"}";
    }

    private String avaliarTrocaLivro(String jsonArgs) {
        String clienteId = extrairValorJson(jsonArgs, "clienteId");
        String nomeLivro = extrairValorJson(jsonArgs, "nomeLivro");
        String estado = extrairValorJson(jsonArgs, "estado");

        Cliente c = clientes.get(clienteId);
        if (c == null) return "{\"status\": \"erro\", \"mensagem\": \"Cliente não encontrado\"}";

        // O livro recebido pode ser um livro genérico, pois só precisamos das informações para validação e cálculo de créditos.
        // Após isso, o sebo iria avaliar o livro e completar os dados após a avaliação.
        Livro livroOferecido = new Livro(200, "Desconhecido", "Diversos", "Independente", 1, 
                                         nomeLivro, 50.0, 1, LocalDate.now(), "PT", "Livro usado");

        // Checagem do estado do produto
        if (!livroOferecido.validarCondicaoTroca(estado)) {
            return "{\"status\": \"erro\", \"mensagem\": \"Troca recusada. Não aceitamos livros no estado: " + estado + "\"}";
        }

        double creditosGanhos = livroOferecido.calcularValorDeTroca();

        c.adicionarCredito(creditosGanhos);

        int novoId = catalogoFisico.size() + catalogoDigital.size() + 1;
        catalogoFisico.put(novoId, livroOferecido);

        return String.format("{\"status\": \"sucesso\", \"mensagem\": \"Livro aceito! Você recebeu R$ %.2f de créditos.\", \"saldo\": %.2f}", 
                             creditosGanhos, c.getSaldo());
    }

    private String extrairValorJson(String json, String chave) {
        String busca = "\"" + chave + "\":\"";
        int inicio = json.indexOf(busca);
        if (inicio == -1) return "0";
        inicio += busca.length();
        int fim = json.indexOf("\"", inicio);
        return json.substring(inicio, fim);
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