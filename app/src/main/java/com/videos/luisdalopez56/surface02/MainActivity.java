package com.videos.luisdalopez56.surface02;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rl;
    SurfaceView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Camara camara = new Camara(this);
        setContentView(camara);
        Grafico grafico = new Grafico(this);

        addContentView(grafico, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    private class Camara extends SurfaceView implements SurfaceHolder.Callback {

        SurfaceHolder contenedor;
        Camera camera;
        Camera.PictureCallback fotografia;

        public Camara(Context context) {
            super(context);
            contenedor = getHolder();
            contenedor.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            contenedor.addCallback(this);
            fotografia = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(String.format("/sdcard/test.jpg",
                                System.currentTimeMillis()));


                        outputStream.write(data);
                        outputStream.close();
                        Log.d("Log","ERROR EN GUARDADO " + data.length);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {

                    }
                    Toast.makeText(getContext(), "FOTOGRAFICA GUARDADA",
                            Toast.LENGTH_LONG).show();
                    refrescaCamera();
                }

                private void refrescaCamera() {
                    if(contenedor.getSurface() == null){
                        return;
                    }
                    try {
                        camera.stopPreview();
                    } catch(Exception e) {

                    }
                    try{
                        camera.setPreviewDisplay(contenedor);
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (event.getAction()==MotionEvent.ACTION_DOWN){
                camera.takePicture(null, null, fotografia);
            }

            return true;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
               camera = Camera.open();
               camera.setDisplayOrientation(90);

               try {
                   camera.setPreviewDisplay(holder);
                   camera.startPreview();
               } catch (IOException e) {
                   System.out.println(e);
                   return;
               }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera = null;
        }
    }

    private class Grafico extends View {

        private Bitmap bitmap;

        public Grafico(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mirilla);

            int anc_ima = bitmap.getWidth();
            int alt_ima = bitmap.getHeight();
            int ancho = canvas.getWidth();
            int alto = canvas.getHeight();

            int pini = (ancho-anc_ima)/2;
            int pins = (alto-alt_ima)/2;

            canvas.drawBitmap(bitmap, pini, pins, null);


        }
    }
}
