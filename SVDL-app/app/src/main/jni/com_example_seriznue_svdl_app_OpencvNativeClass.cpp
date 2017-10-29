#include <com_example_seriznue_svdl_app_OpencvNativeClass.h>


JNIEXPORT jint JNICALL Java_com_example_seriznue_svdl_1app_OpencvNativeClass_convertGray
  (JNIEnv *, jclass, jlong addrRgba, jlong addrGray){
    Mat& mRgb = *(Mat*)addrRgba;
    Mat& mGray = *(Mat*)addrGray;

    int conv;
    jint retVal;
    conv = toGray(mRgb, mGray);

    retVal = (jint)conv;

    return retVal;

}

int toGray(Mat img, Mat& gray){
    cvtColor(img, gray, CV_RGBA2GRAY);
    if (gray.rows==img.rows && gray.cols==img.cols)
    return 1;
    return 0;
}