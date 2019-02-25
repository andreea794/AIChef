package com.teamalpha.aichef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    public interface PreviewListener {
        void onPreviewUpdated(Bitmap data, int width, int height);
    }

    PreviewListener mListener = null;
    SurfaceHolder mHolder = null;
    Camera mCamera = null;
    byte[] buffer;
    int bufferSize;

    private static long lastFrameTaken = System.currentTimeMillis();

    // This variable tells us whether camera is paused
    private boolean isPaused = false;
    // This variable is responsible for getting and setting the camera settings
    private Camera.Parameters parameters;
    // This variable stores the camera preview size
    private Size previewSize;
    // For fast YUV to bitmap conversion
    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Allocation aIn, aOut;
    private Bitmap bmpout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CameraPreview(Context context) {
        super(context);

        rs = RenderScript.create(context);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            // Instantiate listener, so we can send frames to back-end
            mListener = (PreviewListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement PreviewListener");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("CAMERA", "Could not open camera.");
            return;
        }
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            Log.e("CAMERA", "Unable to start camera preview.");
            mCamera.release();
            mCamera = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int i1, int i2) {
        // Now that size is known, set up camera parameters and start the preview
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        if (mCamera != null) {
            parameters = mCamera.getParameters();

            // Continuously focus camera
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            // Have to get previewSizes because not all devices support arbitrary previews
            int width = this.getWidth();
            int height = this.getHeight();
            Size best = null;
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            // You need to choose the most appropriate previewSize for your app
            for (int i = 0; i < previewSizes.size(); i++) {
                Size size = previewSizes.get(i);
                if ((size.width <= width && size.height <= height) || (size.height <= width && size.width <= height)) {
                    if (best == null)
                        best = size;
                    else {
                        int resultArea = best.width * best.height;
                        int newArea = size.width * size.height;
                        if (newArea > resultArea)
                            best = size;
                    }
                }
            }
            if (best != null)
                previewSize = best;
            else
                previewSize = previewSizes.get(0);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            if(bmpout == null) {
                // Create input allocation from preview size
                aIn = Allocation.createSized(rs, Element.U8(rs), previewSize.width * previewSize.height * 3 / 2);
                // Create bitmap
                bmpout = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888);
                // Create output allocation from bitmap
                aOut = Allocation.createFromBitmap(rs, bmpout);

                yuvToRgbIntrinsic.setInput(aIn);
            }

            // Apply parameters to the camera
            mCamera.setParameters(parameters);

            // Set the camera callback to be the one defined in this class
            mCamera.setPreviewCallbackWithBuffer(this);
            bufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(
                    parameters.getPreviewFormat()) / 8;
            buffer = new byte[bufferSize];
            resetBuffer();

            if (!isPaused)
                mCamera.startPreview();
        }
    }

    public void resetBuffer() {
        if (mCamera != null) {
            mCamera.addCallbackBuffer(buffer);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the camera isn't a shared resource, it's very important to release it.

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        long now = System.currentTimeMillis();
        if (now - lastFrameTaken >= 1000) {
            aIn.copyFrom(data);
            yuvToRgbIntrinsic.forEach(aOut);
            aOut.copyTo(bmpout);

            Bitmap rotatedBmp = rotateBitmap(bmpout);

            // Log.d("FRAME","I'm sending frames.");
            mListener.onPreviewUpdated(rotatedBmp, previewSize.width, previewSize.height);

            lastFrameTaken = now;
        }
        // Once we are done with current frame, reset the buffer
        resetBuffer();
    }

    private Bitmap rotateBitmap(Bitmap bmp) {
        Matrix mat = new Matrix();
        mat.postRotate(90);
        return Bitmap.createBitmap(bmp, 0, 0, bmpout.getWidth(), bmpout.getHeight(), mat, true);
    }

    public void pauseOrResume(boolean isPaused) {
        this.isPaused = isPaused;
        if (isPaused)
            mCamera.stopPreview();
        else
            if (mCamera != null) {
                mCamera.setPreviewCallbackWithBuffer(this);
                mCamera.startPreview();
            }
    }
}
