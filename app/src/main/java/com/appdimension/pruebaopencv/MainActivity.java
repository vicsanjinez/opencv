package com.appdimension.pruebaopencv;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private File                   mCascadeFile, mFrontalFaceFile, mHuman;
    private CascadeClassifier      mJavaDetector;


//
//    static {
//        System.loadLibrary("opencv_java");
//        System.loadLibrary("MyLibs");
//    }

    String tipo = "normal"; // or other values

    Mat mRgba, imageGray, imageCanny;

    JavaCameraView javaCameraView;
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status)
            {
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    System.loadLibrary("MyLibs");
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    static{
        if(OpenCVLoader.initDebug())
        {
            Log.d("logs", "inicializo");
        }
        else
        {
            Log.d("logs", "NO inicializo");
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        if(b != null)
            tipo = b.getString("tipo");


        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);

        javaCameraView.setVisibility(SurfaceView.VISIBLE);

        //solucionado con el implements
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (javaCameraView != null)
        {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(OpenCVLoader.initDebug())
        {
            Log.d("logs", "inicializo resume");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else
        {
            Log.d("logs", "NO inicializo resume");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, baseLoaderCallback);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);

        imageGray = new Mat(height, width, CvType.CV_8UC1);

        imageCanny = new Mat(height, width, CvType.CV_8UC1);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        //muetra la imagen en color
        //return mRgba;


        Imgproc.cvtColor(mRgba, imageGray, Imgproc.COLOR_RGB2GRAY);

        //si queremos que opencv haga un procesamiento de imagen basico
        //podemos devolver gray
        //return imageGray;

        Imgproc.Canny(imageGray, imageCanny, 50, 150);
        //return imageCanny;

        if(tipo.equals("bordes"))
        {
            return imageCanny;
        }
        else if(tipo.equals("grises"))
        {
            return imageGray;
        }
        else if(tipo.equals("facedetection"))
        {
            //Log.d("logs","se recibio el parametro facedetection");

            try {
                // load cascade file from application resources
                InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
                File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                mFrontalFaceFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
                FileOutputStream os = new FileOutputStream(mFrontalFaceFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                is.close();
                os.close();

                mJavaDetector = new CascadeClassifier(mFrontalFaceFile.getAbsolutePath());
                if (mJavaDetector.empty()) {
                    Log.e("logs", "Failed to load cascade classifier");
                    mJavaDetector = null;
                } else
                    Log.i("logs", "Loaded frontalface " + mFrontalFaceFile.getAbsolutePath());

                //mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                cascadeDir.delete();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("logs", "Failed to load frontalface. Exception thrown: " + e);
            }

            try {
                // load cascade file from application resources
                InputStream is = getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
                File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                mCascadeFile = new File(cascadeDir, "haarcascade_eye_tree_eyeglasses.xml");
                FileOutputStream os = new FileOutputStream(mCascadeFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                is.close();
                os.close();

                mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                if (mJavaDetector.empty()) {
                    Log.e("logs", "Failed to load cascade classifier");
                    mJavaDetector = null;
                } else
                    Log.i("logs", "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                //mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                cascadeDir.delete();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("logs", "Failed to load cascade. Exception thrown: " + e);
            }

            NativeClass.faceDetectionXML(mRgba.getNativeObjAddr(),mFrontalFaceFile.getAbsolutePath(),mCascadeFile.getAbsolutePath());
            return mRgba;
        }
        else if(tipo.equals("humandetection"))
        {
            //Log.d("logs","se recibio el parametro facedetection");

            try {
                // load cascade file from application resources
                InputStream is = getResources().openRawResource(R.raw.haarcascade_fullbody);
                File cascadeDir = getDir("human", Context.MODE_PRIVATE);
                mHuman = new File(cascadeDir, "haarcascade_fullbody.xml");
                FileOutputStream os = new FileOutputStream(mHuman);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                is.close();
                os.close();

                mJavaDetector = new CascadeClassifier(mHuman.getAbsolutePath());
                if (mJavaDetector.empty()) {
                    Log.e("logs", "Failed to load human classifier");
                    mJavaDetector = null;
                } else
                    Log.i("logs", "Loaded human classifier from " + mHuman.getAbsolutePath());

                cascadeDir.delete();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("logs", "Failed to load human. Exception thrown: " + e);
            }

            NativeClass.humanDetectionXML(mRgba.getNativeObjAddr(),mHuman.getAbsolutePath());
            return mRgba;

        }
        else
        {
            return mRgba;
        }
    }
}
