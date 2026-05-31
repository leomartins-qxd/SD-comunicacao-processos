const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const BASE_URL = "http://localhost:8080";

function askQuestion(query) {
    return new Promise(resolve => rl.question(query, resolve));
}

function formatarSaidaCatalogo(jsonData) {
    console.log("\n=================================================================");
    console.log(` ${'ID'.padEnd(5)} | ${'NOME DO PRODUTO'.padEnd(30)} | ${'TIPO'.padEnd(10)} | PREÇO`);
    console.log("-----------------------------------------------------------------");

    const produtos = jsonData.produtos || [];
    if (produtos.length === 0) {
        console.log(" Nenhum produto disponível.");
        console.log("=================================================================");
        return;
    }

    produtos.forEach(item => {
        console.log(` ${String(item.id).padEnd(5)} | ${item.nome.padEnd(30)} | ${item.type.padEnd(10)} | R$ ${item.preco}`);
    });
    console.log("=================================================================");
}

function formatarSaidaStatus(jsonData) {
    const status = jsonData.status || "erro";
    console.log("\n-----------------------------------------------------------------");

    if (status === "sucesso") {
        console.log(" [✓] OPERAÇÃO REALIZADA COM SUCESSO");
        if (jsonData.mensagem) console.log(` Mensagem: ${jsonData.mensagem}`);
        if (jsonData.saldo !== undefined) console.log(` Saldo Atual: R$ ${jsonData.saldo}`);
        if (jsonData.saldoRestante !== undefined) console.log(` Saldo Restante: R$ ${jsonData.saldoRestante}`);
    } else {
        console.log(" [X] ERRO NA OPERAÇÃO");
        console.log(` Motivo: ${jsonData.mensagem || "Erro desconhecido"}`);
    }
    console.log("-----------------------------------------------------------------");
}

async function main() {
    console.log("=========================================");
    console.log("===   BEM-VINDO AO SISTEMA DO SEBO    ===");
    console.log("=========================================");
    
    const idCliente = await askQuestion("Digite o seu identificador: ");

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

        if (opcao === 0) {
            console.log("\nSaindo do sistema.");
            rl.close();
            break;
        }

        try {
            if (opcao === 1) {
                const res = await fetch(`${BASE_URL}/produtos`);
                formatarSaidaCatalogo(await res.json());

            } else if (opcao === 2) {
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
            console.log(`\nErro de ligação ao Servidor: ${error.message}`);
        }
    }
}

main();