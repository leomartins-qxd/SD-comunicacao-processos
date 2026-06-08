# Projeto Sebo Distribuído - Sistemas Distribuídos
## Relatórios referentes aos Trabalhos 1 e 4 (Comunicação entre Processos e Comunicação Indireta)

Este projeto consiste em um sistema de gerenciamento de um sebo, integrando conceitos modernos de Sistemas Distribuídos. O sistema permite o gerenciamento de produtos físicos e digitais, além de possuir um subsistema de troca de produtos, operando sob uma arquitetura de microsserviços simulados e comunicação indireta em grupo.

## Grupo
**Alunos:**
* Leonardo Martins. Matrícula: 553762
* Rodrigo Albuquerque. Matrícula: 554514

## Evolução da Arquitetura (Trabalho 3 e 4)
O projeto abandonou a comunicação direta via RMI e foi refatorado para utilizar as seguintes tecnologias:

* **API REST HTTP (Javalin):** As operações centrais (listagem de catálogo, compras e consulta de saldo) ocorrem via requisições HTTP na porta 8080. Isso permitiu a integração de clientes de diversas linguagens (Java, Python e JavaScript).
* **Comunicação Indireta em Grupo (JGroups):** O sistema implementa desacoplamento espacial através do JGroups. Um cluster chamado `SeboCluster` é utilizado para disparar notificações assíncronas. Quando o `ServidorVenda` aceita um livro de troca de um cliente, ele emite uma notificação em multicast para todos os outros clientes avisando da nova disponibilidade. O framework garante internamente a gerência de visões e membros (GMS).

## Aprendizado e Dificuldades Encontradas
A transição do RMI para HTTP/JSON simplificou o fluxo de dados e permitiu o uso de bibliotecas de requisição padrão (`HttpClient` do Java 11, `requests` no Python e `fetch` no JavaScript).

A principal dificuldade em relação à Comunicação Indireta foi estruturar a execução concorrente. Integrar o JGroups de forma que o `ReceiverAdapter` ficasse aguardando mensagens silenciosamente em uma *Thread* de fundo, sem bloquear as rotinas do `Scanner` e do menu principal no terminal.

## Estrutura do Projeto
### Entidades Principais
O sistema foi construído utilizando uma hierarquia de classes para representar o estoque do sebo:
1.  **Livro, Apostila, Ebook e Disco:** Entidades que representam os itens físicos e virtuais, estendendo as classes abstratas `ProdutoFisico` e `ProdutoDigital`.

### Módulos de Comunicação
1.  **`vendas.ServidorVenda`:** Servidor centralizado utilizando o micro-framework Javalin e armazenando os dados de estoque/clientes na memória.
2.  **`vendas.ClienteVenda`:** Cliente Java console nativo comunicando-se via `HttpClient` e utilizando a biblioteca `Gson`.
3.  **`cliente.py` e `cliente.js`:** Clientes externos em Python e Node.js estruturados para consumir nativamente a API REST do sistema.
4.  **`notificacoes.ServidorNotificacao` e `notificacoes.ClienteNotificacao`:** Envelopes responsáveis pela comunicação multicast (JChannel/Receiver) integrados ao ciclo de vida das vendas.

## Instruções de Execução

### 1. Iniciar o Servidor
Execute a classe `vendas.ServidorVenda.java`.
*O console avisará que a API REST está ativa na porta `8080` e que o canal JGroups `SeboCluster` foi iniciado com sucesso.*

### 2. Iniciar os Clientes
Abra diferentes terminais para observar a rede atuando:
* **Cliente Java:** Execute a classe `vendas.ClienteVenda.java`. O cliente fará o *bind* instantâneo no grupo do JGroups em segundo plano e, em seguida, exibirá o menu de interação via HTTP.
* **Cliente Python:** Na pasta `cliente_python`, execute `python cliente.py`.
* **Cliente JS:** Na pasta `cliente_js`, execute `node cliente.js`.