package com.octavianguzu.workshopandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
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

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        Button button = (Button) findViewById(R.id.buton);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("logged_in", false)) {
            Intent login_act = new Intent(activity_login.this, activity_profile.class);
            finish();
            startActivity(login_act);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_act = new Intent(activity_login.this, activity_profile.class);
                login_act.putExtra("here", "we go");


                EditText pass = (EditText) findViewById(R.id.password);
                EditText user = (EditText) findViewById(R.id.username);

                Call<LoginData> callable = GitHub.Service.Get().checkAuth(okhttp3.Credentials.basic(user.getText().toString(), pass.getText().toString()));


                callable.enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                        if (response.isSuccessful()) {
                            preferences.edit()
                                    .putBoolean("logged_in", true).apply();
                            finish();
                            startActivity(new Intent(activity_login.this, activity_profile.class));
                        } else {
                            Toast fail_to_login = Toast.makeText(activity_login.this, "Wrong Username or Password", Toast.LENGTH_SHORT);
                            fail_to_login.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginData> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(activity_login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
