import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

class TestServer{
    @Test
    void clientCanConnect()throws IOException {
	//Inicia el servidor en un hilo diferente
	new Thread(servidor::start).start();

	try {
	    //Espera a que el servidor arranque
	    Tread.sleep(300);
	    //Cliente se conecta
	    Socket socket = new Socket("127.0.0.1", 1234);
	    assertTrue(socket.isConnected());
	    socket.close();
	}catch (IOException e){
	    fail("Error en test:" + e.getMessage());
	}                     
    }
    
}
