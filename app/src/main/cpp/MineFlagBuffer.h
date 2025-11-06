//
// Created by jerry on 2025/11/7.
//

#ifndef MINESWEEPER_ANDROID_0_1_MINEFLAGBUFFER_H
#define MINESWEEPER_ANDROID_0_1_MINEFLAGBUFFER_H


#include <jni.h>
#include <vector>
#include <memory>
#include "minesweeper_log.h"

// 内部封装类，不暴露全局变量
class MineFlagBuffer {
public:
    MineFlagBuffer(int width, int height)
            : width(width), height(height), flags(width * height, false) {
        LOGI("MineFlagBuffer created %d x %d", width, height);
    }

    void refresh() {
        std::fill(flags.begin(), flags.end(), false);
    }

    void flag(int row, int col) {
        flags[getLocation(row, col)] = true;
    }

    bool flagged(int row, int col) const {
        return flags[getLocation(row, col)];
    }

private:
    int width, height;
    std::vector<bool> flags;

    int getLocation(int row, int col) const {
        return row * width + col;
    }
};

// 全局智能指针，保持 JNI 接口不动
static std::unique_ptr<MineFlagBuffer> g_flagsBuffer = nullptr;



#endif //MINESWEEPER_ANDROID_0_1_MINEFLAGBUFFER_H
