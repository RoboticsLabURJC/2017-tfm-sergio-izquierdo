#------------------------------
#
# Project created by QtCreator
#
#------------------------------

QT       += core gui widgets

TARGET = CameraCalibration
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    cameracalibrator.cpp

HEADERS  += mainwindow.h \
    cameracalibrator.h

FORMS    += mainwindow.ui

INCLUDEPATH += /usr/include \

LIBS += -L/usr/lib/x86_64-linux-gnu/ \
-lopencv_core \
-lopencv_highgui \
-lopencv_imgproc \
-lopencv_features2d \
-lopencv_calib3d \
-lopencv_contrib \
-lopencv_flann \
-lopencv_gpu \
-lopencv_legacy \
-lopencv_ml \
-lopencv_objdetect \
-lopencv_ts \
-lopencv_video

