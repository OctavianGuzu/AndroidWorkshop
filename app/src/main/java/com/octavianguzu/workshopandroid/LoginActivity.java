package com.octavianguzu.workshopandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.octavianguzu.workshopandroid.model.GitHub;
import com.octavianguzu.workshopandroid.model.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        Button button = (Button) findViewById(R.id.buton);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("auth_hash", null) != null) {
            Intent login_act = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(login_act);
            finish();
            return;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_act = new Intent(LoginActivity.this, ProfileActivity.class);
                login_act.putExtra("here", "we go");


                EditText pass = (EditText) findViewById(R.id.password);
                EditText user = (EditText) findViewById(R.id.username);

                final String authHash = okhttp3.Credentials.basic(user.getText().toString(), pass.getText().toString());

                Call<LoginData> callable = GitHub.Service.Get().checkAuth(authHash);


                callable.enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                        if (response.isSuccessful()) {
                            preferences.edit()
                                    .putString("auth_hash", authHash).apply();
                            goToProfileScreen();
                        } else {
                            Toast fail_to_login = Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT);
                            fail_to_login.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginData> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void goToProfileScreen() {
        finish();
        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));

    }


}
