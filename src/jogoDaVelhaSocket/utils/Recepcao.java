package jogoDaVelhaSocket.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


// reepcao é p o servidor se comunicar com o cliente
public class Recepcao extends Thread {
	
    private final DatagramSocket socket;
    private final Queue<Pacote> fila;

    public Recepcao(DatagramSocket socket) {
    	System.out.println("era p pegar");
        this.socket = socket;
        this.fila = new ArrayBlockingQueue<>(10);
        System.out.println("iniciar thread da recepcao");
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];

                
                
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                this.socket.receive(receivePacket);
                

                ByteArrayInputStream byteInStream = new ByteArrayInputStream(buffer);
                ObjectInputStream objectInStream = new ObjectInputStream(byteInStream);
                Mensagem msg = (Mensagem) objectInStream.readObject();

                this.fila.offer(new Pacote(receivePacket.getAddress(), receivePacket.getPort(), msg));
                System.out.println("o que o cliente digitou" + msg.getFields()[0]);
                System.out.println("metodo run de recepcap ip e porta" + receivePacket.getAddress() + "olha a porta " +receivePacket.getPort());
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        
    }
    
    
    
    public synchronized Pacote receberMensagem() {
        Pacote pacote = this.fila.poll();
        
        if (pacote != null) {
          //  System.out.println("Recebeu o pacote: " + pacote.message().getFields()[0]);
        } else {
          //  System.out.println("Fila vazia, nenhum pacote disponível.");
        }
        
        return pacote;
    }


}