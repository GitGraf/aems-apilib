package at.aems.apilib;

public class ApiConfig {
    private String baseUrl;
    private Integer timeout;
    private String certPath;
    private String certPassword;
    
    public ApiConfig() {
        
    }
    
    public ApiConfig(String baseUrl, Integer timeout, String certPath, String certPassword) {
        super();
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.certPath = certPath;
        this.certPassword = certPassword;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public Integer getTimeout() {
        return timeout;
    }
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    public String getCertPath() {
        return certPath;
    }
    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }
    public String getCertPassword() {
        return certPassword;
    }
    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }
    
    
}
