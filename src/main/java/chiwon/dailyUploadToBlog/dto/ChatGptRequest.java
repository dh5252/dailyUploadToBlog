package chiwon.dailyUploadToBlog.dto;

public class ChatGptRequest {
    private String apiKey;
    private String model;
    private String responseFormat;
    private String system;
    private String user;

    public ChatGptRequest(String apiKey, String responseFormat, String system, String user) {
        this.apiKey = apiKey;
        this.model = "gpt-3.5-turbo-1106";
        this.responseFormat = responseFormat;
        this.system = system;
        this.user = user;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
