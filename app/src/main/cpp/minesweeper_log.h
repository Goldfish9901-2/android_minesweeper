//
// Created by jerry on 2025/11/7.
//

#ifndef MINESWEEPER_ANDROID_0_1_MINESWEEPER_LOG_H
#define MINESWEEPER_ANDROID_0_1_MINESWEEPER_LOG_H
#include "android/log.h"
#define LOG_TAG "MinesweeperNDK"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#endif //MINESWEEPER_ANDROID_0_1_MINESWEEPER_LOG_H
