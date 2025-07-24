import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionsManager{
    private List<Socket> clients;
    private MessageManager messageManager;

    public void ConnectionsManager() {
	this.clients = new ArrayList<>();
	this.messageManager = new MessageManager();
    }

    public void newConnection() {
	Thread 
    }
    

    
    
}
