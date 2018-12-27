package com.boshengjian.photoeditor.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class BitmapTestActivity extends Activity {
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ImageView iv = new ImageView(this);
        setContentView(iv);

        Bitmap bitmap = null;
        try{
            bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
            iv.setImageBitmap(bitmap);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //put it into lru cache
            try{
                if (bitmap != null)
                    bitmap.recycle();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
