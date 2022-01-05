package com2027.cw.dp00405.vsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

public class QRActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Integer direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        Intent intent = getIntent();
        direction = intent.getIntExtra("DIRECTION", 0);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        //need to enable autofocus - look up
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setFacing(direction).build();

        surfaceView = findViewById(R.id.surfaceQR);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        surfaceView.setClickable(true);
        surfaceView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (cameraSource.getCameraFacing() == 0) {
            Field[] declaredFields = CameraSource.class.getDeclaredFields();

            for (Field field : declaredFields) {
                if (field.getType() == Camera.class) {
                    field.setAccessible(true);
                    try {
                        Camera camera = (Camera) field.get(cameraSource);
                        if (camera != null) {
                            Camera.Parameters params = camera.getParameters();
                            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                            camera.setParameters(params);
                            Toast.makeText(QRActivity.this, "Camera has been focused", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //surfaceHolder.addCallback(this);
        //surfaceView.setFocusable(true);
        try {
            cameraSource.start(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            public void release() {

            }

            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    Intent intent = new Intent();
                    intent.putExtra("CONTENTS", qrCodes.valueAt(0).displayValue);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraSource.stop();
    }

}
