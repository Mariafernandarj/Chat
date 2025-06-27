import java.io.*;
import java.net.*;
import java.util.*;

public class MyThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<PrintWriter> clientWriters;
    
    MyThread(Socket socket){
	this.socket = socket;
	this.clientWriters = clientWriters;
    }

    @Override
    public void run(){
	try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
	    out = new PrintWriter(socket.getOutputStream(), true);
	    synchronized(clientWriters){
		clientWriters.add(out);
	    }
	    String message;
	    while((message = in.readLine()) != null){
		System.out.println("Mensaje recibido" + message);
		synchronized(clientWriters){
		    for(PrintWriter writer : clientWriters){
			writer.print(message);
		    }
		}
	    }
	}catch(IOException e){
	    System.out.println("Error con cliente :(" + e.getMessage());
	}finally{
	    try {
		socket.close();
	    } catch(IOException e) {}
		synchronized(clientWriters){
		    clientWriters.remove(out);
		}
		System.out.println("Cliente desconectado");
	}
    }
}
