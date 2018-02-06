package at.aems.apilib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

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
     * Returns the (encrypted) response body. In the case of AEMS, it's likely to be some sort
     * of JSON structure. Use {@link #getDecryptedResponse(EncryptionType, byte[])} to decrypt
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
        byte[] decrypted = type.getImplementation().decrypt(encryptionKey, responseText.getBytes());
        return new String(decrypted);
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
    public String getErrorMessage(boolean decryptFirst) {
        JsonObject error = getAsJsonObject(decryptFirst);
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
    public JsonArray getAsJsonArray(boolean decryptFirst) {
        String json = decryptFirst ? getDecryptedResponse() : getResponseText();
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
     * @return The response as JsonObject
     */
    public JsonObject getAsJsonObject(boolean decryptFirst) {
        String json = decryptFirst ? getDecryptedResponse() : getResponseText();
        try {
            return new JsonParser().parse(json).getAsJsonObject();
        } catch(Exception e) {
            return null;
        }
    }
    
}
