import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageManager {
    private Map<String, Map<String, Object>> users;
    private Map<String, Map<String, Object>> invitations;
    private Map<String, Map<String, Object>>  answer;
    
    // private JSONObject answer = new JSONObject();
    // private String username = jsonMessage.optString("username");
    // private PrintWriter out = new PrintWriter(client.getOutoutStream(), true);
    private MessageManager() {
	this.users = new HashMap<>();
	this.rooms = new HashMap<>();
	this.invitations = new HashMap<>();
    }

    public processMessage(Socket client, String jsonMessage) {
	JSONObject message = new JSONObject(jsonMessage);
	String type = message.optString("type");
       	
	try {
	    PrintWriter out = new PrintWriter(client.getOutoutStream(), true);
	
	    switch (type) {
	    case "IDENTIFY" :
		identifyUser(client, message); //identificar usuarios
		break;
	    case "STATUS" :
		changeStatus(client, message); //cambiar estado
		break;
	    case "USERS" :
		sendUserList(client);//enviar lista usuarios
	        break;	
	    case "TEXT" :
		sendPrivateText(client, message); //enviar texto privado
		break;
	    case "PUBLIC_TEXT" :
		sendPublicText(client, message);//enviasr texto publico
		break;
	    case "NEW_ROOM" :
		createRoom(client, message);//crear sala
		break;
	    case "INVITE" :
		inviteUsers(client, message);//invitar usuarios
	    case "JOIN_ROOM" :
		joinARoom(client, message);//unirse a sala
		break;
	    case "ROOM_USERS" :
		sendUserListToTheRoom(client, message);//enviar_lista_usuarios_sala
		break;
	    case "ROOM_TEXT" :
		sendTextToTheRoom(client, message);//enviar_texto_sala
		break;
	    case "LEAVE_ROOM" :
		leaveRoom(client, message);//dejar_sala
		break;
	    case "DISCONNECT" :
		disconnectUser(client);//desconectar_usuario
		break;
	    default:
		sendInvalidResponse(client);//enviar_respuesta_invalida
	    }	    
	} catch (IOException e) {
	    e.printStackTrace(); 
	}
    }

    public void identifyUser(Socket client, JSONObject message) {
	String username = message.optString("username");
	JSONObject answer = new JSONObject();
	
	try {
	    PrintWriter out = new PrintWriter(client.getOutoutStream(), true);
	    
	    if (users.containsKey(username)) {
		//Usuario ya existe
		answer.put("type","RESPONSE");
		answer.put("operation","IDENTIFY");
		answer.put("result","USER_ALREADY_EXISTS");
		answer.put("extra",username);
	    } else {
		//Nuevo usuario
		Map<String, Object> userData = new HasMap<>();
		userData.put(username, newUser);

		userData.put("type","RESPONSE");
		userData.put("operation","IDENTIFY");
		userData.put("result","SUCCESS");
		userData.put("extra",username);

		notifyNewUser(username); //notificar_nuevo_usuario
	    }
	    out.println(answer.toString());   
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void changeStatus(Socket client, JSONObject message) {
	    String username = getUserByClient(client); //obtener_usuario_por_cliente
	    if (username != null && !username.isEmpty()) {
		String newStatus = message.optString("status");
		users.get(username).put("status", newStatus) ;
		notifyNewStatus(username, newStatus);
	    }
    }
    
    public void sendUserList(Cliente cliente) {
	Map<String, String> userList = new HashMap<>();
	for(Map<String, User> entry : users.entrySet()) {
	    userList.put(entry.getKey(), entry.getValue().getStatus());
	    //Crear respuesta
	    answer.put("type","USER_LIST");
	    answer.put("users",userList);

	    //Convertir a JSON y enviar
	    Gson gson = new Gson();
	    String json = gson.toJson(respuesta);

	    sendJsonResponse(client, answer);
	}
    }
    
    public void sendPrivateText(Cliente cliente, Map<String, String> message ) {
	String recipient = message.get("username");
	String text = message.get("text");
	
	if (users.containsKey(recipient)) {
	    Cliente clientDestination = usuarios.get(recipient).getClient();
 
	    answer.put("type", "TEXT_FROM");
	    answer.put("username", this.getUserByClient(client));
	    answer.put("text", texto);
	    
	    sendJsonResponse(clientDestination, answer);
	} else {
	    answer.put("type", "RESPONSE");
	    answer.put("operation", "TEXT");
	    answer.put("result", "NO_SUCH_USER");
	    answer.put("extra", recipient);
	    
	    sendJsonResponse(clientDestination, answer);
	}
    }
    public void sendPublicText(Client client, Map<String, String> message) {
	String text = message.get("text");
	
	answer.put("type", "Public_TEXT_FROM");
	answer.put("username", getUserByClient(client));
	answer.put("text", text);

	for(Map<String, User> entry : users.entrySet()) {
	    Client clientDestination = entry.getValue().getClient();
	    sendJsonResponse(clientDestination, answer);
	}
    }
    
    public void createRoom(Cliente client, Map<String, String> message) {
	String roomName = message.optString("roomname");

	//crear sala clase salas
	if (rooms.containsKey(roomName)){
	    answer.put("type", "RESPONSE");
	    answer.put("operation", "NEW_ROOM");
	    answer.put("result", "ROOM_ALREADY_EXISTS");
	    answer.put("extra", roomName);
	} else {
	    String user = getUserByClient(client);

	    answer.put("type", "RESPONSE");
	    answer.put("operation", "NEW_ROOM");
	    answer.put("result", "SUCCESS");
	    answer.put("extra", roomName);

	    sendJsonResponse(client, answer);
	}
    }
    public void inviteUsers(Client client, Map<String, String> message) {
	String roomName = message.optString();
    }
    public void joinARoom(Client client, Map<String, String> message ) {
	
    }
    public void sendUserListToTheRoom(Client client, Map<String, String> message) {
	
    }
    public void sendTextToTheRoom(Client client, Map<String, String> message) {
	
    }
    //Métodos auxiliares
    private void sendJsonResponse(Client client, Map<String, Object> response) {
	Gson gson = new Gson();
	try {
	    PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"),true );
	    out.println(gson.toJson(response));
	} catch(IOException e) {
	    e.printStackTrace();
	    return false;
	}   
    }

    private String getUserForSocket(Socket client) {
	return usersForSocket.getOrDefault(client, "unknown");
    }

    def obtener_usuario_por_cliente(self, cliente):
    for username, datos in self.usuarios.items():
        if datos["cliente"] == cliente:
            return username
    return None

}
