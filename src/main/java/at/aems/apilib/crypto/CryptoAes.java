package at.aems.apilib.crypto;

import at.htlgkr.aems.util.crypto.Decrypter;
import at.htlgkr.aems.util.crypto.Encrypter;

public class CryptoAes extends AemsCrypto {

	/**
	 * Encrypts a set of bytes with AES encryption by applying a specific key
	 * @param key The key to be used for encryption
	 * @param encrypted The plain byte array
	 * @return The encrypted byte array, or null if an error occurred
	 */
	@Override
	public byte[] encrypt(byte[] key, byte[] raw) {
		try {
			return Encrypter.requestEncryption(key, raw);
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * Decrypts a set of AES encrypted bytes by applying a specific key
	 * @param key The key to be used for decryption
	 * @param encrypted The encrypted byte array
	 * @return The decrypted byte array, or null if an error occurred
	 */
	@Override
	public byte[] decrypt(byte[] key, byte[] encrypted) {
		try {
			return Decrypter.requestDecryption(key, encrypted);
		} catch(Exception e) {
			return null;
		}
	}

}
