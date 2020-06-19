package administrator;

public class administrator {
	String a_id;
	String password;
	
	public administrator(String id, String password){
		a_id = id;
		this.password = password;
	}
	
	public String getA_id(){
		return a_id;
	}
	
	public String getA_password(){
		return password;
	}
}
