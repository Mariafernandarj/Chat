import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageManager {
    private Map<String, Map<String, Object>> users;
    private Map<String, Map<String, Object>> invitations;
    private Map<String, Map<String, Object>>  rooms;
    
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
	for(Map.Entry<String, Map<String, Object>> entry : users.entrySet()) {
	    userList.put(entry.getKey(), (String) entry.getValue().get("status"));
	}

	JSONObject answer = new JSONObject();
	//Crear respuesta
	answer.put("type","USER_LIST");
	answer.put("users",userList);

	//Convertir a JSON y enviar
	Gson gson = new Gson();
	String json = gson.toJson(respuesta);

	try {
	    PrintWriter out =  new PrintWriter(client.getOutputStream(), true);
	    out.println(answer.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }
    
    public void sendPrivateText(Socket cliente, JSONObject message ) {
	String recipient = message.get("username");
	String text = message.get("text");
	
	try {
	    PrintWriter out =  new PrintWriter(client.getOutputStream(), true);
	     
	    if (users.containsKey(recipient)) {
		Socket clientDestination = usuarios.get(recipient).get("client");
		PrintWriter outDestination =  new PrintWriter(clientDestination.getOutputStream(), true);
	    
		JSONObject answer = new JSONObject();
		answer.put("type", "TEXT_FROM");
		answer.put("username", this.getUserByClient(client));
		answer.put("text", texto);
	    
		outDestination.println(answer.toString());
	    } else {
		JSONObject answer = new JSONObject();
		answer.put("type", "RESPONSE");
		answer.put("operation", "TEXT");
		answer.put("result", "NO_SUCH_USER");
		answer.put("extra", recipient);
	    
		out.println(answer.toString());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void sendPublicText(Client client, Map<String, String> message) {
	String text = message.get("text");
	String sender = getUserByClient(client);

	JSONObject answer = new JSONObject();
	answer.put("type", "Public_TEXT_FROM");
	answer.put("username",sender );
	answer.put("text", text);

	for(Map.Entry<String, Map<String, Object>> entry : users.entrySet()) {
	    try {
		Socket clientUser = (Socket) entry.getValue().get("client");
		PrintWriter out =  new PrintWriter(clientDestination.getOutputStream(), true);
		out.println(answer.toString());
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
    
    public void createRoom(Cliente client, JSONObject message) {
	String roomName = message.optString("roomname");

	try {
	    PrintWriter out =  new PrintWriter(client.getOutputStream(), true);
	    JSONObject answer = new JSONObject();
	    //crear sala clase salas
	    if (rooms.containsKey(roomName)) {
		answer.put("type", "RESPONSE");
		answer.put("operation", "NEW_ROOM");
		answer.put("result", "ROOM_ALREADY_EXISTS");
		answer.put("extra", roomName);
	    } else {
		List<String> members = new ArrayList<>();
		members.add(getUserByClient(client));
		rooms.put(roomName, members);
	    
		answer.put("type", "RESPONSE");
		answer.put("operation", "NEW_ROOM");
		answer.put("result", "SUCCESS");
		answer.put("extra", roomName);	    
	    }
	    out.println(answer.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void inviteUsers(Socket client, JSONObject message) {
	String roomName = message.optString();
    
	try {
	
	    PrintWriter out =  new PrintWriter(client.getOutputStream(), true);
	     
	    if (!rooms.containsKey(roomName)) {
		JSONObject answer = new JSONObject();
		answer.put("type", "RESPONSE");
		answer.put("operation", "INVITE");
		answer.put("result", "NO_SUCH_ROOM");
		answer.put("extra", roomName);

		out.println(answer.toString());
		return();
	    }

	    JSONArray guestsJson = message.getJSONArray("username");
	    List<String> guests = new ArrayList<>();
	    for (int i = 0; i < guestsJson.length(); i++) {
		guests.add(guestsJson.getString(i));
	    }
	
	    for (String guest : guests) {
		if (users.containsKey(guest)) {
		    if (!invitations.containsKey(roomName)) {
			invitations.put(roomName, nem ArrayList<>());
		    }
		    invitations.get(roomName).add(guest);
		
		    JSONObject invitation = new JSONObject();
		    invitation.put("type", "INVITATION");
		    invitation.put("username", guest );
		    invitation.put("roomname", roomName);

		    Socket invalidClient = (Socket) users.get(guest).get("client");
		    PrintWriter outGuest =  new PrintWriter(invalidClient.getOutputStream(), true);
		    invalidGuest.println(invitation.toString());
		} else {
		    JSONObject answer = new JSONObject();
		    answer.put("type", "RESPONSE");
		    answer.put("operation", "INVITE");
		    answer.put("result", "NO_SUCH_USER");
		    answer.put("extra", guest);
			 
		    out.println(respuesta.toString());
		    return;
		}
	    } 
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void joinARoom(Socket client, JSONObject message) {
	String roomName = message.optString("roomname");
	String username = getUserByClient(client);

	try {
	    PrintWriter out = new PrintWriter(client.getOutPutStream(), true);
	    JSONObject answer = new JSONObject();

	    if (rooms.containsKey(roomName) && invitations.containsKey(roomName) && invitations.get(roomName).contains(username)) {

		rooms.get(roomName).add(username);

		answer.put("type","RESPONSE");
		answer.put("operation","JOIN_ROOM");
		answer.put("result","SUCCESS");
		answer.put("extra", roomName );

		out.println(answer.toString());
		notifyUnionRoom(username, roomName);
	    } else {
		answer.put("type","RESPONSE");
		answer.put("operation","JOIN_ROOM");
		answer.put("result","NOT_INVITED");
		answer.put("extra", roomName );

		out.println(answer.toString());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void sendUserListToTheRoom(Socket client, JSONObject message) {
	String roomName = message.optString("roomname");

	try {
	  PrintWriter out = new PrintWriter(client.getOutPutStream(), true);
	  JSONObject answer = new JSONObject();

	  if (rooms.containsKey(roomName)) {
	      Map<String, String> roomUser = new HashMap<>();
	      for (String user : rooms.get(roomName)) {
		  roomUser.put(user, (String) users.get(user).get("status"));
	      }
	      answer.put("type","ROOM_USER_LIST");
	      answer.put("operation",roomName );
	      answer.put("users", roomUser);
	  } else {
	      answer.put("type","RESPONSE");
	      answer.put("operation", "ROOM_USERS");
	      answer.put("result", "NO_SUCH_ROOM");
	      answer.put("extra", roomName);
	  }
	  out.println(answer.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}	
    }
    
    public void sendTextToTheRoom(Socket client, JSONObject message) {
	String roomName = message.optString("roomname");
	String text = mesage.optString("text");
	String username = getUserByClient(client);

	try {
	    PrintWriter out = new PrintWriter(client.getOutPutStream(), true);
	    JSONObject answer = new JSONObject();

	    if (rooms.containsKey(roomName) && rooms.get(roomName).contains(username)) {
		JSONObject roomMessage = new JSONObject();
		roomMessage.put("type", "ROOM_TEXT_FROM");
		roomMessage.put("roomname", roomName);
		roomMessage.put("username", username);
		roomMessage.put("text", text);

		for (String user : rooms.get(roomName)) {
		    Socket clientDEstination = (Socket) users.get(user).get("client");
		    PrintWriter out = new PrintWriter(client.getOutPutStream(), true);
		    outDestination.println(messageRoom.toString());
		}
	    } else {
		answer.put("type","RESPONSE");
		answer.put("operation", "ROOM_USERS");
		if (!rooms.containsKey(roomName)) {
		    answer.put("result", "NO_SUCH_ROOM");
		} else {
		    answer.put("result", "NOT_IN_ROOM");
		}
		answer.put("extra", roomName);

		out.println(answer.toString());
	    }   
	}  catch (Exception e) {
	    e.printStackTrace();
	}	
    }
   
    //Métodos auxiliares
    
    private String getUserByClient(Socket client){
	for (Map.Entry<String, Map<String, Object>> entry : users.entrySet()) {
	    if (entry.getValue().get("client").equals(client)) {
		return entry.getKey();
	    }
	}
	return null;
    }

    private void notifyUnionRoom(String newUser, String roomName) {
	if (!rooms.containsKey(roomName)) return;

	JSONObject notification = new JSONObject();
	notification.put("type", "ROOM_NOTIFICATION");
	notification.put("roomname", roomName);
	notification.put("message", "El usuario " + newUser + "se ha unido a la sala");
	notification.put("new_user", newUser);

	for (String user : rooms.get(roomName)) {
	    if (!user.equals(newUser)) {
		try {
		    Socket client = (Socket) users.get(user).get("client");
		    PrintWriter out = new PrintWriter(client.getOutPutStream(), true);
		    out.println(answer.toString()); 
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }

}
