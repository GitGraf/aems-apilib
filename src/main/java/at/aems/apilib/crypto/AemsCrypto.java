package at.aems.apilib.crypto;

public abstract class AemsCrypto {
	public AemsCrypto() {}
	
	public abstract byte[] encrypt(byte[] key, byte[] raw);
	
	public abstract byte[] decrypt(byte[] key, byte[] raw);
}
