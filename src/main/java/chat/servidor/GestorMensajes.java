import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;


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
		identifyUser(client, message);
		break;
	    case "STATUS" :
		changeStatus(client, message);
		break;
	    case "USERS" :
		sendUserList(client);
	        break;	
	    case "TEXT" :
		sendPrivateText(client, message);
		break;
	    case "PUBLIC_TEXT" :
		sendPublicText(client, message);
		break;
	    case "NEW_ROOM" :
		createRoom(client, message);
		break;
	    case "INVITE" :
		inviteUsers(client, message);
	    case "JOIN_ROOM" :
		joinARoom(client, message);
		break;
	    case "ROOM_USERS" :
		sendUserListToTheRoom(client, message);
		break;
	    case "ROOM_TEXT" :
		sendTextToTheRoom(client, message);
		break;
	    case "LEAVE_ROOM" :
		leaveRoom(client, message);
		break;
	    case "DISCONNECT" :
		disconnectUser(client);
		break;
	    default:
		sendInvalidResponse(client);
	    }	    
	} catch (IOException e) {
	   sendInvalidResponse(client); 
	}
    }

    public void identifyUser(Client client, JSONObject message) {
	throws IOException {
	    String username = mensaje.optString("username");
	    JSONObject answer = new JSONObject();

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
		answer.put("result","USER_ALREADY_EXISTS");
		answer.put("extra",username);

		notifyNewUser(username);
	    }
	    //Enviar respuesta al cliente
	    client.send(respuesta.toString());
	}
    }
    public void changeStatus(Client client, JSONObject message) {
	
    }
    public void sendUserList() {}
    public void sendPrivateText() {}
    public void sendPublicText() {}
    public void createRoom() {}
    public void inviteUsers() {}
    public void joinARoom() {}
    public void sendUserListToTheRoom() {}
    public void sendTextToTheRoom() {}
    public void leaveRoom() {}
    public void disconnectUser(){}

}
