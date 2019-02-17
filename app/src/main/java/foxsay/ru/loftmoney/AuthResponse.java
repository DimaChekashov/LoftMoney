package foxsay.ru.loftmoney;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    private String status;

    private int id;

    @SerializedName("auth_token")
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String status, int id, String token) {
        this.status = status;
        this.id = id;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
