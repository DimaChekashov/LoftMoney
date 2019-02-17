package foxsay.ru.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;

import java.util.UUID;

public class AuthActivity extends AppCompatActivity {

    private MaterialButton enterBtn;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = getToken();

        if (token != null) {
            MainActivity.start(this);
            finish();
        }

        setContentView(R.layout.activity_auth);

        enterBtn = findViewById(R.id.enter_btn);

        api = ((App) getApplication()).getApi();

        enterBtn.setOnClickListener(v -> {
            auth();
        });
    }

    private void auth() {

        String userId = UUID.randomUUID().toString();

        Call<AuthResponse> call = api.auth(userId);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                AuthResponse authResponse = response.body();

                String token = authResponse.getToken();

                saveToken(token);

                MainActivity.start(AuthActivity.this);
                finish();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences.edit()
                .putString("auth_token", token)
                .apply();
    }

    private String getToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        return preferences.getString("auth_token", null);
    }
}
