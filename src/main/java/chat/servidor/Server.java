import java.net.*;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;

public class Server {
    private static final int PORT = 1234;
    public static final Set<PrintWriter> clientWriters = new HashSet<>(); // Colección que almacena los flujos de saliada de todos los clientes
    
    public static void main(String[] args) {
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
}
