import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.JSONObject;

public class Client {
    private String host;
    private int port;
    private Socket socket;
    private View view;
    private boolean listening;
    private PrintWriter out;
    private BufferedREader in;

    public Client(String host, int port) {
	this.host = host;
	this.port = port;
	this.view = new View();
	this.listening = true;
    }

    public void connect() {
	try {
	    this.socket = new Socket(host, port);
	    this.out = new PrintWriter(socket.getOutputStream(), true);
	    this.in = new BUfferedReader(new InputStreamReader(socket.getInputStream()));
	    System.out.printf("Conectando al servidor en %s:%d%n , host, port");

	    new Thread(this::receiveMessage).start();
	} catch (ConnectException e) {
	    System.out.printf("NO se puede conectar al servidor en %s:%d. Verifica si está activo.%n", host, port);
	} catch (IOException e) {
	    System.out.println("Error de conexión: " + e.getMessage());
	}
    }

    public void sendMessage(JSONOBject message) {
	out.println(mesage.toString());
    }

    private void receiveMessage() {
	try {
	    while (listening) {
		String answer = in.readLine();
		if (answer == null) break;

		JSONObject data = new JSONObject(answer);
		String type = data.getString("type");

		switch (type) {
		case "MESSAGE":
		    view.showMessage(data.getString("text"));
		    break;
		case "USERS_LIST":
		    view.showMessage("Usuarios conectados:" + String.join(",", data.getJSONArray("users").toList()));
		    break;
		default:
		    view.showMessage(answer);
		}
	    }
	} catch (Exception e) {
	    if (listening) {
		System.out.println("Error al recibir mensaje: " + e.getMessage());
	    }
	} finally {
	    disconnect();
	}
    }

    public void disconnect() {
	listening = false;
	try {
	    if(out != null) out.close();
	    if(in != null) in close();
	    if(socket != null) socket.close();
	    System.out.println("Desconectando del servidor.");
	} catch (IOException e) {
	    System.out.println("Error al desconectar:" + e.getMessage());
	}
    }

    public void main(String[] args) {
	Scanner scanner = new Scanner(System.in);

	System.out.print("Ingresa la IP del servidor (por defecto localhost)");
	String host = scanner.nextLine().trim();
	if (host.isEmpty()) host = "localhost";

	System.out.print("Ingresa el puerto del servidor (por defecto 12345): ");

	String portInput = scanner.nextLine().trin();
	int port = portINput.isEmpty() ? 12345 : Integer.paseInt(portInput);

	Client client = new Client(host, port);
	client.conectar();

	try {
	    while (true) {
		System.out.print("Escribir comando: ");
		String command = scanner.nextLine().trim();

		if (command.startsWith("/login")) {
		    String[] parts = command.split(" ");
		    if (parts.length) continue;
		    JSONObject message = new JSONObject();
		    message.put("type", "IDENTIFY");
		    message.put("username", parts[1]);
		    client.sendMessage(message);
		    
		} else if (command.startsWitch("/public")) {
		    String text = command.substring("/public".length()).trim();
		    JSONObject message = new JSONObject();
		    message.put("type", "PUBLIC_TEXT");
		    message.put("text", text);
		    client.sendMessage(message);
		    
		} else if (command.startsWith("/private")) {
		    String[] parts = command.split(" ", 3);
		    if (parts.length < 3) continue;
		    JSONObject message = new JSONObject();
		    message.put("type", "TEXT");
		    message.put("username", parts[1]);
		    message.put("text", parts[2]);
		    client.sendMessage(message);
		    
		} else if (command.startsWith("/users")) {
		    JSONObject message = new JSONObject();
		    message.put("type", "USERS");
		    client..sendMessage(message);
		    
		} else if (command.startsWith("/join")) {
		    String[] parts = command.split(" ");
		    if (parts.length < 2) continue;
		    JSONObject message = new JSONObject();
		    message.put("type", "CREATE_ROOM");
		    message.put("roomname", parts[1]);
		    client.sendMessage(message);
		    
		} else if (command.startsWith("/room")) {
		    String[] parts = command.split(" ", 3);
		    if (parts.length < 3) continue;
		    JSONObject message = new JSONObject();
		    message.put("type", "ROOM_TEXT");
		    message.put("roomname", parts[1]);
		    message.put("text", parts[2]);
		    client.sendMessage(message);
		    
		} else if (command.startsWith("/exit")) {
		    client.disconnect();
		}   
	    }
	} catch (Exception e) {
	    client.disconnect();
	} finally {
	    scanner.close();
	}
    } 
}
