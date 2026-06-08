package notificacoes;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;

public class ClienteNotificacao implements Receiver {
    private JChannel channel;

    public void iniciar() throws Exception {
        channel = new JChannel();

        // Define esta classe como a receptora de eventos do canal
        channel.setReceiver(this);

        // Conecta ao mesmo grupo do servidor
        channel.connect("SeboCluster");
    }

    // Este metodo é chamado automaticamente sempre que chega uma nova mensagem
    @Override
    public void receive(Message msg) {
        // Lemos o objeto enviado (neste caso, a String da notificação)
        String notificacao = msg.getObject();
        System.out.println("\n[NOTIFICAÇÃO DO SEBO]: " + notificacao);
    }

    // O JGroups avisa quem entrou ou saiu do sistema
    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** Membros atuais no sistema: " + new_view.getMembers());
    }
}
