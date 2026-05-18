package vendas;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteVenda {
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
                System.out.println(" 5. Sair"); 
                System.out.println("-----------------------------------------"); 
                System.out.print("Opção escolhida: ");
                
                int opcao;
                try {
                    opcao = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\nPor favor, digite um número válido.");
                    continue;
                }

                if (opcao == 5) {
                    System.out.println("\nSaindo do sistema.");
                    break;
                }

                String jsonRequest = "";
                String jsonResponse = "";

                // O cliente empacota os parâmetros em JSON
                switch(opcao) {
                    case 1:
                        jsonRequest = "{}"; 
                        jsonResponse = servico.doOperation(1, jsonRequest);

                        formatarSaidaCatalogo(jsonResponse); 
                        break;
                    case 2:
                        jsonRequest = "{\"clienteId\":\"" + idCliente + "\"}";
                        jsonResponse = servico.doOperation(2, jsonRequest);

                        formatarSaidaStatus(jsonResponse); 
                        break;
                    case 3:
                        System.out.print("Introduza o ID do Produto Físico a comprar: ");
                        String idFisico = sc.nextLine();
                        jsonRequest = "{\"clienteId\":\"" + idCliente + "\", \"produtoId\":\"" + idFisico + "\"}";
                        jsonResponse = servico.doOperation(3, jsonRequest);

                        formatarSaidaStatus(jsonResponse);
                        break;
                    case 4:
                        System.out.print("Introduza o ID do Produto Digital a comprar: ");
                        String idDigital = sc.nextLine();
                        jsonRequest = "{\"clienteId\":\"" + idCliente + "\", \"produtoId\":\"" + idDigital + "\"}";
                        jsonResponse = servico.doOperation(4, jsonRequest);

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

    //Formata e exibe a lista de produtos recebida em JSON
    private static void formatarSaidaCatalogo(String json) {
        System.out.println("\n=================================================================");
        System.out.printf(" %-5s | %-30s | %-10s | %-10s \n", "ID", "NOME DO PRODUTO", "TIPO", "PREÇO");
        System.out.println("-----------------------------------------------------------------");

        // Tratamento simples da string JSON para extrair os objetos do array
        if (!json.contains("[") || json.contains("[]")) {
            System.out.println(" Nenhum produto disponível no catálogo.");
            System.out.println("=================================================================");
            return;
        }

        String conteudoArray = json.substring(json.indexOf("[") + 1, json.lastIndexOf("]"));
        String[] itens = conteudoArray.split("\\},");

        for (String item : itens) {
            String id = extrairCampo(item, "id");
            String nome = extrairCampo(item, "nome");
            String tipo = extrairCampo(item, "tipo");
            String preco = extrairCampo(item, "preco");

            System.out.printf(" %-5s | %-30s | %-10s | R$ %-8s \n", id, nome, tipo, preco);
        }
        System.out.println("=================================================================");
    }

    // Formata e exibe as mensagens de sucesso, erro e alteração de saldos
     
    private static void formatarSaidaStatus(String json) {
        String status = extrairCampo(json, "status");
        
        System.out.println("\n-----------------------------------------------------------------");
        if ("sucesso".equalsIgnoreCase(status)) {
            System.out.println(" [✓] OPERAÇÃO REALIZADA COM SUCESSO");
            
            String mensagem = extrairCampo(json, "mensagem");
            if (!mensagem.isEmpty()) {
                System.out.println(" Mensagem: " + mensagem);
            }
            
            String saldo = extrairCampo(json, "saldo");
            if (!saldo.isEmpty()) {
                System.out.println(" Saldo Atual: R$ " + saldo);
            }
            
            String saldoRestante = extrairCampo(json, "saldoRestante");
            if (!saldoRestante.isEmpty()) {
                System.out.println(" Saldo Restante: R$ " + saldoRestante);
            }
        } else {
            System.out.println(" [X] ERRO NA OPERAÇÃO");
            System.out.println(" Motivo: " + extrairCampo(json, "mensagem"));
        }
        System.out.println("-----------------------------------------------------------------");
    }

    // Função para extrair valores de chaves dentro da string JSON
     
    private static String extrairCampo(String json, String chave) {
        String padraoChave = "\"" + chave + "\":";
        int indexChave = json.indexOf(padraoChave);
        if (indexChave == -1) {
            padraoChave = chave + ":";
            indexChave = json.indexOf(padraoChave);
            if (indexChave == -1) return "";
        }

        int indexInicio = indexChave + padraoChave.length();
        
        // Pula espaços em branco ou aspas iniciais
        while (indexInicio < json.length() && (json.charAt(indexInicio) == ' ' || json.charAt(indexInicio) == '"')) {
            indexInicio++;
        }

        int indexFim = indexInicio;
        while (indexFim < json.length()) {
            char c = json.charAt(indexFim);
            if (c == '"' || c == ',' || c == '}' || c == ']') {
                break;
            }
            indexFim++;
        }

        return json.substring(indexInicio, indexFim).trim();
    }
}