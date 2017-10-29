LOCAL_PATH := $(call my-dir)

include $(CLEAR-VARS)

#opencv
OPENCVROOT:= /home/seriznue/tfm/workspace/OpenCV-2.4.9-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := com_example_seriznue_svdl_app_OpencvNativeClass.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := MyOpencvLibs

include $(BUILD_SHARED_LIBRARY)