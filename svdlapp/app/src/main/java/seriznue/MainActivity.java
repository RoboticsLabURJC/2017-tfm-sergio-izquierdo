package seriznue;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;

import java.util.ArrayList;
import java.util.List;

import seriznue.R;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    private CameraBridgeViewBase _cameraBridgeViewBase;

    private BaseLoaderCallback _baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    // Load ndk built module, as specified in moduleName in build.gradle
                    // after opencv initialization
                    System.loadLibrary("native-lib");
                    _cameraBridgeViewBase.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        // Permissions for Android 6+
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        _cameraBridgeViewBase = (CameraBridgeViewBase) findViewById(R.id.main_surface);
        _cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        _cameraBridgeViewBase.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        disableCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, _baseLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            _baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    public void disableCamera() {
        if (_cameraBridgeViewBase != null)
            _cameraBridgeViewBase.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public MatOfPoint3f get_3d_model_points() {
        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(1.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(2.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(3.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(4.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 1.0f, 0.0f));
        objectPointsList.add(new Point3(1.0f, 1.0f, 0.0f));
        objectPointsList.add(new Point3(2.0f, 1.0f, 0.0f));
        objectPointsList.add(new Point3(3.0f, 1.0f, 0.0f));
        objectPointsList.add(new Point3(4.0f, 1.0f, 0.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }


    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat matColor = inputFrame.rgba();

        MatOfPoint3f objectPoints = get_3d_model_points();
        List<Point> imagePointsList = new ArrayList<Point>(10);
        MatOfPoint2f imagePoints = new MatOfPoint2f();
        imagePoints.fromList(imagePointsList);


        saltRGB(matColor.getNativeObjAddr(),
                objectPoints.getNativeObjAddr(),
                imagePoints.getNativeObjAddr(),
                Camera.getInstance().getRVec().getNativeObjAddr(),
                Camera.getInstance().getTVec().getNativeObjAddr(),
                Camera.getInstance().getCameraMatrix().getNativeObjAddr(),
                Camera.getInstance().getDistortionMat().getNativeObjAddr());

        /*
        Mat matGray = inputFrame.gray();

        salt(matGray.getNativeObjAddr(), 2000);

        Mat matColor = new Mat();

        Imgproc.cvtColor(matGray, matColor, Imgproc.COLOR_GRAY2RGB);
        */
        /*
        Imgproc.putText(matColor, "SVDL_APP", new Point(matGray.rows()/2,matGray.cols()/2),
                Core.FONT_ITALIC, 1.0 ,new  Scalar(255, 0, 0));
        */

        /*
        MatOfPoint3f objectPoints = get_3d_model_points();

        List<Point> imagePointsList    = new ArrayList<Point>(10);
        MatOfPoint2f imagePoints = new MatOfPoint2f();
        imagePoints.fromList(imagePointsList);

        Calib3d.projectPoints(objectPoints,
                imagePoints,
                Camera.getInstance().getRVec(),
                Camera.getInstance().getTVec(),
                Camera.getInstance().getCameraMatrix(),
                Camera.getInstance().getDistortionMat()
                );
        */

        return matColor;
    }

    public native void salt(long matAddrGray, int nbrElem);

    public native void saltRGB(long matAddrRGB,
                               long objectPointsAddr,
                               long imagePointsAddr,
                               long rVecAddr,
                               long tVec,
                               long cameraMatrixAddr,
                               long distortionMatAddr);

}

