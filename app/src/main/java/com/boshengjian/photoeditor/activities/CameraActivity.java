package com.boshengjian.photoeditor.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.boshengjian.photoeditor.R;
import com.boshengjian.photoeditor.utils.Configutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    SurfaceView surfaceView = null;
    Button cameraBtn = null;
    Button flashBtn = null;
    Button swichCamBtn = null;
    ImageView iv = null;
    SurfaceHolder surfaceHolder = null;
    android.hardware.Camera camera = null;
    android.hardware.Camera.Parameters parameters = null;

    boolean isFlashLightOpen = false;
    int position = 1; //  1 means thr rear camera, 0 means the front camera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        surfaceView = findViewById(R.id.surfaceView);
        cameraBtn = findViewById(R.id.buttonTakePic);
        flashBtn = findViewById(R.id.buttonFlash);
        swichCamBtn = findViewById(R.id.buttonSwithCam);

        iv = findViewById(R.id.imageView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFixedSize(177, 144);
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new MySurfaceCallBack());



//        click the camera button to take a photo
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    camera.takePicture(null,null, new TakePhoto());
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        flashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFlash();
                isFlashLightOpen = !isFlashLightOpen;
            }
        });

        swichCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCam();
            }
        });

    }

    private void switchFlash(){
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        if (isFlashLightOpen) {
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            flashBtn.setText("Turn on the flash light");
        } else{
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_ON);
            flashBtn.setText("Turn off the flash light");
        }
        camera.setParameters(parameters);
    }

    private void switchCam() {
        android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
        int count = android.hardware.Camera.getNumberOfCameras();
        for (int i = 0; i < count; i++) {
            android.hardware.Camera.getCameraInfo(i, cameraInfo);
            if (position == 1) {
                if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                        camera = android.hardware.Camera.open(i);
                        camera.setDisplayOrientation(getRotation(CameraActivity.this));
//                        SurfaceTexture surfaceTexture = new SurfaceTexture(10);
//                        camera.setPreviewTexture(surfaceTexture);
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    position = 0;
                    swichCamBtn.setText("Open Front Cam");
                    break;
                }
            } else {
                if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                        camera = android.hardware.Camera.open(i);
                        camera.setDisplayOrientation(getRotation(CameraActivity.this));
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    position = 1;
                    swichCamBtn.setText("Open Rear Cam");
                    break;
                }
            }
        }
    }

    class TakePhoto implements android.hardware.Camera.PictureCallback{
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            if (data.length > 0){
                try {
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    iv.setImageBitmap(bitmap);
                    saveSdcard(data);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void saveSdcard(byte[] data){
        File dir = new File(Configutils.GalleryPath);
        if (!dir.exists()) dir.mkdir();
        try{
            String picName = System.currentTimeMillis() + ".jpg";
            File picFile = new File(dir, picName);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            FileOutputStream fo = new FileOutputStream(picFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bufferedOutputStream);

            fo.flush();
            fo.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            Toast.makeText(this, "successfully saved picture", Toast.LENGTH_SHORT);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    class MySurfaceCallBack implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try{
                camera = android.hardware.Camera.open();
                camera.setDisplayOrientation(getRotation(CameraActivity.this));
                camera.setPreviewDisplay(surfaceHolder);
//                SurfaceTexture surfaceTexture = new SurfaceTexture(10);
//                camera.setPreviewTexture(surfaceTexture);
                camera.startPreview();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            parameters = camera.getParameters();
            List<android.hardware.Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
            if (supportedPictureSizes.isEmpty()){
                parameters.setPreviewSize(width, height);
            } else {
                android.hardware.Camera.Size size = supportedPictureSizes.get(0);
                parameters.setPreviewSize(size.width, size.height);
            }
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setPictureSize(width, height);
            parameters.setJpegQuality(80);
            parameters.setPreviewFrameRate(5);

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null){
                camera.release();
                camera = null;
            }
        }
    }

/*
    This method gets device's current rotation
*/
    private int getRotation(Activity activity){
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    int degree = 0;
    switch (rotation){
//            if current roration is 0, then rorate 90 degree
        case Surface.ROTATION_0:
            degree = 90;
            break;
        case Surface.ROTATION_90:
            degree = 0;
            break;
        case Surface.ROTATION_180:
            degree = 270;
            break;
        case Surface.ROTATION_270:
            degree = 180;
            break;
    }
    return degree;
}

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null){
            camera.stopPreview();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (camera != null){
            camera.startPreview();
        } else{
            try{
                camera = android.hardware.Camera.open();
                camera.setDisplayOrientation(getRotation(CameraActivity.this));
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}


