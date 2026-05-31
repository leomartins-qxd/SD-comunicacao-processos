import requests

# Endereço base onde o servidor está em execução
BASE_URL = "http://localhost:8080"

def formatar_saida_catalogo(json_data):
    """
    Recebe o dicionário JSON correspondente ao catálogo, extrai os produtos
    e exibe-os numa tabela.
    """
    print("\n=================================================================")
    print(f" {'ID':<5} | {'NOME DO PRODUTO':<30} | {'TIPO':<10} | {'PREÇO':<10}")
    print("-----------------------------------------------------------------")

    produtos = json_data.get("produtos", [])
    
    if not produtos:
        print(" Nenhum produto disponível.")
        print("=================================================================")
        return

    # Formata cada campo de acordo com o espaçamento definido no cabeçalho
    for item in produtos:
        print(f" {str(item.get('id')):<5} | {item.get('nome'):<30} | {item.get('type'):<10} | R$ {str(item.get('preco')):<8}")
    print("=================================================================")

def formatar_saida_status(json_data):
    """
    Interpreta os resultados das operações (como compras e trocas),
    lendo a propriedade 'status' e exibindo mensagens e saldos relevantes.
    """
    status = json_data.get("status", "erro")
    print("\n-----------------------------------------------------------------")
    
    if status.lower() == "sucesso":
        print(" [✓] OPERAÇÃO REALIZADA COM SUCESSO")
        # Mostra as informações adicionais caso elas venham anexadas na resposta
        if "mensagem" in json_data:
            print(f" Mensagem: {json_data['mensagem']}")
        if "saldo" in json_data:
            print(f" Saldo Atual: R$ {json_data['saldo']}")
        if "saldoRestante" in json_data:
            print(f" Saldo Restante: R$ {json_data['saldoRestante']}")
    else:
        print(" [X] ERRO NA OPERAÇÃO")
        motivo = json_data.get("mensagem", "Erro desconhecido")
        print(f" Motivo: {motivo}")
    print("-----------------------------------------------------------------")

def main():
    """
    Função principal que gere a interação com o utilizador e faz o mapeamento
    entre as opções escolhidas e as rotas da API no servidor.
    """
    print("=========================================")
    print("===   BEM-VINDO AO SISTEMA DO SEBO    ===")
    print("=========================================")
    
    id_cliente = input("Digite o seu identificador: ")

    # Ciclo infinito para o menu interativo
    while True:
        print("\n-----------------------------------------")
        print(" 1. Listar Catálogo")
        print(" 2. Ver Saldo")
        print(" 3. Comprar Produto Físico")
        print(" 4. Comprar Produto Digital")
        print(" 5. Trocar Livro (Oferecer ao Sebo)")
        print(" 0. Sair")
        print("-----------------------------------------")
        opcao = input("Opção escolhida: ")

        # Proteção contra introdução de caracteres inválidos no menu
        try:
            opcao = int(opcao)
        except ValueError:
            print("\nPor favor, digite um número válido.")
            continue

        if opcao == 0:
            print("\nSaindo do sistema.")
            break

        try:
            if opcao == 1:
                # Efetua um pedido GET (não precisa de corpo de mensagem)
                response = requests.get(f"{BASE_URL}/produtos")
                formatar_saida_catalogo(response.json())

            elif opcao == 2:
                # Prepara os dados e envia através de um pedido POST
                payload = {"clienteId": id_cliente}
                response = requests.post(f"{BASE_URL}/saldo", json=payload)
                formatar_saida_status(response.json())

            elif opcao == 3:
                id_fisico = input("Introduza o ID do Produto Físico a comprar: ")
                payload = {"clienteId": id_cliente, "produtoId": int(id_fisico)}
                response = requests.post(f"{BASE_URL}/comprar/fisico", json=payload)
                formatar_saida_status(response.json())

            elif opcao == 4:
                id_digital = input("Introduza o ID do Produto Digital a comprar: ")
                payload = {"clienteId": id_cliente, "produtoId": int(id_digital)}
                response = requests.post(f"{BASE_URL}/comprar/digital", json=payload)
                formatar_saida_status(response.json())

            elif opcao == 5:
                nome_livro = input("Introduza o Nome do Livro que deseja trocar: ")
                estado = input("O livro possui defeitos? (Se não houver problemas, aperte ENTER): ")
                
                # Se o utilizador apenas pressionar ENTER, assume-se que está num estado novo
                if not estado.strip():
                    estado = "Novo"

                payload = {"clienteId": id_cliente, "nomeLivro": nome_livro, "estado": estado}
                response = requests.post(f"{BASE_URL}/trocar", json=payload)
                formatar_saida_status(response.json())

            else:
                print("\nOpção incorreta. Tente novamente.")

        # Intercepta erros de ligação (ex: Timeout, Connection Refused) se o servidor não estiver ativo
        except requests.exceptions.RequestException as e:
            print(f"Erro de ligação ao Servidor: {e}")

# Ponto de entrada do script
if __name__ == "__main__":
    main()