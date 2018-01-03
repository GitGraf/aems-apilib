# aems-apilib
This library is used to generate the JSON-Body that will be sent to the AEMS-API. It also provides methods to communicate to the API and encryption support.

# Usage
For secure communication, you will have to establish a shared secret key with the API. Once you have your secret key, you can use this library. Here is a simple example: 
```java
AemsUser user = new AemsUser(0, "username", "password");
AemsQueryAction action = new AemsQueryAction(user, EncryptionType.AES);
action.setQuery("mah-quiri");
String json = action.toJson(sharedSecretKey);
``` 
This configuration will dispatch a query to the API. The sensitive data will be encrypted with AES using the sharedSecretKey.
```json
{
  "user_id": 0,
  "action": "QUERY",
  "data": "1ZxCfiHKtnReWlNGclGw_w=="
}
``` 
# Calling the REST Service
You can make use of the static methods of the `AemsAPI` class to communicate with the REST service. You need to supply your `AemsAction` object as well as the shared secret key. The method will do the request and return the (encrypted) response. The response is encrypted with the shared secret key, which means that you are able to decrypt it.
```java
AemsAPI.setUrl("https://api.aems.at");
String response = AemsAPI.call(action, sharedSecretKey);
// You can make use of the Decrypter class in AEMSUtilLib
String decrypted = Decrypter.requestDecryption(sharedSecretKey, response.getBytes());
```

# Examples (not completely up-to-date!)
## No-Auth Actions
Please follow [this link](https://github.com/GitGraf/aems-apilib/wiki/No-Authentication-Actions) to read about actions that do not require full authentication.
## Regular Actions
Please follow [this link](https://github.com/GitGraf/aems-apilib/wiki/Aems-Actions) in order to read about actions that require authentication.
