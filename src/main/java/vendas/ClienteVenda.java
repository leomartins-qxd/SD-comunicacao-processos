package vendas;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import notificacoes.ClienteNotificacao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ClienteVenda {
    private static final Gson gson = new Gson();
    private static final String BASE_URL = "http://localhost:8080";

    // Cliente HTTP nativo do Java para fazer os pedidos à API Javalin
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) {
        // 1. Inicia o JGroups em segundo plano
        try {
            ClienteNotificacao escutador = new ClienteNotificacao();
            escutador.iniciar();
            System.out.println("[*] Ligado ao canal de notificações do Sebo.");
        } catch (Exception e) {
            System.err.println("Aviso: Não foi possível ligar ao chat global.");
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("===   BEM-VINDO AO SISTEMA DO SEBO    ===");
        System.out.println("=========================================");
        System.out.print("Digite o seu identificador: ");
        String idCliente = sc.nextLine();

        while(true) {
            System.out.println("\n-----------------------------------------");
            System.out.println(" 1. Listar Catálogo");
            System.out.println(" 2. Ver Saldo");
            System.out.println(" 3. Comprar Produto Físico");
            System.out.println(" 4. Comprar Produto Digital");
            System.out.println(" 5. Trocar Livro (Oferecer ao Sebo)");
            System.out.println(" 0. Sair");
            System.out.println("-----------------------------------------");
            System.out.print("Opção escolhida: ");

            int opcao;
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nPor favor, digite um número válido.");
                continue;
            }

            if (opcao == 0) {
                System.out.println("\nSaindo do sistema.");
                break;
            }

            JsonObject requestJson = new JsonObject();
            String jsonResponse = "";

            try {
                switch(opcao) {
                    case 1:
                        // Pedido GET para listar produtos
                        jsonResponse = fazerPedidoGet("/produtos");
                        formatarSaidaCatalogo(jsonResponse);
                        break;

                    case 2:
                        // Pedido POST para ver saldo
                        requestJson.addProperty("clienteId", idCliente);
                        jsonResponse = fazerPedidoPost("/saldo", gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;

                    case 3:
                        System.out.print("Introduza o ID do Produto Físico a comprar: ");
                        int idFisico = Integer.parseInt(sc.nextLine());
                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("produtoId", idFisico);

                        jsonResponse = fazerPedidoPost("/comprar/fisico", gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;

                    case 4:
                        System.out.print("Introduza o ID do Produto Digital a comprar: ");
                        int idDigital = Integer.parseInt(sc.nextLine());
                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("produtoId", idDigital);

                        jsonResponse = fazerPedidoPost("/comprar/digital", gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;

                    case 5:
                        System.out.print("Introduza o Nome do Livro que deseja trocar: ");
                        String nomeLivro = sc.nextLine();
                        System.out.print("O livro possui defeitos? (Se não houver problemas, aperte ENTER): ");
                        String estado = sc.nextLine();
                        if (estado.trim().isEmpty()) estado = "Novo";

                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("nomeLivro", nomeLivro);
                        requestJson.addProperty("estado", estado);

                        jsonResponse = fazerPedidoPost("/trocar", gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;

                    default:
                        System.out.println("\nOpção incorreta. Tente novamente.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erro de ligação ao Servidor HTTP: " + e.getMessage());
            }
        }
        sc.close();
        System.exit(0); // Garante que a thread do JGroups também se encerra ao sair
    }

    /**
     * Efetua um pedido HTTP GET
     */
    private static String fazerPedidoGet(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Efetua um pedido HTTP POST enviando o corpo em JSON
     */
    private static String fazerPedidoPost(String endpoint, String corpoJson) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpoJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static void formatarSaidaCatalogo(String json) {
        System.out.println("\n=================================================================");
        System.out.printf(" %-5s | %-30s | %-10s | %-10s \n", "ID", "NOME DO PRODUTO", "TIPO", "PREÇO");
        System.out.println("-----------------------------------------------------------------");
        JsonObject res = gson.fromJson(json, JsonObject.class);
        if (!res.has("produtos") || res.getAsJsonArray("produtos").isEmpty()) {
            System.out.println(" Nenhum produto disponível.");
            System.out.println("=================================================================");
            return;
        }
        JsonArray produtos = res.getAsJsonArray("produtos");
        for (JsonElement element : produtos) {
            JsonObject item = element.getAsJsonObject();
            // Corrigido: 'type' para coincidir com o servidor Javalin
            String tipo = item.has("type") ? item.get("type").getAsString() : "Desconhecido";
            System.out.printf(" %-5s | %-30s | %-10s | R$ %-8s \n",
                    item.get("id").getAsString(),
                    item.get("nome").getAsString(),
                    tipo,
                    item.get("preco").getAsString());
        }
        System.out.println("=================================================================");
    }

    private static void formatarSaidaStatus(String json) {
        JsonObject res = gson.fromJson(json, JsonObject.class);
        String status = res.has("status") ? res.get("status").getAsString() : "erro";
        System.out.println("\n-----------------------------------------------------------------");
        if ("sucesso".equalsIgnoreCase(status)) {
            System.out.println(" [✓] OPERAÇÃO REALIZADA COM SUCESSO");
            if (res.has("mensagem")) System.out.println(" Mensagem: " + res.get("mensagem").getAsString());
            if (res.has("saldo")) System.out.println(" Saldo Atual: R$ " + res.get("saldo").getAsString());
            if (res.has("saldoRestante")) System.out.println(" Saldo Restante: R$ " + res.get("saldoRestante").getAsString());
        } else {
            System.out.println(" [X] ERRO NA OPERAÇÃO");
            System.out.println(" Motivo: " + (res.has("mensagem") ? res.get("mensagem").getAsString() : "Erro desconhecido"));
        }
        System.out.println("-----------------------------------------------------------------");
    }
}