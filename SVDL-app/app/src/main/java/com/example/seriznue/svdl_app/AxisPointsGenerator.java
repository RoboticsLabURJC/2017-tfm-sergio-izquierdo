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

    public MatOfPoint3f get_3d_Axis_points(int axis, int num) {

        List<Point3> objectPointsList = new ArrayList<Point3>(num);

        int i = 0;

        while (i < num) {
            if(axis == 0)
                objectPointsList.add(new Point3((float)i, 0.0f, 0.0f));
            else if(axis == 1)
                objectPointsList.add(new Point3(0.0f, (float)i, 0.0f));
            else if(axis == 2)
                objectPointsList.add(new Point3(0.0f, 0.0f, (float)i));
        }

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

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
        AxisPointsGenerator axisPointsGenerator = AxisPointsGenerator.createAxisPointsGenerator(Camera.getInstance());

        if(axis == 0)
            return ProjectPoints(axisPointsGenerator.get_3d_Axis_X_points());
        if(axis == 1)
            return ProjectPoints(axisPointsGenerator.get_3d_Axis_Y_points());
        if(axis == 2)
            return ProjectPoints(axisPointsGenerator.get_3d_Axis_Z_points());

        return imagePoints;
    }

    private MatOfPoint3f get_3d_Cube_points() {

        List<Point3> objectPointsList = new ArrayList<Point3>(10);

        objectPointsList.add(new Point3(1.5f, 1.5f, -1.5f));
        objectPointsList.add(new Point3(3.5f, 1.5f, -1.5f));
        objectPointsList.add(new Point3(3.5f, 3.5f, -1.5f));
        objectPointsList.add(new Point3(1.5f, 3.5f, -1.5f));
        objectPointsList.add(new Point3(1.5f, 1.5f, -3.5f));
        objectPointsList.add(new Point3(3.5f, 1.5f, -3.5f));
        objectPointsList.add(new Point3(3.5f, 3.5f, -3.5f));
        objectPointsList.add(new Point3(1.5f, 3.5f, -3.5f));

        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return modelPoints;
    }

    public MatOfPoint2f get_plane_lines() {

        List<Point> imagePointsList = new ArrayList<Point>(10);
        MatOfPoint2f imagePoints = new MatOfPoint2f();
        imagePoints.fromList(imagePointsList);

        List<Point3> objectPointsList = new ArrayList<Point3>(10);

        objectPointsList.add(new Point3(1f, 0f, 0f));
        objectPointsList.add(new Point3(1f, 0f, 10f));
        objectPointsList.add(new Point3(2f, 0f, 0f));
        objectPointsList.add(new Point3(2f, 0f, 10f));
        objectPointsList.add(new Point3(3f, 0f, 0f));
        objectPointsList.add(new Point3(3f, 0f, 10f));
        objectPointsList.add(new Point3(4f, 0f, 0f));
        objectPointsList.add(new Point3(4f, 0f, 10f));
        objectPointsList.add(new Point3(5f, 0f, 0f));
        objectPointsList.add(new Point3(5f, 0f, 10f));
        objectPointsList.add(new Point3(6f, 0f, 0f));
        objectPointsList.add(new Point3(6f, 0f, 10f));
        objectPointsList.add(new Point3(7f, 0f, 0f));
        objectPointsList.add(new Point3(7f, 0f, 10f));
        objectPointsList.add(new Point3(8f, 0f, 0f));
        objectPointsList.add(new Point3(8f, 0f, 10f));
        objectPointsList.add(new Point3(9f, 0f, 0f));
        objectPointsList.add(new Point3(9f, 0f, 10f));
        objectPointsList.add(new Point3(10f, 0f, 0f));
        objectPointsList.add(new Point3(10f, 0f, 10f));

        objectPointsList.add(new Point3(0f, 0f, 1f));
        objectPointsList.add(new Point3(10f, 0f, 1f));
        objectPointsList.add(new Point3(0f, 0f, 2f));
        objectPointsList.add(new Point3(10f, 0f, 2f));
        objectPointsList.add(new Point3(0f, 0f, 3f));
        objectPointsList.add(new Point3(10f, 0f, 3f));
        objectPointsList.add(new Point3(0f, 0f, 4f));
        objectPointsList.add(new Point3(10f, 0f, 4f));
        objectPointsList.add(new Point3(0f, 0f, 5f));
        objectPointsList.add(new Point3(10f, 0f, 5f));
        objectPointsList.add(new Point3(0f, 0f, 6f));
        objectPointsList.add(new Point3(10f, 0f, 6f));
        objectPointsList.add(new Point3(0f, 0f, 7f));
        objectPointsList.add(new Point3(10f, 0f, 7f));
        objectPointsList.add(new Point3(0f, 0f, 8f));
        objectPointsList.add(new Point3(10f, 0f, 8f));
        objectPointsList.add(new Point3(0f, 0f, 9f));
        objectPointsList.add(new Point3(10f, 0f, 9f));
        objectPointsList.add(new Point3(0f, 0f, 10f));
        objectPointsList.add(new Point3(10f, 0f, 10f));


        MatOfPoint3f modelPoints = new MatOfPoint3f();
        modelPoints.fromList(objectPointsList);

        return ProjectPoints(modelPoints);
    }

    public MatOfPoint2f get_Cube_points() {

        List<Point> imagePointsList = new ArrayList<Point>(10);
        MatOfPoint2f imagePoints = new MatOfPoint2f();
        imagePoints.fromList(imagePointsList);

        AxisPointsGenerator axisPointsGenerator = AxisPointsGenerator.createAxisPointsGenerator(Camera.getInstance());

        return ProjectPoints(axisPointsGenerator.get_3d_Cube_points());
    }

    public MatOfPoint2f ProjectPoints(MatOfPoint3f points3D) {

        List<Point> imagePointsList = new ArrayList<Point>(10);
        MatOfPoint2f imagePoints = new MatOfPoint2f();
        imagePoints.fromList(imagePointsList);
        Mat rVec = Camera.getInstance().getRVec();
        Mat tVec = Camera.getInstance().getTVec();
        Mat k = Camera.getInstance().getCameraMatrix();
        MatOfDouble dm = Camera.getInstance().getDistortionModel();

        Calib3d.projectPoints(points3D, rVec, tVec, k, dm, imagePoints);

        return imagePoints;
    }





}
