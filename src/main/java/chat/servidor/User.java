public class User {
    private String name;
    private String status;
    private Client client;

    public User(String name, String status, Cliente client){
	this.name = name;
	this.status = status;
	this.client = client;
    }

    public String getName() {
	return name;
    }
    public String Status() {
	return status;
    }
    public Cliente getClient(){
	return client;
    }
    public void setName(String name) {
	this.name = name;
    }
    public void setStatus(String status) {
	this.status = status;
    }
    public void setCliente(Client client){
	this.client = client;
    }
}
