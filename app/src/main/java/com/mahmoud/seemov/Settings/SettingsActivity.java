package com.mahmoud.seemov.Settings;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.mahmoud.seemov.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new SettingsFragment(),"settings_fragment")
                .commit();


    }


}
