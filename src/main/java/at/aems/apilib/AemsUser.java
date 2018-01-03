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

public class AemsUser {
	private int userId;
	private String username;
	private String password;
	
	public AemsUser(int userId, String username, String password) {
		this.userId = userId;
		this.username = username;
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * Calculates the SHA-512 based authentication string for this user and a
	 * given salt. If the salt parameter is null or empty, it will not be applied.
	 * The string will be calculated as follows:
	 * <blockquote>
	 *     sha512(userId:username:password:salt)<p>
	 * or<p>
	 *     sha512(userId:username:password)<p>
	 * if no salt is supplied.
	 * </blockquote>
	 * @param salt The salt to apply, can be null
	 * @return The authentification string for a specific salt
	 */
	public String getAuthString(String salt) {
		String userCredentials = userId + ":" + username + ":" + password;
		if(salt != null && !salt.isEmpty()) {
			userCredentials = userCredentials + ":" + salt;
		}
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
	
	
	
	
}
