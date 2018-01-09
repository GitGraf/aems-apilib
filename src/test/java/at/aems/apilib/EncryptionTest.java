package at.aems.apilib;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import at.aems.apilib.crypto.EncryptionType;

public class EncryptionTest {

    private AemsUser testUser = new AemsUser(0, "Test", "user");
    private byte[] testKey = new byte[16];

    @Test
    public void testAes() {
        AemsQueryAction action = new AemsQueryAction(testUser, EncryptionType.AES);
        action.setQuery("QUERY-adofnewidf");

        String expected = "85DG41K3ZrJzVwosTEBO1AFD22PuZrDN_59pkXaAFR4=";
        String json = action.toJson(testKey);

        JsonObject o = toJsonObject(json);
        assertEquals("encrypted should equal", expected, o.get("data").getAsString());
    }

    @Test
    public void testSsl() {
        AemsQueryAction action = new AemsQueryAction(testUser, EncryptionType.SSL);
        action.setQuery("QUERY-adofnewidf");

        String expected = "UVVFUlktYWRvZm5ld2lkZg==";
        String json = action.toJson(testKey);

        JsonObject o = toJsonObject(json);
        assertEquals("encrypted should equal", expected, o.get("data").getAsString());
    }

    private static JsonObject toJsonObject(String s) {
        return new JsonParser().parse(s).getAsJsonObject();
    }
}
