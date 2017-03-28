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

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.buton);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("logged_in", false)) {
            Intent login_act = new Intent(activity_login.this, activity_profile.class);
            startActivity(login_act);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_act = new Intent(activity_login.this, activity_profile.class);
                login_act.putExtra("here", "we go");

                Toast fail_to_login = Toast.makeText(activity_login.this, "Wrong Username or Password", Toast.LENGTH_SHORT);


                EditText pass = (EditText) findViewById(R.id.password);
                EditText user = (EditText) findViewById(R.id.username);
                if(pass.getText().toString().equals("oct") &&
                        user.getText().toString().equals("Octav")) {
                    finish();
                    startActivity(login_act);
                    preferences.edit().putBoolean("logged_in", true).apply();
                }
                else {
                    fail_to_login.show();
                }
            }
        });
    }


}
