package at.aems.apilib.crypto;

import java.util.Base64;

public class CryptoSsl extends AemsCrypto {

	/**
	 * Encodes a specified byte array in BASE 64. The "key" parameter serves no purpose.
	 * @param key Unused
	 * @param raw The raw bytes to encode
	 * @return The Base64 encoded byte array
	 */
	@Override
	public byte[] encrypt(byte[] key, byte[] raw) {
		return Base64.getUrlEncoder().encode(raw);
	}

	/**
	 * Encodes a specified byte array in BASE 64. The "key" parameter serves no purpose.
	 * @param key Unused
	 * @param raw The raw bytes to decode
	 * @return The decoded byte array
	 */
	@Override
	public byte[] decrypt(byte[] key, byte[] raw) {
		return Base64.getUrlDecoder().decode(raw);
	}

}
