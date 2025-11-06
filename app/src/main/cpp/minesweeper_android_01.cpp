#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("minesweeper_android_01");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("minesweeper_android_01")
//      }
//    }

#include <cstdlib>
#include <cstring>
#include "minesweeper_log.h"
#include "MineFlagBuffer.h"

extern "C" JNIEXPORT jboolean JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_initMineFlaggedBuffer(
        JNIEnv*, jobject, jint width, jint height) {

    try {
        g_flagsBuffer = std::make_unique<MineFlagBuffer>(width, height);
    } catch (const std::bad_alloc&) {
        LOGE("flags: malloc failed");
        return false;
    }

    LOGI("flags: malloc success %d,%d", width, height);
    return true;
}

extern "C" JNIEXPORT void JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_refresh(
        JNIEnv*, jobject) {
    if (g_flagsBuffer) g_flagsBuffer->refresh();
}

extern "C" JNIEXPORT void JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_flag(
        JNIEnv*, jobject, jint row, jint col) {
    if (g_flagsBuffer) g_flagsBuffer->flag(row, col);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_flagged(
        JNIEnv*, jobject, jint row, jint col) {
    return g_flagsBuffer && g_flagsBuffer->flagged(row, col);
}

extern "C" JNIEXPORT void JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_freeFlagsMemory(
        JNIEnv*, jobject) {
    g_flagsBuffer.reset();
}
