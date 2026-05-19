package vendas;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private int messageType; // 0 = Request, 1 = Reply
    private int requestId;
    private String objectReference; // Nome do objeto que fornece o serviço
    private int methodId;         
    private byte[] arguments; // Array de bytes contendo o JSON

    public Mensagem(int messageType, int requestId, String objectReference, int methodId, byte[] arguments) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.objectReference = objectReference;
        this.methodId = methodId;
        this.arguments = arguments;
    }

    public int getMessageType() { return messageType; }
    public int getRequestId() { return requestId; }
    public String getObjectReference() { return objectReference; }
    public int getMethodId() { return methodId; }
    public byte[] getArguments() { return arguments; }
}