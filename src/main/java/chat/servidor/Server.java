import java.net.*;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;

public class Server {
    private final int PORT;
    public static final Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>()); // Colección que almacena los flujos de saliada de todos los clientes

    public Server(int PORT){
	this.PORT = PORT;
    }

    public void start(){
	System.out.println("Servidor iniciado en el puerto" + PORT);
	//Se crea un serverSocket en el puerto especificado y se cierra automaticamete 
	try (ServerSocket serverSocket = new ServerSocket(PORT)) {
	    //Mantiene al servidor funcionando indefinidamente
	    while(true) {
		Socket client = serverSocket.accept();
		System.out.println("Cliente conectado: " + client.getInetAddress());
		new MyThread(client).start();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    public static void main(String[] args) {
	new Server(1234).start();
    }
}
