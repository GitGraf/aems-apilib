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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AemsLoginAction extends AemsNoAuthAction {

	private String username;
	private String password;
	private String salt;
	
	public AemsLoginAction() {
		this(null, null);
	}
	
	public AemsLoginAction(String username, String password) {
		super("LOGIN");
		this.username = username;
		this.password = password;
		this.salt = createSalt();
	}
	
	public void setUsername(String user) {
		username = user;
	}
	
	public void setPassword(String pass) {
		password = pass;
	}

	@Override
	public JsonElement serializeData() {
		JsonObject obj = new JsonObject();
		obj.addProperty("username", username);
		obj.addProperty("auth_str", getAuthString());
		obj.addProperty("salt", salt);
		return obj;
	}

	private String getAuthString() {
		String userCredentials = password + ":" + salt;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(userCredentials.getBytes(StandardCharsets.UTF_8));

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
	}
	
	private String createSalt() {
		Random rand = new SecureRandom();
		StringBuffer buf = new StringBuffer();
		for(int j = 0; j < 16; j++) {
			buf.append((char)(rand.nextInt(25) + 65));
		}
		return buf.toString();
	}

}
