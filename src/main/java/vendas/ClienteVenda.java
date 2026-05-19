package vendas;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteVenda {
    private static Gson gson = new Gson();
    private static int contadorRequisicao = 1; // Conta os IDs das mensagens

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

                JsonObject requestJson = new JsonObject();
                byte[] argumentosJson = null;
                byte[] respostaBytes = null;
                String jsonResponse = "";

                switch(opcao) {
                    case 1:
                        argumentosJson = gson.toJson(requestJson).getBytes();
                        respostaBytes = doOperation(servico, "ServicoSebo", 1, argumentosJson);
                        jsonResponse = new String(respostaBytes);
                        formatarSaidaCatalogo(jsonResponse); 
                        break;
                        
                    case 2:
                        requestJson.addProperty("clienteId", idCliente);
                        argumentosJson = gson.toJson(requestJson).getBytes();
                        respostaBytes = doOperation(servico, "ServicoSebo", 2, argumentosJson);
                        jsonResponse = new String(respostaBytes);
                        formatarSaidaStatus(jsonResponse); 
                        break;
                        
                    case 3:
                        System.out.print("Introduza o ID do Produto Físico a comprar: ");
                        int idFisico = Integer.parseInt(sc.nextLine());
                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("produtoId", idFisico);
                        
                        argumentosJson = gson.toJson(requestJson).getBytes();
                        respostaBytes = doOperation(servico, "ServicoSebo", 3, argumentosJson);
                        jsonResponse = new String(respostaBytes);
                        formatarSaidaStatus(jsonResponse);
                        break;
                        
                    case 4:
                        System.out.print("Introduza o ID do Produto Digital a comprar: ");
                        int idDigital = Integer.parseInt(sc.nextLine());
                        requestJson.addProperty("clienteId", idCliente);
                        requestJson.addProperty("produtoId", idDigital);
                        
                        argumentosJson = gson.toJson(requestJson).getBytes();
                        respostaBytes = doOperation(servico, "ServicoSebo", 4, argumentosJson);
                        jsonResponse = new String(respostaBytes);
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
                        
                        argumentosJson = gson.toJson(requestJson).getBytes();
                        respostaBytes = doOperation(servico, "ServicoSebo", 5, argumentosJson);
                        jsonResponse = new String(respostaBytes);
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

    public static byte[] doOperation(ServicoVenda servico, String objectReference, int methodId, byte[] arguments) throws Exception {
        // Criando a mensagem do request
        Mensagem requestMsg = new Mensagem(0, contadorRequisicao++, objectReference, methodId, arguments);

        byte[] requestBytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(requestMsg);
            requestBytes = bos.toByteArray();
        }

        byte[] replyBytes = servico.comunicar(requestBytes);

        Mensagem replyMsg;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            replyMsg = (Mensagem) ois.readObject();
        }

        return replyMsg.getArguments();
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
            System.out.printf(" %-5s | %-30s | %-10s | R$ %-8s \n", item.get("id").getAsString(), item.get("nome").getAsString(), item.get("tipo").getAsString(), item.get("preco").getAsString());
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