#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QFile>
#include <QTextStream>
#include <QIODevice>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    isCameraRunning = false;
    isCalibrate = false;
    boardSize.width = 8;
    boardSize.height = 6;
    numSeq = 0;
    numRequiredSnapshot = 20;

    connect(this->ui->calibrateButton,  SIGNAL(clicked(bool)), this, SLOT(StartCalibration()));
    connect(ui->saveButton,             SIGNAL(clicked(bool)), this, SLOT(SaveCalibration()));
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_startCameraButton_clicked()
{
    if(!isCameraRunning)
    {
        // open camera stream
        capture.open(CV_CAP_ANY); // default: 0

        if(!capture.isOpened())
            return;

        // set the acquired frame size to the size of its container
        capture.set(CV_CAP_PROP_FRAME_WIDTH, ui->before_img->size().width());
        capture.set(CV_CAP_PROP_FRAME_HEIGHT, ui->before_img->size().height());

        isCameraRunning = true;

        // start timer for acquiring the video
        cameraTimer.start(33); // 33 ms = 30 fps
        // at the timeout() event, execute the cameraTimerTimeout() method
        connect(&cameraTimer, SIGNAL(timeout()), this, SLOT(cameraTimerTimeout()));

        ui->startCameraButton->setText("Stop Camera");
        ui->plainTextEdit->appendPlainText("Images needed for calibration: " + QString::number(numRequiredSnapshot));
        if (numSeq < numRequiredSnapshot)
            ui->takeSnaphotButton->setEnabled(true);
    }
    else
    {
        if(!capture.isOpened())
            return;

        // stop timer
        cameraTimer.stop();
        // release camera stream
        capture.release();

        isCameraRunning = false;
        isCalibrate = false;
        numSeq = 0;

        cameraCalib.Clear();
        imageList.clear();
        ui->startCameraButton->setText("Start Camera");
        ui->before_img->clear();
        ui->after_img->clear();
        ui->takeSnaphotButton->setEnabled(true);
        ui->saveButton->setEnabled(false);
        ui->calibrateButton->setEnabled(false);
        ui->plainTextEdit->appendPlainText("Stop");

    }
}

void MainWindow::cameraTimerTimeout()
{
    if(isCameraRunning && capture.isOpened())
    {
        QImage frameToShow, frameUndistorted;

        capture >> image;

        FindAndDrawPoints();

        cvtColor(image, image, CV_BGR2RGB);

        frameToShow = QImage((const unsigned char*)(image.data), image.cols, image.rows, image.step, QImage::Format_RGB888);

        ui->before_img->setPixmap(QPixmap::fromImage(frameToShow));

        if (isCalibrate)
        {
            Mat undistorted = cameraCalib.Remap(image);
            frameUndistorted = QImage((uchar*)undistorted.data, undistorted.cols, undistorted.rows, undistorted.step, QImage::Format_RGB888);
            ui->after_img->setPixmap(QPixmap::fromImage(frameUndistorted));
        }
    }
}

void MainWindow::FindAndDrawPoints()
{
    std::vector<Point2f> imageCorners;

    bool found = findChessboardCorners(image, boardSize, imageCorners);

    if(found)
        image.copyTo(imageSaved);

    drawChessboardCorners(image, boardSize, imageCorners, found);
}

void MainWindow::on_takeSnaphotButton_clicked()
{
    if (isCameraRunning && imageSaved.data)
    {
        imageList.push_back(imageSaved);

        numSeq++;

        ui->plainTextEdit->appendPlainText("Images needed for calibration: " + QString::number(numRequiredSnapshot-numSeq));

        if(numSeq >= numRequiredSnapshot)
            this->ui->calibrateButton->setEnabled(true);

    }
}

void MainWindow::StartCalibration()
{
    if(numSeq >= numRequiredSnapshot)
    {
        ui->takeSnaphotButton->setEnabled(false);

        // open chessboard images and extract corner points
        successes = cameraCalib.AddChessboardPoints(imageList,boardSize);

        // calibrate the camera frames
        Size calibSize = Size(ui->after_img->size().width(), ui->after_img->size().height());

        cameraCalib.Calibrate(calibSize);

        isCalibrate = true;

        ui->plainTextEdit->appendPlainText("Successful images used: " + QString::number(successes));

        ui->plainTextEdit->appendPlainText(cameraCalib.GetCalibrationAsText());

        ui->saveButton->setEnabled(true);
    }
}

void MainWindow::SaveCalibration()
{
    if(isCalibrate)
    {
        QFile file("calibration.txt");

        if(file.open(QIODevice::ReadWrite))
        {
            QTextStream stream(&file);
            stream << cameraCalib.GetCalibrationAsText();
            file.close();
        }
    }
}
