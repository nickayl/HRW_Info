package com.domenicoaiello.devicespecs.controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.domenicoaiello.devicespecs.BuildConfig;
import com.domenicoaiello.devicespecs.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        ImageView aboutImage = (ImageView) findViewById(R.id.about);

        toolbarTitle.setText("About");
        aboutImage.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewVersionCode = findViewById(R.id.textView4);
        textViewVersionCode.setText(String.format(getString(R.string.version_code), BuildConfig.VERSION_NAME));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home)
            finish();

        return true;
    }
}
