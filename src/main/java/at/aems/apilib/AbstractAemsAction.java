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
import java.util.Base64;
import java.util.Random;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.aems.apilib.crypto.EncryptionType;

public abstract class AbstractAemsAction {
	
	private AemsUser user;
	private String action;
	private boolean saltEnabled;
	protected GsonBuilder builder;
	private EncryptionType encryptionType;
	
	public AbstractAemsAction(AemsUser user, String action, EncryptionType encryption) {
		this.user = user;
		this.action = action;
		this.saltEnabled = false;
		this.encryptionType = encryption;
		builder = new GsonBuilder().setPrettyPrinting()
				.serializeNulls()
				.disableHtmlEscaping()		// In order to keep gson from serializing '=' into '\u003d'
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
	}
	
	public JsonObject toJsonObject() {
		JsonObject object = new JsonObject();
		if(user != null) {
			serializeUserCredentials(object);
		}
		object.addProperty("action", action);
		JsonElement data = serializeData();
		object.add("data", data);
		return object;
	}
	
	private JsonObject serializeUserCredentials(JsonObject object) {
		object.addProperty("user_id", user.getUserId());
		String salt = isSaltEnabled() ? createSalt() : null;
		if(isSaltEnabled()) {
			object.addProperty("salt", salt);
		}
		object.addProperty("auth_str", user.getAuthString(salt));
		return object;
	}
	
	/**
	 * Converts this AemsAction object into a JSON string with all neccessary fields.
	 * @param encryptionKey The key to encrypt the "data" portion of the action.
	 *        If SSL "encryption" is used, this can be null
	 * @return The JSON string repesentation of this object
	 */
	public String toJson(byte[] encryptionKey) {
		JsonObject object = toJsonObject();
		if(encryptionType == EncryptionType.AES) {
			object.remove("auth_str");		// Auth string is not needed in AES encryption
		}
		String data = dataToString(object);
		byte[] encrypted = encryptionType.getImplementation()
				.encrypt(encryptionKey, data.getBytes());
		object.remove("data");
		object.addProperty("data", bytesToString(encrypted));
		return builder.create().toJson(object);
	}

	private String dataToString(JsonObject object) {
		JsonElement e = object.get("data");
		if(e.isJsonPrimitive())
			return e.getAsString();
		return e.toString();
	}

	private String bytesToString(byte[] bytes) {
		if(encryptionType != EncryptionType.SSL) {
			return Base64.getUrlEncoder().encodeToString(bytes);
		}
		return new String(bytes);
	}
	
	public boolean isSaltEnabled() {
		return saltEnabled;
	}
	
	public void disableSalt() {
		this.saltEnabled = false;
	}
	
	public void enableSalt() {
		this.saltEnabled = true;
	}
	
	public EncryptionType getCrypto() {
		return this.encryptionType;
	}
	
	public abstract JsonElement serializeData();
	
	public abstract String getHttpVerb();
	
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
