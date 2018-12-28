package com.boshengjian.photoeditor.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.boshengjian.photoeditor.R;
import com.boshengjian.photoeditor.adapter.CustomGridAdapter;
import com.boshengjian.photoeditor.utils.Configutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class GalleryActivity extends AppCompatActivity {
    GridView gridView;
    CustomGridAdapter customGridAdapter = null;
    File[] fileList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridView);

        fileList = retriveFiles();
        customGridAdapter = new CustomGridAdapter(this, fileList);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
                builder.setMessage("Are you sure to delete?");

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File fileToDelete =  fileList[position];
                        if (fileToDelete.exists()){
                            fileToDelete.delete();
                        }
                        if (customGridAdapter != null){
                            fileList = retriveFiles();
                            customGridAdapter.setFiles(fileList);
                        }
                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.show();
                return false;
            }
        });

    }

    private File[] retriveFiles(){
        final File files = new File(Configutils.GalleryPath);
        final File[] fileList =  files.listFiles();
        return fileList;
    }

}
