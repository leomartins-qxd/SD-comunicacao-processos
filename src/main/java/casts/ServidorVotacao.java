package casts;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorVotacao {
    private static Map<String, Integer> candidatos = new HashMap<>();
    private static boolean votacaoAberta = true;
    private static final int PORTA_TCP = 6789;
    private static final String MULTICAST_ADDR = "239.0.0.1";
    private static final int PORTA_UDP = 12347;

    public static void main(String[] args) {
        candidatos.put("Memórias Póstumas de Brás Cubas", 0);
        candidatos.put("A Divina Comédia", 0);

        new Thread(() -> {
            try {
                Thread.sleep(120000);
                votacaoAberta = false;
                calcularResultados();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();

        try (ServerSocket serverSocket = new ServerSocket(PORTA_TCP)) {
            System.out.println("Servidor de Votação iniciado na porta " + PORTA_TCP);

            while (votacaoAberta) {
                Socket clienteSocket = serverSocket.accept();
                new Thread(new EleitorHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Votação encerrada.");
        }
    }

    private static void calcularResultados() {
        System.out.println("=== RESULTADO FINAL ===");
        candidatos.forEach((k, v) -> System.out.println(k + ": " + v + " votos"));
    }

    public static synchronized void adicionarCandidato(String nome) {
        if (votacaoAberta && !candidatos.containsKey(nome)) {
            candidatos.put(nome, 0);
            System.out.println("Admin adicionou candidato: " + nome);
        }
    }

    public static synchronized void removerCandidato(String nome) {
        if (votacaoAberta) {
            candidatos.remove(nome);
            System.out.println("Admin removeu candidato: " + nome);
        }
    }

    static class EleitorHandler implements Runnable {
        private Socket socket;
        public EleitorHandler(Socket s) { this.socket = s; }

        @Override
        public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                String comando = (String) in.readObject();

                if ("LOGIN_ELEITOR".equals(comando)) {
                    String login = (String) in.readObject();

                    if (votacaoAberta) {
                        List<String> listaNomes = new ArrayList<>(candidatos.keySet());
                        out.writeObject(listaNomes);
                        out.flush();

                        Object inputVoto = in.readObject();
                        int indice = Integer.parseInt(inputVoto.toString());

                        if (indice >= 0 && indice < listaNomes.size()) {
                            String nomeEscolhido = listaNomes.get(indice);
                            synchronized (candidatos) {
                                candidatos.put(nomeEscolhido, candidatos.get(nomeEscolhido) + 1);
                            }
                            out.writeObject("Voto confirmado em: " + nomeEscolhido);
                        } else {
                            out.writeObject("Erro: Índice de candidato inválido.");
                        }
                    } else {
                        out.writeObject("ERRO: Votação encerrada.");
                    }
                    out.flush();
                }

                else if ("ADD_CANDIDATO".equals(comando)) {
                    String novo = (String) in.readObject();
                    synchronized (candidatos) {
                        if (votacaoAberta) {
                            candidatos.putIfAbsent(novo, 0);
                            out.writeObject("Candidato '" + novo + "' adicionado.");
                        } else {
                            out.writeObject("Erro: Votação já encerrada.");
                        }
                    }
                }
                else if ("REMOVE_CANDIDATO".equals(comando)) {
                    String alvo = (String) in.readObject();
                    synchronized (candidatos) {
                        if (votacaoAberta) {
                            if (candidatos.remove(alvo) != null) {
                                out.writeObject("Candidato '" + alvo + "' removido.");
                            } else {
                                out.writeObject("Erro: Candidato não encontrado.");
                            }
                        } else {
                            out.writeObject("Erro: Votação já encerrada.");
                        }
                    }
                }

                out.flush();

            } catch (EOFException e) {
                System.err.println("[SERVER] Cliente desconectou precocemente.");
            } catch (Exception e) {
                System.err.println("[SERVER] Erro no processamento: " + e.getMessage());
            }
        }
    }
}