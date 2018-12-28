package com.boshengjian.photoeditor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

public class CustomGridAdapter extends BaseAdapter {
    private Context context;
    private File[] files;

    public CustomGridAdapter(Context c, File[] files){
        this.context = c;
        this.files = files;
    };

    public void setFiles(File[] files){
        this.files = files;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        int count = 0;
        if (this.files != null){
            count = this.files.length;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return this.files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;

        Bitmap bitmap = BitmapFactory.decodeFile(this.files[position].getAbsolutePath());
        if (convertView == null){
            iv = new ImageView(this.context);
            iv.setLayoutParams(new GridView.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
            iv.setAdjustViewBounds(false);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(8,8,8,8);
        } else {
            iv = (ImageView) convertView;
        }
        iv.setImageBitmap(bitmap);

        return iv;
    }
}
