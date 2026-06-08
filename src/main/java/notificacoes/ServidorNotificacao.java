package notificacoes;
import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;

public class ServidorNotificacao {
    private JChannel channel;

    public void iniciar() throws Exception {
        // Inicializa o canal com as configurações padrão (UDP)
        channel = new JChannel();

        // Conecta-se ao grupo. O nome "SeboCluster" identifica
        channel.connect("SeboCluster");
        System.out.println("Servidor ligado ao grupo JGroups!");
    }

    public void enviarNotificacaoGlobal(String mensagem) throws Exception {
        // Envia uma mensagem para o grupo inteiro (o destino null significa multicast)
        // O JGroups trata automaticamente a serialização do objeto (String neste caso)
        ObjectMessage msg = new ObjectMessage(null, mensagem);
        channel.send(msg);
        System.out.println("Notificação enviada: " + mensagem);
    }

    public void fechar() {
        channel.close();
    }
}