/**
  Copyright 2017 Niklas Graf
	
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
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
