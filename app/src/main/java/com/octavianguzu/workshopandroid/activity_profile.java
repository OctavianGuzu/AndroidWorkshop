package com.octavianguzu.workshopandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
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

import java.text.ParseException;
import java.util.Date;

public class activity_profile extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Button blog = (Button) findViewById(R.id.blog_button);

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/octavian-guzu-698a41134/"));
                startActivity(intent);*/

                startActivity(new Intent(activity_profile.this, RepositoriesActivity.class));
            }
        });


        TextView updated_date = (TextView) findViewById(R.id.updated_data);
        TextView created_date = (TextView) findViewById(R.id.created_data);
        //create_date.setText("Thu, Oct 1, 2015");

        SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM dd, yyyy");
        Date date1 = new Date();
        updated_date.setText(fmt.format(date1));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date c = sdf.parse("2015-10-01");
            created_date.setText(fmt.format(c));
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
                preferences.edit().putBoolean("logged_in", false).apply();
                finish();
                break;
        }
        return true;
    }
}
