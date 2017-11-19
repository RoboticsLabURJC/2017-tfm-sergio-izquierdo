package com.example.seriznue.svdl_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    private static String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba, mGray;
    AxisPointsGenerator axisPointsGenerator = AxisPointsGenerator.createAxisPointsGenerator(Camera.getInstance());

    static {
        System.loadLibrary("MyOpencvLibs");
    }

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }

            super.onManagerConnected(status);
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.i(TAG, "Opencv loaded successfully");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
        else{
            Log.i(TAG, "Opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);

        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Camera.getInstance().changePosition();
        mRgba = inputFrame.rgba();

        List<Point> lineX = axisPointsGenerator.get_Axis_points(0).toList();
        List<Point> lineY = axisPointsGenerator.get_Axis_points(1).toList();
        List<Point> lineZ = axisPointsGenerator.get_Axis_points(2).toList();

        Core.line(mRgba, lineX.get(0), lineX.get(1), new Scalar(256, 0, 0), 5);
        Core.line(mRgba, lineY.get(0), lineY.get(1), new Scalar(0, 256, 0), 5);
        Core.line(mRgba, lineZ.get(0), lineZ.get(1), new Scalar(0, 0, 256), 5);

        for(Point point: axisPointsGenerator.get_Cube_points().toList()) {
            Core.circle(mRgba, point, 5, new Scalar(256, 255, 255), -1);
        }

        List<Point> pointsPlane = axisPointsGenerator.get_plane_lines().toList();

        for(int i = 0; i < pointsPlane.size(); i+=2) {
            Core.line(mRgba, pointsPlane.get(i), pointsPlane.get(i+1), new Scalar(256, 255, 255));
        }

        return mRgba;
    }
}
