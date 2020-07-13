package com.mahmoud.seemov.DetailActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.mahmoud.seemov.R;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState==null)
        getSupportFragmentManager().beginTransaction().add(R.id.detail_container,new DetailFragment()).commit();


    }


}
