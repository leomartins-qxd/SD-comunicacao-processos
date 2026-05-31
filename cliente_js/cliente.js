const readline = require('readline');

// Configuração da interface de leitura e escrita no terminal
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// Endereço base onde o servidor está
const BASE_URL = "http://localhost:8080";

/**
 * Função utilitária que transforma a leitura do terminal baseada em callbacks
 * numa Promise, permitindo o uso de 'await'
 */
function askQuestion(query) {
    return new Promise(resolve => rl.question(query, resolve));
}

/**
 * Formata e exibe o catálogo de produtos devolvido pelo servidor
 */
function formatarSaidaCatalogo(jsonData) {
    console.log("\n=================================================================");
    console.log(` ${'ID'.padEnd(5)} | ${'NOME DO PRODUTO'.padEnd(30)} | ${'TIPO'.padEnd(10)} | PREÇO`);
    console.log("-----------------------------------------------------------------");

    // Extrai o array de produtos do JSON recebido
    const produtos = jsonData.produtos || [];
    
    if (produtos.length === 0) {
        console.log(" Nenhum produto disponível.");
        console.log("=================================================================");
        return;
    }

    // Itera sobre cada produto e imprime os detalhes alinhados
    produtos.forEach(item => {
        console.log(` ${String(item.id).padEnd(5)} | ${item.nome.padEnd(30)} | ${item.type.padEnd(10)} | R$ ${item.preco}`);
    });
    console.log("=================================================================");
}

/**
 * Formata as respostas de operações de negócio (compras, trocas, consulta de saldo).
 * Analisa o campo 'status' para determinar o sucesso ou falha da operação.
 */
function formatarSaidaStatus(jsonData) {
    const status = jsonData.status || "erro";
    console.log("\n-----------------------------------------------------------------");

    if (status === "sucesso") {
        console.log(" [✓] OPERAÇÃO REALIZADA COM SUCESSO");
        // Verifica dinamicamente quais os campos devolvidos pelo servidor para os apresentar
        if (jsonData.mensagem) console.log(` Mensagem: ${jsonData.mensagem}`);
        if (jsonData.saldo !== undefined) console.log(` Saldo Atual: R$ ${jsonData.saldo}`);
        if (jsonData.saldoRestante !== undefined) console.log(` Saldo Restante: R$ ${jsonData.saldoRestante}`);
    } else {
        console.log(" [X] ERRO NA OPERAÇÃO");
        console.log(` Motivo: ${jsonData.mensagem || "Erro desconhecido"}`);
    }
    console.log("-----------------------------------------------------------------");
}

/**
 * Função principal assíncrona que controla o menu de opções, processa as escolhas do usuário e realiza os pedidos HTTP correspondentes.
 */
async function main() {
    console.log("=========================================");
    console.log("===   BEM-VINDO AO SISTEMA DO SEBO    ===");
    console.log("=========================================");
    
    // Solicita o identificador do usuário logo no início
    const idCliente = await askQuestion("Digite o seu identificador: ");

    // Ciclo infinito para manter o menu ativo até o usuário decidir sair
    while (true) {
        console.log("\n-----------------------------------------");
        console.log(" 1. Listar Catálogo");
        console.log(" 2. Ver Saldo");
        console.log(" 3. Comprar Produto Físico");
        console.log(" 4. Comprar Produto Digital");
        console.log(" 5. Trocar Livro (Oferecer ao Sebo)");
        console.log(" 0. Sair");
        console.log("-----------------------------------------");
        
        let opcao = await askQuestion("Opção escolhida: ");
        opcao = parseInt(opcao);

        // Opção de encerramento do programa
        if (opcao === 0) {
            console.log("\nSaindo do sistema.");
            rl.close(); // Fecha a interface do terminal
            break;
        }

        try {
            // Estrutura de controle para encaminhar os pedidos HTTP a escolha do usuário
            if (opcao === 1) {
                // Realiza um pedido GET para obter os dados
                const res = await fetch(`${BASE_URL}/produtos`);
                formatarSaidaCatalogo(await res.json());

            } else if (opcao === 2) {
                // Realiza um pedido POST enviando os dados no corpo da requisição em formato JSON
                const res = await fetch(`${BASE_URL}/saldo`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ clienteId: idCliente })
                });
                formatarSaidaStatus(await res.json());

            } else if (opcao === 3) {
                const idFisico = await askQuestion("Introduza o ID do Produto Físico a comprar: ");
                const res = await fetch(`${BASE_URL}/comprar/fisico`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ clienteId: idCliente, produtoId: parseInt(idFisico) })
                });
                formatarSaidaStatus(await res.json());

            } else if (opcao === 4) {
                const idDigital = await askQuestion("Introduza o ID do Produto Digital a comprar: ");
                const res = await fetch(`${BASE_URL}/comprar/digital`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ clienteId: idCliente, produtoId: parseInt(idDigital) })
                });
                formatarSaidaStatus(await res.json());

            } else if (opcao === 5) {
                const nomeLivro = await askQuestion("Introduza o Nome do Livro que deseja trocar: ");
                let estado = await askQuestion("O livro possui defeitos? (Se não houver problemas, aperte ENTER): ");
                
                // Define um valor predefinido caso o usuário pressione apenas ENTER
                if (!estado.trim()) estado = "Novo";

                const res = await fetch(`${BASE_URL}/trocar`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ clienteId: idCliente, nomeLivro, estado })
                });
                formatarSaidaStatus(await res.json());

            } else {
                console.log("\nOpção incorreta. Tente novamente.");
            }
        } catch (error) {
            // Captura falhas de ligação (ex: se o servidor estiver desligado)
            console.log(`\nErro de ligação ao Servidor: ${error.message}`);
        }
    }
}

// Inicia a execução do programa
main();