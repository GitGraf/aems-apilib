package at.aems.apilib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AemsRegisterAction extends AemsNoAuthAction {

	private String username;
	private String password;
	private String email;
	private String postalCode;
	
	public AemsRegisterAction() {
		super("REGISTER");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPlz() {
		return postalCode;
	}

	public void setPlz(String code) {
		this.postalCode = code;
	}

	@Override
	public JsonElement serializeData() {
		JsonObject object = new JsonObject();
		object.addProperty("username", username);
		object.addProperty("password", password);
		object.addProperty("email", email);
		object.addProperty("postcode", postalCode);
		return object;
	}
	
}
