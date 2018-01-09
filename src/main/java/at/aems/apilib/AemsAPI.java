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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Provides static convenience methods to interact with the API.
 * 
 * @author Niggi
 */
public final class AemsAPI {

    private AemsAPI() {
    }

    private static String BASE_URL = null;

    public static void setUrl(String url) {
        BASE_URL = url;
    }

    /**
     * Dispatches a HTTP request to the {@link #BASE_URL} and returns the plain-text
     * result. The body will automatically be encrypted with the encryption key.
     * 
     * @param action
     *            The {@link AbstractAemsAction} to be executed
     * @param encryptionKey
     *            The key to be used for encrypting sensitive data
     * @return The response of the HTTP call
     * @throws IOException
     *             If an I/O Exception occures (e.g. malformed url)
     */
    public static String call(AbstractAemsAction action, byte[] encryptionKey) throws IOException {
        if (BASE_URL == null)
            throw new IllegalStateException("Base URL cannot be null! Set it using AemsAPI.setUrl(url)");

        HttpURLConnection connection;
        URL apiUrl = new URL(BASE_URL);
        String encryptedJson = action.toJson(encryptionKey);
        connection = (HttpURLConnection) apiUrl.openConnection();

        connection.setRequestMethod(action.getHttpVerb());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", Integer.toString(encryptedJson.length()));
        connection.setDoOutput(true);
        connection.getOutputStream().write(encryptedJson.getBytes("UTF-8"));

        connection.connect();

        String rawResult = readDataFromStream(connection.getInputStream());
        return rawResult;
    }

    private static String readDataFromStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

}
