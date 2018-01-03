package at.aems.apilib.crypto;

public enum EncryptionType {
	
	AES(CryptoAes.class),
	SSL(CryptoSsl.class);
	
	private Class<? extends AemsCrypto> cryptoClass;
	
	EncryptionType(Class<? extends AemsCrypto> clazz) {
		this.cryptoClass = clazz;
	}
	
	public AemsCrypto getImplementation() {
		try {
			return cryptoClass.newInstance();
		} catch(Exception e) {
			throw new RuntimeException("crypto class could not be instantiated");
		}
	}
}
