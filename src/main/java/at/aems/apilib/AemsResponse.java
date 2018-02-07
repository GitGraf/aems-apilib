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

import java.util.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import at.aems.apilib.crypto.Decrypter;
import at.aems.apilib.crypto.EncryptionType;

public class AemsResponse {
    
    private Integer responseCode;
    private String responseMessage;
    private String responseText;
    
    private EncryptionType type;
    private byte[] encryptionKey;
    
    public AemsResponse(Integer responseCode, String responseMessage, String responseText,
            EncryptionType encryption, byte[] key) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseText = responseText;
        this.type = encryption;
        this.encryptionKey = key;
    }

    /**
     * Returns the HTTP response code, like "200" for OK, or "404" for NOT FOUND.
     * @return The HTTP response code
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     * Returns the response message associated with the response code
     * which can be obtained by calling {@link #getResponseCode()}.<br/>
     * Example: If the {@code responseCode} is "200", "OK" will be returned
     * @return The HTTP response message
     */
    public String getResponseMessage() {
        return responseMessage;
    }
    
    /**
     * Returns the (encrypted and encoded) response body.
     * In the case of AEMS, it's likely to be some sort
     * of JSON structure. Use {@link #getDecryptedResponse(EncryptionType, byte[])} to 
     * decrypt and decode
     * @return The response text
     */
    public String getResponseText() {
        return responseText;
    }
    
    /**
     * @return The encryption type which can be used for decrypting the response
     */
    public EncryptionType getEncryption() {
        return type;
    }
    
    /**
     * @return The encryption key which can be used for decrypting the response
     */
    public byte[] getEncryptionKey() {
        return encryptionKey;
    }
    
    /**
     * Returns the decrypted version of the response body ({@link #getResponseText()}).
     * @return The decrypted response
     */
    public String getDecryptedResponse() {
        if(responseText == null) {
            return "";
        }
        // Start with possibly encrypted and encoded byte array
        byte[] decrypted = responseText.getBytes();
        if(isEncoded()) {
            // If response is Base64 encoded - decode
            decrypted = Base64.getUrlDecoder().decode(decrypted);
        }
        if(type == EncryptionType.AES) {
            // If response is AES encrypted - decrypt
            // If response is NOT aes encrypted but type is set to 
            // EncryptionType.AES, this will break (throw exception)
            decrypted = decrypt(decrypted);
        }
        
        return new String(decrypted);
    }
    
    private byte[] decrypt(byte[] decrypted) {
        try {
            return Decrypter.requestDecryption(encryptionKey, decrypted);
        } catch(Exception ex) {
            return decrypted;
        }
    }
    
    private boolean isEncoded() {
        try {
            Base64.getUrlDecoder().decode(responseText);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    /**
     * Returns the error message in the json body of this response, if any.
     * To return the correct message, the body must only contain one
     * JSON Object in the following fashion: <p>
     * <blockquote>
     *  {"error": "This is the error message which will be returned"}
     * </blockquote>
     * If the response text does not match this format, {@code null} will be
     * returned.
     * @param decryptFirst Whether decryption of the response body should be attempted
     * @return The error message which is wrapped in the response body, or {@code null}
     * if the message is not an error.
     */
    public String getErrorMessage() {
        JsonObject error = getAsJsonObject();
        if(error == null)
            return null;
        
        JsonElement errorMsg = error.get("error");
        if(errorMsg == null)
            return null;
        
        return errorMsg.getAsString();
    }
    
    /**
     * Converts the decrypted response text into a {@link JsonArray} if possible.
     * If the response text cannot be converted into a {@link JsonArray}, {@code null}
     * will be returned
     * @return The response as JsonArray
     */
    public JsonArray getAsJsonArray() {
        String json = getDecryptedResponse();
        try {
            return new JsonParser().parse(json).getAsJsonArray();
        } catch(Exception e) {
            return null;
        }
    }
    
    /**
     * Converts the decrypted response text into a {@link JsonObject} if possible.
     * If the response text cannot be converted into a {@link JsonObject}, {@code null}
     * will be returned
     * @return The response as JsonObject, or {@code null} if the response is not a 
     * Json Object
     */
    public JsonObject getAsJsonObject() {
        String json = getDecryptedResponse();
        try {
            return new JsonParser().parse(json).getAsJsonObject();
        } catch(Exception e) {
            return null;
        }
    }
    
    /**
     * Attemtps to fetch a JsonArray out of a response in the form of a JsonObject.
     * This method is suitable if the response is shaped like this:
     * <blockquote>
     *  { <br/>
     *    "arrayKey": ["Array", "Values", "Yay"]<br/>
     *  }
     * </blockquote>
     * @param arrayKey The key of the JsonArray to be fetched. If {@code null}, the first
     * key of the JsonObject will be used.
     * @throws IllegalArgumentException If the response is malformed - Perhaps it is
     * not a JsonObject or it does not contain the specified arrayKey
     * @return The JsonArray within the JsonObject-Response that has the specified arrayKey
     */
    public JsonArray getJsonArrayWithinObject(String arrayKey) {
        JsonObject object = getAsJsonObject();
        if(object == null) {
            throw new IllegalStateException("Response is not a JsonObject!");
        }
        
        // check that the object has a key
        if(object.keySet().isEmpty()) {
            throw new IllegalStateException("Response is JsonObject but has no keys!");
        }
        
        String firstKey = object.keySet().iterator().next();
        // If parameter arrayKey is null, use firstKey
        String keyToUse = arrayKey == null ? firstKey : arrayKey;
        
        if(!object.has(keyToUse) || !object.get(keyToUse).isJsonArray()) {
            throw new IllegalStateException("Error when fetching JsonArray '" + keyToUse + "'. "
                    + "Did you specify the correct key and is the element with that key really a JsonArray?");
        }
        
        return object.get(keyToUse).getAsJsonArray();
        
    }
    
    /**
     * Convenience method. This call is equivalent to the call
     * {@code getJsonArrayWithinObject(null)}
     * @return
     */
    public JsonArray getJsonArrayWithinObject() {
        return getJsonArrayWithinObject(null);
    }
    
}
