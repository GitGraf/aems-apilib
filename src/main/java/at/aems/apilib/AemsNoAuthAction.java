package at.aems.apilib;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class AemsNoAuthAction {
	
	private String action;
	
	protected transient GsonBuilder builder;
	
	public AemsNoAuthAction(String action) {
		this.action = action;
		builder = new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
	}
	
	public abstract JsonElement serializeData();
	
	public String toJson() {
		JsonObject object = new JsonObject();
		object.addProperty("action", action);
		object.add("data", serializeData());
		return builder.create().toJson(object);
				
	}
}
