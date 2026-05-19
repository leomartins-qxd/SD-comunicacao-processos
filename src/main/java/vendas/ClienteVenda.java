package vendas;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteVenda {
    // Instância do conversor JSON
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServicoVenda servico = (ServicoVenda) registry.lookup("ServicoSebo");
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

                String jsonResponse = "";
                // Criando o objeto que vai guardar os parâmetros da requisição
                JsonObject requestJson = new JsonObject();

                switch(opcao) {
                    case 1:
                        // Enviando um objeto JSON vazio: {}
                        jsonResponse = servico.doOperation(1, gson.toJson(requestJson));
                        formatarSaidaCatalogo(jsonResponse); 
                        break;
                        
                    case 2:
                        // Adicionando propriedades ao JSON de forma nativa
                        requestJson.addProperty("clienteId", idCliente);
                        jsonResponse = servico.doOperation(2, gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse); 
                        break;
                        
                    case 3:
                        System.out.print("Introduza o ID do Produto Físico a comprar: ");
                        int idFisico = Integer.parseInt(sc.nextLine());
                        
                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("produtoId", idFisico);
                        
                        jsonResponse = servico.doOperation(3, gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;
                        
                    case 4:
                        System.out.print("Introduza o ID do Produto Digital a comprar: ");
                        int idDigital = Integer.parseInt(sc.nextLine());
                        
                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("produtoId", idDigital);
                        
                        jsonResponse = servico.doOperation(4, gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;
                        
                    case 5:
                        System.out.print("Introduza o Nome do Livro que deseja trocar: ");
                        String nomeLivro = sc.nextLine();
                        
                        System.out.print("O livro possui defeitos? (Se não houver problemas, aperte ENTER. Se houver, descreva. Ex: rasgado): ");
                        String estado = sc.nextLine();
                        
                        // Assume que o estado está bom, já que o cliente não informou nenhum defeito. 
                        if (estado.trim().isEmpty()) {
                            estado = "Novo";
                        }

                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("nomeLivro", nomeLivro);
                        requestJson.addProperty("estado", estado);
                        
                        jsonResponse = servico.doOperation(5, gson.toJson(requestJson));
                        formatarSaidaStatus(jsonResponse);
                        break;
                    default:
                        System.out.println("\nOpção incorreta. Tente novamente.");
                        break;
                }
            }
            sc.close();
            
        } catch (Exception e) {
            System.err.println("Erro de ligação ao Servidor: " + e.getMessage());
        }
    }

    // Formata e exibe a lista de produtos baseada no objeto Json recebido
    private static void formatarSaidaCatalogo(String json) {
        System.out.println("\n=================================================================");
        System.out.printf(" %-5s | %-30s | %-10s | %-10s \n", "ID", "NOME DO PRODUTO", "TIPO", "PREÇO");
        System.out.println("-----------------------------------------------------------------");

        // Deserializando a string JSON da resposta
        JsonObject res = gson.fromJson(json, JsonObject.class);

        // Verificando a existência do array dentro do JSON de forma segura
        if (!res.has("produtos") || res.getAsJsonArray("produtos").isEmpty()) {
            System.out.println(" Nenhum produto disponível no catálogo.");
            System.out.println("=================================================================");
            return;
        }

        JsonArray produtos = res.getAsJsonArray("produtos");

        for (JsonElement element : produtos) {
            JsonObject item = element.getAsJsonObject();
            String id = item.get("id").getAsString();
            String nome = item.get("nome").getAsString();
            String tipo = item.get("tipo").getAsString();
            String preco = item.get("preco").getAsString();

            System.out.printf(" %-5s | %-30s | %-10s | R$ %-8s \n", id, nome, tipo, preco);
        }
        System.out.println("=================================================================");
    }

    // Formata e exibe as mensagens de sucesso, erro e alteração de saldos
    private static void formatarSaidaStatus(String json) {
        JsonObject res = gson.fromJson(json, JsonObject.class);
        String status = res.has("status") ? res.get("status").getAsString() : "erro";
        
        System.out.println("\n-----------------------------------------------------------------");
        if ("sucesso".equalsIgnoreCase(status)) {
            System.out.println("OPERAÇÃO REALIZADA COM SUCESSO");
            
            if (res.has("mensagem")) {
                System.out.println(" Mensagem: " + res.get("mensagem").getAsString());
            }
            if (res.has("saldo")) {
                System.out.println(" Saldo Atual: R$ " + res.get("saldo").getAsString());
            }
            if (res.has("saldoRestante")) {
                System.out.println(" Saldo Restante: R$ " + res.get("saldoRestante").getAsString());
            }
        } else {
            System.out.println("ERRO NA OPERAÇÃO");
            String motivo = res.has("mensagem") ? res.get("mensagem").getAsString() : "Erro desconhecido";
            System.out.println(" Motivo: " + motivo);
        }
        System.out.println("-----------------------------------------------------------------");
    }
}