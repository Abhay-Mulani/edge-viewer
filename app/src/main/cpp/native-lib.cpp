#include <jni.h>
#include <string>
#include <android/log.h>
#include <opencv2/opencv.hpp>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "native-lib", __VA_ARGS__)
using namespace cv;

extern "C" JNIEXPORT void JNICALL
Java_com_example_edgeviewer_MainActivity_setShowEdges(JNIEnv *env, jobject /* this */, jboolean enabled) {
    // store toggle state if needed (global var)
    static bool showEdges = true;
    showEdges = enabled;
    LOGI("setShowEdges: %d", showEdges);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_edgeviewer_MainActivity_sendBitmapToNative(JNIEnv *env, jobject /* this */, jobject bitmap) {
    // Convert Android Bitmap to Mat and process
    AndroidBitmapInfo info;
    void* pixels = nullptr;
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) return;

    Mat rgba(info.height, info.width, CV_8UC4, pixels);
    Mat gray, edges;

    cvtColor(rgba, gray, COLOR_RGBA2GRAY);
    GaussianBlur(gray, gray, Size(5,5), 1.5);
    Canny(gray, edges, 50, 150);

    // Convert edges to RGBA to draw back
    Mat edgesRGBA;
    cvtColor(edges, edgesRGBA, COLOR_GRAY2RGBA);
    edgesRGBA.copyTo(rgba); // write back to bitmap pixels

    AndroidBitmap_unlockPixels(env, bitmap);
}
