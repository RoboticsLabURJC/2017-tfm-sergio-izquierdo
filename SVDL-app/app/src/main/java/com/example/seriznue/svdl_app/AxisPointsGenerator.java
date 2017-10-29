package com.example.seriznue.svdl_app;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seriznue on 10/29/17.
 */

public class AxisPointsGenerator {

    public Camera camera;

    private AxisPointsGenerator(Camera camera) {
        this.camera = camera;
    }

    public static AxisPointsGenerator createAxisPointsGenerator(Camera camera) {
        return new AxisPointsGenerator(camera);
    }

    /*
    private MatOfPoint3f get_3d_Axis_X_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));

        objectPointsList.add(new Point3(1.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(2.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(3.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(4.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(5.0f, 0.0f, 0.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

    private MatOfPoint3f get_3d_Axis_Y_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));

        objectPointsList.add(new Point3(0.0f, 1.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 2.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 3.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 4.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 5.0f, 0.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

    private MatOfPoint3f get_3d_Axis_Z_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));

        objectPointsList.add(new Point3(0.0f, 0.0f, 1.0f));
        objectPointsList.add(new Point3(0.0f, 0.0f, 2.0f));
        objectPointsList.add(new Point3(0.0f, 0.0f, 3.0f));
        objectPointsList.add(new Point3(0.0f, 0.0f, 4.0f));
        objectPointsList.add(new Point3(0.0f, 0.0f, 5.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }
    */

    public MatOfPoint3f get_3d_Axis_X_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(5.0f, 0.0f, 0.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

    public MatOfPoint3f get_3d_Axis_Y_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 5.0f, 0.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

    public MatOfPoint3f get_3d_Axis_Z_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);
        objectPointsList.add(new Point3(0.0f, 0.0f, 0.0f));
        objectPointsList.add(new Point3(0.0f, 0.0f, 5.0f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

    public MatOfPoint2f get_Axis_points(int axis) {

        List<Point> imagePointsList = new ArrayList<Point>(10);
        MatOfPoint2f imagePoints = new MatOfPoint2f();
        imagePoints.fromList(imagePointsList);
        Mat rVec = Camera.getInstance().getRVec();
        Mat tVec = Camera.getInstance().getTVec();
        int depth = tVec.depth();
        Mat k = Camera.getInstance().getCameraMatrix();
        Mat d = Camera.getInstance().getDistortionMat();
        MatOfDouble dm = Camera.getInstance().getDistortionModel();

        AxisPointsGenerator axisPointsGenerator = AxisPointsGenerator.createAxisPointsGenerator(Camera.getInstance());

        if(axis == 0)
            Calib3d.projectPoints(axisPointsGenerator.get_3d_Axis_X_points(), rVec, tVec, k, dm, imagePoints);
        if(axis == 1)
            Calib3d.projectPoints(axisPointsGenerator.get_3d_Axis_Y_points(), rVec, tVec, k, dm, imagePoints);
        if(axis == 2)
            Calib3d.projectPoints(axisPointsGenerator.get_3d_Axis_Z_points(), rVec, tVec, k, dm, imagePoints);

        return imagePoints;
    }

    private MatOfPoint3f get_3d_Cube_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);

        objectPointsList.add(new Point3(0.5f, 0.5f, 0.5f));
        objectPointsList.add(new Point3(1.5f, 0.5f, 0.5f));
        objectPointsList.add(new Point3(1.5f, 1.5f, 0.5f));
        objectPointsList.add(new Point3(0.5f, 1.5f, 0.5f));
        objectPointsList.add(new Point3(0.5f, 0.5f, 1.5f));
        objectPointsList.add(new Point3(1.5f, 0.5f, 1.5f));
        objectPointsList.add(new Point3(1.5f, 1.5f, 1.5f));
        objectPointsList.add(new Point3(0.5f, 1.5f, 1.5f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }



}
