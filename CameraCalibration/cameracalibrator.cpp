#include "cameracalibrator.h"
#include <QString>

CameraCalibrator::CameraCalibrator() :
    flag(0),
    mustInitUndistort(true)
{
}

int CameraCalibrator::AddChessboardPoints(const std::vector<Mat>& imageList, cv::Size & boardSize)
{
    // points on the chessboard
    vector<Point2f> imageCorners;
    vector<Point3f> objectCorners;
    // 3D Scene Points
    // Initialize the chessboard corners in the chessboard reference frame.
    // The corners are at 3D location (X,Y,Z)= (i,j,0)
    for (int i=0; i<boardSize.height; i++)
        for (int j=0; j<boardSize.width; j++)
            objectCorners.push_back(Point3f(i, j, 0.0f));

    // 2D Image Points
    Mat image; // to contain the current chessboard image
    int successes = 0;
    // for all viewpoints
    int listSize = (int) imageList.size();
    for (int i=0; i<listSize; i++)
    {
        // get the image in grayscale
        image = imageList[i];
        cvtColor(image, image,CV_BGR2GRAY);
        // get the chessboard corners
        findChessboardCorners(image, boardSize, imageCorners);
        // get subpixel accuracy on the corners
        cv::cornerSubPix(
                    image,
                    imageCorners,
                    Size(5,5),
                    Size(-1,-1),
                    TermCriteria(
                        TermCriteria::MAX_ITER +
                        TermCriteria::EPS,
                        30,      // max number of iterations
                        0.1) //min accuracy
                    );
        // if we have a good board, add it to our data
        if (imageCorners.size() == (unsigned int) boardSize.area()) {
            // add image and scene points from one view
            AddPoints(imageCorners, objectCorners);
            successes++;
        }
    }
    return successes;
}

// add scene points and corresponding image points
void CameraCalibrator::AddPoints(const vector<Point2f>& imageCorners, const vector<Point3f>& objectCorners)
{
    // 2D image points from one view
    imagePoints.push_back(imageCorners);
    // corresponding 3D scene points
    objectPoints.push_back(objectCorners);
}

Mat CameraCalibrator::getDistCoeffs() const
{
    return distCoeffs;
}

Mat CameraCalibrator::getCameraMatrix() const
{
    return cameraMatrix;
}

// calibrate the camera and returns the re-projection error
double CameraCalibrator::Calibrate(Size &imageSize)
{
    mustInitUndistort = true;

    vector<Mat> rvecs, tvecs;

    return calibrateCamera(objectPoints,
                           imagePoints,
                            imageSize,
                            cameraMatrix,
                            distCoeffs,
                            rvecs, tvecs,
                            flag);
}

// remove distortion in the image (after calibration)
Mat CameraCalibrator::Remap(const Mat &image)
{
    Mat undistorted;
    if (mustInitUndistort) { // called once per calibration
        initUndistortRectifyMap(
                        cameraMatrix,  // computed camera matrix
                        distCoeffs, // computed distortion matrix
                        Mat(), // optional rectification (none)
                        Mat(), // camera matrix to generate undistorted
                        image.size(),  // size of undistorted
                        CV_32FC1,      // type of output map
                        map1, map2);   // the x and y mapping functions
        mustInitUndistort = false;
    }

    // apply mapping functions
    cv::remap(image, undistorted, map1, map2, INTER_LINEAR); // interpolation type

    return undistorted;
}

QString CameraCalibrator::GetCalibrationAsText()
{
    Mat cameraMatrix    = this->getCameraMatrix();
    Mat disCoeffs       = this->getDistCoeffs();

    QString text("");

    text += QString("fx : ") + QString::number(cameraMatrix.at<double>(0, 0)) + QString("\n");
    text += QString("fy : ") + QString::number(cameraMatrix.at<double>(1, 1)) + QString("\n");
    text += QString("cx : ") + QString::number(cameraMatrix.at<double>(0, 2)) + QString("\n");
    text += QString("cy : ") + QString::number(cameraMatrix.at<double>(1, 2)) + QString("\n");
    text += QString("k1 : ") + QString::number(disCoeffs.at<double>(0, 0))    + QString("\n");
    text += QString("k2 : ") + QString::number(disCoeffs.at<double>(0, 1))    + QString("\n");
    text += QString("p1 : ") + QString::number(disCoeffs.at<double>(0, 2))    + QString("\n");
    text += QString("p2 : ") + QString::number(disCoeffs.at<double>(0, 3))    + QString("\n");
    text += QString("p3 : ") + QString::number(disCoeffs.at<double>(0, 4))    + QString("\n");

    return text;
}

void CameraCalibrator::Clear()
{
    objectPoints.clear();
    imagePoints.clear();
    mustInitUndistort = true;
}
