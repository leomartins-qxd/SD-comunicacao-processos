# Projeto Sebo Distribuído - Sistemas Distribuídos
## Referente ao Trabalho 1 - Comunicação entre Processos

Este projeto consiste em um sistema de gerenciamento de um sebo, integrando conceitos de Sockets TCP/UDP, Serialização de Objetos, Multicast e Threads em Java. O sistema permite o gerenciamento de produtos físicos e digitais, além de possuir um subsistema de votação para escolha de títulos favoritos.

## Grupo
**Alunos:**
* Leonardo Martins. Matrícula: 553762
* Rodrigo Albuquerque. Matrícula: 554514

---
## Aprendizado
Essa atividade avançou nossos conhecimentos sobre sockets UDP e TCP, criação de classes mais complexas em Java, tratamento de arquivos e como lidar com Multicasts e Unicasts.

---
## Dificuldades Encontradas

A serialização não foi realmente implementada como requisitado, após muitos problemas ao tentar utilizar bibliotecas. Além disso, um outro obstáculo foi implementar as streams, já que envolve diversos tipos de entradas e saídas (o que causou uma necessidade de ler mais afundo as classes de InputStream e OutputStream do java). Finalmente, houve problemas ao executar o Multicast em dispositivos específicos, em que a mensagem não chegava nos eleitores.

---
## Nota merecida
Utilizamos diversos conceitos em uma linguagem verbosa e não tão simples, alcançando um resultado satisfatório que possibilita representar os estudos de sistemas distribuídos vistos em sala. 
Porém, por conta de detalhes, como a serialização não estar corretamente implementada (o código funciona, porém sua lógica não é a correta), é certo dizer que não alcançamos nota máxima.
Portanto, supomos que uma **nota 9** faria jus ao esforço e implementações que empregamos neste trabalho.

## Horas para realização
Contando as horas de leitura dos conteúdos, video-aulas externas e, principalmente, o próprio desenvolvimento do projeto, passamos cerca de **30 horas** para a realização desta atividade.

## Entidades Principais

O sistema foi construído utilizando uma hierarquia de classes para representar o estoque do sebo:

1.  **Livro:** Entidade base que contém informações como nome, autor, preço, quantidade, data de publicação, idioma e descrição.
2.  **Apostila:** Especialização para materiais didáticos.
3.  **Ebook:** Representação de produtos digitais, incluindo formato do arquivo e tamanho.
4.  **Disco:** Entidade para itens de áudio/música.
5.  **Classes Abstratas:** 
    * `ProdutoFisico`: Define características de itens tangíveis (como peso e dimensões).
    * `ProdutoDigital`: Define características de itens virtuais (como link para download e validade).

---

## Instruções de Execução

### 1. Sistema de Serialização de Livros (Cliente/Servidor TCP)
Este módulo demonstra a transferência de objetos serializados através de streams de rede.

**Ordem de execução:**
1.  **ServidorTCP.java:** Inicie primeiro para que o servidor abra o `ServerSocket` e fique aguardando conexões.
2.  **ClienteTCP.java:** Execute para conectar ao servidor.




### 2. Sistema de Votação (Protocolo Híbrido TCP/UDP)
Este módulo utiliza TCP para ações diretas (votar e gerir candidatos) e UDP Multicast para avisos gerais.

**Ordem de execução:**
1.  **ServidorVotacao.java:** Execute primeiro. Ele inicializa o cronômetro da votação e abre as portas TCP (6789) e UDP (12347).
2.  **Administrador.java:** Execute para adicionar candidatos ou enviar notas informativas via Multicast que todos os eleitores verão.
3.  **Eleitor.java:** Execute um ou mais clientes. O eleitor receberá a lista numerada de candidatos via TCP e também ouvirá os anúncios do administrador via Multicast.

### Opcional
**MainTeste.java:** Pode ser utilizado para gerar os dados iniciais ou testar a lógica de leitura/escrita em arquivos `.dat` (como o `livros.dat`) antes do envio.


