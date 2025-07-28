import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Iterator;

public class ClientMessageManager {
    private View view;

    public void processMessage(JSONObject message) {
	String type = message.optString("type");

	switch (type) {
	case "NEW_USER":
	    manageNewUser(message);
	    break;
	case "NEW_STATUS":
	    manageStatusChange(message);
	    break;
	case "USER_LIST":
	    manageUserList(message);
	    break;
	case "TEXT_FROM":
	    managePrivateMessage(message);
	    break;
	case "PUBLIC_TEXT_FROM":
	    managePublicMessage(message);
	    break;
	case "INVITATION":
	    manageInvitation(message);
	    break;
	case "JOINED_ROOM":
	    manageUnionRoom(message);
	    break;
	case "ROOM_USER_LIST":
	    manageRoomUserList(message);
	    break;
	case "ROOM_TEXT_FROM":
	    manageRoomText(message);
	    break;
	case "LEFT_ROOM":
	    manageRoomAbandonment(message);
	    break;
	case "DISCONNECTED":
	    manageDisconnection(message);
	    break;
	default:
	    System.out.println("Mensaje desconocido: " + message.toString());
	}
    }

    private void manageNewUser(JSONObject message) {
	String username = message.optString("username");
	view.showMessage("Nuevo usuario conectado: " + username);
    }

    private void manageStatusChange(JSONObject message) {
	String username = message.optString("username");
	String newStatus = message.optString("status");
	view.showMessage(username + "ha cambiado su estado a: " + newStatus);
    }

    private void manageUserList(JSONObject message) {
	JSONObject userList = message.optJSONObject("users");
	view.showMessage("Usuarios conectados:");

	if(userList != null) {
	    Iterator<String> keys = userList.keys();
	    while(keys.hasNext()) {
		String users = keys.next();
		String status = userList.optStrig(user);
		view.showMessage(user + " - " + status);
	    }
	}	
    }

    private void managePrivateMessage(JSONObject message) {
	String username = message.optString("username");
	String text = message.optString("text");
	view.showMessage("Mensaje privado de " + username + ": " + text);	
    }

    private void  managePublicMessage(JSONObject message) {
	String username = message.optString("username");
	String text = message.optString("text");
	view.showMessage("Mensaje privado de " + username + ": " + text);	
    }

    private void manageInvitation(JSONObject message) {
	String username = message.optString("username");
	String room = message.optString("roomname");
	view.showMessage("Has sido invitado por  " + username + " a unirte a la sala" + room);
    }
    
    private void manageUnionRoom(JSONObject message) {
	String username = message.optString("username");
	String room = message.optString("roomname");
	view.showMessage(username + " se ha unido a la sala" + room);
    }
    
    private void manageRoomUserList(JSONObject message) {
	String room = message.optString("roomname");
	JSONObject roomUsers = message.optJSONObject("users");
	view.showMessage(username + "Usuarios en la sala " + room + ":");
	if (roomUsers != null) {
	    Iterator<String> keys = roomUsers.keys();
	    while (keys.hasNext()) {
		String user = keys.next();
		String status = roomUsers.optString(user);
		view.showMessage(user + " - " + status);
	    }
	}
    }
    
    private void manageRoomText(JSONObject message) {
	String username = message.optString("username");
	String text = message.optString("text");
	String room = message.optString("roomname");
	view.showMessage(username + " en " + room + ": " + text);
    }
    
    private void manageRoomAbandonment(JSONObject message) {
	String username = message.optString("username");
	String room = message.optString("roomname");
	view.showMessage(username + " ha abandonado la sala " + room + ": " + text);
    }
    
    private void manageDisconnection(JSONObject message) {
	String username = message.optString("username");
	view.showMessage(username + " se ha desconectado");
    }

}
