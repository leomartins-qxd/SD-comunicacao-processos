package casts;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Administrador {
    private static final String MULTICAST_ADDR = "239.0.0.1";
    private static final int PORTA_UDP = 12347;
    private static final int PORTA_TCP = 6789;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Painel Administrativo ---");
            System.out.println("1. Enviar Nota Informativa (Multicast)");
            System.out.println("2. Adicionar Candidato (TCP)");
            System.out.println("3. Remover Candidato (TCP)");
            System.out.print("Escolha: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            try {
                if (opcao == 1) {
                    enviarNotaMulticast(scanner);
                } else if (opcao == 2 || opcao == 3) {
                    gerirCandidatosTCP(opcao, scanner);
                }
            } catch (Exception e) {
                System.out.println("Erro na operação: " + e.getMessage());
            }
        }
    }

    private static void enviarNotaMulticast(Scanner sc) throws Exception {
        InetAddress addr = InetAddress.getByName(MULTICAST_ADDR);
        DatagramSocket ds = new DatagramSocket();
        System.out.print("Texto da nota: ");
        String nota = sc.nextLine();
        byte[] b = nota.getBytes();
        ds.send(new DatagramPacket(b, b.length, addr, PORTA_UDP));
        ds.close();
    }

    private static void gerirCandidatosTCP(int opcao, Scanner sc) throws Exception {
        try (Socket socket = new Socket("localhost", PORTA_TCP);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            if (opcao == 2) {
                out.writeObject("ADD_CANDIDATO");
                System.out.print("Nome do novo candidato: ");
                out.writeObject(sc.nextLine());
            } else {
                out.writeObject("REMOVE_CANDIDATO");
                System.out.print("Nome do candidato a remover: ");
                out.writeObject(sc.nextLine());
            }
            System.out.println("Resposta do Servidor: " + in.readObject());
        }
    }
}