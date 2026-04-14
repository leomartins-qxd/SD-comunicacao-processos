package casts;

import java.io.*;
import java.net.*;
import java.util.*;

public class Eleitor {
    private static volatile boolean jaVotou = false;

    public static void main(String[] args) {
        new Thread(() -> {
            try (MulticastSocket mcs = new MulticastSocket(12347)) {
                InetAddress grp = InetAddress.getByName("239.0.0.1");
                mcs.joinGroup(grp);

                while (true) {
                    byte[] rec = new byte[256];
                    DatagramPacket pkg = new DatagramPacket(rec, rec.length);
                    mcs.receive(pkg);

                    String nota = new String(pkg.getData(), 0, pkg.getLength());

                    System.out.println("\n\n[NOTA ADM]: " + nota);

                    if (!jaVotou) {
                        System.out.print("Sua opção: ");
                    }
                }
            } catch (Exception e) {  }
        }).start();

        try (Socket socket = new Socket("localhost", 6789);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Scanner scanner = new Scanner(System.in);

            out.writeObject("LOGIN_ELEITOR");
            out.flush();

            System.out.print("Login: ");
            out.writeObject(scanner.nextLine());
            out.flush();

            Object resposta = in.readObject();

            if (resposta instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> lista = (List<String>) resposta;

                System.out.println("Por favor, vote no livro que mais deseja que comece\n" +
                        " a ser ofertado em nossos serviços:");
                System.out.println("\n=== CANDIDATOS ===");
                for (int i = 0; i < lista.size(); i++) {
                    System.out.println(i + " - " + lista.get(i));
                }

                System.out.print("\nSua opção: ");
                String indexVoto = scanner.nextLine();

                jaVotou = true;

                out.writeObject(indexVoto);
                out.flush();

                System.out.println("\nResultado final: " + in.readObject());
            } else {
                jaVotou = true;
                System.out.println("\nServidor: " + resposta);
            }

        } catch (Exception e) {
            jaVotou = true;
            System.out.println("\nErro: Votação encerrada ou servidor offline.");
        }
    }
}