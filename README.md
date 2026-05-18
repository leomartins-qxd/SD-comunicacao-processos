# Projeto Sebo Distribuído - Sistemas Distribuídos
## Relatório referente ao Trabalho 1 - Comunicação entre Processos

Este projeto consiste em um sistema de gerenciamento de um sebo, integrando conceitos de RMI e Serialização de Objetos em Java. O sistema permite o gerenciamento de produtos físicos e digitais, além de possuir um subsistema de troca de produtos.

## Grupo
**Alunos:**
* Leonardo Martins. Matrícula: 553762
* Rodrigo Albuquerque. Matrícula: 554514


## Aprendizado
Essa atividade avançou nossos conhecimentos sobre RMI, que requer um jeito novo de programar (diferente dos comumente usados TCP e UDP).
 

## Dificuldades Encontradas

O RMI requer um modo de pensar diferente, o que acaba criando diversas confusões lógicas durante o desenvolvimento do trabalho. Por exemplo, se confundir sobre onde o método
atual realmente está sendo executado, e quais dados ele possui acesso.


## Nota merecida


## Horas para realização
Contando as horas de leitura dos conteúdos, video-aulas externas e, principalmente, o próprio desenvolvimento do projeto, passamos cerca de **20 horas** para a realização desta atividade.

---
# Estrutura do Projeto
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

### Sistema de Venda e Troca de Livros (Cliente/Servidor RMI)
Este módulo demonstra a utilização de RMI em uma arquitetura Cliente/Servidor, usando um sistema de vendas e
trocas de livros e/ou outros produtos.

**Ordem de execução:**
1.  **ServidorVenda.java:** Inicie primeiro para que o servidor fique disposto a novas conexões.
2.  **ClienteVenda.java:** Execute para conectar ao servidor.
