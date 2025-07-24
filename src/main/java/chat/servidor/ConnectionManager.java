import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager{
    private List<Socket> clients;
    private MessageManager messageManager;

    public void ConnectionManager() {
	this.clients = new ArrayList<>();
	this.messageManager = new MessageManager();
    }

    public void newConnection() {
	Thread clientTread = new Thread(() -> manageClient(client));
	clientThread.start();
    }

    private void managerClient(Socket client) {
	synchronized (clients) {
	    clients.add(client);
	}

	try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
	    String message;
	    while ((message = in.redaLine()) != null) {
		messageManager.processMessage(client, message);
	    }
	} catch (IOException e) {
	    System.err.println("Error en la conexión con el cliente:" + e.getMessage());
	} finally {
	    synchronized (clients) {
		clients.remove(client);
	    }
	    try {
		if (!client.isClosed()) {
		    client.close();
		}
	    } catch (IOException e) {
		System.err.println("Error al cerrar el socket: " + e.getMessage());
	    }
	}
    }
    
}
