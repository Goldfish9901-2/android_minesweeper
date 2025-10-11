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
#include "android/log.h"

bool *flags;
int WIDTH, HEIGHT;
extern "C" JNIEXPORT void JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_refresh(JNIEnv *env, jobject thiz) {
    flags ? memset(flags, 0, WIDTH * HEIGHT * sizeof(bool)) : nullptr;
}
extern "C" JNIEXPORT jboolean JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_initMineFlaggedBuffer(
        [[maybe_unused]] JNIEnv *env, [[maybe_unused]] jobject thiz, jint width, jint height) {
    flags = nullptr;
    WIDTH = width, HEIGHT = height;
    auto field = env->GetFieldID(env->GetObjectClass(thiz),"mines", "I");
    if (field == nullptr)return false;
    flags = static_cast<bool *>(calloc(height * width, sizeof(int *)));
    if (flags == nullptr)return false;
    return true;
}

inline int getLocation(int row, int col) {
    return row * WIDTH + col;
}

extern "C" JNIEXPORT void JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_flag(JNIEnv *env, jobject thiz,
                                                                 jint row, jint col) {
    flags[getLocation(row, col)] = true;
}
extern "C" JNIEXPORT jboolean JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_flagged(JNIEnv *env, jobject thiz,
                                                                    jint row, jint col) {
    return flags[getLocation(row, col)];
}
extern "C" JNIEXPORT void JNICALL
Java_org_goldfish_minesweeper_1android_101_logic_Controller_freeFlagsMemory(JNIEnv *env,
                                                                            jobject thiz) {
    if (flags == nullptr)return;
    free(flags);
    flags = nullptr;
}