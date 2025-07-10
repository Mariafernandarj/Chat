import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;


public class MessageManager {
    private Map<String, Object> users;
    private Map<String, Object> rooms;
    private Map<String, Object> invitations;

    private MessageManager() {
	this.users = new HashMap<>();
	this.rooms = new HashMap<>();
	this.invitations = new HashMap<>();
    }

    public processMessage(Client client, String jsonMessage) {
	try {
	    JSONObject message = new JSONObject(jsonMessage);
	    String type = message.optString("type");

	    switch () {
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
	   sendInvalidResponse(client); 
	}
    }

    public void identifyUser(Client client, JSONObject message) {
	throws IOException {
	    String username = message.optString("username");
	    Map<String, Object> answer = new HashMap<>();

	    if (users.containsKey(username)) {
		//Usuario ya existe
		answer.put("type","RESPONSE");
		answer.put("operation","IDENTIFY");
		answer.put("result","USER_ALREADY_EXISTS");
		answer.put("extra",username);
	    } else {
		//Nuevo usuario
		User newUser = new User(username, "ACTIVE", client);
		users.put(username, newUser);

		answer.put("type","RESPONSE");
		answer.put("operation","IDENTIFY");
		answer.put("result","SUCCESS");
		answer.put("extra",username);

		notifyNewUser(username); //notificar_nuevo_usuario
	    }
	    Gson gson = new Gson();
	    String json = gson.toJson(answer);
	    
	    PrintWriterbout = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true);
	    out.println(json);
	}
    }
    
    public void changeStatus(Client client, JSONObject message) {
	throws IOException {
	    String username = this.getUserByClient(client); //obtener_usuario_por_cliente
	    if (username != null && !username.isEmpty()) {
		String newStatus = message.optString("status");
		users.get(username).put("status", newStatus) ;
		notifyNewStatus(username, newStatus);
	    }
	}
    }
    
    public void sendUserList(Cliente cliente) {
	Map<String, String> userList = new HashMap<>();
	for(Map<String, User> entry : users.entrySet()) {
	    userList.put(entry.getKey(), entry.getValue().getStatus());
	    //Crear respuesta
	    Map<String, Object>  answer = new HashMap<>();
	    answer.put("type","USER_LIST");
	    answer.put("users",userList);

	    //Convertir a JSON y enviar
	    Gson gson = new Gson();
	    String json = gson.toJson(respuesta);

	    try {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"),true );
		out.println(json);
 	    } catch(IOException e) {
		e.printStackTrace();	
	    }  
	}
    }
    public void sendPrivateText(Cliente cliente, JSONObject message ) {
	String recipient = message.optString("username");
	String text = message.optString("text");
	if (users.containsKey(recipient)) {
	    Cliente clientDestination = this.usuarios(recipient, "client");
	    Map<String, Object>  answer = new HashMap<>();
	    answer.put("type", "TEXT_FROM");
	    answer.put("username", this.getUserByClient(client));
	    answer.put("text", texto);
	    clientDestination.
	} else {
	    answer.put("type", "RESPONSE");
	    answer.put("operation", "TEXT");
	    answer.put("result", "NO_SUCH_USER");
	    answer.put("extra", recipient);
	}
    }
    public void sendPublicText() {}
    public void createRoom() {}
    public void inviteUsers() {}
    public void joinARoom() {}
    public void sendUserListToTheRoom() {}
    public void sendTextToTheRoom() {}
    public void leaveRoom() {}
    public void disconnectUser(){}

}
