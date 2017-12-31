package at.aems.apilib;

import java.security.SecureRandom;
import java.util.Random;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class AbstractAemsAction {
	
	private AemsUser user;
	private String action;
	private boolean saltEnabled;
	protected GsonBuilder builder;

	
	public AbstractAemsAction(AemsUser user, String action) {
		this.user = user;
		this.action = action;
		this.saltEnabled = true;
		builder = new GsonBuilder().setPrettyPrinting()
				.serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
	}
	
	public String toJson() {
		JsonObject object = new JsonObject();
		object.addProperty("user_id", user.getUserId());
		String salt = isSaltEnabled() ? createSalt() : null;
		if(isSaltEnabled()) {
			object.addProperty("salt", salt);
		}
		object.addProperty("auth_str", user.getAuthString(salt));
		object.addProperty("action", action);
		JsonElement data = serializeData();
		object.add("data", data);
		return builder.create().toJson(object);
	}
	
	public boolean isSaltEnabled() {
		return saltEnabled;
	}
	
	public void setSaltEnabled(boolean enable) {
		this.saltEnabled = enable;
	}
	
	public void disableSalt() {
		this.saltEnabled = false;
	}
	
	public abstract JsonElement serializeData();
	
	private String createSalt() {
		Random rand = new SecureRandom();
		StringBuffer buf = new StringBuffer();
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		for(int j = 0; j < 16; j++) {
			buf.append(s.charAt(rand.nextInt(s.length())));
		}
		return buf.toString();
	}
}
