package jogoDaVelhaSocket.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


// recepcao é p o servidor se comunicar com o cliente
public class Recepcao extends Thread {
	
    private final DatagramSocket socket;
    private final Queue<Pacote> fila;

    public Recepcao(DatagramSocket socket) {
    	
        this.socket = socket;
        this.fila = new ArrayBlockingQueue<>(10);
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
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        
    }
    
    
    
    public synchronized Pacote receberMensagem() {
        Pacote pacote = this.fila.poll();
        return pacote;
    }


}