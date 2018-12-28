package com.boshengjian.photoeditor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.boshengjian.photoeditor.activities.CameraActivity;
import com.boshengjian.photoeditor.activities.GalleryActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camBtn = (Button) findViewById(R.id.btn1);
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });
        Button galleryBtn = (Button) findViewById(R.id.btn2);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });

    }
}
