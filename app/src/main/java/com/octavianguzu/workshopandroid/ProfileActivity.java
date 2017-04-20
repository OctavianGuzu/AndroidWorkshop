package com.octavianguzu.workshopandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octavianguzu.workshopandroid.model.GitHub;
import com.octavianguzu.workshopandroid.model.GithubProfile;

import java.text.ParseException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    TextView updated_date;
    TextView created_date;
    TextView username;
    TextView occupation;
    TextView description;
    TextView city;
    TextView email;
    TextView public_repos;
    TextView private_repos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        updated_date = (TextView) findViewById(R.id.updated_data);
        created_date = (TextView) findViewById(R.id.created_data);
        username = (TextView) findViewById(R.id.username);
        occupation = (TextView) findViewById(R.id.occupation);
        description = (TextView) findViewById(R.id.description);
        city = (TextView) findViewById(R.id.city);
        email = (TextView) findViewById(R.id.email);
        public_repos = (TextView) findViewById(R.id.public_repos);
        private_repos = (TextView) findViewById(R.id.private_repos);

        Button blog = (Button) findViewById(R.id.blog_button);

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, RepositoriesActivity.class));
            }
        });

        fetchProfile();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().remove("auth_hash").apply();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }

    private void updateUI(GithubProfile profile) {
        username.setText(profile.getName());
        occupation.setText("Student");
        updated_date.setText(profile.getUpdatedAt());
        created_date.setText(profile.getCreatedAt());
        description.setText(profile.getBio());
        city.setText(profile.getLocation());
        email.setText(profile.getEmail());
        public_repos.setText(String.valueOf(profile.getPublicRepos()));
        private_repos.setText(String.valueOf(profile.getTotalPrivateRepos()));
    }

    private void fetchProfile() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Call<GithubProfile> profileCall = GitHub.Service.Get().getUserProfile(preferences.getString("auth_hash", null));
        profileCall.enqueue(new Callback<GithubProfile>() {
            @Override
            public void onResponse(Call<GithubProfile> call, Response<GithubProfile> response) {
                if (response.isSuccessful()) {
                    GithubProfile profile = response.body();
                    Toast.makeText(ProfileActivity.this, "Got info from GitHub", Toast.LENGTH_SHORT).show();
                    updateUI(profile);
                } else {
                    Toast.makeText(ProfileActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                    preferences.edit().remove("auth_hash").apply();
                    finish();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<GithubProfile> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ProfileActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
